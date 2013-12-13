package allout58.mods.prisoncraft;

import net.minecraftforge.common.Configuration;

public class Config
{
    //Blocks
    public static int prisonManager;
    public static int prisonUnbreak;
    
    public static void init(Configuration config)
    {
        config.load();
        
        prisonManager=config.getBlock("prisonManager", 4000).getInt();
        prisonUnbreak=config.getBlock("prisonUnbreak", 4001).getInt();
        
        config.save();
    }
}
