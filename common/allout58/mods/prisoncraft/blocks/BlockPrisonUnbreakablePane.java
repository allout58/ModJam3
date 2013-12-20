package allout58.mods.prisoncraft.blocks;

import java.util.List;

import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPrisonUnbreakablePane extends BlockPane implements ITileEntityProvider
{

    public BlockPrisonUnbreakablePane(int par1, Material par2Material)
    {
        super(par1, "", "", par2Material, false);
    }


    @Override
    public TileEntity createNewTileEntity(World world)
    {
        // TODO Auto-generated method stub
        return new TileEntityPrisonUnbreakable();
    }
}
