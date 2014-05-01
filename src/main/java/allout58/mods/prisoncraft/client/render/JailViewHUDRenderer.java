package allout58.mods.prisoncraft.client.render;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.jail.JailedPersonData;
import allout58.mods.prisoncraft.network.JVRequestPacket;
import allout58.mods.prisoncraft.tileentities.TileEntityJailView;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
// import net.minecraft.network.packet.Packet250CustomPayload;
// import cpw.mods.fml.common.network.PacketDispatcher;

public class JailViewHUDRenderer extends TileEntitySpecialRenderer
{
    public static final double WIDTH = 3;
    public static final double HEIGHT = 2;
    public static final int FONT_HEIGHT = 15;
    public static final int COL_1_X = 0;
    public static final int COL_2_X = 75;
    public static final int COL_3_X = 100;
    public static final int MAX_ROWS = 13;
    public static List<JailedPersonData> people = new ArrayList<JailedPersonData>();
    private int ticks = 0;
    private boolean hasFirstChecked = false;

    public static String clampString(String s, int size)
    {
        if (s.length() <= size) return s;
        if (size < 4) return "";
        return s.substring(0, size - 3) + "...";
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float tick)
    {
        if (tileentity.blockMetadata == 0)
        {
            //drawText("Not linked", 20, 20, 1);
            return;
        }
        if (!hasFirstChecked)
        {
            hasFirstChecked = true;
            System.out.println("First time: send all please!");

            PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            PrisonCraft.channels.get(Side.CLIENT).writeAndFlush(new JVRequestPacket());
        }

        ticks++;

        if (ticks % 100 == 0)
        {
            for (int i = 0; i < people.size(); i++)
            {
                if (people.get(i).time > -1)
                {
                    people.get(i).time--;
                }
                if (people.get(i).time == 0)
                {
                    people.remove(i);
                }

            }
        }
        if (ticks % 1000 == 0)
        {
            updateAllPeople();
            System.out.println("Update all people");
        }
        if (ticks >= 4000)
        {
            ticks = 0;
            hasFirstChecked = false;
        }

        Tessellator tess = Tessellator.instance;

        glPushMatrix();

        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);

        glTranslated(x + 0.5, y + .52, z + 0.5);

        EntityLivingBase player = Minecraft.getMinecraft().renderViewEntity;

        Vector3f vPlayer = new Vector3f(((float) player.posX), ((float) player.posY), ((float) player.posZ));
        Vector3f vTE = new Vector3f((float) (tileentity.xCoord + 0.5), tileentity.yCoord, (float) (tileentity.zCoord + 0.5));

        Vector3f vRes = Vector3f.sub(vPlayer, vTE, null);

        double ro = Math.atan2(vRes.x, vRes.z) * 180 / Math.PI + 180;

        glRotated(ro, 0, 1, 0);

        double dx = -0.5 * WIDTH;
        double dy = 0;
        double dz = 0;

        tess.startDrawingQuads();
        tess.setColorRGBA(0x8e, 0x8e, 0x8e, 255);
        tess.addVertex(dx, dy, dz);
        tess.addVertex(dx, dy + HEIGHT, dz);
        tess.addVertex(dx + WIDTH, dy + HEIGHT, dz);
        tess.addVertex(dx + WIDTH, dy, dz);
        tess.draw();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_LIGHTING);

        drawText(EnumChatFormatting.UNDERLINE.toString() + "Name", COL_1_X, 0, 0.012F);
        drawText(EnumChatFormatting.UNDERLINE.toString() + "Time", COL_2_X - 10, 0, 0.012F);
        drawText(EnumChatFormatting.UNDERLINE.toString() + "Reason", COL_3_X, 0, 0.012F);

        glTranslated(0, -.05, 0);

        int s = MAX_ROWS;

        for (int i = 0; i < people.size() && s > 0; i++)
        {
            if (people.get(i).jail.equals(((TileEntityJailView) tileentity).jailname))
            {
                String time = "\u221E";
                if (people.get(i).time >= -1)
                {
                    time = Integer.toString(people.get(i).time);
                }

                drawText(clampString(people.get(i).name, 20), COL_1_X - 5, FONT_HEIGHT * (MAX_ROWS - s + 1), 0.009F);
                drawText(time, COL_2_X + 15, FONT_HEIGHT * (MAX_ROWS - s + 1), 0.009F);
                drawText(clampString(people.get(i).reason, 34), COL_3_X + 25, FONT_HEIGHT * (MAX_ROWS - s + 1), 0.009F);
                s--;
            }
        }

        glPopMatrix();
    }

    private void drawText(String text, int x, int y, float size)
    {
        glPushMatrix();

        FontRenderer frender = Minecraft.getMinecraft().fontRenderer;
        double extraX = 0;
        double extraY = 0;
        if (text.equals("\u221E"))
        {
            size += .015F;
            extraX = 1.33;
            extraY = .295;
        }
        glTranslated(WIDTH * .5 - .1 + extraX, HEIGHT - .1 + extraY, -.005);
        glRotated(180, 0, 0, 1);

        glScalef(size, size, 0F);

        frender.drawString(text, x, y, 4210752);

        glPopMatrix();
    }

    private void updateAllPeople()
    {
        for (int i = 0; i < people.size(); i++)
        {
            if (people.get(i).time > -1)
            {
                getPerson(people.get(i));
            }
        }
    }

    private void getPerson(JailedPersonData person)
    {
        PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        PrisonCraft.channels.get(Side.CLIENT).writeOutbound(new JVRequestPacket(person.name));
    }
}
