package grey.future;

import grey.future.block.ModBlocks;
import grey.future.item.ModItems;
import grey.future.world.gen.CustomTallChunkGenerator;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreyFuture implements ModInitializer {
	public static final String MOD_ID = "grey-future";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		registerChunkGenerator();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		CompostingChanceRegistry.INSTANCE.add(
				ModItems.BRAIN,
				0.8f
		);

	}

	private void registerChunkGenerator() {
		net.minecraft.registry.Registry.register(
				Registries.CHUNK_GENERATOR,
				Identifier.of(MOD_ID, "tall_generator"),
				CustomTallChunkGenerator.CODEC
		);
	}
}