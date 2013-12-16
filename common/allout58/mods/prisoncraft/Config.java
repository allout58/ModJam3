package allout58.mods.prisoncraft;

import net.minecraftforge.common.Configuration;

public class Config
{
    //Blocks
    public static int prisonManager;
    public static int prisonUnbreak;
    //Items
    public static int configWand;
    //Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;
    
    public static void init(Configuration config)
    {
        config.load();
        
        prisonManager=config.getBlock("prisonManager", 4000).getInt();
        prisonUnbreak=config.getBlock("prisonUnbreak", 4001).getInt();
        
        configWand=config.getItem("configWand", 8000).getInt();
        
        changeGameMode=config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory=config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement=config.get("JailOptions","AllowNoPlayerMovement",true).getBoolean(true);
        noJumping=config.get("JailOptions","AllowNoPlayerJumping",true).getBoolean(true);
        removeJailPerms=config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);
        
        config.save();
    }
}
