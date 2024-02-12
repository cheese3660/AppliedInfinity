package com.cheese.appliedinfinity.items;

import com.cheese.appliedinfinity.saving.PersistentStorage;
import com.cheese.appliedinfinity.storage.InfinityFluidStorage;
import com.cheese.appliedinfinity.storage.InfinityItemStorage;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class FluidDisk extends Item {

    public int size;
    public FluidDisk(int size) {
        super(new Properties().durability(0));
    }

    public String getGuid(ItemStack itemStack) {
        CompoundTag compoundTag;
        if (itemStack.hasTag()) {
            compoundTag = itemStack.getTag();
        } else {
            compoundTag = new CompoundTag();
        }
        assert compoundTag != null;
        if (compoundTag.contains("guid")) {
            return compoundTag.getString("guid");
        } else {
            var guid = UUID.randomUUID().toString();
            compoundTag.putString("guid", guid);
            itemStack.setTag(compoundTag);
            return guid;
        }
    }

    public boolean hasGuid(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            var tag = itemStack.getTag();
            return tag.contains("guid");
        }
        return false;
    }

    public @Nullable InfinityFluidStorage getStorage(ItemStack stack) {
        if (hasGuid(stack)) {
            var server = ServerLifecycleHooks.getCurrentServer();
            PersistentStorage<InfinityFluidStorage> persistentStorage = PersistentStorage.getInstanceFor(server,getGuid(stack), size, InfinityFluidStorage::new, InfinityFluidStorage::new,"fluid");
            return persistentStorage.get();
        } else {
            return null;
        }
    }
//    @Override
//    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
//        if (hasGuid(itemStack)) {
//            var storage = getStorage(itemStack);
//            if (size == 0 || Screen.hasShiftDown()) {
//                var usage = storage.getUsage();
//                var buckets = usage/1000;
//                var mB = usage % 1000;
//                components.add(Component.literal(String.format("Usage: %d.%03d buckets",buckets,mB)));
//            } else {
//                components.add(Component.literal(String.format("Usage: %01.2f %%",(storage.getUsage() * 100d / (double)storage.getSize()))));
//            }
//        }
//        if (Screen.hasShiftDown()) {
//            if (hasGuid(itemStack)) {
//                components.add(Component.literal(String.format("GUID: %s", getGuid(itemStack))));
//            } else {
//                components.add(Component.literal("No GUID set yet."));
//            }
//        }
//        super.appendHoverText(itemStack, level, components, tooltipFlag);
//    }
}
