package dev.dewy.dqs.test.networking;

import dev.dewy.dqs.utils.crypto.AESEncryption;
import dev.dewy.dqs.utils.crypto.PacketEncryption;
import dev.dewy.dqs.networking.Client;
import dev.dewy.dqs.networking.Server;
import dev.dewy.dqs.networking.Session;
import dev.dewy.dqs.protocol.packet.DefaultPacketHeader;
import dev.dewy.dqs.protocol.packet.PacketHeader;
import dev.dewy.dqs.protocol.packet.PacketProtocol;

import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

public class TestProtocol extends PacketProtocol
{
    private PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;

    @SuppressWarnings("unused")
    private TestProtocol()
    {
    }

    public TestProtocol(SecretKey key)
    {
        this.setSecretKey(key);
    }

    public void setSecretKey(SecretKey key)
    {
        this.register(0, PingPacket.class);
        try
        {
            this.encrypt = new AESEncryption(key);
        } catch (GeneralSecurityException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getSRVRecordPrefix()
    {
        return "_test";
    }

    @Override
    public PacketHeader getPacketHeader()
    {
        return this.header;
    }

    @Override
    public PacketEncryption getEncryption()
    {
        return this.encrypt;
    }

    @Override
    public void newClientSession(Client client, Session session)
    {
        session.addListener(new ClientSessionListener());
    }

    @Override
    public void newServerSession(Server server, Session session)
    {
        session.addListener(new ServerSessionListener());
    }
}
