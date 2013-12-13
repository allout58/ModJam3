package allout58.mods.prisoncraft;

import net.minecraftforge.common.Configuration;

public class Config
{
    //Blocks
    public static int prisonManager;
    public static int prisonUnbreak;
    //Items
    public static int configWand;
    
    public static void init(Configuration config)
    {
        config.load();
        
        prisonManager=config.getBlock("prisonManager", 4000).getInt();
        prisonUnbreak=config.getBlock("prisonUnbreak", 4001).getInt();
        
        configWand=config.getItem("configWand", 8000).getInt();
        
        config.save();
    }
}
