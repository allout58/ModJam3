package allout58.mods.prisoncraft.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonUnbreak extends TileEntity
{
    private int fakeBlockID=2;
    
    public int getFakeBlockID()
    {
        return fakeBlockID;
    }
    
    public void setFakeBlockID(int id)
    {
        fakeBlockID=id;
    }
    
}
