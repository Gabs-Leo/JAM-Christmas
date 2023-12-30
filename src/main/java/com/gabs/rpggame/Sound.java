package com.gabs.rpggame;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class Sound {
	
	private Clip clip;
	private String soundName;
	//public static Sound bg = new Sound("sounds/Snowy.wav");
	public Sound(String path) {
		try {
			File file = new File(Thread.currentThread().getContextClassLoader().getResource(path).getFile());
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			this.clip = AudioSystem.getClip();
			this.clip.open(audioStream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.soundName = path.replace("sounds/", "").replace(".wav", "");
	}
	public static void playSound(String soundName){
		new Sound("sounds/"+soundName+".wav").play();
	}
	public void play() {
		new Thread() {
			public void start() {
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				if(soundName.equals("fall"))
					volume.setValue(-20f);
				if(soundName.equals("jump"))
					volume.setValue(-5f);
				if(soundName.equals("fail"))
					volume.setValue(-5f);
				clip.start();
			}
		}.start();
	}
	
	public void loop() {
		new Thread() {
			public void start() {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}.start();
	}
	
	public void stop() {
		clip.stop();
	}
}	
