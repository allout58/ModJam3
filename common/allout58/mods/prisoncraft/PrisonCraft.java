package allout58.mods.prisoncraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.commands.JailCommand;
import allout58.mods.prisoncraft.commands.UnJailCommand;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.tileentities.TileEntityList;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModConstants.MODID, name = ModConstants.NAME, version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
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

//        @Override
//        public String getTranslatedTabLabel()
//        {
//            return StringUtils.localize("strings.Title");
//        }
    };
    
    @Instance("PrisonCraft")
    public static PrisonCraft instance;
    
    @SidedProxy(clientSide = "allout58.mods.prisoncraft.client.ClientProxy", serverSide = "allout58.mods.prisoncraft.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
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
    }
}
