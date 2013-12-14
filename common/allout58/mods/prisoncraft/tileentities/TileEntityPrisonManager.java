package allout58.mods.prisoncraft.tileentities;

import java.util.Vector;

import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonManager extends TileEntity implements IInventory
{
    private ItemStack[] playerInventory;
    public static final int INVENTORY_SIZE = 40;

    public static final int START_MAIN = 0;
    public static final int START_HOTBAR = 32; // ??
    public static final int START_ARMOR = 36; // ??

    public boolean hasJailedPlayer = false;

    public int tpCoord[] = new int[3];

    private EntityPlayer jailedPlayer;
    public String playerName;
    
    private Boolean isDirty=false;

    public TileEntityPrisonManager()
    {
        playerInventory = new ItemStack[INVENTORY_SIZE];
    }

    public void changeBlocks(NBTTagCompound locs)
    {
        // give xyz names
        int x1 = locs.getInteger("x1");
        int y1 = locs.getInteger("y1");
        int z1 = locs.getInteger("z1");
        int x2 = locs.getInteger("x2");
        int y2 = locs.getInteger("y2");
        int z2 = locs.getInteger("z2");
        // loop through each block - todo... ;)

    }

    public void click(EntityPlayer player)// tmp test fcn
    {
        if (!hasJailedPlayer)
        {
            jailPlayer(player);

        }
        else
        {
            unjailPlayer(player);
            hasJailedPlayer = false;
        }
    }

    public void jailPlayer(EntityPlayer player)
    {
        isDirty=true;
        tpCoord[0] = xCoord;
        tpCoord[1] = yCoord + 1;
        tpCoord[2] = zCoord;
        jailedPlayer = player;
        playerName=player.username;
        hasJailedPlayer = true;
        player.mountEntity(null);
        player.setPositionAndUpdate(tpCoord[0] + .5, tpCoord[1], tpCoord[2] + .5);
        // Take their inventory
        for (int i = START_MAIN; i < START_HOTBAR; i++)
        {
            playerInventory[i] = player.inventory.mainInventory[i];
        }
        for (int i = START_HOTBAR; i < START_ARMOR; i++)
        {
            playerInventory[i] = player.inventory.mainInventory[i];
        }
        for (int i = START_ARMOR; i < INVENTORY_SIZE; i++)
        {
            playerInventory[i] = player.inventory.armorInventory[i - START_ARMOR];
        }
        player.inventory.clearInventory(-1, -1);
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 300, false));
    }

    public void unjailPlayer(EntityPlayer player)
    {
        isDirty=true;
        hasJailedPlayer = false;
        jailedPlayer = null;
        playerName="";
        // give their inventory

        for (int i = START_MAIN; i < START_HOTBAR; i++)
        {
            player.inventory.mainInventory[i] = playerInventory[i];
        }
        for (int i = START_HOTBAR; i < START_ARMOR; i++)
        {
            player.inventory.mainInventory[i] = playerInventory[i];
        }
        for (int i = START_ARMOR; i < INVENTORY_SIZE; i++)
        {
            player.inventory.armorInventory[i - START_ARMOR] = playerInventory[i];
        }
        player.removePotionEffect(Potion.moveSlowdown.id);
        player.removePotionEffect(Potion.jump.id);
        
    }

    @Override
    public void updateEntity()
    {
        if (isDirty)
        {
            isDirty = false;
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
        }
        if (hasJailedPlayer)
        {
            jailedPlayer.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 300, false));
        }
    }

    /* NBT */

    @Override
    public void readFromNBT(NBTTagCompound tags)
    {
        hasJailedPlayer = tags.getBoolean("HasJailedPlayer");
        tpCoord = tags.getIntArray("tpCoord");
        jailedPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(tags.getString("PlayerUsername"));
        NBTTagList tagList = tags.getTagList("Items");
        playerInventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound tagCompound = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < playerInventory.length)
            {
                playerInventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags)
    {
        tags.setBoolean("HasJailedPlayer", hasJailedPlayer);
        tags.setIntArray("tpCoord", tpCoord);
        if (hasJailedPlayer)
        {
            tags.setString("PlayerUsername", jailedPlayer.username);
        }
        // Write the ItemStacks in the inventory to NBT
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < playerInventory.length; ++currentIndex)
        {
            if (playerInventory[currentIndex] != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                playerInventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tags.setTag("Items", tagList);

    }

    /* Packets */
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    /* Inventory */
    @Override
    public int getSizeInventory()
    {
        return playerInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return playerInventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= amount)
            {
                setInventorySlotContents(slot, null);
            }
            else
            {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null)
        {
            setInventorySlotContents(slot, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        playerInventory[slot] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
        {
            itemStack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        // TODO Possibly add custom names
        return "container.PlayerHeldInventory";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        // TODO This needs to change w/ custom names
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        // TODO Actually set this up to only let in appropriate parts
        return true;
    }
}
