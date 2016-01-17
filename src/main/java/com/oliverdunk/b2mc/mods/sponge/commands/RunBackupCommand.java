package com.oliverdunk.b2mc.mods.sponge.commands;

import com.oliverdunk.b2mc.mods.sponge.B2MC;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RunBackupCommand implements CommandExecutor {

    B2MC b2mc;

    public RunBackupCommand(B2MC b2mc){
        this.b2mc = b2mc;
    }

    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        if(commandSource.hasPermission("b2mc.runbackup")){
            commandSource.sendMessage(Text.builder("Starting backup. Progress will be shown in the console...").color(TextColors.GREEN).build());
            b2mc.runBackup();
        }else{
            commandSource.sendMessage(Text.builder("You do not have permission to force a backup!").color(TextColors.RED).build());
        }
        return CommandResult.success();
    }

}
