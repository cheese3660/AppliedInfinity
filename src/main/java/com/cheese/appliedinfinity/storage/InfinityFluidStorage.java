package com.cheese.appliedinfinity.storage;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import com.cheese.appliedinfinity.interfaces.Savable;
import com.cheese.appliedinfinity.saving.PersistentStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import java.util.HashMap;

public class InfinityFluidStorage implements StorageCell, Savable {

    public long size;
    public long usage;
    public long getSize() {
        return size;
    }

    public long getUsage() {
        return usage;
    }
    public HashMap<AEFluidKey,Long> fluidCounts;
    public PersistentStorage<InfinityFluidStorage> persistentStorage;

    public InfinityFluidStorage(PersistentStorage<InfinityFluidStorage> persistentStorage, CompoundTag tag) {
        this.persistentStorage = persistentStorage;
        // Now we load all our data from the tag
        this.usage = tag.getLong("usage");
        this.size = tag.getLong("size");
        this.fluidCounts = new HashMap<>();
        var list = (ListTag)tag.get("items");
        for (int i = 0; i < list.size(); i++) {
            var compound = list.getCompound(i);
            var key = compound.getCompound("key");
            var itemKey = AEFluidKey.fromTag(key);
            var count = compound.getLong("value");
            this.fluidCounts.put(itemKey,count);
        }
    }
    public InfinityFluidStorage(PersistentStorage<InfinityFluidStorage> persistentStorage, long sizeInBuckets) {
        this.persistentStorage = persistentStorage;
        this.size = sizeInBuckets * 1000; // Buckets to mB
        this.usage = 0;
        this.fluidCounts = new HashMap<>();
    }


    @Override
    public double getIdleDrain() {
        return 5.0d;
    }

    @Override
    public void persist() {
        persistentStorage.setDirty();
    }

    @Override
    public Component getDescription() {
        return Component.literal("Test description");
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (var key : fluidCounts.keySet()) {
            out.set(key, fluidCounts.get(key));
        }
    }
    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return (what instanceof AEFluidKey) && size == 0;
    }

    @Override
    public CellState getStatus() {
        if (usage == 0) {
            return CellState.EMPTY;
        } else if (usage >= size && size != 0) {
            return CellState.FULL;
        } else {
            return CellState.NOT_EMPTY;
        }
    }


    @Override
    public CompoundTag serialize() {
        var tag = new CompoundTag();
        tag.putLong("size", size);
        tag.putLong("usage", usage);
        var list = new ListTag();
        for (var key : fluidCounts.keySet()) {
            var compound = new CompoundTag();
            var keyTag = key.toTag();
            compound.put("key",keyTag);
            compound.putLong("value", fluidCounts.get(key));
            list.add(compound);
        }
        tag.put("items",list);
        return tag;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (what instanceof AEFluidKey itemKey) {
            if (size != 0 && size == usage) return 0;
            long added;
            if (size != 0 && usage + amount >= size) {
                added = size - usage;
            } else {
                added = amount;
            }
            if (mode == Actionable.MODULATE) {
                usage += added;
                if (fluidCounts.containsKey(itemKey)) {
                    fluidCounts.put(itemKey, fluidCounts.get(itemKey) + added);
                } else {
                    fluidCounts.put(itemKey, added);
                }
                persistentStorage.setDirty();
            }
            return added;
        } else {
            return 0;
        }
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (what instanceof AEFluidKey itemKey) {
            long removed;
            if (!fluidCounts.containsKey(itemKey)) return 0;
            var current = fluidCounts.get(itemKey);
            if (amount > current) {
                removed = current;
            } else {
                removed = amount;
            }
            var remaining = current - removed;
            if (mode == Actionable.MODULATE) {
                if (remaining == 0) {
                    fluidCounts.remove(itemKey);
                } else {
                    fluidCounts.put(itemKey, remaining);
                }
                usage -= removed;
                persistentStorage.setDirty();
            }
            return removed;
        } else {
            return 0;
        }
    }
}
