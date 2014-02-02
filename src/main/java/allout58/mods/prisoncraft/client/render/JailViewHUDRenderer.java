package allout58.mods.prisoncraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class JailViewHUDRenderer extends TileEntitySpecialRenderer
{

    public static double WIDHT = 3;
    public static double HEIGHT = 4;

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick)
    {
        Tessellator tess = Tessellator.instance;

//        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glPushMatrix();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glDepthMask(false);
        GL11.glTranslated(x, y, z);

//        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;
//        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * tick;
//        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * tick;
//        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * tick;
//
        double offset = 0.05;
        double delta = 1 + 2 * offset;

        double dx = 0-offset;//tileentity.xCoord - px - offset;
        double dy = 1;//tileentity.yCoord - py - offset+1;
        double dz = 0-offset;//tileentity.zCoord - pz - offset;

        tess.startDrawingQuads();
        tess.setColorOpaque(0, 0, 0);
        tess.addVertex(dx, dy, dz);
        tess.addVertex(dx, dy + delta, dz);
        tess.addVertex(dx + delta, dy + delta, dz);
        tess.addVertex(dx + delta, dy, dz);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();

//        GL11.glPopAttrib();
    }

}
