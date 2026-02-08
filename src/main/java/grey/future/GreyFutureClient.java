package grey.future;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import grey.future.world.music.TallRealmMusicManager;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class GreyFutureClient implements ClientModInitializer {
    private static final Identifier SKY_TEXTURE = Identifier.of("grey-future", "textures/sky/sky.png");

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TallRealmMusicManager.updateMusic();
        });
    }
}