package ch.zhaw.pm2.ironbelchers.game.weapons;


import ch.zhaw.pm2.ironbelchers.game.Bullet;
import javafx.geometry.Point2D;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class BasicWeapon implements IWeapon {
    private final AtomicLong reloadingTimeRemaining = new AtomicLong(0);

    private final int bulletSpeed;

    private final int damage;

    private final int reloadingTime;

    private static final int STANDARD_BULLET_SPEED = 10;

    private static final int STANDARD_DAMAGE = 30;

    private static final int STANDARD_RELOADING_TIME = 5;

    private final ExecutorService executor;

    public BasicWeapon() {
        this(STANDARD_BULLET_SPEED, STANDARD_DAMAGE, STANDARD_RELOADING_TIME);
    }

    protected BasicWeapon(int bulletSpeed, int damage, int reloadingTime) {
        this.bulletSpeed = bulletSpeed;
        this.damage = damage;
        this.reloadingTime = reloadingTime;
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public Bullet fire(Point2D direction, Point2D position, int shipId) {
        if (isReloading()) {
            return null;
        }
        reloadingTimeRemaining.set(reloadingTime);
        executor.execute(this::reload);
        return new Bullet(position, bulletSpeed, damage, direction, shipId);
    }

    @Override
    public void reload() {
        while (isReloading()) {
            reloadingTimeRemaining.decrementAndGet();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean isReloading() {
        return reloadingTimeRemaining.get() > 0;
    }

    public void stop() {
        executor.shutdownNow();
    }
}
