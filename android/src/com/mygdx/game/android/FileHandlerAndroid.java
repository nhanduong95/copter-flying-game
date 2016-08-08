package com.mygdx.game.android;

import android.content.Context;
import android.os.Handler;

import com.mygdx.game.FileHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Master on 1/6/2016.
 */
public class FileHandlerAndroid implements FileHandler {
    Handler handler;
    Context context;
    public FileHandlerAndroid(Context context) {
        this.context = context;
    }
    @Override
    public FileOutputStream getOutputStream(String Filename) {
        try {
            FileOutputStream fos = context.openFileOutput(Filename, 0);
            return fos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FileInputStream getInputStream(String Filename) {
        try {
            FileInputStream fis = context.openFileInput(Filename);
            return fis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
