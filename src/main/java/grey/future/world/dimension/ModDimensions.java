package grey.future.world.dimension;

import grey.future.GreyFuture;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    public static final RegistryKey<DimensionOptions> GREYFUTUREDIM_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            Identifier.of(GreyFuture.MOD_ID, "greyfuturedim"));
    public static final RegistryKey<World> GREYFUTUREDIM_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            Identifier.of(GreyFuture.MOD_ID, "greyfuturedim"));
    public static final RegistryKey<DimensionType> GREYFUTURE_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            Identifier.of(GreyFuture.MOD_ID, "greyfuturedim_type"));


}
