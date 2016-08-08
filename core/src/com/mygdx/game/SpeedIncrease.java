//package com.mygdx.game;
//
//
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * This class handles the speed increasing of all moving objects (except for the helicopter)
// * after a time period in the game.
// */
//public class SpeedIncrease {
//    private Timer timer;
//    private int numberOfSpeeding;
//    SpeedIncrease(int secondsDelay){
//        // secondsDelay is the number of seconds passes before the speed is increased
//        timer = new Timer();
//        // schedule the speed up to repeat after secondsDelay*1000 (seconds)
//        // The initial delay is also secondsDelay*1000 (seconds)
//        timer.schedule(new SpeedUp(), secondsDelay*1000, secondsDelay*1000);
//    }
//    class SpeedUp extends TimerTask {
//        @Override
//        public void run() {
//            numberOfSpeeding++;
//            StaticValues.HORIZONTAL_FLOATING_SPEED -= 50;
//            if (numberOfSpeeding == 7) {
//                timer.cancel();
//            }
//        }
//    }
//}
