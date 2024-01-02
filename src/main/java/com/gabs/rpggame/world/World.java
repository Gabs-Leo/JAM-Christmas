package com.gabs.rpggame.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.gabs.rpggame.Main;
import com.gabs.rpggame.entities.BallPlatform;
import com.gabs.rpggame.entities.Prop;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public World(String path) {
		try {
			BufferedImage mapSprite = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
			WIDTH = mapSprite.getWidth();
			HEIGHT = mapSprite.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[mapSprite.getWidth() * mapSprite.getHeight()];
			
			mapSprite.getRGB(0, 0, 
							WIDTH, 
							HEIGHT,
							pixels,
							0,
							WIDTH);
			
			for (int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					int currentTile = pixels[xx + (yy * WIDTH)];
					
					Tile tile = new Tile();
					tile.setX(xx * Main.GameProperties.TileSize)
						.setY(yy * Main.GameProperties.TileSize)
						.setType(CollisionType.NO_COLLISION)
						.setSprite(Main.spritesheet.getSprite(0, 150, Main.GameProperties.TileSize, Main.GameProperties.TileSize));

					//Gift
					if(currentTile == 0xFF000300){
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(0, 225, 2500, 3600))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.NO_COLLISION);
						Main.entities.add(prop);
					}
					else if (currentTile == 0xFFFC0000) {
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(0, 0, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.BLOCK);
						Main.entities.add(prop);
					}else if (currentTile == 0xFFFB0000) {
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(75, 0, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.BLOCK);
						Main.entities.add(prop);
					}else if (currentTile == 0xFFFF0000) {
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(0, 75, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.BLOCK);
						Main.entities.add(prop);
					} else if (currentTile == 0xFFFE0000) {
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(150, 150, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setSprite(Main.spritesheet.getSprite(75, 75, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.BLOCK);
						Main.entities.add(prop);
					}

					//Ball String
					else if(currentTile == 0xFF00FE00){
						Prop prop = new Prop();
						prop
								.setSprite(Main.spritesheet.getSprite(150, 0, Main.GameProperties.TileSize, Main.GameProperties.TileSize))
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);

						tile.setType(CollisionType.NO_COLLISION);
						Main.entities.add(prop);
					}

					//Balls
					else if(currentTile == 0xFF00FF00){
						int random = Main.generateRandomInt(1,3);
						System.out.println(random);
						BallPlatform prop = new BallPlatform(
							random == 1 ? "RED":
							random == 2 ? "BLUE":
							random == 3 ? "YELLOW": "RED"
						);
						prop
								.setX(xx * Main.GameProperties.TileSize)
								.setY(yy * Main.GameProperties.TileSize);
						tile.setType(CollisionType.BLOCK);
						prop.setTile(xx + (yy * WIDTH));
						Main.entities.add(prop);
					}

					//Player Position
					else if(currentTile == 0xFF0000FF) {
						Main.player.setX(xx*Main.GameProperties.TileSize);
						Main.player.setY(yy*Main.GameProperties.TileSize);
					}

					tiles[xx + (yy * WIDTH)] = tile;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xStart = Camera.getX() / Main.GameProperties.TileSize;
		int yStart = Camera.getY() / Main.GameProperties.TileSize;
		
		int xFinal = xStart +
				Main.GameProperties.ScreenWidth*Main.GameProperties.ScreenScale / Main.GameProperties.TileSize;
		int yFinal = yStart +
				Main.GameProperties.ScreenHeight*Main.GameProperties.ScreenScale / Main.GameProperties.TileSize;
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				
				if(xx < 0 || yy < 0 ||
				   xx >= WIDTH || yy >= HEIGHT) continue;
				tiles[xx + (yy * WIDTH)].render(g);
			}
		}
	}
	public static boolean placeFree(int nextX, int nextY) {
		int x = Main.player.getWidth();
		int y = Main.player.getHeight();
		
		int x1 = nextX / x;
		int y1 = nextY / y;
		
		int x2 = (nextX + x - 1) / x;
		int y2 = nextY / y;
		
		int x3 = nextX / x;
		int y3 = (nextY + y - 1) / y;
		
		int x4 = (nextX + x - 1) / x;
		int y4 = (nextY + y - 1) / y;
		
		try {
			return 	tiles[x1 + y1*World.WIDTH].getType() == CollisionType.NO_COLLISION &&
					tiles[x2 + y2*World.WIDTH].getType() == CollisionType.NO_COLLISION &&
					tiles[x3 + y3*World.WIDTH].getType() == CollisionType.NO_COLLISION &&
					tiles[x4 + y4*World.WIDTH].getType() == CollisionType.NO_COLLISION;
		} catch(Exception e) {
			return false;
		}
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
	public World setTiles(Tile[] tiles) {
		World.tiles = tiles;
		return this;
	}
}
