package allout58.mods.prisoncraft.network;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public ChannelHandler()
    {
        addDiscriminator(0, JailPacket.class);
        addDiscriminator(1, UnjailPacket.class);
        addDiscriminator(2, JVRequestPacket.class);
        addDiscriminator(3, JVSendPersonPacket.class);
        addDiscriminator(4, UpdateHammerPacket.class);
        addDiscriminator(5, SettingSyncPacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target)
            throws Exception
    {
        msg.writeBytes(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg)
    {
        msg.readBytes(source);
        msg.postProcess();
    }

}
