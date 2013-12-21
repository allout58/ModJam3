package allout58.mods.prisoncraft;

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
    // Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;

    public static int[] unbreakIDWhitelist;

    public static void init(Configuration config)
    {
        config.load();

        int startBlock = 4000;

        prisonManager = config.getBlock("prisonManager", startBlock++).getInt();
        prisonUnbreak = config.getBlock("prisonUnbreak", startBlock++).getInt();
        prisonUnbreakGlass = config.getBlock("prisonUnbreakGlass", startBlock++).getInt();
        prisonUnbreakPaneGlass = config.getBlock("prisonUnbreakPaneGlass", startBlock++).getInt();
        prisonUnbreakPaneIron = config.getBlock("prisonUnbreakPaneIron", startBlock++).getInt();

        configWand = config.getItem("configWand", 8000).getInt();

        changeGameMode = config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory = config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement = config.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
        noJumping = config.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
        removeJailPerms = config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);

        unbreakIDWhitelist = config.get("WallWhitelist", "BlockIDS", ModConstants.WHITELIST_WALL_IDS, "Currently, non-full blocks will render incorrectly. You have been warned.").getIntList();

        config.save();
    }
}
