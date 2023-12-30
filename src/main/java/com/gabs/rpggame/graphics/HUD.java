package com.gabs.rpggame.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gabs.rpggame.Main;

public class HUD {

	private int frames = 0;
	private int timePlayed = 0;

	public void render(Graphics g) {
		/*
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, Main.GameProperties.ScreenWidth, 40);
		
		g.setColor(Color.red);
		g.fillRect(10, 10, Main.player.getMaxLife(), 20);
		
		g.setColor(Color.green);
		g.fillRect(10, 10, Main.player.getLife(), 20);
		
		g.setFont(new Font("Javanese Text", Font.PLAIN, 17));
		g.drawString(""+timePlayed, Main.GameProperties.ScreenWidth - 30, 25);*/
		g.drawImage(Main.spritesheet.getSprite(375, 0), 0,0, null);
		g.drawImage(Main.spritesheet.getSprite(375+75, 0), 75,0, null);
		g.setColor(Color.white);
		g.setFont(new Font("Javanese Text", Font.PLAIN, 30));
		g.drawString(timePlayed+"s", 62, 50);
	}

	public void eventTick(){
		if(frames >= 144){
			timePlayed++;
			frames = 0;
		}
		frames++;
	}

	public int getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(int timePlayed) {
		this.timePlayed = timePlayed;
	}
}
