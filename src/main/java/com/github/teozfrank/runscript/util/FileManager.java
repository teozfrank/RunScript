package com.github.teozfrank.runscript.util;

import com.github.teozfrank.runscript.main.RunScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

public class FileManager {

    private RunScript plugin;
    private HashMap<String, String> cachedCode;
    private List<File> eventFiles;

    public FileManager(RunScript plugin) {
        this.plugin = plugin;
        cachedCode = new HashMap<String, String>();
        eventFiles = new ArrayList<File>();
        setupEventFiles();
    }

    /**
     * get the cached code stored for a file name
     * @param fileName the file name of the code to get from
     * @return the cached code to pass to the interpreter
     */
    public String getCachedCode(String fileName) {
        return cachedCode.get(fileName);
    }

    /**
     * add an event file to the file list
     * @param file the file
     */
    public void addEventFile(File file) {
        eventFiles.add(file);
    }

    /**
     * add cached code to the cache
     * @param fileName the file name
     * @param eventCode the code as a string
     */
    public void addCachedCode(String fileName, String eventCode) {
        cachedCode.put(fileName, eventCode);
    }

    /**
     * get and event file by the file name
     * @param fileName the name of the file
     * @return the file if exists, returns null if it does not
     */
    public File getEventFileByName(String fileName) {
        for(File file: getEventFiles()) {
            if(file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    /**
     * setup the event files adding them to the list of files
     */
    private void setupEventFiles() {
        addEventFile(new File(Util.eventBlockPath, "BlockBreakEvent.txt"));
        addEventFile(new File(Util.eventBlockPath, "BlockBurnEvent.txt"));
        addEventFile(new File(Util.eventEntityPath, "CreatureSpawnEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "AsyncPlayerChatEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerCommandPreprocessEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerDeathEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerInteractEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerJoinEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerKickEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerLoginEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerMoveEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerPickupItemEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerQuitEvent.txt"));
        addEventFile(new File(Util.eventPlayerPath, "PlayerTeleportEvent.txt"));
        addEventFile(new File(Util.eventWeatherPath, "WeatherChangeEvent.txt"));
    }

    /**
     * get a list of the event files
     * @returna list of files
     */
    public List<File> getEventFiles() {
        return eventFiles;
    }

    /**
     * load the events text files contents to the cache
     */
    public void loadEventsCodeToCache() {

        for(File file: getEventFiles()) {
            List<String> stringList = getStringList(file);
            StringBuilder stringBuilder = new StringBuilder();

            for (String string : stringList) {
                stringBuilder.append(string);
            }
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("adding event code with filename: " + file.getName());
            }
            addCachedCode(file.getName(), stringBuilder.toString());
        }

    }

    /**
     * reload the events text file contents
     */
    public void reloadEventsCode() {
        if(!cachedCode.isEmpty()) {
            if(plugin.isDebugEnabled()) {
                SendConsoleMessage.debug("cached code is not empty, clearing it.");
            }
            cachedCode.clear();
        }

        loadEventsCodeToCache();
    }

    /**
     * get contents of text file directly from the file as a string
     * @param fileName the file name
     * @return the text file contents as a single string
     */
    public String getCodeFromFile(String fileName) {

        List<String> stringList = getStringList(getEventFileByName(fileName));
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : stringList) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * get contents of text file directly from the file as a list of strings
     * @param file the file
     * @return a list of Strings for the file
     */
    public List<String> getStringList(File file) {
        List<String> stringList = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringList.add(line);
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringList;
    }

    /**
     * creates a text file in a path if it does not exist
     * @param basePath the path of the file
     * @param fileName the file name to create
     */
    public void checkAndCreateTextFile(String basePath, String fileName) {
        if (!new File(basePath, fileName).exists()) {
            File file = new File(basePath, fileName);
            try {
                SendConsoleMessage.info("creating " + fileName);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * setup the event directory and create the required text files
     */
    public void setupEventsTextFiles() {

        File eventDir = new File(Util.eventBasePath);
        File eventPlayerDir = new File(Util.eventPlayerPath);
        File eventBlockDir = new File(Util.eventBlockPath);
        File eventEntityDir = new File(Util.eventEntityPath);
        File eventWeatherDir = new File(Util.eventWeatherPath);

        if(!eventDir.exists()) {
            try {
                eventDir.mkdir();
            } catch (SecurityException e) {

            }
        }

        if(!eventPlayerDir.exists()) {
            try {
                eventPlayerDir.mkdir();
            } catch (SecurityException e) {

            }
        }

        if(!eventBlockDir.exists()) {
            try {
                eventBlockDir.mkdir();
            } catch (SecurityException e) {

            }
        }

        if(!eventEntityDir.exists()) {
            try {
                eventEntityDir.mkdir();
            } catch (SecurityException e) {

            }
        }

        if(!eventWeatherDir.exists()) {
            try {
                eventWeatherDir.mkdir();
            } catch (SecurityException e) {

            }
        }

        CodeSource src = FileManager.class.getProtectionDomain().getCodeSource();

        if(src != null) {
            URL jar = src.getLocation();

            try {
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                ZipEntry ze = null;

                while ((ze = zip.getNextEntry()) != null) {
                    String entryName = ze.getName();
                    if(entryName.contains("Player") && entryName.endsWith(".txt")) {
                        this.checkAndCreateTextFile(Util.eventPlayerPath, entryName);
                    }
                    if(entryName.contains("Block") && entryName.endsWith(".txt")) {
                        this.checkAndCreateTextFile(Util.eventBlockPath, entryName);
                    }
                    if(entryName.contains("Entity") || entryName.contains("Creature")) {
                        if(entryName.endsWith(".txt")) {
                            this.checkAndCreateTextFile(Util.eventEntityPath, entryName);
                        }
                    }
                    if(entryName.contains("Weather") && entryName.endsWith(".txt")) {
                        this.checkAndCreateTextFile(Util.eventWeatherPath, entryName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * should we cache the values from the files?
     * @return true if enabled false if not
     */
    public boolean isUsingCache() {
        return plugin.getConfig().getBoolean("runscript.usecache");
    }

    /**
     * is debug mode enabled for the plugin
     * @return true if enabled, false if not
     */
    public boolean isDebugEnabled() {
        return plugin.getConfig().getBoolean("runscript.debug.enabled");
    }
}
