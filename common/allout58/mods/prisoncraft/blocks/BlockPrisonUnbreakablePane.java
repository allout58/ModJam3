package allout58.mods.prisoncraft.blocks;

import java.util.List;
import java.util.Random;

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

    public BlockPrisonUnbreakablePane(int blockID, String str1, String str2, Material par2Material)
    {
        super(blockID, str1, str2, par2Material, false);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightValue(.2F);
        setUnlocalizedName("prisonUnbreakablePane");
    }
  
    @Override
    public boolean canPaneConnectTo(IBlockAccess access, int x, int y, int z, ForgeDirection dir)
    {
        TileEntity te=access.getBlockTileEntity(x, y, z);
        int bId=-1;
        if(te instanceof TileEntityPrisonUnbreakable)
        {
            bId=((TileEntityPrisonUnbreakable)te).getFakeBlockID();
        }
        return super.canPaneConnectTo(access, x, y, z, dir) || access.getBlockId(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ)==bId;
    }
        
    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }
    
    @Override
    protected boolean canSilkHarvest()
    {
        return false;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int oldID, int oldMeta)
    {
        TileEntity logic = world.getBlockTileEntity(x, y, z);
        if (logic instanceof TileEntityPrisonUnbreakable)
        {
            if (((TileEntityPrisonUnbreakable) logic).canDestroy())
            {
                super.breakBlock(world, x, y, z, oldID, oldMeta);
            }
            else
            {
                int fakeID=((TileEntityPrisonUnbreakable)logic).getFakeBlockID();
                super.breakBlock(world, x, y, z, oldID, oldMeta);
                world.setBlock(x, y, z, oldID, oldMeta, 3);
                TileEntity te = world.getBlockTileEntity(x, y, z);
                if(te instanceof TileEntityPrisonUnbreakable)
                {
                    ((TileEntityPrisonUnbreakable)te).setFakeBlockID(fakeID);
                }
            }
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        // TODO Auto-generated method stub
        return new TileEntityPrisonUnbreakable();
    }
}
