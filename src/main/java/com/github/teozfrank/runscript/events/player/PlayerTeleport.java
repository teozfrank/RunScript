package com.github.teozfrank.runscript.events.player;

import bsh.EvalError;
import bsh.Interpreter;
import com.github.teozfrank.runscript.main.RunScript;
import com.github.teozfrank.runscript.util.FileManager;
import com.github.teozfrank.runscript.util.SendConsoleMessage;
import com.github.teozfrank.runscript.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.util.List;

/**
 The MIT License (MIT)

 Copyright (c) 2014 teozfrank

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

public class PlayerTeleport implements Listener {

    private RunScript plugin;

    public PlayerTeleport(RunScript plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent playerTeleportEvent) {

        FileManager fm = plugin.getFileManager();
        Interpreter interpreter = plugin.getInterpreter();

        try {
            interpreter.set("playerTeleportEvent", playerTeleportEvent);
        } catch(EvalError e) {
            e.printStackTrace();
        }

        String fileName = "PlayerTeleportEvent.txt";
        String code;

        if(fm.isUsingCache()) {
            code = fm.getCachedCode(fileName);
        } else {
            code = fm.getCodeFromFile(fileName);
        }

        try {
            interpreter.eval(code);
        } catch (EvalError e) {
            SendConsoleMessage.severe("There was an error parsing " + fileName + ", please check your code!");
        }
    }
}
