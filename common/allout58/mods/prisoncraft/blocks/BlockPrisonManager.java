package allout58.mods.prisoncraft.blocks;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.TextureConstants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class BlockPrisonManager extends Block
{
    public Icon top, bottom, side;

    public BlockPrisonManager(int par1, Material par2Material)
    {
        super(par1, par2Material);
        setHardness(200.0F);
        setUnlocalizedName("prisonManager");
        setCreativeTab(PrisonCraft.creativeTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        if (side == 1) return this.top;
        if (side == 0) return this.bottom;
        return this.side;
    }

    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        if (side == ForgeDirection.DOWN.ordinal()) return this.bottom;
        if (side == ForgeDirection.UP.ordinal()) return this.top;
        return this.side;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
        this.side = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.bottom = this.top = ir.registerIcon(TextureConstants.RESOURCE_CONTEXT + ":"+this.getUnlocalizedName().substring(5) + "_top_bottom");
    }
}
