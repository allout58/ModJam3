package allout58.mods.prisoncraft.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import allout58.mods.prisoncraft.CommonProxy;
import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.constants.TextureConstants;
import allout58.mods.prisoncraft.jail.JailMan;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBanHammer extends Item
{
    public ItemBanHammer(int id)
    {
        super(id);
        setUnlocalizedName("banhammer");
        setCreativeTab(PrisonCraft.creativeTab);
        setMaxStackSize(1);
        setTextureName(TextureConstants.RESOURCE_CONTEXT + ":" + getUnlocalizedName().substring(5));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            float f = 1.0F;
            float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
            float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
            double d = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
            double d1 = (player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.6200000000000001D) - (double) player.yOffset;
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
            Vec3 vec3d = Vec3.createVectorHelper(d, d1, d2);
            float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
            float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
            float f5 = -MathHelper.cos(-f1 * 0.01745329F);
            float f6 = MathHelper.sin(-f1 * 0.01745329F);
            float f7 = f4 * f5;
            float f8 = f6;
            float f9 = f3 * f5;
            double d3 = 5000D;
            Vec3 vec3d1 = vec3d.addVector((double) f7 * d3, (double) f8 * d3, (double) f9 * d3);
            MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do_do(vec3d, vec3d1, false, true);

            if (movingobjectposition == null)
            {
                return stack;
            }
            if (movingobjectposition.typeOfHit == EnumMovingObjectType.ENTITY)
            {
                if (movingobjectposition.entityHit instanceof EntityPlayer)
                {
                    player.swingItem();
                    JailMan.TryJailPlayer((EntityPlayer) movingobjectposition.entityHit, player, .25);
                }
            }
        }
        return stack;

    }

    @Override
    @SideOnly(Side.CLIENT)
    /** Allows items to add custom lines of information to the mouseover description. */
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List infoList, boolean par4)
    {
        // TODO See if this can be localized
        if (CommonProxy.shouldAddAdditionalInfo())
        {
            infoList.add("Use this tool to send the player you are looking at to jail!");
        }
        else
        {
            infoList.add(CommonProxy.additionalInfoInstructions());
        }
    }
}
