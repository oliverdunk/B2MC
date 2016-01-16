package com.oliverdunk.b2mc;

import com.oliverdunk.b2mc.commands.RunBackupCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;

public class B2MC extends JavaPlugin {

    private static Config config;

    public void onEnable(){
        long startTime = System.currentTimeMillis();

        //Initialize plugin
        this.saveDefaultConfig();
        config = new Config();
        config.loadConfig(this.getConfig());

        Bukkit.getPluginCommand("runbackup").setExecutor(new RunBackupCommand(this));
        scheduleBackup();

        this.getLogger().info("B2MC has been enabled! Took " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    /**
     * Forces a backup.
     */
    public void runBackup(){
        new BackupRunner(this, config).runTaskAsynchronously(this);
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
        new BackupRunner(this, config).runTaskTimerAsynchronously(this, ticksTillMidnight, 20 * 60 * 60 * 24);
    }

}