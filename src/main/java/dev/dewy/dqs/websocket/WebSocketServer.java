package dev.dewy.dqs.websocket;

import dev.dewy.dqs.cache.data.tab.PlayerEntry;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static dev.dewy.dqs.utils.Constants.*;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer
{
    protected final String[] messages = CONFIG.websocket.enable ? new String[CONFIG.websocket.client.maxChatCount] : null;

    public WebSocketServer()
    {
        super(new InetSocketAddress(CONFIG.websocket.bind.address, CONFIG.websocket.bind.port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
        conn.send(String.format(
                "{\"command\":\"init\",\"maxChatCount\":%d}",
                CONFIG.websocket.client.maxChatCount
        ));
        CACHE.getTabListCache().getTabList().getEntries().stream().map(this::getUpdatePlayerCommand).forEach(conn::send);

        synchronized (this.messages)
        {
            Arrays.stream(this.messages).filter(Objects::nonNull).forEachOrdered(conn::send);
        }
        conn.send(this.getTabDataCommand());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        WEBSOCKET_LOG.alert(ex);
        if (conn != null)
        {
            conn.close();
        } else
        {
            System.exit(1);
        }
    }

    @Override
    public void onStart()
    {
        WEBSOCKET_LOG.success("WebSocket server started!");
    }

    public void fireChat(String message)
    {
        if (CONFIG.websocket.enable)
        {
            message = String.format(
                    "{\"command\":\"chat\",\"chat\":%s}",
                    message
            );
            synchronized (this.messages)
            {
                System.arraycopy(this.messages, 1, this.messages, 0, this.messages.length - 1);
                this.messages[this.messages.length - 1] = message;
                this.broadcast(message);
            }
        }
    }

    public void fireReset()
    {
        if (CONFIG.websocket.enable)
        {
            this.broadcast("{\"command\":\"reset\"}");
        }
    }

    public void firePlayerListUpdate()
    {
        if (CONFIG.websocket.enable)
        {
            this.broadcast(this.getTabDataCommand());
        }
    }

    public void updatePlayer(PlayerEntry entry)
    {
        if (CONFIG.websocket.enable)
        {
            this.broadcast(this.getUpdatePlayerCommand(entry));
        }
    }

    public void removePlayer(UUID id)
    {
        if (CONFIG.websocket.enable)
        {
            this.broadcast(String.format("{\"command\":\"removePlayer\",\"uuid\":\"%s\"}", id.toString()));
        }
    }

    public void shutdown()
    {
        if (CONFIG.websocket.enable)
        {
            WEBSOCKET_LOG.info("Shutting down...");
            try
            {
                this.stop(5000);
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            WEBSOCKET_LOG.success("Shut down.");
        }
    }

    protected String getUpdatePlayerCommand(PlayerEntry entry)
    {
        return String.format(
                "{\"command\":\"player\",\"name\":\"%s\",\"uuid\":\"%s\",\"ping\":%d}",
                entry.getName(),
                entry.getId().toString(),
                entry.getPing()
        );
    }

    protected String getTabDataCommand()
    {
        return String.format(
                "{\"command\":\"tabData\",\"header\":%s,\"footer\":%s}",
                CACHE.getTabListCache().getTabList().getHeader(),
                CACHE.getTabListCache().getTabList().getFooter()
        );
    }
}
