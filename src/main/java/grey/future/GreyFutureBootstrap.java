package grey.future;

import grey.future.world.gen.CustomTallChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class GreyFutureBootstrap {
    public static void bootstrap() {
        System.out.println("BOOTSTRAP CALLED!");
        Registry.register(
                Registries.CHUNK_GENERATOR,
                Identifier.of("grey-future", "custom_tall"),
                CustomTallChunkGenerator.CODEC
        );
    }
}