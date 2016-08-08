package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.ConstantValues;
import com.mygdx.game.Player;
import com.mygdx.game.Screens.GameOverScreen;
import com.mygdx.game.Screens.ScreenController;
import com.mygdx.game.StaticValues;
import com.parse.ParseObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * This is where to place the game logic
 */
public class GameController {
    protected Array<CaveObject> caveTerrains = new Array<CaveObject>();
    protected Array<CaveObject> caveUpperPillars = new Array<CaveObject>();
    protected Array<CaveObject> caveLowerPillars = new Array<CaveObject>();
    protected Array<FloatingItem>floatingItems = new Array<FloatingItem>();
    private Array<ParticleEffect> particleEffects = new Array<ParticleEffect>();

    private TextureRegion caveAbove, caveBelow;
    private TextureRegion pillarUp, pillarDown;
    private TextureRegion fuel, heart, star, rocket;
    private TextureAtlas atlas;
    protected Helicopter helicopter;

    private Vector2 lastUpperPillarPos;
    private Vector2 lastLowerPillarPos;
    private Vector2 lastUpperFloatingPos;
    private Vector2 lastLowerFloatingPos;
    private Vector2 upperPillarPos;
    private Vector2 lowerPillarPos;

    private boolean canSpawnUpperItems = false;
    private long lastUpperItemAppearance, lastLowerItemAppearance,
            upperAppearanceInterval, lowerAppearanceInterval;
    private float heartAppearanceTime = 50;
    private float fuelAppearanceTime = 30;
    private float rocketAppearanceTime = 10;
    private float starAppearanceTime;

    private Sound pickupSound;
    private Sound explosionSound;
    private Sound crashSound;

