package com.cheese.appliedinfinity.setup;

import com.cheese.appliedinfinity.items.FluidDisk;
import com.cheese.appliedinfinity.items.ItemDisk;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final RegistryObject<Item> DISK_512K = Registration.ITEMS.register("disk_512k", () -> new ItemDisk(524288));
    public static final RegistryObject<Item> DISK_2M = Registration.ITEMS.register("disk_2m", () -> new ItemDisk(2097152));
    public static final RegistryObject<Item> DISK_8M = Registration.ITEMS.register("disk_8m", () -> new ItemDisk(8388608));
    public static final RegistryObject<Item> DISK_32M = Registration.ITEMS.register("disk_32m", () -> new ItemDisk(33554432));
    public static final RegistryObject<Item> DISK_128M = Registration.ITEMS.register("disk_128m", () -> new ItemDisk(134217728));
    public static final RegistryObject<Item> DISK_512M = Registration.ITEMS.register("disk_512m",() -> new ItemDisk(536870912));
    public static final RegistryObject<Item> DISK_INF = Registration.ITEMS.register("disk_inf", () -> new ItemDisk(0));
    public static final RegistryObject<Item> FLUID_DISK_512K = Registration.ITEMS.register("fluid_disk_512k", () -> new FluidDisk(524288));
    public static final RegistryObject<Item> FLUID_DISK_2M = Registration.ITEMS.register("fluid_disk_2m", () -> new FluidDisk(2097152));
    public static final RegistryObject<Item> FLUID_DISK_8M = Registration.ITEMS.register("fluid_disk_8m", () -> new FluidDisk(8388608));
    public static final RegistryObject<Item> FLUID_DISK_32M = Registration.ITEMS.register("fluid_disk_32m", () -> new FluidDisk(33554432));
    public static final RegistryObject<Item> FLUID_DISK_128M = Registration.ITEMS.register("fluid_disk_128m", () -> new FluidDisk(134217728));
    public static final RegistryObject<Item> FLUID_DISK_512M = Registration.ITEMS.register("fluid_disk_512m",() -> new FluidDisk(536870912));
    public static final RegistryObject<Item> FLUID_DISK_INF = Registration.ITEMS.register("fluid_disk_inf", () -> new FluidDisk(0));
    public static final RegistryObject<Item> QUANTUM_CALCULATION_PROCESSOR = Registration.ITEMS.register("quantum_calculation_processor",ModItems::supplyEmpty);
    public static final RegistryObject<Item> QUANTUM_LOGIC_PROCESSOR = Registration.ITEMS.register("quantum_logic_processor",ModItems::supplyEmpty);
    public static final RegistryObject<Item> QUANTUM_ENGINEERING_PROCESSOR = Registration.ITEMS.register("quantum_engineering_processor",ModItems::supplyEmpty);
    public static final RegistryObject<Item> QUANTUM_COMPUTER = Registration.ITEMS.register("quantum_computer",ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_512K = Registration.ITEMS.register("storage_part_512k", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_2M = Registration.ITEMS.register("storage_part_2m", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_8M = Registration.ITEMS.register("storage_part_8m", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_32M = Registration.ITEMS.register("storage_part_32m", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_128M = Registration.ITEMS.register("storage_part_128m", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_512M = Registration.ITEMS.register("storage_part_512m", ModItems::supplyEmpty);
    public static final RegistryObject<Item> STORAGE_PART_INF = Registration.ITEMS.register("storage_part_inf", ModItems::supplyEmpty);
    public static void register() {

    }

    private static Item supplyEmpty() {
        return new Item(new Item.Properties());
    }
}
