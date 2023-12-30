package com.gabs.rpggame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.gabs.rpggame.world.Camera;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gabs.rpggame.entities.DamageShot;
import com.gabs.rpggame.entities.Enemy;
import com.gabs.rpggame.entities.Entity;
import com.gabs.rpggame.entities.Player;
import com.gabs.rpggame.graphics.Dialog;
import com.gabs.rpggame.graphics.GameOverScreen;
import com.gabs.rpggame.graphics.HUD;
import com.gabs.rpggame.graphics.MainMenu;
import com.gabs.rpggame.graphics.PauseScreen;
import com.gabs.rpggame.graphics.Spritesheet;
import com.gabs.rpggame.graphics.Transition;
import com.gabs.rpggame.world.Direction;
import com.gabs.rpggame.world.EventTrigger;
import com.gabs.rpggame.world.World;

public class Main extends Canvas implements Runnable, KeyListener {
	/*
	 * Made with <3 By Gabs
	 */
	public static JFrame frame;
	public static GameProperties GameProperties;
	public static Assets assets;
	private Thread thread;
	private boolean running = true;
	
	public static Player player;
	public static List<Entity> entities;
	public static List<DamageShot> damageShots;
	public static List<Enemy> enemies;
	public static Spritesheet spritesheet;
	public static World world;
	public static Random random;
	public static HUD ui;
	public GameOverScreen gameOver = new GameOverScreen();
	public PauseScreen pauseScreen = new PauseScreen();
	public Transition transition = new Transition();
	public MainMenu mainMenu = new MainMenu();
	private BufferedImage image;
	public static GameState state;
	
	public static void main(String[] args) {
		try {
			File file = new File(
					Objects.requireNonNull(
							Thread.currentThread().getContextClassLoader().getResource("game-properties.yml")
					).getFile()
			);
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			
			spritesheet = new Spritesheet("/sprites/christmas_spritesheet.png");
			GameProperties = mapper.readValue(file, GameProperties.class);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		Main main = new Main();
		main.start();
	};
	
	public Main() {
		random = new Random();
		startFrame();
		image = new BufferedImage(GameProperties.ScreenWidth, GameProperties.ScreenHeight, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		damageShots = new ArrayList<>();
		
		ui = new HUD();
		player = new Player();
		addKeyListener(this);
		
		//world = new World("maps/christmas_map.png");
		entities.add(player);
		
		state = GameState.MAIN_MENU;
		
		//Sound.bg.loop();
	};
	
	public void eventTick() {
		switch(state) {
		case RUNNING:{
				for(int i = 0; i < entities.size(); i++)
					entities.get(i).eventTick();
				for(int i = 0; i < enemies.size(); i++)
					enemies.get(i).eventTick();
				for(int i = 0; i < damageShots.size(); i++)
					damageShots.get(i).eventTick();
				ui.eventTick();
			}
			break;
		case PAUSED:
			break;
		case GAME_OVER:
			break;
		case MAIN_MENU:
			break;
		default:
			break;
		}
	}
	
	public void render() {
		
		var bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, GameProperties.ScreenWidth, GameProperties.ScreenHeight);
		
		if(world != null){
			try{
				world.render(g);
			} catch (Exception e){
				System.out.println("err");
			}
		}

		for(int i = 0; i < entities.size(); i++) 
			entities.get(i).render(g);
		for(int i = 0; i < enemies.size(); i++) 
			enemies.get(i).render(g);
		for(int i = 0; i < damageShots.size(); i++) 
			damageShots.get(i).render(g);
		//ui.render(g);
		//transition.render(g);
		//dialogs.get(0).render(g);
		switch(state) {
			case RUNNING:
				ui.render(g);
				break;
			case PAUSED:
				pauseScreen.render(g);
				break;
			case GAME_OVER:
				gameOver.setTimePlayed(ui.getTimePlayed());
				gameOver.render(g);
				break;
			case MAIN_MENU:
				mainMenu.render(g);
				break;
			default:
				break;
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, GameProperties.ScreenWidth*GameProperties.ScreenScale, GameProperties.ScreenHeight*GameProperties.ScreenScale, null);
		bs.show();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		running = true;
		thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 144;
		double interval = 1000000000 / amountOfTicks;
		//double nanoSeconds = 1000000000 / amountOfTicks;
		//double delta = 0;
		double lag = 0;

		@SuppressWarnings("unused")
		int frames = 0;

		double timer = System.currentTimeMillis();
		requestFocus();

		while(running) {
			Long currentTime = System.nanoTime();
			double delta = currentTime - lastTime;
			lag += delta;

			while (lag >= interval) {
				eventTick();
				lag -= interval;
				frames++;
			}

			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println(frames);
				frames = 0;
				timer += 1000;
			}

			render();
			lastTime = currentTime;
		}
		stop();
	};

