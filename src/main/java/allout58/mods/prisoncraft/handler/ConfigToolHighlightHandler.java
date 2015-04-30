package allout58.mods.prisoncraft.handler;

import allout58.mods.prisoncraft.config.ConfigChangableBlocks;
import allout58.mods.prisoncraft.items.ItemConfigWand;
import allout58.mods.prisoncraft.tileentities.TileEntityPrisonUnbreakable;
import allout58.mods.prisoncraft.util.RenderHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ConfigToolHighlightHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        //Some source and many ideas come from ProfMobius and his mod Opis. Thanks!

        if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null)
        {
            if (!(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemConfigWand))
                return;
            if (!(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().hasTagCompound()))
                return;

            NBTTagCompound stackTags = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().stackTagCompound;
            double partialTicks = event.partialTicks;

            EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;

            final double offset = 0.05;

            if (stackTags.hasKey("tpIn"))
            {
                int coord[] = stackTags.getIntArray("tpIn");
                RenderHelper.DrawColoredBigBoxInWorld(player, partialTicks, coord[0], coord[1], coord[2],coord[0],coord[1]+1,coord[2], offset, 255, 255, 255, 200);
            }

            if (stackTags.hasKey("tpOut"))
            {
                int coord[] = stackTags.getIntArray("tpOut");
                RenderHelper.DrawColoredBigBoxInWorld(player, partialTicks, coord[0], coord[1], coord[2], coord[0],coord[1]+1,coord[2],offset, 0, 255, 0, 125);
            }

            if (stackTags.hasKey("jailCoord1") && !stackTags.hasKey("jailCoord2"))
            {
                int coord[] = stackTags.getIntArray("jailCoord1");
                RenderHelper.DrawColoredSingleBoxInWorld(player, partialTicks, coord[0], coord[1], coord[2], offset, 0, 0, 175, 100);
            }
            if (stackTags.hasKey("jailCoord2"))
            {
                int coord[] = stackTags.getIntArray("jailCoord1");
                int coord2[] = stackTags.getIntArray("jailCoord2");
                RenderHelper.DrawColoredBigBoxInWorld(player, partialTicks, coord[0], coord[1], coord[2], coord2[0], coord2[1], coord2[2], offset, 0, 0, 175, 100);

                //Find unusable blocks
                // give xyz names
                int x1 = coord[0];
                int y1 = coord[1];
                int z1 = coord[2];
                int x2 = coord2[0];
                int y2 = coord2[1];
                int z2 = coord2[2];
                // force ..1 to be lower than ..2
                if (x1 > x2)
                {
                    x1 += x2;
                    x2 = x1 - x2;
                    x1 -= x2;
                }
                if (y1 > y2)
                {
                    y1 += y2;
                    y2 = y1 - y2;
                    y1 -= y2;
                }
                if (z1 > z2)
                {
                    z1 += z2;
                    z2 = z1 - z2;
                    z1 -= z2;
                }
                // loop through each block
                for (int i = x1; i <= x2; i++)
                {
                    for (int j = y1; j <= y2; j++)
                    {
                        for (int k = z1; k <= z2; k++)
                        {
                            Block b = player.worldObj.getBlock(i, j, k);
                            TileEntity te = player.worldObj.getTileEntity(i, j, k);
                            if (b.isAir(player.worldObj, i, j, k)) continue;
                            if (te instanceof TileEntityPrisonUnbreakable)
                                continue;
//                            if (te != null)
//                                RenderHelper.DrawColoredSingleBoxInWorld(player, partialTicks, i, j, k, 0.04, 200, 0, 10, 120);

                            String name=b.blockRegistry.getNameForObject(b);
                            if (!ConfigChangableBlocks.getInstance().isValidName(name))
                                RenderHelper.DrawColoredSingleBoxInWorld(player, partialTicks, i, j, k, 0.04, 200, 0, 10, 120);
                        }
                    }
                }
            }
        }
    }
}
