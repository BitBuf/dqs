package dev.dewy.dqs;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import dev.dewy.dqs.client.DQSClientSession;
import dev.dewy.dqs.networking.Client;
import dev.dewy.dqs.networking.Server;
import dev.dewy.dqs.networking.SessionFactory;
import dev.dewy.dqs.packet.ingame.client.ClientChatPacket;
import dev.dewy.dqs.packet.ingame.client.player.ClientPlayerRotationPacket;
import dev.dewy.dqs.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import dev.dewy.dqs.packet.ingame.server.ServerJoinGamePacket;
import dev.dewy.dqs.profiles.GameProfile;
import dev.dewy.dqs.protocol.*;
import dev.dewy.dqs.protocol.game.entity.player.Hand;
import dev.dewy.dqs.protocol.status.PlayerInfo;
import dev.dewy.dqs.protocol.status.ServerStatusInfo;
import dev.dewy.dqs.protocol.status.VersionInfo;
import dev.dewy.dqs.protocol.status.handler.ServerInfoBuilder;
import dev.dewy.dqs.server.DQSServerConnection;
import dev.dewy.dqs.server.DQSServerListener;
import dev.dewy.dqs.taribone.TariboneController;
import dev.dewy.dqs.taribone.TariboneListener;
import dev.dewy.dqs.taribone.entity.player.TariboneDQSPlayer;
import dev.dewy.dqs.taribone.heuristic.EuclideanHeuristic;
import dev.dewy.dqs.taribone.pathing.astar.TariboneAStar;
import dev.dewy.dqs.taribone.task.api.TaskExecutor;
import dev.dewy.dqs.taribone.ticker.TariboneTicker;
import dev.dewy.dqs.taribone.world.World;
import dev.dewy.dqs.taribone.world.physics.TariboneWorldPhysics;
import dev.dewy.dqs.utils.Authenticator;
import dev.dewy.dqs.utils.ServerData;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static dev.dewy.dqs.utils.Constants.*;

public class DQS
{
    protected static DQS instance;

    protected final SessionFactory sessionFactory = new DQSSessionFactory(this);
    protected final AtomicReference<DQSServerConnection> currentPlayer = new AtomicReference<>();
    public EventWaiter waiter = new EventWaiter();
    public boolean inQueue = true;
    public int currentPos = -1;
    public boolean connectedToProxy;
    protected DQSProtocol protocol;
    protected Client client;
    public Server server;
    protected Authenticator authenticator;
    protected BufferedImage serverIcon;
    private World world;
    private ServerData serverData;
    private TariboneAStar pathFinder;
    private TaskExecutor taskExecutor;
    private TariboneController controller;
    private TariboneTicker ticker;
    private TariboneDQSPlayer player;

    public Thread mainThread = Thread.currentThread();

    public static void main(String... args) throws LoginException
    {
        DEFAULT_LOG.info("Starting DQS %s...", VERSION);

        if (CONFIG.websocket.enable)
        {
            WEBSOCKET_LOG.info("Starting WebSocket based server...");
            WEBSOCKET_SERVER.start();
        }

        DQS dqs = new DQS();
        instance = dqs;
        dqs.start();
    }

    public static DQS getInstance()
    {
        return DQS.instance;
    }

