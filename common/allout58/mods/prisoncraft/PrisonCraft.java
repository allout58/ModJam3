package allout58.mods.prisoncraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import allout58.mods.prisoncraft.blocks.BlockList;
import allout58.mods.prisoncraft.constants.ModConstants;
import allout58.mods.prisoncraft.items.ItemList;
import allout58.mods.prisoncraft.tileentities.TileEntityList;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Config.init(new Configuration(event.getSuggestedConfigurationFile()));
        
        BlockList.init();
        ItemList.init();
        TileEntityList.init();
    }
}
