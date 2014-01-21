package allout58.mods.prisoncraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.constants.TextureConstants;

public class ClientProxy extends CommonProxy
{
    public static int renderPass;
    
    public static int BlockMultiLayerRenderer;
    
    @Override
    public void registerRenderers()
    {
        BlockMultiLayerRenderer = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockLayeredRenderer());
    }
}
