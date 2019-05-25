package me.shedaniel.materialisation;

import me.shedaniel.materialisation.blocks.MaterialisingTableBlock;
import me.shedaniel.materialisation.containers.MaterialisingTableContainer;
import me.shedaniel.materialisation.items.ColoredItem;
import me.shedaniel.materialisation.items.MaterialisedPickaxeItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.container.BlockContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.util.Optional;

public class Materialisation implements ModInitializer {
    
    // TODO: Disable Anvil Enchanting
    
    public static final Block MATERIALISING_TABLE = new MaterialisingTableBlock();
    public static final Identifier MATERIALISING_TABLE_CONTAINER = new Identifier(ModReference.MOD_ID, "materialising_table");
    public static final Identifier MATERIALISING_TABLE_RENAME = new Identifier(ModReference.MOD_ID, "materialising_table_rename");
    public static final Identifier MATERIALISING_TABLE_PLAY_SOUND = new Identifier(ModReference.MOD_ID, "materialising_table_play_sound");
    public static final Item MATERIALISED_PICKAXE = new MaterialisedPickaxeItem(new Item.Settings());
    public static final Item HANDLE = new ColoredItem(new Item.Settings());
    public static final Item PICKAXE_HEAD = new ColoredItem(new Item.Settings());
    
    public static <T> Optional<T> getReflectionField(Object parent, Class<T> clazz, int index) {
        try {
            Field field = parent.getClass().getDeclaredFields()[index];
            if (!field.isAccessible())
                field.setAccessible(true);
            return Optional.ofNullable(clazz.cast(field.get(parent)));
        } catch (Exception e) {
            System.out.printf("Reflection failed! Trying to get #" + index + " from %s", clazz.getName());
            return Optional.empty();
        }
    }
    
    @Override
    public void onInitialize() {
        registerBlock("materialising_table", MATERIALISING_TABLE, ItemGroup.DECORATIONS);
        ContainerProviderRegistry.INSTANCE.registerFactory(MATERIALISING_TABLE_CONTAINER, (syncId, identifier, playerEntity, packetByteBuf) -> {
            return new MaterialisingTableContainer(syncId, playerEntity.inventory, BlockContext.create(playerEntity.world, packetByteBuf.readBlockPos()));
        });
        ServerSidePacketRegistry.INSTANCE.register(MATERIALISING_TABLE_RENAME, (packetContext, packetByteBuf) -> {
            if (packetContext.getPlayer().container instanceof MaterialisingTableContainer) {
                MaterialisingTableContainer container = (MaterialisingTableContainer) packetContext.getPlayer().container;
                String string_1 = SharedConstants.stripInvalidChars(packetByteBuf.readString());
                if (string_1.length() <= 35)
                    container.setNewItemName(string_1);
            }
        });
        ClientSidePacketRegistry.INSTANCE.register(MATERIALISING_TABLE_PLAY_SOUND, (packetContext, packetByteBuf) -> {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_ANVIL_USE, 1, 1));
        });
        registerItem("materialised_pickaxe", MATERIALISED_PICKAXE);
        registerItem("handle", HANDLE);
        registerItem("pickaxe_head", PICKAXE_HEAD);
    }
    
    private void registerBlock(String name, Block block) {
        registerBlock(name, block, new Item.Settings());
    }
    
    private void registerBlock(String name, Block block, ItemGroup group) {
        registerBlock(name, block, new Item.Settings().itemGroup(group));
    }
    
    private void registerBlock(String name, Block block, Item.Settings settings) {
        Registry.register(Registry.BLOCK, new Identifier(ModReference.MOD_ID, name), block);
        registerItem(name, new BlockItem(block, settings));
    }
    
    private void registerItem(String name, Item item) {
        Registry.register(Registry.ITEM, new Identifier(ModReference.MOD_ID, name), item);
    }
    
}