    public GameController(TextureAtlas atlas) {
        pickupSound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_PICKUP, Sound.class);
        explosionSound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_EXPLOSION, Sound.class);
        crashSound = StaticValues.ASSET_MANAGER.get(ConstantValues.SOUND_CRASH, Sound.class);

        // Initialize all the screen objects
        this.atlas = atlas;
        caveAbove = atlas.findRegion(ConstantValues.CAVE_ABOVE);
        caveBelow = atlas.findRegion(ConstantValues.CAVE_BELOW);
        pillarUp = atlas.findRegion(ConstantValues.CAVE_PILLAR);
        pillarDown = new TextureRegion(pillarUp);
        pillarDown.flip(true, true);
        star = atlas.findRegion(ConstantValues.STAR);
        fuel = atlas.findRegion(ConstantValues.FUEL);
        heart = atlas.findRegion(ConstantValues.HEART);
        rocket = atlas.findRegion(ConstantValues.ROCKET);

        helicopter = new Helicopter(atlas.findRegion(ConstantValues.HELICOPTER_01),
                new Vector2(100, StaticValues.SCREEN_HEIGHT /2));

        lastUpperPillarPos = new Vector2();
        lastLowerPillarPos = new Vector2();
        lastUpperFloatingPos = new Vector2();
        lastLowerFloatingPos = new Vector2();

        lastUpperItemAppearance = System.currentTimeMillis();
        lastLowerItemAppearance = System.currentTimeMillis();
        upperAppearanceInterval = 500;
        lowerAppearanceInterval = 2000;

        if (StaticValues.START_NEW_GAME)
            initScene();
        else {
            if (!StaticValues.GAME_SAVED)
                initScene();
            else {
                loadGame_new();
                StaticValues.GAME_SAVED = false;
            }
        }

    }

    public void updateObjects() {
        addObstacle();
        setScrollingScreen();
        for (GameObject obj : caveTerrains){
            obj.update();
        }
        for (GameObject obj : caveUpperPillars){
            obj.update();
        }
        for(GameObject obj : caveLowerPillars){
            obj.update();
        }
        for(GameObject obj: floatingItems){
            obj.update();
        }
        helicopter.update();
        pickUpItem();
        checkObstacleCollision();
    }

    public void renderObjects(SpriteBatch spriteBatch) {
        for (GameObject obj : caveUpperPillars){
            obj.render(spriteBatch);
        }
        for (GameObject obj : caveLowerPillars){
            obj.render(spriteBatch);
        }
        for (GameObject obj : caveTerrains){
            obj.render(spriteBatch);
        }
        for (GameObject obj: floatingItems){
            obj.render(spriteBatch);
        }
        helicopter.render(spriteBatch);


        for (ParticleEffect p : particleEffects){
            p.draw(spriteBatch);
            p.update(Gdx.graphics.getDeltaTime());

            if (p.isComplete()){
                particleEffects.removeValue(p, false);
            }
        }
    }

    private void initScene() {
        // Set the cave below
        caveTerrains.add(new CaveObject(caveBelow, new Vector2(0, 0),
                        ConstantValues.CAVE_BELOW));

        caveTerrains.add(new CaveObject(caveBelow, new Vector2(caveBelow.getRegionWidth(), 0),
                        ConstantValues.CAVE_BELOW));

        caveTerrains.add(new CaveObject(caveBelow, new Vector2(caveBelow.getRegionWidth()*2, 0),
                        ConstantValues.CAVE_BELOW));

        // Set the cave above
        caveTerrains.add(new CaveObject(caveAbove,
                            new Vector2(0, StaticValues.SCREEN_HEIGHT - caveAbove.getRegionHeight()),
                            ConstantValues.CAVE_ABOVE));

        caveTerrains.add(new CaveObject(caveAbove,
                            new Vector2(caveAbove.getRegionWidth(),
                                StaticValues.SCREEN_HEIGHT - caveAbove.getRegionHeight()),
                            ConstantValues.CAVE_ABOVE));

        caveTerrains.add(new CaveObject(caveAbove, new Vector2(caveAbove.getRegionWidth() * 2,
                            StaticValues.SCREEN_HEIGHT - caveAbove.getRegionHeight()),
                            ConstantValues.CAVE_ABOVE));
    }

    private void setScrollingScreen() {
        for (CaveObject terrain : caveTerrains) {
            // Set caveBelow object
            if(terrain.getType().equals(ConstantValues.CAVE_BELOW)
                    && terrain.getPosition().x < -caveBelow.getRegionWidth()) {
                caveTerrains.add(new CaveObject(caveBelow,
                        new Vector2(caveBelow.getRegionWidth()*3 + terrain.getPosition().x, 0),
                                    ConstantValues.CAVE_BELOW));

            // Set caveAbove object
            }else if(terrain.getType().equals(ConstantValues.CAVE_ABOVE)
                    && terrain.getPosition().x < -caveAbove.getRegionWidth()) {
                caveTerrains.add(new CaveObject(caveAbove,
                        new Vector2(caveBelow.getRegionWidth() * 3 + terrain.getPosition().x,
                                StaticValues.SCREEN_HEIGHT - caveAbove.getRegionHeight()),
                                ConstantValues.CAVE_ABOVE));
            }

            // Remove the cave objects that have gone out of the screen
            // The cave object has the same width as the screen
            if (terrain.getPosition().x < -terrain.getTexture().getRegionWidth()) {
                caveTerrains.removeValue(terrain, false);
            }
        }
    }

    private void addObstacle() {
        if (System.currentTimeMillis() - lastUpperItemAppearance >= upperAppearanceInterval) {
            lastUpperItemAppearance = System.currentTimeMillis();
            upperAppearanceInterval = MathUtils.random(3000, 5000);

            upperPillarPos = new Vector2(StaticValues.SCREEN_WIDTH + 10,
                StaticValues.SCREEN_HEIGHT - pillarDown.getRegionHeight());

            if(lastUpperPillarPos.x > upperPillarPos.x){
                upperAppearanceInterval = MathUtils.random(100);
                return;
            }

            // Add pillars
            caveUpperPillars.add(new CaveObject(pillarUp, upperPillarPos, ConstantValues.CAVE_PILLAR));
            lastUpperPillarPos = upperPillarPos;
            canSpawnUpperItems = true;
        }

        // Check and add floating items
        if(System.currentTimeMillis() - lastUpperItemAppearance
                >= upperAppearanceInterval/2 + MathUtils.random(-100, 100) && canSpawnUpperItems){
            canSpawnUpperItems = false;
            heartAppearanceTime -= MathUtils.random(3, 10);
            fuelAppearanceTime -= MathUtils.random(3,10);
            starAppearanceTime -= MathUtils.random(5,10);
            rocketAppearanceTime -= MathUtils.random(5,10);

            Vector2 randomPos = new Vector2();
            randomPos.x = StaticValues.SCREEN_WIDTH + 10;
            randomPos.y = StaticValues.SCREEN_HEIGHT / 2
                    + MathUtils.random(StaticValues.SCREEN_HEIGHT / 2 - caveAbove.getRegionHeight() - heart.getRegionHeight());

            if (heartAppearanceTime <= 0) {
                randomPos.y = StaticValues.SCREEN_HEIGHT / 2
                        + MathUtils.random(StaticValues.SCREEN_HEIGHT / 2 - caveAbove.getRegionHeight() - heart.getRegionHeight());
                floatingItems.add(new FloatingItem(heart, randomPos, ConstantValues.HEART, 0, 1, 0));
                heartAppearanceTime = MathUtils.random(20, 30);

            } else if (fuelAppearanceTime <= 0) {
                randomPos.y = StaticValues.SCREEN_HEIGHT / 2
                        + MathUtils.random(StaticValues.SCREEN_HEIGHT / 2 - caveAbove.getRegionHeight() - fuel.getRegionHeight());
                floatingItems.add(new FloatingItem(fuel, randomPos, ConstantValues.FUEL, 0, 0, 20));
                fuelAppearanceTime = MathUtils.random(10, 20);

            } else if (starAppearanceTime <= 0) {
                randomPos.y = StaticValues.SCREEN_HEIGHT / 2
                        + MathUtils.random(StaticValues.SCREEN_HEIGHT / 2 - caveAbove.getRegionHeight() - star.getRegionHeight());
                floatingItems.add(new FloatingItem(star, randomPos, ConstantValues.STAR, 10, 0, 0));
                starAppearanceTime = MathUtils.random(5, 15);

            } else if (rocketAppearanceTime <= 0) {
                Vector2 pos = new Vector2(StaticValues.SCREEN_WIDTH + 10, helicopter.getHeliPos().y);
                floatingItems.add(new Rocket(rocket, pos, System.currentTimeMillis()));
                rocketAppearanceTime = MathUtils.random(10,20);
            }
        }

        // Add pillars and floating items on the half below screen
        if(System.currentTimeMillis() - lastLowerItemAppearance >= lowerAppearanceInterval){
            lastLowerItemAppearance = System.currentTimeMillis();
            lowerAppearanceInterval = MathUtils.random(4000, 7000);

            lowerPillarPos = new Vector2(StaticValues.SCREEN_WIDTH + MathUtils.random(100, 300), 0);

            // Add pillars
            caveLowerPillars.add(new CaveObject(pillarDown, lowerPillarPos, ConstantValues.CAVE_PILLAR));
            lastLowerPillarPos = lowerPillarPos;
        }

        // Remove the pillars that have gone out of the screen
        for (CaveObject pillar : caveUpperPillars) {
            if (pillar.getPosition().x < -pillar.getTexture().getRegionWidth())
                caveUpperPillars.removeValue(pillar, false);
        }

        for (CaveObject pillar : caveLowerPillars) {
            if (pillar.getPosition().x < -pillar.getTexture().getRegionWidth())
                caveLowerPillars.removeValue(pillar, false);
        }

        // Remove the floating items that have gone out of the screen
        for (FloatingItem item : floatingItems) {
            if (item.getPosition().x < -item.getTexture().getRegionWidth()) {
                floatingItems.removeValue(item, false);
            }
        }
    }

    private void checkObstacleCollision() {
        for (GameObject terrain : caveTerrains) {
            //If the helicopter collides the cave terrain
            if (terrain.getBounds().overlaps(helicopter.getBounds())){
                if(!terrain.getOverlappedState() && !StaticValues.GAME_OVER){
                    terrain.setHasOverlapped(true, helicopter);
                    particleEffects.add(terrain.getExplosion());
                    crashSound.play(StaticValues.SOUND_VOLUME);
                }
                StaticValues.GAME_OVER = true;
                break;
            }
        }
        for (GameObject pillar : caveUpperPillars) {
            //If the helicopter collides the cave pillar
            if (helicopter.getBounds().overlaps(pillar.getBounds())) {
                if(!pillar.getOverlappedState() && !StaticValues.GAME_OVER){
                    pillar.setHasOverlapped(true, helicopter);
                    StaticValues.PLAYER.alterLife(-1);
                    particleEffects.add(pillar.getExplosion());
                    crashSound.play(StaticValues.SOUND_VOLUME);

                }
                break;
            }
        }
        for (GameObject pillar : caveLowerPillars) {
            //If the helicopter collides the cave pillar
            if (helicopter.getBounds().overlaps(pillar.getPillarBounds())) {
                if(!pillar.getOverlappedState() && !StaticValues.GAME_OVER){
                    pillar.setHasOverlapped(true, helicopter);
                    StaticValues.PLAYER.alterLife(-1);
                    particleEffects.add(pillar.getExplosion());
                    crashSound.play(StaticValues.SOUND_VOLUME);

                }
                break;
            }
        }

        if(StaticValues.PLAYER.getLife() == 0)
            StaticValues.GAME_OVER = true;

        // Enter the GAME_OVER state
        if (StaticValues.GAME_OVER)
            gameOver();
    }

    private void pickUpItem(){
        if(StaticValues.GAME_RUNNING){
            for(FloatingItem floatingItem: floatingItems){
                if(helicopter.getBounds().overlaps(floatingItem.getBounds())){
                    if(!floatingItem.hasOverlapped){
                        floatingItem.hasOverlapped = true;

                        if (floatingItem.getType().equals(ConstantValues.STAR)){
                            StaticValues.PLAYER.alterScore(floatingItem.getPoints());
                            pickupSound.play(StaticValues.SOUND_VOLUME);
                        }

                        else if (floatingItem.getType().equals(ConstantValues.FUEL)){
                            StaticValues.PLAYER.alterFuel(floatingItem.getFuel());
                            pickupSound.play(StaticValues.SOUND_VOLUME);
                        }
                        else if (floatingItem.getType().equals(ConstantValues.HEART)) {
                            StaticValues.PLAYER.alterLife(floatingItem.getLife());
                            pickupSound.play(StaticValues.SOUND_VOLUME);
                        }
                        else if (floatingItem.getType().equals(ConstantValues.ROCKET)) {
                            StaticValues.PLAYER.alterLife(-1);
                            floatingItem.setHasOverlapped(true, helicopter);
                            particleEffects.add(floatingItem.getExplosion());
                            explosionSound.play(StaticValues.SOUND_VOLUME);
                        }
                        floatingItems.removeValue(floatingItem, false);
                    }
                }
            }
        }
    }

    // Open the game-over screen
    public void gameOver() {
        if (!helicopter.isOnScreen()) {
            StaticValues.GAME_RUNNING = false;
            StaticValues.GAME_STARTED = false;
            StaticValues.GAME_OVER = false;

            // Put the score on cloud
            StaticValues.SCORES_SAVED.put("score", StaticValues.PLAYER.getScore());
            StaticValues.SCORES_SAVED.saveEventually();

            // Switch screen
            ScreenController.setCurrentScreen(new GameOverScreen());
        }
    }

    // Save the game before exiting the Main screen
    public void saveGame_new() {
        StaticValues.GAME_RUNNING = false;
        StaticValues.GAME_STARTED = false;
        StaticValues.GAME_OVER = false;

        FileOutputStream fos1 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_TERRAIN_ARRAY);
        String output1 = "";
        for (CaveObject obj : caveTerrains) {
            output1 = output1+obj.getPosition().x+":"+obj.getPosition().y+":"+obj.getType()+"\n";
        }
        try {
            fos1.write(output1.getBytes());
            fos1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos2 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_CAVE_UPPER);
        String output2 = "";
        for (CaveObject obj : caveUpperPillars){
            output2 = output2+obj.getPosition().x+":"+obj.getPosition().y+":"+obj.getType()+"\n";
        }
        try {
            fos2.write(output2.getBytes());
            fos2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos3 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_CAVE_LOWER);
        String output3 = "";
        for (CaveObject obj : caveLowerPillars){
            output3 = output3+obj.getPosition().x+":"+obj.getPosition().y+":"+obj.getType()+"\n";
        }
        try {
            fos3.write(output3.getBytes());
            fos3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos4 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_FLOATING_ITEMS);
        String output4 = "";
        for (FloatingItem obj : floatingItems) {
            output4 = output4+obj.getPosition().x+":"+obj.getPosition().y+":"+obj.getType()+":"+obj.getPoints()+":"+obj.getLife()+":"+obj.getFuel()+"\n";
        }
        try {
            fos4.write(output4.getBytes());
            fos4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos5 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_PLAYER);
        String output5 = "";
        output5 = output5 + helicopter.getPosition().x + ":" +
                helicopter.getPosition().y + ":" +
                StaticValues.PLAYER.getScore() + ":" +
                StaticValues.PLAYER.getLife() + ":" +
                StaticValues.PLAYER.getFuel();
        try {
            fos5.write(output5.getBytes());
            fos5.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos6 = StaticValues.FILE_HANDLER.getOutputStream(ConstantValues.SAVEGAME_SETTINGS);
        String output6 = "";
        output6 = output6 + StaticValues.SOUND_VOLUME + ":" +
                StaticValues.MUSIC_VOLUME + ":" +
                StaticValues.HORIZONTAL_FLOATING_SPEED;
        try {
            fos6.write(output6.getBytes());
            fos6.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StaticValues.GAME_SAVED = true;
    }

    // Return to the game after existing the Main screen
    public void loadGame_new() {
        FileInputStream fis = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_TERRAIN_ARRAY);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        try {
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split(":");
                if (tokens[2].equals(ConstantValues.CAVE_ABOVE)) {
                    System.out.println("Drawing cave above");
                    caveTerrains.add(new CaveObject(caveAbove, new Vector2(Float.parseFloat(tokens[0]),
                            Float.parseFloat(tokens[1])), ConstantValues.CAVE_ABOVE));
                } else if (tokens[2].equals(ConstantValues.CAVE_BELOW)) {
                    System.out.println("Drawing cave below");
                    caveTerrains.add(new CaveObject(caveBelow, new Vector2(Float.parseFloat(tokens[0]),
                            Float.parseFloat(tokens[1])), ConstantValues.CAVE_BELOW));
                }
                line = reader.readLine();
            }
            fis.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis2 = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_CAVE_UPPER);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(fis2));
        try {
            String line = reader2.readLine();
            while(line != null) {
                String[] tokens = line.split(":");
                caveUpperPillars.add(new CaveObject(pillarUp, new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1])), ConstantValues.CAVE_PILLAR));
                line = reader2.readLine();
            }
            fis2.close();
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        upperAppearanceInterval = MathUtils.random(3000, 5000);
        lastUpperItemAppearance = System.currentTimeMillis();

        FileInputStream fis3 = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_CAVE_LOWER);
        BufferedReader reader3 = new BufferedReader(new InputStreamReader(fis3));
        try {
            String line = reader3.readLine();
            while(line != null) {
                String[] tokens = line.split(":");
                caveLowerPillars.add(new CaveObject(pillarDown, new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1])), ConstantValues.CAVE_PILLAR));
                line = reader3.readLine();
            }
            fis3.close();
            reader3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        upperAppearanceInterval = MathUtils.random(4000, 7000);
        lastLowerItemAppearance = System.currentTimeMillis();

        FileInputStream fis4 = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_FLOATING_ITEMS);
        BufferedReader reader4 = new BufferedReader(new InputStreamReader(fis4));
        try {
            String line = reader4.readLine();
            while(line != null) {
                String[] tokens = line.split(":");
                if (tokens[2].equals(ConstantValues.ROCKET))
                    floatingItems.add(new Rocket(atlas.findRegion(tokens[2]),
                            new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1])), 0));
                else
                    floatingItems.add(new FloatingItem(atlas.findRegion(tokens[2]),
                            new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1])),
                            tokens[2],
                            Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5])));
                line = reader4.readLine();
            }
            fis4.close();
            reader4.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis5 = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_PLAYER);
        BufferedReader reader5 = new BufferedReader(new InputStreamReader(fis5));
        try {
            String line = reader5.readLine();
            System.out.println(line);
            String[] tokens = line.split(":");
            helicopter.setHeliPos(new Vector2(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1])));
            StaticValues.PLAYER.setScore(Integer.parseInt(tokens[2]));
            StaticValues.PLAYER.setLife(Integer.parseInt(tokens[3]));
            StaticValues.PLAYER.setFuel(Integer.parseInt(tokens[4]));

            fis5.close();
            reader5.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream fis6 = StaticValues.FILE_HANDLER.getInputStream(ConstantValues.SAVEGAME_SETTINGS);
        BufferedReader reader6 = new BufferedReader(new InputStreamReader(fis6));
        try {
            String line = reader6.readLine();
            System.out.println(line);
            String[] tokens = line.split(":");
            StaticValues.SOUND_VOLUME = Float.parseFloat(tokens[0]);
            StaticValues.MUSIC_VOLUME = Float.parseFloat(tokens[1]);
            StaticValues.HORIZONTAL_FLOATING_SPEED = Float.parseFloat(tokens[2]);

            System.out.println("Music: " + Float.parseFloat(tokens[1]) + " Sound: " + Float.parseFloat(tokens[0])
                            + " Speed: " + Float.parseFloat(tokens[2]));

            fis6.close();
            reader6.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
