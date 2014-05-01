package allout58.mods.prisoncraft.network;

import allout58.mods.prisoncraft.config.Config;
import io.netty.buffer.ByteBuf;

/**
 * Created by James Hollowell on 4/30/2014.
 */
public class SettingSyncPacket implements IPacket
{

    public SettingSyncPacket()
    {
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        Config.readFromPacket(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        Config.writeToPacket(bytes);
    }

    @Override
    public void postProcess()
    {
    }
}
