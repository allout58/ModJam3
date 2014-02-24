package allout58.mods.prisoncraft.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import allout58.mods.prisoncraft.client.render.JailViewHUDRenderer;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.jail.JailedPersonData;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
    public static final byte JV_SEND_ALL = 0;
    public static final byte JV_SEND_ONE = 1;
    public static final byte JV_RECIEVE_ALL = 2;
    public static final byte JV_RECIEVE_ONE = 3;
    public static final byte JV_RECIEVE_NONE = 4;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {

        if (packet.channel.equals(ModConstants.JAIL_PACKET_CHANNEL) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            handleJail(packet);
        }
        else if (packet.channel.equals(ModConstants.UNJAIL_PACKET_CHANNEL) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            handleUnjail(packet);
        }
        else if (packet.channel.equals(ModConstants.JV_CLIENT_TO_SERVER_PACKET_CHANNEL) && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            handleJVCtoS(packet);
        }
        else if (packet.channel.equals(ModConstants.JV_SERVER_TO_CLIENT_PACKET_CHANNEL) && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            handleJVStoC(packet);
        }
    }

    private void handleJail(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        String name;
        String jailer;
        double time;

        try
        {
            name = inputStream.readUTF();
            jailer = inputStream.readUTF();
            time = inputStream.readDouble();
            if (JailPermissions.getInstance().playerCanUse(jailer, PermissionLevel.Jailer))
            {
                JailMan.getInstance().TryJailPlayer(name, jailer, "", time);
            }
            else
            {
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(jailer);
                if (player != null)
                {
                    player.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.invalidperms.tool"));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

    }

    private void handleUnjail(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        String name;
        String jailer;

        try
        {
            name = inputStream.readUTF();
            jailer = inputStream.readUTF();

            if (JailPermissions.getInstance().playerCanUse(jailer, PermissionLevel.Jailer))
            {
                JailMan.getInstance().TryUnjailPlayer(name, jailer);
            }
            else
            {
                EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(jailer);
                if (player != null)
                {
                    player.sendChatToPlayer(new ChatMessageComponent().addText("[" + ModConstants.NAME + "] " + EnumChatFormatting.RED.toString()).addKey("string.invalidperms.tool"));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    private void handleJVCtoS(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        byte type;
        String uname;

        try
        {
            type = inputStream.readByte();
            if (type == PacketHandler.JV_SEND_ALL)
            {
                System.out.println("Sending all from server to client");
                PacketHandler.updateAllClient();
            }
            else if (type == PacketHandler.JV_SEND_ONE)
            {
                System.out.println("Sending one from client to server");
                uname = inputStream.readUTF();
                PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(MinecraftServer.getServer().worldServerForDimension(0));
                ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
                DataOutputStream outputStream = new DataOutputStream(bos);
                if (ws.people.size() > 0)
                {
                    try
                    {
                        boolean found = false;
                        for (int i = 0; i < ws.people.size(); i++)
                        {
                            if (ws.people.get(i).name.equalsIgnoreCase(uname))
                            {
                                found = true;
                                ws.people.get(i).updateTime(ws.worldObj);
                                outputStream.writeByte(PacketHandler.JV_RECIEVE_ONE);
                                outputStream.writeUTF(ws.people.get(i).jail);
                                outputStream.writeUTF(ws.people.get(i).name);
                                outputStream.writeInt(ws.people.get(i).time);
                                outputStream.writeUTF((ws.people.get(i).reason != null) ? ws.people.get(i).reason : "");
                            }
                        }
                        if (!found)
                        {

                            outputStream.writeByte(PacketHandler.JV_RECIEVE_NONE);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
                else
                {
                    try
                    {
                        outputStream.write(PacketHandler.JV_RECIEVE_NONE);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
                Packet250CustomPayload outPacket = new Packet250CustomPayload();
                outPacket.channel = ModConstants.JV_SERVER_TO_CLIENT_PACKET_CHANNEL;
                outPacket.data = bos.toByteArray();
                outPacket.length = bos.size();
                PacketDispatcher.sendPacketToAllPlayers(outPacket);
            }
            else
            {
                throw new IllegalArgumentException("The type sent to PrisonCraft's packet handler was bad. type: " + type);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return;
        }

    }

    private void handleJVStoC(Packet250CustomPayload packet)
    {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        byte type;
        String uname;

        try
        {
            type = inputStream.readByte();
            if (type == PacketHandler.JV_RECIEVE_ALL)
            {
                System.out.println("Recieving all from server");
                int size = inputStream.readInt();
                JailViewHUDRenderer.people.clear();
                for (int i = 0; i < size; i++)
                {
                    JailedPersonData pd = new JailedPersonData();
                    pd.jail = inputStream.readUTF();
                    pd.name = inputStream.readUTF();
                    pd.time = inputStream.readInt();
                    pd.reason = inputStream.readUTF();
                    JailViewHUDRenderer.people.add(pd);
                }
            }
            else if (type == PacketHandler.JV_RECIEVE_ONE)
            {
                System.out.println("Recieving one from server");
                JailedPersonData pd = new JailedPersonData();
                pd.jail = inputStream.readUTF();
                pd.name = inputStream.readUTF();
                pd.time = inputStream.readInt();
                pd.reason = inputStream.readUTF();
                for (int i = 0; i < JailViewHUDRenderer.people.size(); i++)
                {
                    if (JailViewHUDRenderer.people.get(i).name.equals(pd.name))
                    {
                        JailViewHUDRenderer.people.get(i).jail = pd.jail;
                        JailViewHUDRenderer.people.get(i).time = pd.time;
                        JailViewHUDRenderer.people.get(i).reason = pd.reason;
                        break;
                    }
                }
            }
            else if (type == PacketHandler.JV_RECIEVE_NONE)
            {
                 System.out.println("Recieved none from server");
                // not really sure what do with this...
            }
            else
            {
                throw new IllegalArgumentException("The type sent to PrisonCraft's packet handler was bad. type: " + type);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return;
        }
    }

    public static void updateAllClient()
    {
        System.out.println("Sending all from server to client");
        PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(MinecraftServer.getServer().worldServerForDimension(0));
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
//        if (ws.people.size() > 0)
//        {
            try
            {
                outputStream.writeByte(PacketHandler.JV_RECIEVE_ALL);
                outputStream.writeInt(ws.people.size());
                for (int i = 0; i < ws.people.size(); i++)
                {
                    ws.people.get(i).updateTime(ws.worldObj);
                    outputStream.writeUTF(ws.people.get(i).jail);
                    outputStream.writeUTF(ws.people.get(i).name);
                    outputStream.writeInt(ws.people.get(i).time);
                    outputStream.writeUTF((ws.people.get(i).reason != null) ? ws.people.get(i).reason : "");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

//        }
//        else
//        {
//            try
//            {
//                outputStream.write(PacketHandler.JV_RECIEVE_NONE);
//            }
//            catch (Exception ex)
//            {
//                ex.printStackTrace();
//            }
//        }
        Packet250CustomPayload outPacket = new Packet250CustomPayload();
        outPacket.channel = ModConstants.JV_SERVER_TO_CLIENT_PACKET_CHANNEL;
        outPacket.data = bos.toByteArray();
        outPacket.length = bos.size();
        PacketDispatcher.sendPacketToAllPlayers(outPacket);
    }
    
}
