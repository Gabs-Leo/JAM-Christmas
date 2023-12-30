package com.gabs.rpggame.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowEvent;

import com.gabs.rpggame.GameState;
import com.gabs.rpggame.Main;
import com.gabs.rpggame.world.Direction;
import com.gabs.rpggame.world.World;

public class MainMenu implements UI {
	
	private int option = 0;
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Main.GameProperties.ScreenWidth, Main.GameProperties.ScreenHeight);
		g.drawImage(Main.spritesheet.getSprite(2500, 225, Main.GameProperties.ScreenWidth, Main.GameProperties.ScreenHeight), 0,0, null);
		g.setColor(new Color(100, 100, 100));
		g.setFont(new Font("arial", Font.PLAIN, 40));
		g.drawString("Start", Main.GameProperties.ScreenWidth-100, 120);
		g.drawString("Exit", Main.GameProperties.ScreenWidth-90, 160);
		
		switch(option) {
			case 0:
				g.setColor(Color.WHITE);
				g.drawString("Start", Main.GameProperties.ScreenWidth-100, 120);
				break;
			case 1:
				g.setColor(Color.WHITE);
				g.drawString("Exit", Main.GameProperties.ScreenWidth-90, 160);
				break;
		}
	}
	
	public void changeOption( Direction direction ) {
		switch(direction) {
		case DOWN:
			option = option == 1 ? 0 : 1;
			break;
		case UP:
			option = option == 0 ? 1 : 0;
			break;
		default:
			break;
		}
	}

	public int getOption() {
		return option;
	}

	public MainMenu setOption(int option) {
		this.option = option;
		return this;
	}

	@Override
	public void trigger() {
		switch(option) {
			case 0:
				Main.startGame();
				break;
			case 1:
				Main.closeGame();
				break;
		}
	}
}
