package allout58.mods.prisoncraft.jail;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonManager;

public class JailedPersonData
{
    public String jail="";
    public String name = "";
    public int time = -1;
    public String reason = "";
    public int coord[];

    public void updateTime(World worldObj)
    {
        TileEntity te=worldObj.getBlockTileEntity(coord[0], coord[1], coord[2]);
        if(te instanceof TileEntityPrisonManager)
        {
            time=((TileEntityPrisonManager)te).secsLeftJailTime;
        }
    }
}
