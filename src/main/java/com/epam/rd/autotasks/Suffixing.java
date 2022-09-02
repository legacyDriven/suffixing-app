package com.epam.rd.autotasks;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Suffixing {

    public static void main(String[] args) throws IOException {

        String PROPERTIES_PATH = args[0];

        Logger logger = Logger.getLogger(Suffixing.class.getName());
        Properties properties = new Properties();

        try(var input = new FileInputStream(PROPERTIES_PATH)){
            properties.load(input);
        }
        String mode = properties.getProperty("mode");
        String suffix = properties.getProperty("suffix");
        String files = properties.getProperty("files");

        if (!mode.equalsIgnoreCase("copy")&&!mode.equalsIgnoreCase("move")) {
            logger.log(Level.SEVERE, "Mode is not recognized: " + mode);
            return;
        }
        if (suffix == null || suffix.isBlank()) {
            logger.log(Level.SEVERE, "No suffix is configured");
            return;
        }
        if (files == null || files.isBlank()) {
            logger.log(Level.WARNING, "No files are configured to be copied/moved");
            return;
        }
        for (String file : files.split(":")) {
            File f = new File(file);
            if (!f.exists()) {
                logger.log(Level.SEVERE, "No such file: "+f.getPath().replace("\\", "/"));
            } else {
                int at = file.lastIndexOf('.');
                String newFile = file.substring(0, at)+suffix+file.substring(at);
                File dest = new File(newFile);
                if(mode.equalsIgnoreCase("copy")) {
                    FileUtils.copyFile(f.getAbsoluteFile(), dest.getAbsoluteFile());
                    logger.log(Level.INFO, f.getPath()
                            .replace("\\", "/") + " -> " + dest.getPath()
                            .replace("\\", "/"));
                }
                else {
                    FileUtils.moveFile(f.getAbsoluteFile(), dest.getAbsoluteFile());
                    logger.log(Level.INFO, f.getPath()
                            .replace("\\", "/") + " => " + dest.getPath()
                            .replace("\\", "/"));
                }}}}}
