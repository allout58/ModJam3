package allout58.mods.prisoncraft.permissions;

import java.util.HashMap;
import java.util.Map;

public enum PermissionLevel
{
    Default(0), Jailer(1), PermissionGiver(2), ConfigurationManager(3), FinalWord(1000);

    private int value;

    private PermissionLevel(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    private static final Map<Integer, PermissionLevel> intToTypeMap = new HashMap<Integer, PermissionLevel>();
    static
    {
        for (PermissionLevel type : PermissionLevel.values())
        {
            intToTypeMap.put(type.value, type);
        }
    }

    public static PermissionLevel fromInt(int i)
    {
        PermissionLevel type = intToTypeMap.get(Integer.valueOf(i));
        if (type == null) throw new IndexOutOfBoundsException();
        return type;
    }
}
