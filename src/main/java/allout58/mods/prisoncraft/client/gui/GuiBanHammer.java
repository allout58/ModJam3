package allout58.mods.prisoncraft.client.gui;

import allout58.mods.prisoncraft.PrisonCraft;
import allout58.mods.prisoncraft.items.ItemBanHammer;
import allout58.mods.prisoncraft.network.UpdateHammerPacket;
import allout58.mods.prisoncraft.util.ColorUtil;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glDisable;

public class GuiBanHammer extends GuiScreen
{
    private GuiTextField jailname;
    private GuiTextField time;
    private ItemStack is;

    private int centerX;
    private int centerY;

    private boolean isJailOK = true;
    private boolean isTimeOK = true;

    public GuiBanHammer(ItemStack stack)
    {
        is = stack;
    }

    @Override
    public void initGui()
    {
        centerX = this.width / 2;
        centerY = this.height / 2;
        super.initGui();

        Keyboard.enableRepeatEvents(true);

        jailname = new GuiTextField(this.fontRendererObj, centerX - 50, 75, 100, 13);
        jailname.setCanLoseFocus(true);
        jailname.setFocused(true);
        jailname.setEnabled(true);
        jailname.setEnableBackgroundDrawing(true);
        jailname.setMaxStringLength(30);
        jailname.setText("");

        time = new GuiTextField(this.fontRendererObj, centerX - 50, 95, 100, 13);
        time.setCanLoseFocus(true);
        time.setEnabled(true);
        time.setEnableBackgroundDrawing(true);
        time.setMaxStringLength(30);
        time.setText("-1");

        ItemStack is = this.mc.thePlayer.getCurrentEquippedItem();
        if (is != null && is.getItem() instanceof ItemBanHammer && is.hasTagCompound())
        {
            if (is.getTagCompound().hasKey("jailname"))
            {
                jailname.setText(is.getTagCompound().getString("jailname"));
            }
            if (is.getTagCompound().hasKey("time"))
            {
                double d = is.getTagCompound().getDouble("time");
                time.setText(String.valueOf(d));
            }
        }

        // id, x, y, width, height, text
        buttonList.add(new GuiButton(1, centerX - 50, 122, 48, 20, "OK"));
        buttonList.add(new GuiButton(1, centerX + 2, 122, 48, 20, "Cancel"));
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        // id is the id you give your button
        switch (guibutton.id)
        {
            case 1:
                if (isJailOK && isTimeOK)
                {
                    PrisonCraft.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
                    PrisonCraft.channels.get(Side.CLIENT).writeAndFlush(new UpdateHammerPacket(Minecraft.getMinecraft().thePlayer.getDisplayName(), jailname.getText(), Double.parseDouble(time.getText())));
                    this.mc.displayGuiScreen((GuiScreen) null);
                    this.mc.setIngameFocus();
                }
                break;
            case 2:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
            default:
                FMLLog.severe("Unknown button clicked!");
        }
    }

    @Override
    public void drawScreen(int p1, int p2, float p3)
    {
        this.drawDefaultBackground();
        super.drawScreen(p1, p2, p3);
        jailname.drawTextBox();
        time.drawTextBox();
        drawFG();
    }

    private void drawFG()
    {
        glDisable(GL_LIGHTING);
        this.fontRendererObj.drawString("Jail name:", centerX - 55 - this.fontRendererObj.getStringWidth("Jail name:"), 78, 16777215);
        this.fontRendererObj.drawString("Time:", centerX - 55 - this.fontRendererObj.getStringWidth("Time:"), 98, 16777215);
        if (!isJailOK)
        {
            this.fontRendererObj.drawString("Jail name not valid.", centerX + 55, 78, ColorUtil.getIntFromRGB(250, 10, 10));
        }
        if (!isTimeOK)
        {
            this.fontRendererObj.drawString("Time is not a valid number.", centerX + 55, 98, ColorUtil.getIntFromRGB(250, 10, 10));
        }
    }

    protected void keyTyped(char par1, int par2)
    {
        if (jailname.isFocused() && jailname.textboxKeyTyped(par1, par2))
        {
            tryParseJail();
        }
        else if (time.isFocused() && time.textboxKeyTyped(par1, par2))
        {
            tryParseTime();
        }
        else
        {
            super.keyTyped(par1, par2);
        }
    }

    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        boolean jF = jailname.isFocused();
        boolean tF = time.isFocused();
        jailname.mouseClicked(par1, par2, par3);
        time.mouseClicked(par1, par2, par3);
        if (jF && !jailname.isFocused())// if jailname lost focus
        {
            tryParseJail();
        }
        if (tF && !time.isFocused())// if time lost focus
        {
            tryParseTime();
        }
    }

    private void tryParseTime()
    {
        String t = time.getText();
        try
        {
            Integer.parseInt(t);
            isTimeOK = true;
        }
        catch (NumberFormatException e)
        {
            isTimeOK = false;
        }
    }

    private void tryParseJail()
    {
        String j = jailname.getText();
        if (j.contains(" ")) isJailOK = false;
        else isJailOK = true;
    }
}
