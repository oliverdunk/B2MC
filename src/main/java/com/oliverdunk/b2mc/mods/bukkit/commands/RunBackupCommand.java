package com.oliverdunk.b2mc.mods.bukkit.commands;

import com.oliverdunk.b2mc.mods.bukkit.B2MC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RunBackupCommand implements CommandExecutor{

    private B2MC b2mc;

    public RunBackupCommand(B2MC b2mc){
        this.b2mc = b2mc;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(commandSender.hasPermission("b2mc.runbackup")){
            commandSender.sendMessage(ChatColor.GREEN + "Starting backup. Progress will be shown in the console...");
            b2mc.runBackup();
        }else{
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to force a backup!");
        }
        return true;
    }

}
