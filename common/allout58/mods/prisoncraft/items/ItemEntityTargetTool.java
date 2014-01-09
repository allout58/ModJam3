package allout58.mods.prisoncraft.items;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.world.World;

public class ItemEntityTargetTool extends Item
{

    public ItemEntityTargetTool(int id)
    {
        super(id - ModConstants.ITEM_ID_DIFF);
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
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY)
            {
                if (mc.objectMouseOver.entityHit instanceof EntityPlayer)
                {
                    player.swingItem();
                    if (!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
                    stack.stackTagCompound.setString("userHit", ((EntityPlayer) mc.objectMouseOver.entityHit).username);
                }
            }
        }
        return stack;

    }
}
