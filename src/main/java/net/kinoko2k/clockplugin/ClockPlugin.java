package net.kinoko2k.clockplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockPlugin extends JavaPlugin {

    private TextDisplay clockDisplay;

    @Override
    public void onEnable() {
        getLogger().info("針が動き出す...これが時計かぁ...");

        World world = Bukkit.getWorlds().get(0);
        Location clockLocation = new Location(world, 0, 100, 0);

        clockDisplay = world.spawn(clockLocation, TextDisplay.class);
        clockDisplay.setText("時計を再読み込みしています。");
        clockDisplay.setBillboard(org.bukkit.entity.Display.Billboard.VERTICAL);
        startClockUpdateTask();
    }

    @Override
    public void onDisable() {
        getLogger().info("時計が動かなくなりました。");

        if (clockDisplay != null && !clockDisplay.isDead()) {
            clockDisplay.remove();
        }
    }

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    private void startClockUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String currentTime = LocalTime.now().format(timeFormat);
                if (clockDisplay != null && !clockDisplay.isDead()) {
                    clockDisplay.setText(currentTime);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }
}
