package com.mygdx.game;

/**
 * Created by User on 27-Dec-15.
 */
public class Player {
    private int score = 0;
    private int life = 5;
    private int fuel = 200;
    private int maxFuel = 400;

    public int getScore(){
        return score;
    }
    public int getLife(){
        return life;
    }
    public int getFuel(){
        return fuel;
    }
    public int getMaxFuel() { return maxFuel; }
    public void setScore(int score) { this.score = score; }
    public void setLife(int life) { this.life = life; }
    public void setFuel(int fuel) { this.fuel = fuel; }

    public void alterScore (int scoreToAlter) {
        score += scoreToAlter;
        System.out.print("Player score: " + score);
    }
    public void alterLife (int lifeToAlter) {
        if (life > 0)
            life += lifeToAlter;
        System.out.print("Player life: " + life);
    }
    public void alterFuel(int fuelToAlter){
        fuel += fuelToAlter;
    }
}
