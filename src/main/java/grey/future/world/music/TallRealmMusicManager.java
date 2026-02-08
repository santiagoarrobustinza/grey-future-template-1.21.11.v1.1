package grey.future.world.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import grey.future.sound.ModSounds;

public class TallRealmMusicManager {
    private static final Identifier TALL_REALM_DIMENSION = Identifier.of("grey-future", "tall_realm");
    private static PositionedSoundInstance currentMusic = null;

    public static void updateMusic() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world == null) {
            stopTallRealmMusic();
            return;
        }

        boolean inTallRealm = client.world.getRegistryKey().getValue().equals(TALL_REALM_DIMENSION);

        if (inTallRealm) {
            // Check if music has finished or hasn't started
            if (currentMusic == null || !client.getSoundManager().isPlaying(currentMusic)) {
                playTallRealmMusic();
            }
        } else {
            stopTallRealmMusic();
        }
    }

    public static void playTallRealmMusic() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (ModSounds.TALL_REALM_MUSIC == null) {
            return; // Sound not registered yet
        }

        try {
            currentMusic = new PositionedSoundInstance(
                    ModSounds.TALL_REALM_MUSIC,
                    SoundCategory.MASTER,
                    1.0f,
                    1.0f,
                    net.minecraft.util.math.random.Random.create(),
                    0.0,
                    0.0,
                    0.0
            );
            client.getSoundManager().play(currentMusic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopTallRealmMusic() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (currentMusic != null) {
            client.getSoundManager().stop(currentMusic);
            currentMusic = null;
        }
    }
}