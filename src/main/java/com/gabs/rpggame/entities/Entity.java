package com.gabs.rpggame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabs.rpggame.Main;
import com.gabs.rpggame.world.Camera;
import com.gabs.rpggame.world.CollisionMask;

public abstract class Entity {
	private String name;
	private String mapColor;
	private int x, y, width = Main.GameProperties.TileSize, height = Main.GameProperties.TileSize;
	@JsonIgnore
	private BufferedImage sprite;
	private CollisionMask collisionMask = new CollisionMask(Main.GameProperties.TileSize, Main.GameProperties.TileSize);
	
	private boolean targetable = false;
	
	public Entity() {
		collisionMask.setVisible(Main.GameProperties.ShowCollisionMask);
	}
	public Entity(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}


	public void eventTick() {
		this.collisionMask.setX(this.getX() + this.getWidth() / 2 - this.collisionMask.getWidth() / 2);
		this.collisionMask.setY(this.getY() + this.getHeight() / 2 - this.collisionMask.getHeight() / 2);
	}
	
	public void render(Graphics g) {
		if(this instanceof Prop)
			g.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		
		if(this.getCollisionMask().isVisible()) {
			g.setColor(new Color(0f, 0f, 0f, .5f));
			g.fillRect((this.getCollisionMask().getX() - Camera.getX()), 
						this.getCollisionMask().getY() - Camera.getY(), 
						this.getCollisionMask().getWidth(), this.getCollisionMask().getHeight());
		}
	}
	
	public int getX() {
		return x;
	}

	public Entity setX(int x) {
		this.collisionMask.setX(x);
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Entity setY(int y) {
		this.collisionMask.setY(y);
		this.y = y;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Entity setWidth(int width) {
		this.width = width;
		return this;
	}
 
	public int getHeight() {
		return height;
	}

	public Entity setHeight(int height) {
		this.height = height;
		return this;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public Entity setSprite(BufferedImage sprite) {
		this.sprite = sprite;
		return this;
	}
	public CollisionMask getCollisionMask() {
		return collisionMask;
	}
	public Entity setCollisionMask(CollisionMask collisionMask) {
		this.collisionMask = collisionMask;
		return this;
	}
	public boolean isTargetable() {
		return targetable;
	}
	public Entity setTargetable(boolean targetable) {
		this.targetable = targetable;
		return this;
	}
	public String getName() {
		return name;
	}
	public Entity setName(String name) {
		this.name = name;
		return this;
	}
	public String getMapColor() {
		return mapColor;
	}
	public Entity setMapColor(String mapColor) {
		this.mapColor = mapColor;
		return this;
	}
}
