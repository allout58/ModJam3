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
    public static int prisonJailView;
    // Items
    public static int configWand;
    public static int banhammer;
    public static int oliveBranch;
    public static int jailLink;

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
        prisonJailView = config.getBlock("prisonJailView", startBlock++).getInt();

        int startItem = 8000;
        configWand = config.getItem("configWand", startItem++).getInt();
        banhammer = config.getItem("banhammer", startItem++).getInt();
        oliveBranch = config.getItem("oliveBranch", startItem++).getInt();
        jailLink = config.getItem("jailLink", startItem++).getInt();

       
        
        unbreakIDWhitelistDefault = config.get("WallWhitelistDefault", "DefaultBlockIDS", ModConstants.WHITELIST_WALL_IDS, "Currently, most non-full blocks will render incorrectly. You have been warned.").getIntList();
        config.addCustomCategoryComment("WallWhiteListDefault", "This is the default for each world. As of version 0.0.3, this is configurable in game on a per world basis");

        config.save();
    }
}
