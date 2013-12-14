package allout58.mods.prisoncraft.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreakable extends TileEntity
{
    private int blockID=1;
   
    private Boolean isDirty=false;
    
    public int getBlockID()
    {
        return blockID;
    }
    
    public void setBlockID(int id)
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
}
