package allout58.mods.prisoncraft.config;

import allout58.mods.prisoncraft.constants.ModConstants;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.config.Configuration;

public class Config
{
    // Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;
    public static int holdingRadius;
    // Logging options
    public static boolean logJailing;

    public static String[] unbreakIDWhitelistDefault = ModConstants.getWHITELIST_WALL_BLOCKS();

    private static boolean isInit = false;
    private static Configuration conf;

    public static void init(Configuration config)
    {
        isInit = true;
        conf = config;
        config.load();

        changeGameMode = config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory = config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement = config.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
        noJumping = config.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
        removeJailPerms = config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);

        holdingRadius = config.get("JailOptions", "HoldingRadius", 30, "Radius from TP location to allow jailed players to roam.").getInt();

        logJailing = config.get("LoggingOptions", "LogJailing", true, "Log when players are jailed").getBoolean(true);

        config.save();
    }

    public static void writeToPacket(ByteBuf buff)
    {
        buff.writeBoolean(noMovement);
        buff.writeBoolean(noJumping);
    }

    public static void readFromPacket(ByteBuf buff)
    {
        noMovement = buff.readBoolean();
        noJumping = buff.readBoolean();
    }

    public static void reload()
    {
        if (isInit)
        {
            conf.load();

            noMovement = conf.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
            noJumping = conf.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);

            conf.save();
        }
    }
}
