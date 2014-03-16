package allout58.mods.prisoncraft.network;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.items.ItemBanHammer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class UpdateHammerPacket implements IPacket
{
    private String uname = "";
    private String jailname = "";
    private double jTime = -1;

    public UpdateHammerPacket()
    {
    }

    public UpdateHammerPacket(String name, String jail, double time)
    {
        uname = name;
        jailname = jail;
        jTime = time;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        uname=NetworkUtils.readString(bytes);
        jailname=NetworkUtils.readString(bytes);
        jTime=bytes.readDouble();
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        NetworkUtils.writeString(bytes, uname);
        NetworkUtils.writeString(bytes, jailname);
        bytes.writeDouble(jTime);
    }

    @Override
    public void postProcess()
    {
        EntityPlayer player=MinecraftServer.getServer().worldServers[0].getPlayerEntityByName(uname);
        if(player!=null)
        {
            ItemStack is=player.getCurrentEquippedItem();
            if(is.getItem() instanceof ItemBanHammer)
            {
                NBTTagCompound tag=is.getTagCompound();
                if(tag==null)
                {
                    tag=new NBTTagCompound();
                }
                tag.setString("jailname", jailname);
                tag.setDouble("time", jTime);
                is.setTagCompound(tag);
            }
            else
            {
                PrisonCraft.logger.error("Client-server desync. Ignoring Banhammer update packet from GUI.");
            }
        }
    }

}
