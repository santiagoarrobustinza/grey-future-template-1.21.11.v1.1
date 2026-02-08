package grey.future.world;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimensionRegistry {
    // Define your custom dimension key
    public static final RegistryKey<World> TALL_DIMENSION = RegistryKey.of(
            RegistryKeys.WORLD,
            Identifier.of("grey-future", "tall_realm")
    );

    // Define your custom dimension type key
    public static final RegistryKey<DimensionType> TALL_DIMENSION_TYPE = RegistryKey.of(
            RegistryKeys.DIMENSION_TYPE,
            Identifier.of("grey-future", "tall_realm_type")
    );
}