    public void start() throws LoginException
    {
        try
        {
            {
                Thread mainThread = Thread.currentThread();
                Thread commandReaderThread = new Thread(() ->
                {
                    try (Scanner s = new Scanner(System.in))
                    {
                        long lastPress = 0L;
                        while (true)
                        {
                            s.nextLine(); //TODO: command processing from CLI
                            long now = System.currentTimeMillis();
                            if (lastPress + 10000L >= now)
                            {
                                break;
                            } else
                            {
                                DEFAULT_LOG.info("Are you sure you want to terminate this DQS instance? Press enter again to confirm.");
                                lastPress = now;
                            }
                        }
                    }
                    SHOULD_RECONNECT = false;
                    if (this.isConnected())
                    {
                        if (this.ticker != null)
                        {
                            this.ticker.stop();
                        }

                        if (this.taskExecutor != null)
                        {
                            this.taskExecutor.stop();
                        }

                        this.client.getSession().disconnect("user disconnect");
                    }
                    mainThread.interrupt();
                }, "DQS command processor thread");
                commandReaderThread.setDaemon(true);
                commandReaderThread.start();
            }
            { //TODO: clean this up
                Collection<Runnable> modules = new ArrayDeque<>();
                if (CONFIG.client.extra.antiafk.enabled)
                {
                    MODULE_LOG.trace("Enabling AntiAFK");
                    modules.add(() ->
                    {
                        if (CONFIG.client.extra.antiafk.runEvenIfClientsConnected || this.currentPlayer.get() == null)
                        {
                            boolean swingHand = CONFIG.client.extra.antiafk.actions.swingHand;
                            boolean rotate = CONFIG.client.extra.antiafk.actions.rotate;

                            int action = -1;
                            if (swingHand && rotate)
                            {
                                action = ThreadLocalRandom.current().nextInt(2);
                            } else if (swingHand)
                            {
                                action = 0;
                            } else if (rotate)
                            {
                                action = 1;
                            }
                            switch (action)
                            {
                                case 0:
                                    this.client.getSession().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
                                    break;
                                case 1:
                                    this.client.getSession().send(new ClientPlayerRotationPacket(
                                            true,
                                            -90 + (90 - -90) * ThreadLocalRandom.current().nextFloat(),
                                            -90 + (90 - -90) * ThreadLocalRandom.current().nextFloat()
                                    ));
                                    break;
                            }
                        }
                    });
                }

                AtomicInteger i = new AtomicInteger(0);

                modules.add(() ->
                {
                    if ((i.getAndIncrement() >> 1) >= CONFIG.modules.chatSpammer.delaySeconds)
                    {
                        i.set(0);

                        if (CONFIG.modules.chatSpammer.enabled)
                        {
                            this.client.getSession().send(new ClientChatPacket(CONFIG.modules.chatSpammer.messages.get(ThreadLocalRandom.current().nextInt(CONFIG.modules.chatSpammer.messages.size()))));
                        }
                    }
                });

                Thread moduleRunnerThread = new Thread(() ->
                {
                    try
                    {
                        while (true)
                        {
                            Thread.sleep(500L);
                            if (this.isConnected() && ((DQSProtocol) this.client.getSession().getPacketProtocol()).getSubProtocol() == SubProtocol.GAME)
                            {
                                modules.forEach(Runnable::run);
                            }
                        }
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                });
                moduleRunnerThread.setDaemon(true);
                moduleRunnerThread.start();
            }
            if (CONFIG.server.extra.timeout.enable)
            {
                long millis = CONFIG.server.extra.timeout.millis;
                long interval = CONFIG.server.extra.timeout.interval;
                Thread timeoutThread = new Thread(() ->
                {
                    try
                    {
                        while (true)
                        {
                            Thread.sleep(interval);
                            DQSServerConnection currentPlayer = this.currentPlayer.get();
                            if (currentPlayer != null && currentPlayer.isConnected() && System.currentTimeMillis() - currentPlayer.getLastPacket() >= millis)
                            {
                                currentPlayer.disconnect("Timed out");
                            }
                        }
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                });
                timeoutThread.setDaemon(true);
                timeoutThread.start();
            }

            this.logIn();
            this.startServer();
            CACHE.reset(true);

            do
            {
                this.logIn();
                this.connect();

                saveConfig();
                //wait for client to disconnect before starting again
                CLIENT_LOG.info("Disconnected. Reason: %s", ((DQSClientSession) this.client.getSession()).getDisconnectReason());
            } while (SHOULD_RECONNECT && CACHE.reset(true) && this.delayBeforeReconnect());
        } catch (Exception e)
        {
            DEFAULT_LOG.alert(e);
        } finally
        {
            DEFAULT_LOG.info("Shutting down...");
            if (this.server != null)
            {
                this.server.close(true);
            }
            WEBSOCKET_SERVER.shutdown();
            saveConfig();
        }
    }

    public void connect()
    {
        synchronized (this)
        {
            if (this.isConnected())
            {
                throw new IllegalStateException("Already connected!");
            }

            String address = CONFIG.client.server.address;
            int port = CONFIG.client.server.port;

            CLIENT_LOG.info("Connecting to %s:%d...", address, port);
            this.ticker = new TariboneTicker(this);
            this.client = new Client(address, port, this.protocol, this.sessionFactory);
            this.client.getSession().addListener(new TariboneListener(this));
            this.controller = new TariboneController(this);
            this.client.getSession().connect(true);
            ticker.start();
        }
    }

    public boolean isConnected()
    {
        return this.client != null && this.client.getSession() != null && this.client.getSession().isConnected();
    }

    protected void startServer()
    {
        synchronized (this)
        {
            if (this.server != null)
            {
                throw new IllegalStateException("Server already started!");
            }
            if (CONFIG.server.enabled)
            {
                try (InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode("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")))
                {
                    this.serverIcon = ImageIO.read(is);
                } catch (IOException e)
                {
                    //impossible
                    throw new AssertionError();
                }

                String address = CONFIG.server.bind.address;
                int port = CONFIG.server.bind.port;

                SERVER_LOG.info("Starting server on %s:%d...", address, port);
                this.server = new Server(address, port, DQSProtocol.class, this.sessionFactory);
                this.server.setGlobalFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
                this.server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, CONFIG.server.verifyUsers);
                this.server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, (ServerInfoBuilder) session -> new ServerStatusInfo(
                        new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION),
                        new PlayerInfo(
                                CONFIG.server.ping.maxPlayers,
                                this.currentPlayer.get() == null ? 0 : 1,
                                new GameProfile[0]
                        ),
                        String.format(CONFIG.server.ping.motd, this.protocol.getProfile().getName()),
                        this.serverIcon,
                        true
                ));
                this.server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, (ServerLoginHandler) session ->
                {
                    DQSServerConnection connection = ((DQSServerListener) this.server.getListeners().stream()
                            .filter(DQSServerListener.class::isInstance)
                            .findAny().orElseThrow(IllegalStateException::new))
                            .getConnections().get(session);
                    if (!this.currentPlayer.compareAndSet(null, connection))
                    {
                        if (CONFIG.server.kickPrevious)
                        {
                            this.currentPlayer.get().setPlayer(false);
                            this.currentPlayer.get().disconnect("§7[§b§lDQS§r§7] §fA different, whitelisted player has taken control.");
                            this.currentPlayer.set(connection);
                        } else
                        {
                            connection.disconnect("§7[§b§lDQS§r§7] §fA player is already connected to this instance.");
                            return;
                        }
                    }
                    connection.setPlayer(true);
                    session.send(new ServerJoinGamePacket(
                            CACHE.getPlayerCache().getEntityId(),
                            CACHE.getPlayerCache().isHardcore(),
                            CACHE.getPlayerCache().getGameMode(),
                            CACHE.getPlayerCache().getDimension(),
                            CACHE.getPlayerCache().getDifficulty(),
                            CACHE.getPlayerCache().getMaxPlayers(),
                            CACHE.getPlayerCache().getWorldType(),
                            CACHE.getPlayerCache().isReducedDebugInfo()
                    ));
                    SERVER_LOG.info("Player connected: %s", session.getRemoteAddress());

                    connectedToProxy = true;

                    if (this.currentPlayer.get() != connection)
                    {
                        SERVER_LOG.alert("login handler fired when session wasn't set yet...");
                    }
                });
                this.server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, CONFIG.server.compressionThreshold);
                this.server.addListener(new DQSServerListener(this));
                this.server.bind(false);
            }
        }
    }

    public void logIn()
    {
        AUTH_LOG.info("Logging in...");
        if (this.authenticator == null)
        {
            this.authenticator = new Authenticator();
        }
        this.protocol = this.authenticator.handleRelog();
        if (CONFIG.server.enabled && CONFIG.server.ping.favicon)
        {
            new Thread(() ->
            {
                try
                {
                    this.serverIcon = ImageIO.read(new URL(String.format("https://crafatar.com/avatars/%s?size=64&overlay&default=MHF_Steve", this.protocol.getProfile().getId())));
                } catch (IOException e)
                {
                    System.err.printf("Unable to download server icon for \"%s\":\n", this.protocol.getProfile().getName());
                    e.printStackTrace();
                }
            }, "Server icon downloader thread").start();
        }
        CACHE.getProfileCache().setProfile(this.protocol.getProfile());
        AUTH_LOG.success("Logged in.");
    }

    protected boolean delayBeforeReconnect()
    {
        try
        {
            for (int i = CONFIG.modules.autoReconnect.delaySeconds; SHOULD_RECONNECT && i > 0; i--)
            {
                CLIENT_LOG.info("Reconnecting in %d", i);
                Thread.sleep(1000L);
            }
            return true;
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public SessionFactory getSessionFactory()
    {
        return this.sessionFactory;
    }

    public DQSProtocol getProtocol()
    {
        return this.protocol;
    }

    public Client getClient()
    {
        return this.client;
    }

    public Server getServer()
    {
        return this.server;
    }

    public Authenticator getAuthenticator()
    {
        return this.authenticator;
    }

    public BufferedImage getServerIcon()
    {
        return this.serverIcon;
    }

    public void setServerIcon(BufferedImage serverIcon)
    {
        this.serverIcon = serverIcon;
    }

    public AtomicReference<DQSServerConnection> getCurrentPlayer()
    {
        return this.currentPlayer;
    }

    public TaskExecutor getTaskExecutor()
    {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor activity)
    {
        if (this.taskExecutor != null)
        {
            this.taskExecutor.stop();
        }

        this.taskExecutor = activity;
    }

    public TariboneAStar getPathFinder()
    {
        return pathFinder;
    }

    public TariboneController getController()
    {
        return controller;
    }

    public World getWorld()
    {
        return world;
    }

    public void setWorld(World world)
    {
        this.world = world;

        if (this.pathFinder == null)
        {
            this.pathFinder = new TariboneAStar(new EuclideanHeuristic(), new TariboneWorldPhysics(world));
        }
    }

    public ServerData getServerData()
    {
        return serverData;
    }

    public void setServerData(ServerData serverData)
    {
        this.serverData = serverData;
    }

    public TariboneDQSPlayer getPlayer()
    {
        return player;
    }

    public void setPlayer(TariboneDQSPlayer player)
    {
        this.player = player;
    }
}
