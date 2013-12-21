package allout58.mods.prisoncraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class PrisonCraftWorldSave extends WorldSavedData
{
    public static final String key = "prisoncraft";
    
    public static World worldObj;

    private List tesList = new ArrayList(); //int[] only!

    public PrisonCraftWorldSave()
    {
        super(key);
    }
    
    public PrisonCraftWorldSave(String keyIn)
    {
        super(PrisonCraftWorldSave.key);
    }
    
    public List getTesList()
    {
        this.markDirty();
        return tesList;
    }

    public static PrisonCraftWorldSave forWorld(World world)
    {
        worldObj=world;
        // Retrieves the PrisonCraftWorldSave instance for the given world, creating it
        // if necessary
        MapStorage storage = world.perWorldStorage;
        PrisonCraftWorldSave result = (PrisonCraftWorldSave) storage.loadData(PrisonCraftWorldSave.class, key);
        if (result == null)
        {
            result = new PrisonCraftWorldSave();
            storage.setData(key, result);
        }
        return result;
    }

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        if (tags.hasKey("NumTEs"))
        {
            int size = tags.getInteger("NumTEs");
            for(int i=0;i<size;i++)
            {
                NBTTagCompound t=tags.getCompoundTag(i+"");
                int coord[]=t.getIntArray("coord");
                
                tesList.add(coord);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        tags.setInteger("NumTEs", tesList.size());
        for (int i = 0; i < tesList.size(); i++)
        {
            NBTTagCompound t = new NBTTagCompound();
            t.setIntArray("coord", (int[]) tesList.get(i));
            tags.setCompoundTag(i + "", t);// HACKY! :D
        }
    }

}
