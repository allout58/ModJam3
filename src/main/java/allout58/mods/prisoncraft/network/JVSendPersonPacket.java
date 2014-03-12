package allout58.mods.prisoncraft.network;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.client.render.JailViewHUDRenderer;
import allout58.mods.prisoncraft.jail.JailedPersonData;
import io.netty.buffer.ByteBuf;

public class JVSendPersonPacket implements IPacket
{
    private List<JailedPersonData> peeps = new ArrayList<JailedPersonData>();

    public JVSendPersonPacket()
    {
    }

    public JVSendPersonPacket(List<JailedPersonData> people)
    {
        peeps = people;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            System.out.println("Receiving people on client...");
            int size = bytes.readInt();
            JailViewHUDRenderer.people.clear();
            for (int i = 0; i < size; i++)
            {
                JailedPersonData pd = new JailedPersonData();
                pd.jail = NetworkUtils.readString(bytes);
                pd.name = NetworkUtils.readString(bytes);
                pd.time = bytes.readInt();
                pd.reason = NetworkUtils.readString(bytes);
                JailViewHUDRenderer.people.add(pd);
            }
        }
        else
        {
            PrisonCraft.logger.error("JVSendPacket tried to read on server");
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            System.out.println("Sending people from server...");
            bytes.writeInt(peeps.size());
            for (int i = 0; i < peeps.size(); i++)
            {
                NetworkUtils.writeString(bytes, peeps.get(i).jail);
                NetworkUtils.writeString(bytes, peeps.get(i).name);
                bytes.writeInt(peeps.get(i).time);
                NetworkUtils.writeString(bytes, peeps.get(i).reason);
            }
        }
        else
        {
            PrisonCraft.logger.error("JVSendPacket tried to write on client");
        }
    }

    @Override
    public void postProcess()
    {

    }

}
