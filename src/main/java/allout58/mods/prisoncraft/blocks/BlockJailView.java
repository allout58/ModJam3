package allout58.mods.prisoncraft.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import allout58.libs.LayeredTextureBlock.block.BlockLayeredTexture;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.tileentities.TileEntityJailView;

public class BlockJailView extends BlockLayeredTexture implements ITileEntityProvider
{
    public Icon top, bottom, side, side_nolink;

    public BlockJailView(int par1, Material par2Material)
    {
        super(par1, par2Material);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setUnlocalizedName("jailView");
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + this.getUnlocalizedName().substring(5) + "_side");
        setCreativeTab(PrisonCraft.creativeTab);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityJailView();
    }

}
