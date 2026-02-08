package grey.future.item;

import grey.future.GreyFuture;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BRAIN = registerItem( "brain", new Item(new Item.Settings()
            .registryKey(RegistryKey.of(RegistryKeys.ITEM,Identifier.of(GreyFuture.MOD_ID,"brain")))
            .food(ModFoodComponents.BRAIN, ModFoodComponents.BRAIN_EFFECT)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(GreyFuture.MOD_ID, name), item);
    }

    public static void registerModItems(){
        GreyFuture.LOGGER.info("Registering Mod Items for" + GreyFuture.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(BRAIN);
        });
    }
}
