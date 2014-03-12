package allout58.libs.LayeredTextureBlock.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import allout58.mods.prisoncraft.constants.TextureConstants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockLayeredTexture extends Block
{
    /**
     * The blocks current render pass (0=base,1=overlay).
     */
    public static int renderPass;

    /**
     * Icon used when a side doesn't need to be rendered on a pass.
     */
    public IIcon blank = new IIcon()
    {
        @Override
        @SideOnly(Side.CLIENT)
        public int getIconWidth()
        {

            return 16;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int getIconHeight()
        {
            return 16;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getMinU()
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getMaxU()
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getInterpolatedU(double d0)
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getMinV()
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getMaxV()
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getInterpolatedV(double d0)
        {
            return 0;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getIconName()
        {
            return "LayeredTexture_blank";
        }

    };

    public BlockLayeredTexture( Material par2Material)
    {
        super(par2Material);
    }

    @Override
    public final boolean canRenderInPass(int pass)
    {
        BlockLayeredTexture.renderPass = pass;
        return true;
    }

    @Override
    public final int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
        this.blank = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":blank");
    }
}
