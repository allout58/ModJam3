package allout58.mods.prisoncraft;

import java.io.File;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.Configuration;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.commands.ChangableIdConfigCommand;
import allout58.mods.prisoncraft.commands.ChangeJailPermsCommand;
import allout58.mods.prisoncraft.commands.JailCommand;
import allout58.mods.prisoncraft.commands.PermLevelCommand;
import allout58.mods.prisoncraft.commands.UnJailCommand;
import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.config.ConfigChangableIDs;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import allout58.mods.prisoncraft.tileentities.TileEntityList;
import allout58.mods.prisoncraft.network.PacketHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version = "0.0.2b")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels={ModConstants.JAILPACKETCHANNEL,ModConstants.UNJAILPACKETCHANNEL}, packetHandler = PacketHandler.class)
public class PrisonCraft
{
    public static CreativeTabs creativeTab = new CreativeTabs("PrisonCraft")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.plateChain;
        }
    };

    @Instance(ModConstants.MODID)
    public static PrisonCraft instance;

    @SidedProxy(clientSide = "allout58.mods.prisoncraft.client.ClientProxy", serverSide = "allout58.mods.prisoncraft.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = Logger.getLogger(ModConstants.MODID);
        logger.setParent(FMLLog.getLogger());

        Config.init(new Configuration(event.getSuggestedConfigurationFile()));

        BlockList.init();
        ItemList.init();
        TileEntityList.init();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new JailCommand());
        event.registerServerCommand(new UnJailCommand());
        event.registerServerCommand(new ChangeJailPermsCommand());
        event.registerServerCommand(new ChangableIdConfigCommand());
        event.registerServerCommand(new PermLevelCommand());
        SaveHandler saveHandler = (SaveHandler) event.getServer().worldServerForDimension(0).getSaveHandler();
        File configFile = new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/PCUnbreakableIDs.txt");
        ConfigChangableIDs.getInstance().load(configFile);
        if (Config.logJailing)
        {
            File jailRecordFile = new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/JailingRecord.csv");
            JailMan.getInstance().initializeRecorder(jailRecordFile);
        }
        JailPermissions.getInstance().load();
        // Grant full jail perms on singleplayer
        if (event.getServer().isSinglePlayer())
        {
            JailPermissions.getInstance().addUserPlayer(event.getServer().getServerOwner(), PermissionLevel.FinalWord);
        }
    }

    @EventHandler
    public void serverUnload(FMLServerStoppingEvent event)
    {
        ConfigChangableIDs.getInstance().save();
        JailPermissions.getInstance().save();
    }
}
