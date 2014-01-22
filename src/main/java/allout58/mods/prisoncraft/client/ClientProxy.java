package allout58.mods.prisoncraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderGiantZombie;
import allout58.libs.LayedTextureBlock.client.BlockLayeredRenderer;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.constants.TextureConstants;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        allout58.libs.LayedTextureBlock.client.ClientProxy.registerRenderer();
    }
}
