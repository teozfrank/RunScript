package com.github.teozfrank.runscript.main;

import bsh.EvalError;
import bsh.Interpreter;
import com.github.teozfrank.runscript.events.block.BlockBreak;
import com.github.teozfrank.runscript.events.block.BlockBurn;
import com.github.teozfrank.runscript.events.entity.CreatureSpawn;
import com.github.teozfrank.runscript.events.player.*;
import com.github.teozfrank.runscript.util.FileManager;
import com.github.teozfrank.runscript.util.SendConsoleMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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

public class RunScript extends JavaPlugin {

    private Interpreter interpreter;
    private FileManager fileManager;
    private static RunScript instance;

    @Override
    public void onEnable() {
        instance = this;
        this.interpreter = new Interpreter();

        try {
            interpreter.set("plugin", this);// gives us access to call plugin in all our text files
        } catch(EvalError e) {
            e.printStackTrace();
        }

        this.fileManager = new FileManager(this);

        this.setupFiles();
        // Block Events
        new BlockBreak(this);
        new BlockBurn(this);
        // Player Events
        new AsyncPlayerChat(this);
        new PlayerDeath(this);
        new PlayerInteract(this);
        new PlayerJoin(this);
        new PlayerLogin(this);
        new PlayerMove(this);
        new PlayerQuit(this);
        new PlayerRespawn(this);
        new PlayerCommandPreProcess(this);
        new PlayerPickupItem(this);
        new PlayerKick(this);
        new PlayerTeleport(this);

        // Entity events
        new CreatureSpawn(this);
    }

    /**
     * setup the files required for the plugin
     */
    private void setupFiles() {

        if(!new File(this.getDataFolder(), "config.yml").exists()) {
            SendConsoleMessage.info("creating config.yml");
            saveDefaultConfig();
        }

        getFileManager().setupEventsTextFiles();
    }

    /**
     * get the interpreter object
     * @return the interpreter object
     */
    public Interpreter getInterpreter() {
        return this.interpreter;
    }

    /**
     * get the file manager object
     * @return the file manager object
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    public static RunScript getInstance() {
        return instance;
    }
}
