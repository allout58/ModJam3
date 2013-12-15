package allout58.mods.prisoncraft.tileentities;

import allout58.mods.prisoncraft.blocks.BlockList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreakable extends TileEntity
{
    private int fakeBlockID=1;
    private boolean isReverting=false;
   
    private Boolean isDirty=false;
    
    public int getFakeBlockID()
    {
        return fakeBlockID;
    }
    
    public void setFakeBlockID(int id)
    {
        fakeBlockID=id;
        isDirty=true;
    }
    
    public void revert()
    {
        isReverting=true;
        worldObj.setBlock(xCoord, yCoord, zCoord, fakeBlockID, 0, 3);
    }
    
    public boolean canDestroy()
    {
        return !isReverting;
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
        super.readFromNBT(tags);
        fakeBlockID=tags.getInteger("fakeBlockID");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setInteger("fakeBlockID",fakeBlockID);
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
