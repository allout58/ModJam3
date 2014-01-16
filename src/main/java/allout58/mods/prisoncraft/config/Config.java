package allout58.mods.prisoncraft.config;

import allout58.mods.prisoncraft.constants.ModConstants;
import net.minecraftforge.common.Configuration;

public class Config
{
    // Blocks
    public static int prisonManager;
    public static int prisonUnbreak;
    public static int prisonUnbreakGlass;
    public static int prisonUnbreakPaneGlass;
    public static int prisonUnbreakPaneIron;
    // Items
    public static int configWand;
    public static int banhammer;
    public static int oliveBranch;
    // Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;
    //Loggin options
    public static boolean logJailing;

    public static int[] unbreakIDWhitelistDefault;

    public static void init(Configuration config)
    {
        config.load();

        int startBlock = 4000;

        prisonManager = config.getBlock("prisonManager", startBlock++).getInt();
        prisonUnbreak = config.getBlock("prisonUnbreak", startBlock++).getInt();
        prisonUnbreakGlass = config.getBlock("prisonUnbreakGlass", startBlock++).getInt();
        prisonUnbreakPaneGlass = config.getBlock("prisonUnbreakPaneGlass", startBlock++).getInt();
        prisonUnbreakPaneIron = config.getBlock("prisonUnbreakPaneIron", startBlock++).getInt();

        int startItem = 8000;
        configWand = config.getItem("configWand", startItem++).getInt();
        banhammer = config.getItem("banhammer", startItem++).getInt();
        oliveBranch = config.getItem("oliveBranch", startItem++).getInt();

        changeGameMode = config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory = config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement = config.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
        noJumping = config.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
        removeJailPerms = config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);
        
        logJailing=config.get("LoggingOptions","LogJailing",true,"Log when players are jailed").getBoolean(true);
        
        unbreakIDWhitelistDefault = config.get("WallWhitelistDefault", "DefaultBlockIDS", ModConstants.WHITELIST_WALL_IDS, "Currently, most non-full blocks will render incorrectly. You have been warned.").getIntList();
        config.addCustomCategoryComment("WallWhiteListDefault", "This is the default for each world. As of version 0.0.3, this is configurable in game on a per world basis");

        config.save();
    }
}
