package allout58.mods.prisoncraft.util;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

/**
 * Created by James Hollowell on 4/30/2014.
 */
public class RenderHelper
{
    /**
 * Render a block in the world
 *
 * @param player       The player to render from
 * @param partialTicks Partial ticks from renderer
 * @param x            X coordinate of block
 * @param y            Y coordinate of block
 * @param z            Z coordinate
 * @param offset       Offset (added amount to each side of block)
 * @param r            R of the color to render
 * @param g            B of the color to render
 * @param b            G of the color to render
 * @param a            Alpha of the color to render
 */
public static void DrawColoredSingleBoxInWorld(EntityLivingBase player, double partialTicks, double x, double y, double z, double offset, int r, int g, int b, int a)
{
    double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
    double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
    double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

    final double delta = 1 + 2 * offset;

    GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glDepthMask(false);

    double rx = x - px - offset;
    double ry = y - py - offset;
    double rz = z - pz - offset;

    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();

    tessellator.setColorRGBA(r, g, b, a);

    tessellator.addVertex(rx, ry, rz);
    tessellator.addVertex(rx + delta, ry, rz);
    tessellator.addVertex(rx + delta, ry, rz + delta);
    tessellator.addVertex(rx, ry, rz + delta);

    tessellator.addVertex(rx, ry + delta, rz);
    tessellator.addVertex(rx, ry + delta, rz + delta);
    tessellator.addVertex(rx + delta, ry + delta, rz + delta);
    tessellator.addVertex(rx + delta, ry + delta, rz);

    tessellator.addVertex(rx, ry, rz);
    tessellator.addVertex(rx, ry + delta, rz);
    tessellator.addVertex(rx + delta, ry + delta, rz);
    tessellator.addVertex(rx + delta, ry, rz);

    tessellator.addVertex(rx, ry, rz + delta);
    tessellator.addVertex(rx + delta, ry, rz + delta);
    tessellator.addVertex(rx + delta, ry + delta, rz + delta);
    tessellator.addVertex(rx, ry + delta, rz + delta);

    tessellator.addVertex(rx, ry, rz);
    tessellator.addVertex(rx, ry, rz + delta);
    tessellator.addVertex(rx, ry + delta, rz + delta);
    tessellator.addVertex(rx, ry + delta, rz);

    tessellator.addVertex(rx + delta, ry, rz);
    tessellator.addVertex(rx + delta, ry + delta, rz);
    tessellator.addVertex(rx + delta, ry + delta, rz + delta);
    tessellator.addVertex(rx + delta, ry, rz + delta);

    tessellator.draw();

    GL11.glEnable(GL11.GL_TEXTURE_2D);

    GL11.glPopAttrib();
}

    public static void DrawColoredBigBoxInWorld(EntityLivingBase player, double partialTicks, double x, double y, double z, double x2, double y2, double z2, double offset, int r, int g, int b, int a)
    {
        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        final double delta = 1 + 2 * offset;

        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        double rx = x - px - offset;
        double ry = y - py - offset;
        double rz = z - pz - offset;

        double rx2 = x2 - px - offset;
        double ry2 = y2 - py - offset;
        double rz2 = z2 - pz - offset;

        //Swap to make x < x2, etc
        if (rx > rx2)
        {
            double tmp = rx;
            rx = rx2;
            rx2 = tmp;
        }
        if (ry > ry2)
        {
            double tmp = ry;
            ry = ry2;
            ry2 = tmp;
        }

        if (rz > rz2)
        {
            double tmp = rz;
            rz = rz2;
            rz2 = tmp;
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA(r, g, b, a);

        // Bottom outside
        tessellator.addVertex(rx, ry, rz);
        tessellator.addVertex(rx2 + delta, ry, rz);
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);
        tessellator.addVertex(rx, ry, rz2 + delta);

        // Bottom inside
        tessellator.addVertex(rx, ry, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry, rz);
        tessellator.addVertex(rx, ry, rz);

        // Top outside
        tessellator.addVertex(rx, ry2 + delta, rz);
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);

        // Top inside
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx, ry2 + delta, rz);

        // rz-
        tessellator.addVertex(rx, ry, rz);
        tessellator.addVertex(rx, ry2 + delta, rz);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);
        tessellator.addVertex(rx2 + delta, ry, rz);

        // rz- inside
        tessellator.addVertex(rx2 + delta, ry, rz);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);
        tessellator.addVertex(rx, ry2 + delta, rz);
        tessellator.addVertex(rx, ry, rz);

        // rz+ outside
        tessellator.addVertex(rx, ry, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);

        // rz+ inside
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);
        tessellator.addVertex(rx, ry, rz2 + delta);

        // X1 outside
        tessellator.addVertex(rx, ry, rz);
        tessellator.addVertex(rx, ry, rz2 + delta);
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx, ry2 + delta, rz);

        // X1 inside
        tessellator.addVertex(rx, ry2 + delta, rz);
        tessellator.addVertex(rx, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx, ry, rz2 + delta);
        tessellator.addVertex(rx, ry, rz);

        // rx2 outside
        tessellator.addVertex(rx2 + delta, ry, rz);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);

        // rx2 inside
        tessellator.addVertex(rx2 + delta, ry, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz2 + delta);
        tessellator.addVertex(rx2 + delta, ry2 + delta, rz);
        tessellator.addVertex(rx2 + delta, ry, rz);

        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopAttrib();
    }
}
