package allout58.mods.prisoncraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemConfigWand extends Item
{

    public ItemConfigWand(int id)
    {
        super(id);
        setUnlocalizedName("prisonConfigWand");
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            //reset
            stack.stackTagCompound = null;
        }
        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if(!stack.stackTagCompound.hasKey("x1"))
        {
            stack.stackTagCompound.setInteger("x1", x);
            stack.stackTagCompound.setInteger("y1", y);
            stack.stackTagCompound.setInteger("z1", z);
        }
        else if(!stack.stackTagCompound.hasKey("x2"))
        {
            stack.stackTagCompound.setInteger("x2", x);
            stack.stackTagCompound.setInteger("y2", y);
            stack.stackTagCompound.setInteger("z2", z);
            System.out.println("1:{" + stack.stackTagCompound.getInteger("x1") +", " + stack.stackTagCompound.getInteger("y1") +", "+ stack.stackTagCompound.getInteger("z1")+"} 2:{"+stack.stackTagCompound.getInteger("x2")+", "+stack.stackTagCompound.getInteger("y2")+", "+stack.stackTagCompound.getInteger("z2")+"}");
        }
        
        return true;
    }

}
