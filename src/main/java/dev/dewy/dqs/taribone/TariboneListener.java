package dev.dewy.dqs.taribone;

import dev.dewy.dqs.DQS;
import dev.dewy.dqs.exceptions.taribone.ChunkNotLoadedException;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.networking.event.session.*;
import dev.dewy.dqs.packet.Packet;
import dev.dewy.dqs.packet.ingame.client.world.ClientTeleportConfirmPacket;
import dev.dewy.dqs.packet.ingame.server.ServerDifficultyPacket;
import dev.dewy.dqs.packet.ingame.server.ServerJoinGamePacket;
import dev.dewy.dqs.packet.ingame.server.entity.*;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import dev.dewy.dqs.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import dev.dewy.dqs.packet.ingame.server.entity.spawn.*;
import dev.dewy.dqs.packet.ingame.server.world.ServerBlockChangePacket;
import dev.dewy.dqs.packet.ingame.server.world.ServerChunkDataPacket;
import dev.dewy.dqs.packet.ingame.server.world.ServerMultiBlockChangePacket;
import dev.dewy.dqs.packet.ingame.server.world.ServerUnloadChunkPacket;
import dev.dewy.dqs.protocol.DQSProtocol;
import dev.dewy.dqs.protocol.SubProtocol;
import dev.dewy.dqs.protocol.game.chunk.Column;
import dev.dewy.dqs.protocol.game.entity.metadata.Position;
import dev.dewy.dqs.protocol.game.entity.type.GlobalEntityType;
import dev.dewy.dqs.protocol.game.world.block.BlockChangeRecord;
import dev.dewy.dqs.taribone.entity.*;
import dev.dewy.dqs.taribone.entity.player.TariboneDQSPlayer;
import dev.dewy.dqs.taribone.entity.player.TaribonePlayer;
import dev.dewy.dqs.taribone.world.Dimension;
import dev.dewy.dqs.taribone.world.World;
import dev.dewy.dqs.taribone.world.block.Block;
import dev.dewy.dqs.taribone.world.chunk.Chunk;
import dev.dewy.dqs.taribone.world.chunk.ChunkLocation;
import dev.dewy.dqs.utils.ServerData;
import dev.dewy.dqs.utils.vector.Vector3d;

import java.util.UUID;

public class TariboneListener implements SessionListener
{
    private final DQS dqs;

    private TariboneDQSPlayer player;
    private ServerData server;
    private World world;

    public TariboneListener(DQS dqs)
    {
        this.dqs = dqs;
    }

