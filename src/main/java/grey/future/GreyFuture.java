package grey.future;

import grey.future.block.ModBlocks;
import grey.future.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreyFuture implements ModInitializer {
	public static final String MOD_ID = "grey-future";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		CompostingChanceRegistry.INSTANCE.add(
				ModItems.BRAIN,
				0.8f
		);

	}
}