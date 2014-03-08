package allout58.mods.prisoncraft.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.constants.ModConstants;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class ConnectionHandler implements IConnectionHandler
{

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);

        try
        {
            outputStream.writeBoolean(Config.noJumping);
            outputStream.writeBoolean(Config.noMovement);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload outPacket = new Packet250CustomPayload();
//        outPacket.channel = ModConstants.SYNC_SETTINGS_FROM_SERVER_PACKET_CHANNEL;
        outPacket.data = bos.toByteArray();
        outPacket.length = bos.size();
        PacketDispatcher.sendPacketToPlayer(outPacket, player);
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
    {
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {
    }

    @Override
    public void connectionClosed(INetworkManager manager)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            Config.reload();
        }
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
    }

}
