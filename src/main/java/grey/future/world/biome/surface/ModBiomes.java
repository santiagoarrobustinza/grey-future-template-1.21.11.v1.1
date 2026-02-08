package grey.future.world.biome.surface;

import grey.future.GreyFuture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;

public class ModBiomes {
    public static final RegistryKey<Biome> GREY_FUTURE_BIOME_PLAIN = RegistryKey.of(RegistryKeys.BIOME,
            Identifier.of(GreyFuture.MOD_ID,"grey_future_biome_plain"));

    //public static void boostrap(Registerable<Biome> context) {
        //context.register(GREY_FUTURE_BIOME_PLAIN, greyfuturebiomeplain(context));
    //}

    //public static Biome greyfuturebiomeplain(Registerable<Biome> context) {
        //SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        //spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.PORCUPINE, 2, 3, 5));
        //spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));

    //}

    }
