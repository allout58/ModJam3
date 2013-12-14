package allout58.mods.prisoncraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;

public class PacketHandler implements IPacketHandler
{

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel.equals(ModConstants.PACKETCHANNEL))
        {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
            try
            {
                String mode = inputStream.readUTF();
                if (mode == "unjail")
                {
                    ((TileEntityPrisonManager) ((EntityPlayer) player).worldObj.getBlockTileEntity(inputStream.readInt(), inputStream.readInt(), inputStream.readInt())).unjailPlayer();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
    }

}
