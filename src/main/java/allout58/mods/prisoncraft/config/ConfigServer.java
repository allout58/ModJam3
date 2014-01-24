package allout58.mods.prisoncraft.config;

import net.minecraftforge.common.Configuration;

public class ConfigServer
{
    // Config options
    public static boolean changeGameMode;
    public static boolean takeInventory;
    public static boolean noMovement;
    public static boolean noJumping;
    public static boolean removeJailPerms;
    // Loggin options
    public static boolean logJailing;

    public static void init(Configuration config)
    {
        config.load();
        changeGameMode = config.get("JailOptions", "ChangePlayerGameMode", true).getBoolean(true);
        takeInventory = config.get("JailOptions", "TakePlayerInventory", true).getBoolean(true);
        noMovement = config.get("JailOptions", "AllowNoPlayerMovement", true).getBoolean(true);
        noJumping = config.get("JailOptions", "AllowNoPlayerJumping", false, "This feature is very buggy. Use at your own risk.").getBoolean(false);
        removeJailPerms = config.get("JailOptions", "RemoveJailedPlayerJailPerms", true).getBoolean(true);

        logJailing = config.get("LoggingOptions", "LogJailing", true, "Log when players are jailed").getBoolean(true);
        config.save();
    }
}
