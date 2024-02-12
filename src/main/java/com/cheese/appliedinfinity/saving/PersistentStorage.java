package com.cheese.appliedinfinity.saving;

import com.cheese.appliedinfinity.interfaces.Savable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.function.BiFunction;

public class PersistentStorage<T extends Savable> extends SavedData {
    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.put("storage", storage.serialize());
        return compoundTag;
    }

    private T storage;

    private PersistentStorage() {
        this.storage = null;
    }

    public static <T1 extends Savable> PersistentStorage<T1> create(int size, BiFunction<PersistentStorage<T1>,Integer,T1> createConstructor) {
        var storage =  new PersistentStorage<T1>();
        storage.storage = createConstructor.apply(storage, size);
        return storage;
    }

    public static <T1 extends Savable> PersistentStorage<T1> load(CompoundTag tag, BiFunction<PersistentStorage<T1>,CompoundTag,T1> loadConstructor) {
        var data = new PersistentStorage<T1>();
        data.storage = loadConstructor.apply(data, tag.getCompound("storage"));
        return data;
    }

    public T get() {
        return storage;
    }

    public static <T1 extends Savable> PersistentStorage<T1> getInstanceFor(MinecraftServer server, String guid, int size,
                                                                            BiFunction<PersistentStorage<T1>,Integer,T1> createConstructor,
                                                                            BiFunction<PersistentStorage<T1>,CompoundTag,T1> loadConstructor,
                                                                            String type) {
        return server.overworld().getDataStorage().computeIfAbsent(tag -> load(tag,loadConstructor),() -> create(size,createConstructor), "ai_" + type + "_disk_" + guid);
    }
}
