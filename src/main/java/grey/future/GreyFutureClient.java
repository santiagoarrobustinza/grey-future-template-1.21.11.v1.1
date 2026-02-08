package grey.future;

import grey.future.mixin.DimensionEffectsMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import grey.future.world.music.TallRealmMusicManager;

public class GreyFutureClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TallRealmMusicManager.updateMusic();
        });

    }
}