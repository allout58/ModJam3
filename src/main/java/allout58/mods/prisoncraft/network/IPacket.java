package allout58.mods.prisoncraft.network;

import io.netty.buffer.ByteBuf;

public interface IPacket
{
    public void readBytes(ByteBuf bytes);
    public void writeBytes(ByteBuf bytes);
    
    public void postProcess();
}
