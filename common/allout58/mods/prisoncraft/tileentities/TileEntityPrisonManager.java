package allout58.mods.prisoncraft.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrisonManager extends TileEntity implements IInventory
{
    private ItemStack[] playerInventory;
    public static final int IVENTORY_SIZE=1;
    
    public TileEntityPrisonManager()
    {
        playerInventory=new ItemStack[IVENTORY_SIZE];
    }
    
    public void changeBlocks(NBTTagCompound locs)
    {
        //give xyz names
        int x1=locs.getInteger("x1");
        int y1=locs.getInteger("y1");
        int z1=locs.getInteger("z1");
        int x2=locs.getInteger("x2");
        int y2=locs.getInteger("y2");
        int z2=locs.getInteger("z2");
        //loop through each block - todo... ;)
        
    }

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
