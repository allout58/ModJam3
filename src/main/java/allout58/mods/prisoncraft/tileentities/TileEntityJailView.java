package allout58.mods.prisoncraft.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityJailView extends TileEntity
{
    public String jailname;

    private boolean isDirty = false;

    public boolean setJailName(String name)
    {
        if (!(jailname != null && !jailname.isEmpty()))
        {
            jailname = name;
            this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void updateEntity()
    {
        if (isDirty)
        {
            isDirty = false;
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
        }
    }

    /* NBT */
    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        if(tags.hasKey("jailname"))
        {
            jailname=tags.getString("jailname");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        if(jailname!=null && !jailname.isEmpty())
        {
            tags.setString("jailname", jailname);
        }
    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }
}
