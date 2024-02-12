package com.cheese.appliedinfinity.storage;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import com.cheese.appliedinfinity.items.FluidDisk;
import com.cheese.appliedinfinity.items.ItemDisk;
import com.cheese.appliedinfinity.saving.PersistentStorage;
import com.cheese.appliedinfinity.setup.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class InfinityHandler implements ICellHandler{
    @Override
    public boolean isCell(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemDisk || itemStack.getItem() instanceof FluidDisk;
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack itemStack, @Nullable ISaveProvider iSaveProvider) {
        if (itemStack.getItem() instanceof ItemDisk disk) {
            disk.getGuid(itemStack);
            return disk.getStorage(itemStack);
        }
        if (itemStack.getItem() instanceof FluidDisk disk) {
            disk.getGuid(itemStack);
            return disk.getStorage(itemStack);
        }
        return null;
    }
}
