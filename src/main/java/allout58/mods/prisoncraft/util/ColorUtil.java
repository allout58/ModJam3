package allout58.mods.prisoncraft.util;

public class ColorUtil
{
    public static int getIntFromRGB(int r, int g, int b)
    {
        return (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }
}
