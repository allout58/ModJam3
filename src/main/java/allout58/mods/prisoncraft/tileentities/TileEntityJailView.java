package allout58.mods.prisoncraft.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityJailView extends TileEntity
{
    public String jailname = "NULL";

    private boolean isDirty = false;

    public boolean setJailName(String name)
    {
        if (jailname == "NULL")
        {
            jailname = name;
            isDirty = true;
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
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    /* NBT */
    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        jailname = tags.getString("jailname");
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setString("jailname", jailname);
    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}
