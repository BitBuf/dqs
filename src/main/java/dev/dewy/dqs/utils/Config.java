package dev.dewy.dqs.utils;

import java.util.Arrays;
import java.util.List;

public final class Config
{
    public Authentication authentication = new Authentication();
    public Service service = new Service();
    public Client client = new Client();
    public Debug debug = new Debug();
    public Log log = new Log();
    public Modules modules = new Modules();
    public Server server = new Server();
    public Websocket websocket = new Websocket();
    private transient boolean donePostLoad = false;

    public synchronized Config doPostLoad()
    {
        if (this.donePostLoad)
        {
            throw new IllegalStateException("Config post-load already done!");
        }
        this.donePostLoad = true;

        return this;
    }

    public static final class Authentication
    {
        public boolean doAuthentication = true;
        public String email = "john.doe@example.com";
        public String password = "my_secure_password";
        public String username = "Steve";
        public String uuid = "lamo";
    }

    public static final class Service
    {
        public boolean discordService = true;

        public String token = "default";

        public String channelId = "default";

        public String operatorId = "326039530971070474";
        public String subscriberId = "default";

        public String tariboneIp = "127.0.0.1";

        public int cooldown = 5;
        public String prefix = "&";
    }

    public static final class Client
    {
        public Extra extra = new Extra();
        public Server server = new Server();

        public static final class Extra
        {
            public AntiAFK antiafk = new AntiAFK();

            public static final class AntiAFK
            {
                public Actions actions = new Actions();
                public boolean enabled = true;
                public boolean runEvenIfClientsConnected = false;

                public static final class Actions
                {
                    public boolean rotate = true;
                    public boolean swingHand = true;
                }
            }
        }

        public static final class Server
        {
            public String address = "2b2t.org";
            public int port = 25565;
        }
    }

    public static final class Debug
    {
        public Packet packet = new Packet();
        public boolean printDataFields = false;
        public Server server = new Server();

        public static final class Packet
        {
            public boolean received = false;
            public boolean receivedBody = false;
            public boolean preSent = false;
            public boolean preSentBody = false;
            public boolean postSent = false;
            public boolean postSentBody = false;
        }

        public static final class Server
        {
            public Cache cache = new Cache();

            public static final class Cache
            {
                public boolean sendingmessages = true;
                public boolean unknownplayers = false;
            }
        }
    }

    public static final class Log
    {
        public boolean printDebug = false;
        public boolean storeDebug = true;
    }

    public static final class Server
    {
        public Bind bind = new Bind();
        public int compressionThreshold = 256;
        public boolean enabled = false;
        public Extra extra = new Extra();
        public Ping ping = new Ping();
        public boolean verifyUsers = false;

        public static final class Bind
        {
            public String address = "0.0.0.0";
            public int port = 25565;
        }

        public static final class Extra
        {
            public Timeout timeout = new Timeout();

            public static final class Timeout
            {
                public boolean enable = true;
                public long millis = 5000L;
                public long interval = 100L;
            }
        }

        public static final class Ping
        {
            public boolean favicon = true;
            public int maxPlayers = Integer.MAX_VALUE;
            public String motd = "§7[§b§lDQS§r§7] §fDQS 3.0.0 Instance for %s";
        }
    }

    public static final class Modules
    {
        public AutoReconnect autoReconnect = new AutoReconnect();
        public AutoDisconnect autoDisconnect = new AutoDisconnect();
        public AutoRespawn autoRespawn = new AutoRespawn();
        public AutoFish autoFish = new AutoFish();
        public ChatSpammer chatSpammer = new ChatSpammer();
        public ChatRelay chatRelay = new ChatRelay();
        public GameCommands gameCommands = new GameCommands();
        public Focus focus = new Focus();
        public Notifications notifications = new Notifications();
        public Whitelist whitelist = new Whitelist();

        public static final class Focus
        {
            public boolean enabled = false;
            public boolean focused = true;

            public List<String> accounts = Arrays.asList(
                    "bean",
                    "gggg"
            );
        }

        public static final class AutoReconnect
        {
            public boolean enabled = true;

            public int delaySeconds = 3;
        }

        public static final class AutoRespawn
        {
            public boolean enabled = false;

            public int delaySeconds = 1;
        }

        public static final class ChatSpammer
        {
            public boolean enabled = false;

            public int delaySeconds = 20;
            public List<String> messages = Arrays.asList(
                    "bean",
                    "gggg"
            );
        }

        public static final class ChatRelay
        {
            public boolean enabled = false;
        }

        public static final class GameCommands
        {
            public String prefix = "&";
        }

        public static final class AutoFish
        {
            public boolean enabled = false;
        }

        public static final class Notifications
        {
            public boolean enabled = true;

            public boolean playerInRange = true;
            public boolean crystalInRange = true;

            public boolean nearlyFinishedQueueing = true;
            public int threshold = 30;

            public boolean relogged = true;
            public boolean serverMessages = true;
        }

        public static final class AutoDisconnect
        {
            public boolean enabled = true;

            public boolean playerInRange = true;
            public boolean crystalInRange = true;

            public boolean nearlyFinishedQueueing = false;
            public int threshold = 5;
        }

        public static final class Whitelist
        {
            public boolean enabled = true;
            public List<String> whitelist = Arrays.asList(
                    "Taribone",
                    "user"
            );
            public String kickmsg = "§7[§b§lDQS§r§7] §fAccess denied.";
        }
    }

    public static final class Websocket
    {
        public Bind bind = new Bind();
        public Client client = new Client();
        public boolean enable = false;

        public static final class Bind
        {
            public String address = "0.0.0.0";
            public int port = 8080;
        }

        public static final class Client
        {
            public int maxChatCount = 512;
        }
    }
}
