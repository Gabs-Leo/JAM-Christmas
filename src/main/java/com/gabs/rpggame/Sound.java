package com.gabs.rpggame;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.sound.sampled.*;
import javax.swing.*;

public class Sound {
	
	private Clip clip;
	private String soundId;
	public static Sound bg = new Sound("sounds/bg.wav");
	public Sound(String path) {
		try {
			File file = new File(
					Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(path)).getFile()
			);

			//File file = new File(path);

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			this.clip = AudioSystem.getClip();
			this.clip.open(audioStream);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		this.soundId = path.replace("sounds/", "").replace(".wav", "");
	}
	public static void playSound(String soundName){
		new Sound("sounds/"+soundName+".wav").play();
	}
	public void play() {
		new Thread() {
			public void start() {
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				if(soundId.equals("fall"))
					volume.setValue(-20f);
				if(soundId.equals("jump"))
					volume.setValue(-5f);
				if(soundId.equals("fail"))
					volume.setValue(-5f);
				clip.start();
			}
		}.start();
	}
	
	public void loop() {
		new Thread() {
			public void start() {
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(-10f);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}.start();
	}
	
	public void stop() {
		clip.stop();
	}
}	
