package com.github.teozfrank.runscript.events.weather;

import bsh.EvalError;
import bsh.Interpreter;
import com.github.teozfrank.runscript.main.RunScript;
import com.github.teozfrank.runscript.util.FileManager;
import com.github.teozfrank.runscript.util.SendConsoleMessage;
import com.github.teozfrank.runscript.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

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

public class WeatherChange implements Listener {
    private RunScript plugin;

    public WeatherChange(RunScript plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWeatherChange(WeatherChangeEvent weatherChangeEvent) {

        FileManager fm = plugin.getFileManager();
        Interpreter interpreter = plugin.getInterpreter();

        try {
            interpreter.set("weatherChangeEvent", weatherChangeEvent);// gives us access to the event
        } catch (EvalError e) {
            e.printStackTrace();
        }

        List<String> stringList = fm.getStringList(new File(Util.eventWeatherPath, "WeatherChangeEvent.txt"));
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : stringList) {
            stringBuilder.append(string);
        }

        try {
            interpreter.eval(stringBuilder.toString());
        } catch (EvalError e) {
            SendConsoleMessage.severe("There was an error parsing WeatherChangeEvent.txt, please check your code!");
        }
    }
}
