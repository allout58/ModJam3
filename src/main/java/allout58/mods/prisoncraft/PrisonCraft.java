package allout58.mods.prisoncraft;

import java.io.File;
import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.commands.ChangeJailPermsCommand;
import allout58.mods.prisoncraft.commands.JailCommand;
import allout58.mods.prisoncraft.commands.PermLevelCommand;
import allout58.mods.prisoncraft.commands.PrisonCraftCommand;
import allout58.mods.prisoncraft.commands.ReasonCommand;
import allout58.mods.prisoncraft.commands.UnJailCommand;
import allout58.mods.prisoncraft.config.Config;
import allout58.mods.prisoncraft.config.ConfigChangableBlocks;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.handler.ConfigToolHighlightHandler;
import allout58.mods.prisoncraft.handler.IMCHandler;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.jail.JailMan;
import allout58.mods.prisoncraft.network.ChannelHandler;
import allout58.mods.prisoncraft.permissions.JailPermissions;
import allout58.mods.prisoncraft.permissions.PermissionLevel;
import allout58.mods.prisoncraft.tileentities.TileEntityList;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModConstants.MODID, name = ModConstants.NAME)
//@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels={ModConstants.JAIL_PACKET_CHANNEL,ModConstants.UNJAIL_PACKET_CHANNEL, ModConstants.JV_CLIENT_TO_SERVER_PACKET_CHANNEL,ModConstants.JV_SERVER_TO_CLIENT_PACKET_CHANNEL}, packetHandler = PacketHandler.class)
public class PrisonCraft
{
    public static EnumMap<Side, FMLEmbeddedChannel> channels;
  
    public static CreativeTabs creativeTab = new CreativeTabs("PrisonCraft")
    {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Items.chainmail_chestplate;
        }
    };

    @Instance(ModConstants.MODID)
    public static PrisonCraft instance;

    @SidedProxy(clientSide = "allout58.mods.prisoncraft.client.ClientProxy", serverSide = "allout58.mods.prisoncraft.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;
    
//    private File configBase;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        channels=NetworkRegistry.INSTANCE.newChannel(ModConstants.MODID, new ChannelHandler());
        proxy.registerRenderers();
        logger=event.getModLog();

        Config.init(new Configuration(event.getSuggestedConfigurationFile()));
//        configBase=event.getModConfigurationDirectory();
        
        MinecraftForge.EVENT_BUS.register(new ConfigToolHighlightHandler());

        BlockList.init();
        ItemList.init();
        TileEntityList.init();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        FMLInterModComms.sendMessage("prisoncraft", "blacklist", Block.blockRegistry.getNameForObject(Blocks.bookshelf));
    }
    
    @EventHandler
    public void handleIMC(IMCEvent event)
    {
        IMCHandler.HandleIMC(event.getMessages());
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new JailCommand());
        event.registerServerCommand(new UnJailCommand());
        event.registerServerCommand(new ChangeJailPermsCommand());
        event.registerServerCommand(new PrisonCraftCommand());
        event.registerServerCommand(new PermLevelCommand());
        event.registerServerCommand(new ReasonCommand());
        //event.registerServerCommand(new JamCraftCommand());
        SaveHandler saveHandler = (SaveHandler) event.getServer().worldServerForDimension(0).getSaveHandler();
        File configFile = new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/PCUnbreakableIDs.txt");
        ConfigChangableBlocks.getInstance().load(configFile);
        
//        ConfigServer.init(new Configuration(new File(configBase,ModConstants.MODID+"-server.cfg")));
        
        if (Config.logJailing)
        {
            File jailRecordFile = new File(saveHandler.getWorldDirectory().getAbsolutePath() + "/JailingRecord.csv");
            JailMan.getInstance().initializeRecorder(jailRecordFile);
        }
        JailPermissions.getInstance().load();
        // Grant full jail perms on singleplayer
        if (event.getServer().isSinglePlayer())
        {
            logger.info("Single-player world: adding player with server-level permissions");
            JailPermissions.getInstance().addUserPlayer(event.getServer().getServerOwner(), PermissionLevel.FinalWord);
        }
    }

    @EventHandler
    public void serverUnload(FMLServerStoppingEvent event)
    {
        ConfigChangableBlocks.getInstance().save();
        JailPermissions.getInstance().save();
    }
}
