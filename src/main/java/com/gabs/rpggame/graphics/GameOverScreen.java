package com.gabs.rpggame.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gabs.rpggame.GameState;
import com.gabs.rpggame.Main;

public class GameOverScreen implements UI {
	private int frame;
	private int duration = 40;
	private boolean showGameOverMessage;
	private int timePlayed;

	private boolean isGameWon = false;

	@Override
	public void trigger() {

	}

	public void render(Graphics g) {
		if(Main.state == GameState.GAME_OVER) {
			g.setColor(new Color(0, 0, 0, .6f));
			g.fillRect(0, 0, Main.GameProperties.ScreenWidth, Main.GameProperties.ScreenHeight);
			
			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Javanese Text", Font.PLAIN, 48));
			if(isGameWon){
				g.drawString("Congratulations!", Main.GameProperties.ScreenWidth/2 - 170, Main.GameProperties.ScreenHeight/2 - 40);
				g.setColor(new Color(255, 255, 255));
				g.setFont(new Font("Javanese Text", Font.PLAIN, 20));
				g.drawString("You saved the christmas by placing the star at the top of the tree!", Main.GameProperties.ScreenWidth/2 - 280, Main.GameProperties.ScreenHeight/2);
			}else{
				g.drawString("Game Over", Main.GameProperties.ScreenWidth/2 - 120, Main.GameProperties.ScreenHeight/2 - 40);

			}

			g.setColor(new Color(255, 255, 255));
			g.setFont(new Font("Javanese Text", Font.PLAIN, 24));
			g.drawString("Time Played: "+this.getTimePlayed()+" seconds", Main.GameProperties.ScreenWidth/2 - 120,Main.GameProperties.ScreenHeight/2+20);


			frame++;
			if(frame >= duration) {
				frame = 0;
				if(showGameOverMessage)
					showGameOverMessage = false;
				else
					showGameOverMessage = true;
			}
			if(showGameOverMessage) {
				g.drawString("> Press [ Z ] to Restart <", Main.GameProperties.ScreenWidth/2 - 125, Main.GameProperties.ScreenHeight/2 + 60);
			}

		}
	}

	public int getTimePlayed() {
		return timePlayed;
	}

	public void setTimePlayed(int timePlayed) {
		this.timePlayed = timePlayed;
	}

	public boolean isGameWon() {
		return isGameWon;
	}

	public void setGameWon(boolean gameWon) {
		isGameWon = gameWon;
	}
}
