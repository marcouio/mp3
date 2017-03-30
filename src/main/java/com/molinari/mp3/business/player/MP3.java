package com.molinari.mp3.business.player;

/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.converter.Converter.ProgressListener;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class MP3 implements ProgressListener {
	private String filename;
	private AdvancedPlayer player;

	// constructor that takes the name of an MP3 file
	public MP3(final String filename) {
		this.filename = filename;
	}

	public void close() {
		if (player != null) {
			player.close();
		}
	}

	// play the MP3 file to the sound card
	public void play() {
		try {
			final FileInputStream fis = new FileInputStream(filename);
			final BufferedInputStream bis = new BufferedInputStream(fis);
			player = new AdvancedPlayer(bis);
		} catch (final Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

		// run in new thread to play in background
		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
				} catch (final Exception e) {
					System.out.println(e);
				}
			}
		}.start();

	}

	// test client
	public static void main(final String[] args) {
		final String filename = "/home/kiwi/Musica/AB - AB III/Alter Bridge - All Hope Is Gone.mp3";
		MP3 mp3 = new MP3(filename);
		mp3.play();

		// do whatever computation you like, while music plays
		final int N = 4000;
		double sum = 0.0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum += Math.sin(i + j);
			}
		}
		System.out.println(sum);

		// when the computation is done, stop playing it
		mp3.close();

		// play from the beginning
		mp3 = new MP3(filename);
		mp3.play();

	}

	@Override
	public void converterUpdate(final int updateID, final int param1, final int param2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void parsedFrame(final int frameNo, final Header header) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFrame(final int frameNo, final Header header) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decodedFrame(final int frameNo, final Header header, final Obuffer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean converterException(final Throwable t) {
		// TODO Auto-generated method stub
		return false;
	}

}
