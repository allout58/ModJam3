package allout58.mods.prisoncraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket>
{
    public ChannelHandler()
    {
        addDiscriminator(0, JailPacket.class);
        addDiscriminator(1, UnjailPacket.class);
        addDiscriminator(2, JVRequestPacket.class);
        addDiscriminator(3, JVSendPersonPacket.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception
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
