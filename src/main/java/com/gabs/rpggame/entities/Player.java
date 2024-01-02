package com.gabs.rpggame.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.gabs.rpggame.Main;
import com.gabs.rpggame.Sound;
import com.gabs.rpggame.world.Camera;
import com.gabs.rpggame.world.Direction;
import com.gabs.rpggame.world.World;

public class Player extends AliveEntity {
	
	private boolean right, left, up, down;
	private boolean moving;
	private boolean jump = false;
	private boolean jumping = false;
	public int jumpHeight = Main.GameProperties.TileSize*4;
	public int jumpFrames = 0;
	public int jumpSpeed = 4;
	private int speed = 2;
	private int gravity = 4;
	private Direction direction = Direction.LEFT;
	private final BufferedImage leftSprite = Main.spritesheet.getSprite(75, 150);
	private final BufferedImage rightSprite = Main.spritesheet.getSprite(150, 150);

	public Player() {
		super();

		this.setWidth( Main.GameProperties.TileSize )
			.setHeight( Main.GameProperties.TileSize );
		this.setSprite(leftSprite);
		this.setTargetable(true);
	}

	@Override
	public void eventTick() {
		//Movement
		this.setMoving(false);
		focusCameraOnPlayer();

		//Jumping
		if(this.isJump())
			updateJump();
		if(this.isJumping())
			jump();
		this.fall();

		//Movement
		if(this.isRight() &&
				World.placeFree(this.getX() + this.getSpeed(), this.getY()) &&
				this.getX() + this.getWidth() <= World.WIDTH*Main.GameProperties.TileSize)
		{
			this.setDirection(Direction.RIGHT);
			this.setMoving(true);
			this.setX(this.getX() + this.getSpeed());
		}
		else if (this.isLeft() &&
				World.placeFree(this.getX() - this.getSpeed(), this.getY()) &&
				this.getX() >= 0)
		{
			this.setDirection(Direction.LEFT);
			this.setMoving(true);
			this.setX(this.getX() - this.getSpeed());
		}



		//Win and Lose
		if(this.getY() >= Camera.getY()+Main.GameProperties.ScreenHeight || this.getY() > 3375)
			Main.gameOver();
		if(this.getY()<= 200)
			Main.win();

		super.eventTick();
	}
	
	@Override
	public void render(Graphics g) {
		if (this.getDirection() == Direction.RIGHT)
			g.drawImage(rightSprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		else if(this.getDirection() == Direction.LEFT)
			g.drawImage(leftSprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);

		super.render(g);
	}

	public void jump(){
		if(World.placeFree(this.getX(), this.getY()-jumpSpeed)){
			this.setY(this.getY() - jumpSpeed);
			this.setJumpFrames(this.getJumpFrames()+jumpSpeed);
			if(this.getJumpFrames() >= this.getJumpHeight()){
				this.setJumping(false);
				this.setJump(false);
				this.setJumpFrames(0);
			}
		} else {
			this.setJumping(false);
			this.setJump(false);
			this.setJumpFrames(0);
		}
	}

	public void updateJump(){
		if(!World.placeFree(this.getX(), this.getY()+1)){
			this.setJumping(true);
			Sound.playSound("jump");
		}else {
			setJump(false);
		}
	}

	public void fall(){
		if (World.placeFree(this.getX(), this.getY() + 1) && !this.isJumping()) {
			this.setMoving(true);
			if(World.placeFree(this.getX(), this.getY() + this.getGravity()))
            	this.setY(Math.min(this.getY() + this.getGravity(), (World.HEIGHT - 1) * Main.GameProperties.TileSize));
			else
				this.setY(Math.min(this.getY() + 1, (World.HEIGHT - 1) * Main.GameProperties.TileSize));
		}
	}

	public void focusCameraOnPlayer(){
		if(Camera.getY() == 0){
			Camera.setY(
					Camera.clamp(
							this.getY() - Main.GameProperties.ScreenHeight/2,
							0,
							World.HEIGHT*Main.GameProperties.TileSize - Main.GameProperties.ScreenHeight
					)
			);
		}
		if(Main.GameProperties.Clamp) {
			Camera.setX(
					Camera.clamp(
							this.getX() - Main.GameProperties.ScreenWidth/2,
							0,
							World.WIDTH*Main.GameProperties.TileSize - Main.GameProperties.ScreenWidth
					)
			);
			Camera.setY(
					Math.min(Camera.clamp(
							this.getY() - Main.GameProperties.ScreenHeight/2,
								0,
								World.HEIGHT*Main.GameProperties.TileSize - Main.GameProperties.ScreenHeight),
							Camera.getY()
			));
		} else {
			Camera.setX(this.getX() - Main.GameProperties.ScreenWidth/2);
			Camera.setY(this.getY() - Main.GameProperties.ScreenHeight/2);
		}
	}

	private int getGravity() {
		return this.gravity;
	}

	public boolean isRight() {
		return right;
	}
	public Player setRight(boolean right) {
		this.right = right;
		return this;
	}
	public boolean isLeft() {
		return left;
	}
	public Player setLeft(boolean left) {
		this.left = left;
		return this;
	}
	public boolean isUp() {
		return up;
	}
	public Player setUp(boolean up) {
		this.up = up;
		return this;
	}
	public boolean isDown() {
		return down;
	}
	public Player setDown(boolean down) {
		this.down = down;
		return this;
	}
	public int getSpeed() {
		return speed;
	}
	public Player setSpeed(int speed) {
		this.speed = speed;
		return this;
	}
	public Direction getDirection() {
		return direction;
	}
	public Player setDirection(Direction direction) {
		this.direction = direction;
		return this;
	}
	public boolean isMoving() {
		return moving;
	}
	public Player setMoving(boolean moving) {
		this.moving = moving;
		return this;
	}
	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public int getJumpHeight() {
		return jumpHeight;
	}

	public void setJumpHeight(int jumpHeight) {
		this.jumpHeight = jumpHeight;
	}

	public int getJumpFrames() {
		return jumpFrames;
	}

	public void setJumpFrames(int jumpFrames) {
		this.jumpFrames = jumpFrames;
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}
}