	public void startFrame() {		
		this.setPreferredSize(new Dimension(GameProperties.ScreenWidth*GameProperties.ScreenScale, GameProperties.ScreenHeight*GameProperties.ScreenScale));
		frame = new JFrame("Christmas Platform");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
	@Override
	public void keyPressed(KeyEvent e) {
		//Pause Menu
		if(state == GameState.PAUSED) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
				pauseScreen.changeOption(Direction.DOWN);
			else if(e.getKeyCode() == KeyEvent.VK_UP)
				pauseScreen.changeOption(Direction.UP);
			else if(e.getKeyCode() == KeyEvent.VK_Z)
				pauseScreen.trigger();
		}

		//Main Menu
		else if(state == GameState.MAIN_MENU) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN)
				mainMenu.changeOption(Direction.DOWN);
			else if(e.getKeyCode() == KeyEvent.VK_UP)
				mainMenu.changeOption(Direction.UP);
			if(e.getKeyCode() == KeyEvent.VK_Z)
				mainMenu.trigger();
		}

		//Player Movement
		else if(state == GameState.RUNNING) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.setRight(true);
				//player.setJump(true);
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				player.setLeft(true);
				//player.setJump(true);
			}
			if(e.getKeyCode() == KeyEvent.VK_UP){
				player.setJump(true);
			}
		}

		else if(state == GameState.GAME_OVER){
			if(e.getKeyCode() == KeyEvent.VK_Z){
				Main.state = GameState.RUNNING;
				startGame();
			}
		}

		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(state == GameState.PAUSED) {
				transition.endTransition();
				//Sound.bg.play();
				state = GameState.RUNNING;
			} else if (state == GameState.RUNNING){
				//Sound.bg.stop();
				transition.startTransition();
				state = GameState.PAUSED;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setRight(false);
			//player.getRightAnimation().setIndex(player.getRightAnimation().getStartIndex());

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.setLeft(false);
			//player.getLeftAnimation().setIndex(player.getLeftAnimation().getStartIndex());
		}
		/*
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			player.setUp(false);
			//player.getUpAnimation().setIndex(player.getUpAnimation().getStartIndex());

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setDown(false);
			//player.getDownAnimation().setIndex(player.getDownAnimation().getStartIndex());
		}*/

	}
	public static void startGame(){
		Camera.setY(0);
		ui.setTimePlayed(0);
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		damageShots = new ArrayList<>();


		Main.world = new World("/maps/christmas_map.png");
		entities.add(player);
		Main.player.focusCameraOnPlayer();

		Main.state = GameState.RUNNING;

	}
	public static void gameOver(){
		Main.state = GameState.GAME_OVER;
		Sound.playSound("fail");
	}
	public static int generateRandomInt(int min, int max){
		return (int) ((Math.random() * (max+1 - min)) + min);
	}
	public static void closeGame() {
		Main.frame.dispatchEvent(new WindowEvent(Main.frame, WindowEvent.WINDOW_CLOSING));
	}
}
