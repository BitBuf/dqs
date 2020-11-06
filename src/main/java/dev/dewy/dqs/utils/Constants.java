package dev.dewy.dqs.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import dev.dewy.dqs.services.cache.DataCache;
import dev.dewy.dqs.events.client.DQSClientSession;
import dev.dewy.dqs.events.client.handler.incoming.*;
import dev.dewy.dqs.events.client.handler.incoming.entity.*;
import dev.dewy.dqs.events.client.handler.incoming.spawn.*;
import dev.dewy.dqs.discord.HelpCommand;
import dev.dewy.dqs.discord.data.InfoCommand;
import dev.dewy.dqs.discord.data.ModulesCommand;
import dev.dewy.dqs.discord.data.PosCommand;
import dev.dewy.dqs.discord.dewy.KillCommand;
import dev.dewy.dqs.discord.modules.*;
import dev.dewy.dqs.discord.utility.*;
import dev.dewy.dqs.events.HandlerRegistry;
import dev.dewy.dqs.events.server.DQSServerConnection;
import dev.dewy.dqs.events.server.handler.incoming.LoginStartHandler;
import dev.dewy.dqs.events.server.handler.incoming.ServerChatHandler;
import dev.dewy.dqs.events.server.handler.incoming.ServerKeepaliveHandler;
import dev.dewy.dqs.events.server.handler.incoming.movement.PlayerPositionHandler;
import dev.dewy.dqs.events.server.handler.incoming.movement.PlayerPositionRotationHandler;
import dev.dewy.dqs.events.server.handler.incoming.movement.PlayerRotationHandler;
import dev.dewy.dqs.events.server.handler.outgoing.LoginSuccessOutgoingHandler;
import dev.dewy.dqs.events.server.handler.postoutgoing.JoinGamePostHandler;
import dev.dewy.dqs.utils.websocket.WebSocketServer;
import net.daporkchop.lib.binary.oio.appendable.PAppendable;
import net.daporkchop.lib.binary.oio.reader.UTF8FileReader;
import net.daporkchop.lib.binary.oio.writer.UTF8FileWriter;
import net.daporkchop.lib.common.misc.file.PFiles;
import net.daporkchop.lib.logging.LogAmount;
import net.daporkchop.lib.logging.Logger;
import net.daporkchop.lib.logging.Logging;
import net.daporkchop.lib.logging.impl.DefaultLogger;
import net.daporkchop.lib.minecraft.text.parser.MCFormatParser;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public final class Constants
{
    public static final String VERSION = "3.2.1";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final DefaultLogger DEFAULT_LOG = Logging.logger;
    public static final Logger AUTH_LOG = DEFAULT_LOG.channel("DQS Auth");
    public static final Logger CACHE_LOG = DEFAULT_LOG.channel("DQS Cache");
    public static final Logger CLIENT_LOG = DEFAULT_LOG.channel("DQS Client");
    public static final Logger CHAT_LOG = DEFAULT_LOG.channel("Chat");
    public static final Logger DISCORD_LOG = DEFAULT_LOG.channel("Discord");
    public static final Logger MODULE_LOG = DEFAULT_LOG.channel("DQS Modules");
    public static final Logger SERVER_LOG = DEFAULT_LOG.channel("DQS Server");
    public static final Logger WEBSOCKET_LOG = DEFAULT_LOG.channel("DQS WebSocket");

    public static final File CONFIG_FILE = new File("config.json");
    public static final DataCache CACHE;
    public static final WebSocketServer WEBSOCKET_SERVER;
    public static final HandlerRegistry<DQSClientSession> CLIENT_HANDLERS = new HandlerRegistry.Builder<DQSClientSession>()
            .setLogger(CLIENT_LOG)
            //
            // Inbound packets
            //
            .registerInbound(new AdvancementsHandler())
            .registerInbound(new BlockChangeHandler())
            .registerInbound(new BossBarHandler())
            .registerInbound(new ChatHandler())
            .registerInbound(new ChunkDataHandler())
            .registerInbound(new ClientKeepaliveHandler())
            .registerInbound(new GameStateHandler())
            .registerInbound(new JoinGameHandler())
            .registerInbound(new LoginSuccessHandler())
            .registerInbound(new MultiBlockChangeHandler())
            .registerInbound(new PlayerHealthHandler())
            .registerInbound(new PlayerPosRotHandler())
            .registerInbound(new RespawnHandler())
            .registerInbound(new SetSlotHandler())
            .registerInbound(new SetWindowItemsHandler())
            .registerInbound(new StatisticsHandler())
            .registerInbound(new TabListDataHandler())
            .registerInbound(new TabListEntryHandler())
            .registerInbound(new UnloadChunkHandler())
            .registerInbound(new UnlockRecipesHandler())
            .registerInbound(new UpdateTileEntityHandler())
            //ENTITY
            .registerInbound(new EntityAttachHandler())
            .registerInbound(new EntityCollectItemHandler())
            .registerInbound(new EntityDestroyHandler())
            .registerInbound(new EntityEffectHandler())
            .registerInbound(new EntityEquipmentHandler())
            .registerInbound(new EntityHeadLookHandler())
            .registerInbound(new EntityMetadataHandler())
            .registerInbound(new EntityPositionHandler())
            .registerInbound(new EntityPositionRotationHandler())
            .registerInbound(new EntityPropertiesHandler())
            .registerInbound(new EntityRemoveEffectListener())
            .registerInbound(new EntityRotationHandler())
            .registerInbound(new EntitySetPassengersHandler())
            .registerInbound(new EntityTeleportHandler())
            //SPAWN
            .registerInbound(new SpawnExperienceOrbHandler())
            .registerInbound(new SpawnMobHandler())
            .registerInbound(new SpawnObjectHandler())
            .registerInbound(new SpawnPaintingPacket())
            .registerInbound(new SpawnPlayerHandler())
            .build();
    public static final HandlerRegistry<DQSServerConnection> SERVER_HANDLERS = new HandlerRegistry.Builder<DQSServerConnection>()
            .setLogger(SERVER_LOG)
            //
            // Inbound packets
            //
            .registerInbound(new LoginStartHandler())
            .registerInbound(new ServerChatHandler())
            .registerInbound(new ServerKeepaliveHandler())
            //PLAYER MOVEMENT
            .registerInbound(new PlayerPositionHandler())
            .registerInbound(new PlayerPositionRotationHandler())
            .registerInbound(new PlayerRotationHandler())
            //
            // Outbound packets
            //
            .registerOutbound(new LoginSuccessOutgoingHandler())
            //
            // Post-outbound packets
            //
            .registerPostOutbound(new JoinGamePostHandler())
            .build();
    public static Config CONFIG;
    public static volatile boolean SHOULD_RECONNECT;
    public static volatile boolean RATE_LIMITED = false;

    public static JDA DISCORD;

    static
    {
        String date = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(Date.from(Instant.now()));
        File logFolder = PFiles.ensureDirectoryExists(new File("log"));
        DEFAULT_LOG.addFile(new File(logFolder, String.format("%s.log", date)), LogAmount.NORMAL)
                .enableANSI()
                .setFormatParser(MCFormatParser.DEFAULT)
                .setLogAmount(LogAmount.NORMAL);

        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> DEFAULT_LOG.alert(String.format("Uncaught exception in thread \"%s\"!", thread), e));

        loadConfig();
        saveConfig();

        if (CONFIG.service.subscriberId.equals("default") || CONFIG.service.token.equals("default"))
        {
            DISCORD_LOG.error("Subscriber ID or token not specified.");
        }

        if (CONFIG.service.discordService)
        {
            CommandClientBuilder commandClient = new CommandClientBuilder();

            commandClient.setActivity(Activity.playing("for you!"));
            commandClient.setPrefix(CONFIG.service.prefix);
            commandClient.setOwnerId(CONFIG.service.operatorId);

            commandClient.setHelpWord("DWWWWWWWWWWWWWWWWWWWWWWjljubmjhbgWWWWWJIWJFUJEWMFCNWEF");

            commandClient.addCommands(
                    new HelpCommand(),
                    new SignInCommand(),
                    new PosCommand(),
                    new AutoReconnectCommand(),
                    new SpammerCommand(),
                    new AutoRespawnCommand(),
                    new WhitelistCommand(),
                    new InfoCommand(),
                    new ModulesCommand(),
                    new SayCommand(),
                    new WhisperCommand(),
                    new KillCommand(),
                    new FocusCommand(),
                    new NotificationsCommand(),
                    new RelogCommand(),
                    new RecoverCommand(),
                    new RelayCommand(),
                    new AutoDisconnectCommand(),
                    new AutoReplyCommand(),
                    new MailForwardCommand(),
                    new DisconnectCommand(),
                    new ConnectCommand(),
                    new SwitchCommand());

            try
            {
                DISCORD = new JDABuilder(AccountType.BOT)
                        .setToken(CONFIG.service.token)
                        .addEventListeners(commandClient.build())
                        .build();
            } catch (LoginException e)
            {
                e.printStackTrace();
            }
        }

        if (CONFIG.log.printDebug)
        {
            DEFAULT_LOG.setLogAmount(LogAmount.DEBUG);
        }
        if (CONFIG.log.storeDebug)
        {
            DEFAULT_LOG.addFile(new File(logFolder, String.format("%s-debug.log", date)), LogAmount.DEBUG);
        }

        SHOULD_RECONNECT = CONFIG.modules.autoReconnect.enabled;

        CACHE = new DataCache();
        WEBSOCKET_SERVER = new WebSocketServer();
    }

    private Constants()
    {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static synchronized void loadConfig()
    {
        DEFAULT_LOG.info("Loading config...");

        Config config;
        if (PFiles.checkFileExists(CONFIG_FILE))
        {
            try (Reader reader = new UTF8FileReader(CONFIG_FILE))
            {
                config = GSON.fromJson(reader, Config.class);
            } catch (IOException e)
            {
                throw new RuntimeException("Unable to load config!", e);
            }
        } else
        {
            config = new Config();
        }

        CONFIG = config.doPostLoad();
        DEFAULT_LOG.info("Config loaded.");
    }

    public static synchronized void saveConfig()
    {
        DEFAULT_LOG.info("Saving config...");

        if (CONFIG == null)
        {
            DEFAULT_LOG.warn("Config is not set, saving default config!");
            CONFIG = new Config().doPostLoad();
        }

        try (PAppendable out = new UTF8FileWriter(PFiles.ensureFileExists(CONFIG_FILE)))
        {
            GSON.toJson(CONFIG, out);
        } catch (IOException e)
        {
            throw new RuntimeException("Unable to save config!", e);
        }

        DEFAULT_LOG.info("Config saved.");
    }
}
