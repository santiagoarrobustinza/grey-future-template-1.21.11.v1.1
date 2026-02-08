package grey.future;

import grey.future.block.ModBlocks;
import grey.future.item.ModItems;
import grey.future.world.gen.CustomTallChunkGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import grey.future.sound.ModSounds;

public class GreyFuture implements ModInitializer {
	public static final String MOD_ID = "grey-future";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModSounds.register();
		System.out.println("BOOTSTRAP CALLED!");
		Registry.register(
				Registries.CHUNK_GENERATOR,
				Identifier.of("grey-future", "custom_tall"),
				CustomTallChunkGenerator.CODEC
		);
		ModBlocks.registerModBlocks();

		ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> true);

		CompostingChanceRegistry.INSTANCE.add(
				ModItems.BRAIN,
				0.8f
		);


		// Kill any mobs that spawn in tall_realm
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (world.getRegistryKey().getValue().equals(Identifier.of("grey-future", "tall_realm"))) {
				if (entity instanceof MobEntity && !(entity instanceof PlayerEntity)) {
					entity.discard();
				}
			}
		});
	}
}