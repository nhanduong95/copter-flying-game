package com.mygdx.game.Screens;

/**
 * Creates the screen that is about to be used
 */
public class ScreenController {
    private static Screen currentScreen;

    public static void setCurrentScreen(Screen screen){
        if (currentScreen != null)
            currentScreen.dispose();
        currentScreen = screen;
        currentScreen.create();
    }

    public static Screen getCurrentScreen(){
        return currentScreen;
    }

}
