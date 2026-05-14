package net.chixozhmix.dnmmod.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class BossMusicHandler extends AbstractTickableSoundInstance {
    private final Entity boss;

    public BossMusicHandler(Entity boss, IBossMusic musicBoss) {

        super(
                musicBoss.getBossMusic(),
                SoundSource.MUSIC,
                SoundInstance.createUnseededRandom()
        );

        this.boss = boss;

        this.looping = true;
        this.delay = 0;

        this.volume = 1f;
        this.pitch = 1f;
    }

    @Override
    public void tick() {

        if (boss.isRemoved() || !boss.isAlive()) {
            this.stop();
            return;
        }

        this.x = boss.getX();
        this.y = boss.getY();
        this.z = boss.getZ();
    }
}
