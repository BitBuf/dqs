package dev.dewy.dqs.handler;

import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.protocol.packet.Packet;
import dev.dewy.dqs.utils.ObjObjBoolFunction;
import net.daporkchop.lib.logging.Logger;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static dev.dewy.dqs.utils.Constants.CONFIG;

public class HandlerRegistry<S extends Session>
{
    protected final Map<Class<? extends Packet>, ObjObjBoolFunction<? extends Packet, S>> inboundHandlers;

    protected final Map<Class<? extends Packet>, BiFunction<? extends Packet, S, ? extends Packet>> outboundHandlers;

    protected final Map<Class<? extends Packet>, BiConsumer<? extends Packet, S>> postOutboundHandlers;

    protected final Logger logger;

    private HandlerRegistry(Map<Class<? extends Packet>, ObjObjBoolFunction<? extends Packet, S>> inboundHandlers, Map<Class<? extends Packet>, BiFunction<? extends Packet, S, ? extends Packet>> outboundHandlers, Map<Class<? extends Packet>, BiConsumer<? extends Packet, S>> postOutboundHandlers, Logger logger)
    {
        this.inboundHandlers = inboundHandlers;
        this.outboundHandlers = outboundHandlers;
        this.postOutboundHandlers = postOutboundHandlers;
        this.logger = logger;
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> boolean handleInbound(P packet, S session)
    {
        if (CONFIG.debug.packet.received)
        {
            this.logger.debug("Received packet: %s@%08x", CONFIG.debug.packet.receivedBody ? packet : packet.getClass(), System.identityHashCode(packet));
        }
        ObjObjBoolFunction<P, S> handler = (ObjObjBoolFunction<P, S>) this.inboundHandlers.get(packet.getClass());
        return handler == null || handler.apply(packet, session);
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> P handleOutgoing(P packet, S session)
    {
        if (CONFIG.debug.packet.preSent)
        {
            this.logger.debug("Sending packet: %s@%08x", CONFIG.debug.packet.preSentBody ? packet : packet.getClass(), System.identityHashCode(packet));
        }
        BiFunction<P, S, P> handler = (BiFunction<P, S, P>) this.outboundHandlers.get(packet.getClass());
        return handler == null ? packet : handler.apply(packet, session);
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet> void handlePostOutgoing(P packet, S session)
    {
        if (CONFIG.debug.packet.postSent)
        {
            this.logger.debug("Sent packet: %s@%08x", CONFIG.debug.packet.postSentBody ? packet : packet.getClass(), System.identityHashCode(packet));
        }
        PostOutgoingHandler<P, S> handler = (PostOutgoingHandler<P, S>) this.postOutboundHandlers.get(packet.getClass());
        if (handler != null)
        {
            handler.accept(packet, session);
        }
    }

    public interface IncomingHandler<P extends Packet, S extends Session> extends ObjObjBoolFunction<P, S>
    {
        /**
         * Handle a packet
         *
         * @param packet  the packet to handle
         * @param session the session the packet was received on
         * @return whether or not the packet should be forwarded
         */
        @Override
        boolean apply(P packet, S session);

        Class<P> getPacketClass();
    }

    public interface OutgoingHandler<P extends Packet, S extends Session> extends BiFunction<P, S, P>
    {
        @Override
        P apply(P packet, S session);

        Class<P> getPacketClass();
    }

    public interface PostOutgoingHandler<P extends Packet, S extends Session> extends BiConsumer<P, S>
    {
        @Override
        void accept(P packet, S session);

        Class<P> getPacketClass();
    }


    public static class Builder<S extends Session>
    {
        protected final Map<Class<? extends Packet>, ObjObjBoolFunction<? extends Packet, S>> inboundHandlers = new IdentityHashMap<>();

        protected final Map<Class<? extends Packet>, BiFunction<? extends Packet, S, ? extends Packet>> outboundHandlers = new IdentityHashMap<>();

        protected final Map<Class<? extends Packet>, BiConsumer<? extends Packet, S>> postOutboundHandlers = new IdentityHashMap<>();

        protected Logger logger;

        public <P extends Packet> Builder<S> registerInbound(Class<P> clazz, BiConsumer<P, S> handler)
        {
            return this.registerInbound(clazz, (packet, session) ->
            {
                handler.accept(packet, session);
                return true;
            });
        }

        public <P extends Packet> Builder<S> registerInbound(Class<P> clazz, ObjObjBoolFunction<P, S> handler)
        {
            this.inboundHandlers.put(clazz, handler);
            return this;
        }

        public Builder<S> registerInbound(IncomingHandler<? extends Packet, S> handler)
        {
            this.inboundHandlers.put(handler.getPacketClass(), handler);
            return this;
        }

        public <P extends Packet> Builder<S> registerOutbound(Class<P> clazz, BiFunction<P, S, P> handler)
        {
            this.outboundHandlers.put(clazz, handler);
            return this;
        }

        public Builder<S> registerOutbound(OutgoingHandler<? extends Packet, S> handler)
        {
            this.outboundHandlers.put(handler.getPacketClass(), handler);
            return this;
        }

        public <P extends Packet> Builder<S> registerPostOutbound(Class<P> clazz, BiConsumer<P, S> handler)
        {
            this.postOutboundHandlers.put(clazz, handler);
            return this;
        }

        public Builder<S> registerPostOutbound(PostOutgoingHandler<? extends Packet, S> handler)
        {
            this.postOutboundHandlers.put(handler.getPacketClass(), handler);
            return this;
        }

        public HandlerRegistry<S> build()
        {
            return new HandlerRegistry<>(this.inboundHandlers, this.outboundHandlers, this.postOutboundHandlers, this.logger);
        }

        public Map<Class<? extends Packet>, ObjObjBoolFunction<? extends Packet, S>> getInboundHandlers()
        {
            return this.inboundHandlers;
        }

        public Map<Class<? extends Packet>, BiFunction<? extends Packet, S, ? extends Packet>> getOutboundHandlers()
        {
            return this.outboundHandlers;
        }

        public Map<Class<? extends Packet>, BiConsumer<? extends Packet, S>> getPostOutboundHandlers()
        {
            return this.postOutboundHandlers;
        }

        public Logger getLogger()
        {
            return this.logger;
        }

        public Builder<S> setLogger(Logger logger)
        {
            this.logger = logger;
            return this;
        }
    }
}
