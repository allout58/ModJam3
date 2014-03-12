package allout58.mods.prisoncraft.network;

import allout58.mods.prisoncraft.PrisonCraft;
import io.netty.buffer.ByteBuf;

public class NetworkUtils
{
    public static void writeString(ByteBuf buff, String string)
    {
        if (string != null)
        {
            int size = string.length();
            buff.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                buff.writeChar(string.toCharArray()[i]);
            }
        }
        else
        {
            PrisonCraft.logger.error("Trying to write a null string", new Throwable());
        }
    }

    public static String readString(ByteBuf buff)
    {
        int size = buff.readInt();
        char[] s1 = new char[size];
        for (int i = 0; i < size; i++)
        {
            s1[i] = buff.readChar();
        }
        return s1.toString();
    }
}
