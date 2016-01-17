package com.oliverdunk.b2mc.mods.sponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.oliverdunk.b2mc.mods.sponge.commands.RunBackupCommand;
import com.oliverdunk.b2mc.runnables.BackupRunner;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.Calendar;

@Plugin(id = "b2mc", name = "B2MC", version = "1.0")
public class B2MC {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject
    private Injector injector;

    private static Config config;

    @Listener
    public void onServerStarted(GameStartedServerEvent event){
        long startTime = System.currentTimeMillis();

        //Initialize plugin
        config = injector.getInstance(Config.class);
        config.loadConfig(null);

        //Register command
        addCommand(new RunBackupCommand(this), "Forces a backup.", "runbackup");

        scheduleBackup();

        logger.info("B2MC has been enabled! Took " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    /**
     * Registers a command for the TwentyTicks plugin.
     *
     * @param executor The executor which will be called when the command is run
     * @param description Description of the command
     * @param command The command label which will be used to run the command
     */
    private void addCommand(CommandExecutor executor, String description, String command){
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Text.of(description))
                .permission("twentyticks.commands." + command)
                .executor(executor)
                .build();
        game.getCommandManager().register(this, myCommandSpec, command);
    }

    /**
     * Forces a backup.
     */
    public void runBackup(){
        Scheduler scheduler = game.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder();
        taskBuilder.async().execute(createRunner()).submit(this);
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
        Scheduler scheduler = game.getScheduler();
        Task.Builder taskBuilder = scheduler.createTaskBuilder();

        taskBuilder.delayTicks(ticksTillMidnight).intervalTicks(20 * 60 * 60 * 24).execute(createRunner()).async().submit(this);
    }

    private BackupRunner createRunner(){
        return new BackupRunner(config){

            @Override
            public void log(String message) {
                logger.info(message);
            }

            @Override
            public void setAutoSave(boolean autoSave) {
                //TODO: Add implementation once it is avaliable in Sponge
            }

        };
    }

}
