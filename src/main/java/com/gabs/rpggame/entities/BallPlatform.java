package com.gabs.rpggame.entities;

import com.gabs.rpggame.Main;
import com.gabs.rpggame.Sound;
import com.gabs.rpggame.world.CollisionType;
import com.gabs.rpggame.world.Tile;
import com.gabs.rpggame.world.World;

import java.awt.image.BufferedImage;

public class BallPlatform extends Prop{
    private String type;
    private BufferedImage redSprite = Main.spritesheet.getSprite(150, 75);
    private BufferedImage blueSprite = Main.spritesheet.getSprite(225, 75);
    private BufferedImage yellowSprite = Main.spritesheet.getSprite(300, 75);
    private BufferedImage yellowSwitch = Main.spritesheet.getSprite(225, 0);
    private BufferedImage yellowOff = Main.spritesheet.getSprite(300, 0);
    private int tile;
    private boolean playerStep = false;
    private int frames = 0;

    //Blue Ball
    private boolean isFalling = false;
    private int fallSpeed = 8;
    private int fallTime = 2;

    //Yellow Ball
    private int switchTime = 2;
    private boolean hasCollision = true;
    private Sound sound = new Sound("sounds/fall.wav");

    public BallPlatform(String type){
        this.type = type;
        this.setSprite(
            type.equals("RED") ? redSprite :
            type.equals("BLUE") ? blueSprite :
            type.equals("YELLOW") ? yellowSprite : redSprite
        );
        this.switchTime = Main.generateRandomInt(3,5);
    }

    @Override
    public void eventTick() {
        super.eventTick();
        switch (type){
            case "BLUE":{
                if(isPlayerOnTop()){
                    isFalling = true;
                    playerStep = true;
                }
                if(isFalling && frames >= fallTime*60){
                    World.tiles[this.getTile()].setType(CollisionType.NO_COLLISION);
                    this.setY(this.getY() + fallSpeed);
                    sound.play();
                }
                if(playerStep)
                    frames++;
                break;
            }
            case "YELLOW":{
                if(frames >= switchTime*60){
                    if(hasCollision){
                        World.tiles[this.getTile()].setType(CollisionType.NO_COLLISION);
                        this.setSprite(yellowOff);
                        hasCollision = false;
                        frames=0;
                    } else {
                        World.tiles[this.getTile()].setType(CollisionType.BLOCK);
                        this.setSprite(yellowSprite);
                        hasCollision = true;
                        frames=0;
                    }
                } else if(frames >= (switchTime-2)*60 && hasCollision)
                    this.setSprite(yellowSwitch);

                frames++;
                break;
            }
        }

    }

    public boolean isPlayerOnTop(){
        return((Main.player.getX() >= this.getX() && Main.player.getX() < this.getX()+this.getWidth() ||
                (Main.player.getX() > this.getX()-Main.GameProperties.TileSize && Main.player.getX()+Main.player.getWidth() < this.getX()+this.getWidth())) &&
                Main.player.getY() == this.getY()-Main.player.getHeight());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTile() {
        return tile;
    }

    public void setTile(int tile) {
        this.tile = tile;
    }
}
