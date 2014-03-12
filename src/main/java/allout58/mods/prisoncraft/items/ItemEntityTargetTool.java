package allout58.mods.prisoncraft.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import allout58.mods.prisoncraft.PrisonCraft;

public class ItemEntityTargetTool extends Item
{

    public ItemEntityTargetTool()
    {
        super();
        setCreativeTab(PrisonCraft.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    public boolean getShareTag()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY)
            {
                if (mc.objectMouseOver.entityHit instanceof EntityPlayer)
                {
                    player.swingItem();
                    if (!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
                    stack.stackTagCompound.setString("userHit", ((EntityPlayer) mc.objectMouseOver.entityHit).getDisplayName());
                }
            }
        }

        return stack;

    }
}
