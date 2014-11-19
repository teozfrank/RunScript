package com.github.teozfrank.runscript.util;

import com.github.teozfrank.runscript.main.RunScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
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

    public FileManager(RunScript plugin) {
        this.plugin = plugin;
    }

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

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
