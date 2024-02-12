package com.cheese.appliedinfinity.storage;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import com.cheese.appliedinfinity.interfaces.Savable;
import com.cheese.appliedinfinity.saving.PersistentStorage;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.HashMap;

public class InfinityItemStorage implements StorageCell, Savable {

    private PersistentStorage<InfinityItemStorage> persistentStorage;

    private long size;
    private long usage;
    private HashMap<AEItemKey, Long> itemCounts;

    public InfinityItemStorage(PersistentStorage<InfinityItemStorage> persistentStorage, CompoundTag tag) {
        this.persistentStorage = persistentStorage;
        // Now we load all our data from the tag
        this.usage = tag.getLong("usage");
        this.size = tag.getLong("size");
        this.itemCounts = new HashMap<>();
        var list = (ListTag)tag.get("items");
        for (int i = 0; i < list.size(); i++) {
            var compound = list.getCompound(i);
            var key = compound.getCompound("key");
            var itemKey = AEItemKey.fromTag(key);
            var count = compound.getLong("value");
            this.itemCounts.put(itemKey,count);
        }
    }

    public InfinityItemStorage(PersistentStorage<InfinityItemStorage> persistentStorage, int size) {
        this.persistentStorage = persistentStorage;
        this.size = size;
        this.usage = 0;
        this.itemCounts = new HashMap<>();
    }


    public long getSize() {
        return size;
    }

    public long getUsage() {
        return usage;
    }

    public CompoundTag serialize() {
        var tag = new CompoundTag();
        tag.putLong("size", size);
        tag.putLong("usage", usage);
        var list = new ListTag();
        for (var key : itemCounts.keySet()) {
            var compound = new CompoundTag();
            var keyTag = key.toTag();
            compound.put("key",keyTag);
            compound.putLong("value",itemCounts.get(key));
            list.add(compound);
        }
        tag.put("items",list);
        return tag;
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if (what instanceof AEItemKey itemKey) {
            if (size != 0 && size == usage) return 0;
            long added;
            if (size != 0 && usage + amount >= size) {
                added = size - usage;
            } else {
                added = amount;
            }
            if (mode == Actionable.MODULATE) {
                usage += added;
                if (itemCounts.containsKey(itemKey)) {
                    itemCounts.put(itemKey, itemCounts.get(itemKey) + added);
                } else {
                    itemCounts.put(itemKey, added);
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
        if (what instanceof AEItemKey itemKey) {
            long removed;
            if (!itemCounts.containsKey(itemKey)) return 0;
            var current = itemCounts.get(itemKey);
            if (amount > current) {
                removed = current;
            } else {
                removed = amount;
            }
            var remaining = current - removed;
            if (mode == Actionable.MODULATE) {
                if (remaining == 0) {
                    itemCounts.remove(itemKey);
                } else {
                    itemCounts.put(itemKey, remaining);
                }
                usage -= removed;
                persistentStorage.setDirty();
            }
            return removed;
        } else {
            return 0;
        }
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        for (var key :itemCounts.keySet()) {
            out.set(key,itemCounts.get(key));
        }
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return (what instanceof AEItemKey) && size == 0;
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
}
