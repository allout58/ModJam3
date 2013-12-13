package allout58.mods.prisoncraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class PrisonCraftWorldSave extends WorldSavedData
{
    public static final String key = "prisoncraft";

    public PrisonCraftWorldSave()
    {
        super(key);
    }

    public static PrisonCraftWorldSave forWorld(World world)
    {
        // Retrieves the MyWorldData instance for the given world, creating it
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
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        // TODO Auto-generated method stub

    }

}
