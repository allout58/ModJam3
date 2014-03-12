package allout58.mods.prisoncraft.handler;

import org.lwjgl.opengl.GL11;

import allout58.mods.prisoncraft.items.ItemConfigWand;
import allout58.mods.prisoncraft.jail.PrisonCraftWorldSave;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
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
            if (!(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemConfigWand)) return;
            if (!(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().hasTagCompound())) return;

             
            // if
            // (Minecraft.getMinecraft().theWorld.isAirBlock(modOpis.selectedBlock.x,
            // modOpis.selectedBlock.y, modOpis.selectedBlock.z)) return;

            NBTTagCompound stackTags = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().stackTagCompound;
//            if (Minecraft.getMinecraft().theWorld.provider.dimensionId != stackTags.getInteger(par1Str)) return;
            double partialTicks = event.partialTicks;

            EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
            double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

            double offset = 0.05;
            double delta = 1 + 2 * offset;

            GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
            if (stackTags.hasKey("tpIn"))
            {
                int coord[] = stackTags.getIntArray("tpIn");
                int bx = coord[0];
                int by = coord[1];
                int bz = coord[2];
                double x = bx - px - offset;
                double y = by - py - offset;
                double z = bz - pz - offset;
                drawSingleBox(x, y, z, delta, 255, 255, 255, 175);
            }

            if (stackTags.hasKey("tpOut"))
            {
                int coord[] = stackTags.getIntArray("tpOut");
                int bx = coord[0];
                int by = coord[1];
                int bz = coord[2];
                double x = bx - px - offset;
                double y = by - py - offset;
                double z = bz - pz - offset;
                drawSingleBox(x, y, z, delta, 0, 255, 0, 125);
            }

            if (stackTags.hasKey("jailCoord1") && !stackTags.hasKey("jailCoord2"))
            {
                int coord[] = stackTags.getIntArray("jailCoord1");
                int bx = coord[0];
                int by = coord[1];
                int bz = coord[2];
                double x = bx - px - offset;
                double y = by - py - offset;
                double z = bz - pz - offset;
                drawSingleBox(x, y, z, delta, 255, 0, 0, 125);
            }
            if (stackTags.hasKey("jailCoord2"))
            {
                int coord[] = stackTags.getIntArray("jailCoord1");
                int bx = coord[0];
                int by = coord[1];
                int bz = coord[2];
                double x = bx - px - offset;
                double y = by - py - offset;
                double z = bz - pz - offset;
                int coord2[] = stackTags.getIntArray("jailCoord2");
                int bx2 = coord2[0];
                int by2 = coord2[1];
                int bz2 = coord2[2];
                double x2 = bx2 - px - offset;
                double y2 = by2 - py - offset;
                double z2 = bz2 - pz - offset;

                if (x > x2)
                {
                    double tmp = x;
                    x = x2;
                    x2 = tmp;
                }
                if (y > y2)
                {
                    double tmp = y;
                    y = y2;
                    y2 = tmp;
                }

                if (z > z2)
                {
                    double tmp = z;
                    z = z2;
                    z2 = tmp;
                }

                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();

                tessellator.setColorRGBA(255, 0, 0, 100);

                // Bottom outside
                tessellator.addVertex(x, y, z);
                tessellator.addVertex(x2 + delta, y, z);
                tessellator.addVertex(x2 + delta, y, z2 + delta);
                tessellator.addVertex(x, y, z2 + delta);

                // Bottom inside
                tessellator.addVertex(x, y, z2 + delta);
                tessellator.addVertex(x2 + delta, y, z2 + delta);
                tessellator.addVertex(x2 + delta, y, z);
                tessellator.addVertex(x, y, z);

                // Top outside
                tessellator.addVertex(x, y2 + delta, z);
                tessellator.addVertex(x, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z);

                // Top inside
                tessellator.addVertex(x2 + delta, y2 + delta, z);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x, y2 + delta, z2 + delta);
                tessellator.addVertex(x, y2 + delta, z);

                // Z-
                tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y2 + delta, z);
                tessellator.addVertex(x2 + delta, y2 + delta, z);
                tessellator.addVertex(x2 + delta, y, z);

                // Z- inside
                tessellator.addVertex(x2 + delta, y, z);
                tessellator.addVertex(x2 + delta, y2 + delta, z);
                tessellator.addVertex(x, y2 + delta, z);
                tessellator.addVertex(x, y, z);

                // Z+ outside
                tessellator.addVertex(x, y, z2 + delta);
                tessellator.addVertex(x2 + delta, y, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x, y2 + delta, z2 + delta);

                // Z+ inside
                tessellator.addVertex(x, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y, z2 + delta);
                tessellator.addVertex(x, y, z2 + delta);

                // X1 outside
                tessellator.addVertex(x, y, z);
                tessellator.addVertex(x, y, z2 + delta);
                tessellator.addVertex(x, y2 + delta, z2 + delta);
                tessellator.addVertex(x, y2 + delta, z);

                // X1 inside
                tessellator.addVertex(x, y2 + delta, z);
                tessellator.addVertex(x, y2 + delta, z2 + delta);
                tessellator.addVertex(x, y, z2 + delta);
                tessellator.addVertex(x, y, z);

                // X2 outside
                tessellator.addVertex(x2 + delta, y, z);
                tessellator.addVertex(x2 + delta, y2 + delta, z);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y, z2 + delta);

                // X2 inside
                tessellator.addVertex(x2 + delta, y, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z2 + delta);
                tessellator.addVertex(x2 + delta, y2 + delta, z);
                tessellator.addVertex(x2 + delta, y, z);

                tessellator.draw();
            }

            GL11.glEnable(GL11.GL_TEXTURE_2D);

            GL11.glPopAttrib();
        }

    }

    private void drawSingleBox(double x, double y, double z, double delta, int r, int g, int b, int a)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA(r, g, b, a);

        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x + delta, y, z);
        tessellator.addVertex(x + delta, y, z + delta);
        tessellator.addVertex(x, y, z + delta);

        tessellator.addVertex(x, y + delta, z);
        tessellator.addVertex(x, y + delta, z + delta);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x + delta, y + delta, z);

        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x, y + delta, z);
        tessellator.addVertex(x + delta, y + delta, z);
        tessellator.addVertex(x + delta, y, z);

        tessellator.addVertex(x, y, z + delta);
        tessellator.addVertex(x + delta, y, z + delta);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x, y + delta, z + delta);

        tessellator.addVertex(x, y, z);
        tessellator.addVertex(x, y, z + delta);
        tessellator.addVertex(x, y + delta, z + delta);
        tessellator.addVertex(x, y + delta, z);

        tessellator.addVertex(x + delta, y, z);
        tessellator.addVertex(x + delta, y + delta, z);
        tessellator.addVertex(x + delta, y + delta, z + delta);
        tessellator.addVertex(x + delta, y, z + delta);

        tessellator.draw();
    }
}
