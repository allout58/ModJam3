package allout58.mods.prisoncraft.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import allout58.mods.prisoncraft.constants.ModConstants;

import net.minecraftforge.common.Configuration;

public class ConfigChangableIDs
{
    private static ConfigChangableIDs instance;

    private List<Integer> unbreakIDWhitelist = new ArrayList<Integer>();
    private File propFile;

    public static ConfigChangableIDs getInstance()
    {
        if (instance == null)
        {
            instance = new ConfigChangableIDs();
        }
        return instance;
    }

    public void load(File f)
    {
        unbreakIDWhitelist.clear();
        propFile = f;
        if (!f.exists())
        {
            try
            {
                f.createNewFile();
                for (int i = 0; i < Config.unbreakIDWhitelistDefault.length; i++)
                {
                    unbreakIDWhitelist.add(Config.unbreakIDWhitelistDefault[i]);
                }
                save();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            BufferedReader read = new BufferedReader(new FileReader(f));

            String ln;
            while ((ln = read.readLine()) != null)
            {
                try
                {
                    int id = Integer.parseInt(ln);
                    unbreakIDWhitelist.add(id);
                }
                catch (NumberFormatException e)
                {
                }
            }
            read.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            FileWriter writer = new FileWriter(propFile);

            for (int i = 0; i < unbreakIDWhitelist.size(); i++)
            {
                writer.write(unbreakIDWhitelist.get(i).toString() + "\n");
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int[] getIDs()
    {
        int[] ret = new int[unbreakIDWhitelist.size()];
        for (int i = 0; i < unbreakIDWhitelist.size(); i++)
        {
            ret[i] = unbreakIDWhitelist.get(i).intValue();
        }
        return ret;
    }

    public boolean addID(int id)
    {
        return unbreakIDWhitelist.add(id);
    }

    public boolean removeID(int id)
    {
        int loc = unbreakIDWhitelist.indexOf(id);
        if (loc > -1)
        {
            unbreakIDWhitelist.remove(loc);
        }
        return loc > -1;
    }
}
