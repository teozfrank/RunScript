package com.github.teozfrank.runscript.commands;

import com.github.teozfrank.runscript.main.RunScript;
import com.github.teozfrank.runscript.util.FileManager;
import com.github.teozfrank.runscript.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Copyright teozfrank / FJFreelance 2014 All rights reserved.
 */
public class RSReloadCmd implements CommandExecutor {

    private RunScript plugin;

    public RSReloadCmd(RunScript plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        FileManager fm = plugin.getFileManager();
        plugin.reloadConfig();
        Util.sendMsg(sender, "config.yml reloaded!");
        if(fm.isUsingCache()) {
            fm.reloadEventsCode();
            Util.sendMsg(sender, "events cache reloaded!");
        }
        return true;
    }
}
