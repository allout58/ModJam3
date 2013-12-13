package allout58.mods.prisoncraft.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreakable extends TileEntity
{
    private int blockID=1;
    
    public int getBlockID()
    {
        return blockID;
    }
    
    public void wallBroken(int x, int y, int z)
    {
        //reset broken block
    }
}
