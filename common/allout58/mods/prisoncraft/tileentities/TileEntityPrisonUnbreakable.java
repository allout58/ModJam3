package allout58.mods.prisoncraft.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreakable extends TileEntity
{
    private int blockID=1;
   
    private Boolean isDirty=false;
    
    public int getFakeBlockID()
    {
        return blockID;
    }
    
    public void setFakeBlockID(int id)
    {
        blockID=id;
        isDirty=true;
    }
    
    public void wallBroken(int x, int y, int z)
    {
        //reset broken block
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
    
    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        blockID=tags.getInteger("blockID");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setInteger("blockID",blockID);
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
