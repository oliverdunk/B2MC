package com.oliverdunk.b2mc.mods.bukkit;

import com.oliverdunk.b2mc.mods.bukkit.commands.RunBackupCommand;
import com.oliverdunk.b2mc.runnables.BackupRunner;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.logging.Logger;

public class B2MC extends JavaPlugin {

    private static Config config;
    private static Logger logger;

    public void onEnable(){
        long startTime = System.currentTimeMillis();

        //Initialize plugin
        this.saveDefaultConfig();
        config = new Config();
        logger = getLogger();
        config.loadConfig(this.getConfig());

        Bukkit.getPluginCommand("runbackup").setExecutor(new RunBackupCommand(this));
        scheduleBackup();

        this.getLogger().info("B2MC has been enabled! Took " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    /**
     * Forces a backup.
     */
    public void runBackup(){
        new BukkitRunnable(){
            public void run() {
                createRunner().run();
            }
        }.runTaskAsynchronously(this);
    }

    private void scheduleBackup(){
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 1);
        midnight.set(Calendar.DAY_OF_YEAR, midnight.get(Calendar.DAY_OF_YEAR) + 1);
        long tillMidnight = midnight.getTimeInMillis() - System.currentTimeMillis() - 1;
        long ticksTillMidnight = tillMidnight / 50;

        //Run every day at midnight
        new BukkitRunnable(){
            public void run() {
                createRunner().run();
            }
        }.runTaskTimerAsynchronously(this, ticksTillMidnight, 20 * 60 * 60 * 24);
    }

    private BackupRunner createRunner(){
        return new BackupRunner(config){

            @Override
            public void log(String message) {
                logger.info(message);
            }

            @Override
            public void setAutoSave(boolean autoSave) {
                for(World world : Bukkit.getServer().getWorlds()){
                    if(autoSave) world.save();
                    world.setAutoSave(autoSave);
                }
            }

        };
    }

}