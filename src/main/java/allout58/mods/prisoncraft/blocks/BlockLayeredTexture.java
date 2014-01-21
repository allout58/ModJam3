package allout58.mods.prisoncraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.client.ClientProxy;
import allout58.mods.prisoncraft.constants.TextureConstants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public abstract class BlockLayeredTexture extends Block
{
    public Icon blank;
    
    public BlockLayeredTexture(int par1, Material par2Material)
    {
        super(par1, par2Material);
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public int getRenderType()
    {
        return ClientProxy.BlockMultiLayerRenderer;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass=pass;
        return true;
    }
    
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
        this.blank=ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":blank");
    }
    
    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        return this.getBlockTextureByPass(world, x, y, z, side,0);
    }
    
    /**
     * Gets the texture for a side for a render pass
     * @param world
     * @param x
     * @param y
     * @param z
     * @param side
     * @param pass
     * @return The icon to render
     */
    public Icon getBlockTextureByPass(IBlockAccess world, int x, int y, int z, int side, int pass)
    {
        return this.blank;
    }
}
