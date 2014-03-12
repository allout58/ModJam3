package allout58.mods.prisoncraft.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.jail.JailedPersonData;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;

public class JVRequestPacket implements IPacket
{
    private List<JailedPersonData> peeps = new ArrayList<JailedPersonData>();
    private boolean sendAll = true;
    private String name = "";

    public JVRequestPacket()
    {
        sendAll = true;
    }

    public JVRequestPacket(String name)
    {
        sendAll = false;
        this.name = name;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            System.out.println("Recieving request on server...");
            sendAll = bytes.readBoolean();
            PrisonCraftWorldSave ws = PrisonCraftWorldSave.forWorld(MinecraftServer.getServer().getEntityWorld());
            if (sendAll)
            {
                peeps = ws.people;
            }
            else
            {
                name = NetworkUtils.readString(bytes);
                for (int i = 0; i < ws.people.size(); i++)
                {
                    if (ws.people.get(i).name.equalsIgnoreCase(name))
                    {
                        ws.people.get(i).updateTime(ws.worldObj);
                        peeps.add(ws.people.get(i));
                    }
                }
            }
            PrisonCraft.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
            PrisonCraft.channels.get(Side.SERVER).writeOutbound(new JVSendPersonPacket(peeps));
        }
        else
        {
            PrisonCraft.logger.error("JVRequestPacket read data not called on server");
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            System.out.println("Requesting from client...");
            bytes.writeBoolean(sendAll);
            NetworkUtils.writeString(bytes, name);
        }
        else
        {
            PrisonCraft.logger.error("JVRequestPacket write data not called on client");
        }
    }

    @Override
    public void postProcess()
    {
        
    }

}
