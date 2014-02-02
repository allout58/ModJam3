package allout58.mods.prisoncraft.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class JailViewHUDRenderer extends TileEntitySpecialRenderer
{

    public static double WIDTH = 3;
    public static double HEIGHT = 3;

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick)
    {
        Tessellator tess = Tessellator.instance;

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;

        Vector3f vPlayer = new Vector3f(((float) player.posX), ((float) player.posY), ((float) player.posZ));
        Vector3f vTE = new Vector3f(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);

        Vector3f vRes = Vector3f.sub(vPlayer, vTE, null);

        double ro = Math.atan2(vRes.x, vRes.z) * 180 / Math.PI + 180;

        GL11.glRotated(ro, 0, 1, 0);

        // double px = player.lastTickPosX + (player.posX - player.lastTickPosX)
        // * tick;
        // double py = player.lastTickPosY + (player.posY - player.lastTickPosY)
        // * tick;
        // double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ)
        // * tick;

        // double offset = 0.05;
        // double delta = 1 + 2 * offset;

        double dx = -0.5 * WIDTH;// tileentity.xCoord - px - offset;
        double dy = 0;// tileentity.yCoord - py - offset+1;
        double dz = 0;// tileentity.zCoord - pz - offset;

        tess.startDrawingQuads();
        tess.setColorOpaque(0, 0, 0);
        tess.addVertex(dx, dy, dz);
        tess.addVertex(dx, dy + HEIGHT, dz);
        tess.addVertex(dx + WIDTH, dy + HEIGHT, dz);
        tess.addVertex(dx + WIDTH, dy, dz);
        tess.draw();
        
        

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glPopMatrix();

        // GL11.glPopAttrib();
    }

}
