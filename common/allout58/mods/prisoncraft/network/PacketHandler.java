package allout58.mods.prisoncraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager,
                    Packet250CustomPayload packet, Player player) {
            
            if (packet.channel.equals(ModConstants.JAILPACKETCHANNEL) && FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER) {
                    handleJail(packet);
            }
            else if(packet.channel.equals(ModConstants.UNJAILPACKETCHANNEL) && FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER) {
                handleUnjail(packet);
        }
    }

    private void handleJail(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
        
        String name;
        String jailer;
        double time;
        
        try {
                name=inputStream.readUTF();
                jailer=inputStream.readUTF();
                time=inputStream.readDouble();
                JailMan.getInstance().TryJailPlayer(name, jailer, time);
        } catch (IOException e) {
                e.printStackTrace();
                return;
        }
        
    }
    
    private void handleUnjail(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
        
        String name;
        String jailer;
        
        try {
                name=inputStream.readUTF();
                jailer=inputStream.readUTF();
                JailMan.getInstance().TryUnjailPlayer(name, jailer);
        } catch (IOException e) {
                e.printStackTrace();
                return;
        }
    }
}
