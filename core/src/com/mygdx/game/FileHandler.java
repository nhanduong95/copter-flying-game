package com.mygdx.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Master on 1/6/2016.
 */
public interface FileHandler {
    FileOutputStream getOutputStream(String Filename);
    FileInputStream getInputStream(String Filename);
}
