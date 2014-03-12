package allout58.mods.prisoncraft.config;

import net.minecraftforge.common.config.Configuration;
import allout58.mods.prisoncraft.constants.ModConstants;

public class Config
{
//	public static final String BLOCKCAT="Blocks";
//	public static final String ITEMCAT="Items";
	
//    // Blocks
//    public static int prisonManager;
//    public static int prisonUnbreak;
//    public static int prisonUnbreakGlass;
//    public static int prisonUnbreakPaneGlass;
//    public static int prisonUnbreakPaneIron;
//    public static int prisonJailView;
//    // Items
//    public static int configWand;
//    public static int banhammer;
//    public static int oliveBranch;
//    public static int jailLink;
    
    // Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;
    // Logging options
    public static boolean logJailing;

    public static int[] unbreakIDWhitelistDefault;
    
    private static boolean isInit=false;
    private static Configuration conf;

    public static void init(Configuration config)
    {
        isInit=true;
        conf=config;
        config.load();

//        int startBlock = 4000;
//        
//        prisonManager = config.get(BLOCKCAT,"prisonManager", startBlock++).getInt();
//        prisonUnbreak = config.get(BLOCKCAT,"prisonUnbreak", startBlock++).getInt();
//        prisonUnbreakGlass = config.get(BLOCKCAT,"prisonUnbreakGlass", startBlock++).getInt();
//        prisonUnbreakPaneGlass = config.get(BLOCKCAT,"prisonUnbreakPaneGlass", startBlock++).getInt();
//        prisonUnbreakPaneIron = config.get(BLOCKCAT,"prisonUnbreakPaneIron", startBlock++).getInt();
//        prisonJailView = config.get(BLOCKCAT,"prisonJailView", startBlock++).getInt();
//
//        int startItem = 8000;
//        configWand = config.get(ITEMCAT,"configWand", startItem++).getInt();
//        banhammer = config.get(ITEMCAT,"banhammer", startItem++).getInt();
//        oliveBranch = config.get(ITEMCAT,"oliveBranch", startItem++).getInt();
//        jailLink = config.get(ITEMCAT,"jailLink", startItem++).getInt();

        changeGameMode = config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory = config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement = config.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
        noJumping = config.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
        removeJailPerms = config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);

        logJailing = config.get("LoggingOptions", "LogJailing", true, "Log when players are jailed").getBoolean(true);
        
//        unbreakIDWhitelistDefault = config.get("WallWhitelistDefault", "DefaultBlockIDS", ModConstants.WHITELIST_WALL_IDS, "Currently, most non-full blocks will render incorrectly. You have been warned.").getIntList();
//        config.addCustomCategoryComment("WallWhiteListDefault", "This is the default for each world. As of version 0.0.3, this is configurable in game on a per world basis");

        config.save();
    }

    public static void reload()
    {
        if(isInit)
        {
            conf.load();
            
            noMovement = conf.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
            noJumping = conf.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
                        
            conf.save();
        }
    }
}
