package allout58.mods.prisoncraft.tileentities;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreakable extends TileEntity
{
//    private int fakeBlockID = 1;
    private Block fakeBlock=Blocks.stone;
    private int fakeBlockMeta = 0;
    private boolean isReverting = false;

    private Boolean isDirty = false;

    public Block getFakeBlock()
    {
        return fakeBlock;
    }

    public void setFakeBlock(Block block)
    {
        fakeBlock=block;
        isDirty = true;
    }

    public int getFakeBlockMeta()
    {
        return fakeBlockMeta;
    }

    public void setFakeBlockMeta(int meta)
    {
        fakeBlockMeta = meta;
        isDirty = true;
    }

    public void revert()
    {
        isReverting = true;
        worldObj.setBlock(xCoord, yCoord, zCoord, fakeBlock, fakeBlockMeta, 3);
    }

    public boolean canDestroy()
    {
        return isReverting;
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

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        super.readFromNBT(tags);
        String name=tags.getString("fakeBlockName");
        fakeBlock = Block.getBlockFromName(name);
        fakeBlockMeta = tags.getInteger("fakeBlockMeta");
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        super.writeToNBT(tags);
        tags.setString("fakeBlockName",Block.blockRegistry.getNameForObject(fakeBlock)); 
        tags.setInteger("fakeBlockMeta", fakeBlockMeta);
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
