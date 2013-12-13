package allout58.mods.prisoncraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
            stack.stackTagCompound = null;
            this.setDamage(stack, 0);
        }
        return stack;
    }

}