    @Override
    public void packetReceived(PacketReceivedEvent pre)
    {
        DQSProtocol protocol = (DQSProtocol) pre.getSession().getPacketProtocol();

        if (protocol.getSubProtocol() != SubProtocol.GAME)
        {
            return;
        }

        Packet packet = pre.getPacket();

        if (packet instanceof ServerSpawnObjectPacket)
        {
            ServerSpawnObjectPacket p = (ServerSpawnObjectPacket) packet;
            // TODO

            if (p.getEntityId() == 0)
            {
//                Constants.TARIBONE_LOG.warn("Received spawn object with EID == 0: " + p.getType());
                return;
            }

            TariboneObject obj = new TariboneObject(p.getEntityId(), p.getUUID());

            obj.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            obj.setPitch(p.getPitch());
            obj.setYaw(p.getYaw());
            obj.setVelocity(new Vector3d(p.getMotionX(), p.getMotionY(), p.getMotionZ()));
            obj.setData(0);
            obj.setType(p.getType());

            world.loadEntity(obj);
        } else if (packet instanceof ServerSpawnExpOrbPacket)
        {
            // 0x01 Spawn Experience Orb
            ServerSpawnExpOrbPacket p = (ServerSpawnExpOrbPacket) packet;

            // TODO: Aaah! XP orbs have no UUID! :O
            TariboneExperienceOrb orb = new TariboneExperienceOrb(p.getEntityId(), UUID.randomUUID());
            orb.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            orb.setCount(p.getExp());
            world.loadEntity(orb);
        } else if (packet instanceof ServerSpawnGlobalEntityPacket)
        {
            // 0x02 Spawn Global Entity
            ServerSpawnGlobalEntityPacket p = (ServerSpawnGlobalEntityPacket) packet;

            if (p.getType() != GlobalEntityType.LIGHTNING_BOLT)
            {
//                Constants.TARIBONE_LOG.warn("Received spawn global entity for non-lightning strike");
                return;
            }

            // TODO: Aaah! Lightning strikes have no UUID! :O
            TariboneLightningStrike ls = new TariboneLightningStrike(p.getEntityId(), UUID.randomUUID());
            ls.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            world.loadEntity(ls);

            // TODO: Remove entity?
        } else if (packet instanceof ServerSpawnMobPacket)
        {
            // 0x03 Spawn Mob
            ServerSpawnMobPacket p = (ServerSpawnMobPacket) packet;

            // TODO: double check the getType().ordinal() works as expected.
            TariboneEntity e = new TariboneMob(p.getEntityId(), p.getUUID(), p.getType());
            e.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            e.setYaw(p.getYaw());
            e.setHeadYaw(p.getHeadYaw());
            e.setPitch(p.getPitch());
            e.setVelocity(new Vector3d(p.getMotionX(), p.getMotionY(), p.getMotionZ()));
            world.loadEntity(e);
        } else if (packet instanceof ServerSpawnPaintingPacket)
        {
            // 0x04 Spawn painting
            ServerSpawnPaintingPacket p = (ServerSpawnPaintingPacket) packet;

            TaribonePainting painting = new TaribonePainting(p.getEntityId(), p.getUUID());
            painting.setLocation(new Vector3d(p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ()));
            // TODO: Direction, type
            world.loadEntity(painting);
        } else if (packet instanceof ServerSpawnPlayerPacket)
        {
            // 0x05 Spawn Player
            ServerSpawnPlayerPacket p = (ServerSpawnPlayerPacket) packet;

            TaribonePlayer pl = new TaribonePlayer(p.getEntityId(), p.getUUID());
            pl.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            pl.setPitch(p.getPitch());
            pl.setYaw(p.getYaw());
            // TODO Metadata

            dqs.getWorld().loadEntity(pl);
        } else if (packet instanceof ServerBlockChangePacket)
        {
            // 0x0B Block Change
            ServerBlockChangePacket p = (ServerBlockChangePacket) packet;

            BlockChangeRecord r = p.getRecord();
            Position pos = r.getPosition();

            if (pos.getY() > 255)
            {
                // https://github.com/Steveice10/MCProtocolLib/issues/347
//                Constants.TARIBONE_LOG.warn("Ignoring BlockChange: (" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")");
                return;
            }

            Block b;

            try
            {
                b = dqs.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
            } catch (ChunkNotLoadedException e)
            {
//                Constants.TARIBONE_LOG.warn("Received BlockChange for block in unloaded chunk: " + pos);
                return;
            }

            b.setInternalState(r.getBlock());
        } else if (packet instanceof ServerDifficultyPacket)
        {
            ServerDifficultyPacket p = (ServerDifficultyPacket) packet;

            dqs.getServerData().setDifficulty(p.getDifficulty());
        } else if (packet instanceof ServerMultiBlockChangePacket)
        {
            // 0x10 Multi Block Change
            ServerMultiBlockChangePacket p = (ServerMultiBlockChangePacket) packet;

            for (BlockChangeRecord r : p.getRecords())
            {
                Position pos = r.getPosition();

                //logger.info("MultiBlockChange: (" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + ")");
                Block b;
                try
                {
                    b = dqs.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
                } catch (ChunkNotLoadedException e)
                {
//                    Constants.TARIBONE_LOG.warn("Received MultiBlockChange for block in unloaded chunk: " + pos);
                    return;
                }

                b.setInternalState(r.getBlock());
            }
        } else if (packet instanceof ServerUnloadChunkPacket)
        {
            // 0x1D Unload Chunk
            ServerUnloadChunkPacket p = (ServerUnloadChunkPacket) packet;

            world.unloadChunk(p.getX(), p.getZ());
        } else if (packet instanceof ServerChunkDataPacket)
        {
            // 0x20 Chunk Data
            ServerChunkDataPacket p = (ServerChunkDataPacket) packet;

            Column newCol = p.getColumn();

            try
            {
                Chunk chunk = world.getChunk(new ChunkLocation(newCol.getX(), newCol.getZ()));

                // col.hasBiomeData() is currently the only way to determine the 'ground-up contrinous' property.
                // See http://wiki.vg/Chunk_Format#Ground-up_continuous for more details
                if (newCol.hasBiomeData())
                {
                    // Replace the previous chunk
                    //logger.info("Replacing pre-existing chunk: " + new ChunkLocation(newCol.getX(), newCol.getZ()));
                    world.loadChunk(new Chunk(world, p.getColumn()));
                } else
                {
                    // Only update the new chunk sections
                    StringBuilder s = new StringBuilder();

                    for (int i = 0; i < newCol.getChunks().length; i++)
                    {
                        if (newCol.getChunks()[i] == null)
                        {
                            // Chunk not updated
                            continue;
                        }

                        s.append(i).append(" ");

                        chunk.getHandle().getChunks()[i] = newCol.getChunks()[i];
                    }
                    //logger.info("Updating pre-existing chunk: " + new ChunkLocation(newCol.getX(), newCol.getZ()) + ", sections: " + s);
                }
            } catch (ChunkNotLoadedException ex)
            {
                // New chunk
                world.loadChunk(new Chunk(world, p.getColumn()));
            }
        } else if (packet instanceof ServerJoinGamePacket)
        {
            // 0x23 Join Game
            ServerJoinGamePacket p = (ServerJoinGamePacket) packet;

            // Init the game
            this.world = new World(Dimension.forId(p.getDimension()), p.getWorldType());
            dqs.setWorld(world);

            this.server = new ServerData();
            server.setMaxPlayers(p.getMaxPlayers());
            server.setDifficulty(p.getDifficulty());
            dqs.setServerData(server);

            this.player = new TariboneDQSPlayer(dqs, p.getEntityId());
            player.setGameMode(p.getGameMode());
            dqs.setPlayer(player);
        } else if (packet instanceof ServerEntityMovementPacket)
        {
            // 0x25 Entity Relative Move
            // 0x26 Entity Look And Relative Move
            // 0x27 Entity Look
            // 0x28 Entity

            ServerEntityMovementPacket p = (ServerEntityMovementPacket) packet;

            TariboneEntity e = world.getEntity(p.getEntityId());

            if (e == null)
            {
//                Constants.TARIBONE_LOG.warn("Received entity movement packet for unknown entity: " + p.getEntityId());

                return;
            }

            if (packet instanceof ServerEntityPositionPacket)
            {
                // 0x25
                e.setLocation(e.getLocation().add(new Vector3d(p.getMovementX(), p.getMovementY(), p.getMovementZ())));
                e.setOnGround(p.isOnGround());
            } else if (packet instanceof ServerEntityRotationPacket)
            {
                // 0x27
                e.setPitch(p.getPitch());
                e.setYaw(p.getYaw());
                e.setOnGround(p.isOnGround());
            } else if (packet instanceof ServerEntityPositionRotationPacket)
            {
                // 0x26
                e.setLocation(e.getLocation().add(new Vector3d(p.getMovementX(), p.getMovementY(), p.getMovementZ())));
                e.setPitch(p.getPitch());
                e.setYaw(p.getYaw());
                e.setOnGround(p.isOnGround());
            }
            // 0x28
            // Do nothing.

        } else if (packet instanceof ServerPlayerAbilitiesPacket)
        {
            // 0x2B Player Abilities
            ServerPlayerAbilitiesPacket p = (ServerPlayerAbilitiesPacket) packet;

            TariboneDQSPlayer player = dqs.getPlayer();
            player.setWalkSpeed(p.getWalkSpeed());
        } else if (packet instanceof ServerPlayerPositionRotationPacket)
        {
            // 0x2E Player Position And Look
            ServerPlayerPositionRotationPacket p = (ServerPlayerPositionRotationPacket) packet;

            TariboneDQSPlayer player = dqs.getPlayer();
            player.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            player.setPitch(p.getPitch());
            player.setYaw(p.getYaw());
            player.setOnGround(true);

            Session session = dqs.getClient().getSession();
            session.send(new ClientTeleportConfirmPacket(p.getTeleportId()));

//            Constants.TARIBONE_LOG.info("Received new Player position: " + player.getLocation());
        } else if (packet instanceof ServerEntityDestroyPacket)
        {
            // 0x30 Destroy Entities
            ServerEntityDestroyPacket p = (ServerEntityDestroyPacket) packet;

            for (int id : p.getEntityIds())
            {
                if (world.isEntityLoaded(id))
                {
                    world.unloadEntity(id);
                } else
                {
//                    Constants.TARIBONE_LOG.warn("Received entity destroy packet for unknown entity: " + id);
                }
            }
        } else if (packet instanceof ServerEntityHeadLookPacket)
        {
            // 0x34 Entity Head Look
            ServerEntityHeadLookPacket p = (ServerEntityHeadLookPacket) packet;

            TariboneEntity e = world.getEntity(p.getEntityId());

            if (e == null)
            {
//                Constants.TARIBONE_LOG.warn("Received entity head look packet for unknown entity: " + p.getEntityId());

                return;
            }

            e.setHeadYaw(p.getHeadYaw());
        } else if (packet instanceof ServerEntityVelocityPacket)
        {
            // 0x3B Entity Velocity
            ServerEntityVelocityPacket p = (ServerEntityVelocityPacket) packet;

            TariboneEntity e = world.getEntity(p.getEntityId());

            if (e == null)
            {
//                Constants.TARIBONE_LOG.warn("Received entity velocity packet for unknown entity: " + p.getEntityId());
                return;
            }

            e.setVelocity(new Vector3d(p.getMotionX(), p.getMotionY(), p.getMotionZ()));
        } else if (packet instanceof ServerPlayerHealthPacket)
        {
            // 0x3E Update Health
            ServerPlayerHealthPacket p = (ServerPlayerHealthPacket) packet;

            player.setHealth(p.getHealth());
        } else if (packet instanceof ServerEntityTeleportPacket)
        {
            // 0x49 Entity Teleport
            ServerEntityTeleportPacket p = (ServerEntityTeleportPacket) packet;

            TariboneEntity e = world.getEntity(p.getEntityId());

            if (e == null)
            {
//                Constants.TARIBONE_LOG.warn("Received entity movement packet for unknown entity: " + p.getEntityId());

                return;
            }

            e.setLocation(new Vector3d(p.getX(), p.getY(), p.getZ()));
            e.setYaw(p.getYaw());
            e.setPitch(p.getPitch());
        } else
        {
//            Constants.TARIBONE_LOG.debug("Recieved unhandled packet: " + packet.getClass().getName());
        }
    }

    @Override
    public void packetSent(PacketSentEvent event)
    {

    }

    @Override
    public void connected(ConnectedEvent event)
    {

    }

    @Override
    public void disconnecting(DisconnectingEvent event)
    {

    }

    @Override
    public void packetSending(PacketSendingEvent event)
    {

    }

    @Override
    public void disconnected(DisconnectedEvent event)
    {
//        Constants.TARIBONE_LOG.info("Disconnected: " + event.getReason());

        if (event.getCause() != null)
        {
//            Constants.TARIBONE_LOG.warn("Connection closed unexpectedly!", event.getCause());
        }
    }
}
