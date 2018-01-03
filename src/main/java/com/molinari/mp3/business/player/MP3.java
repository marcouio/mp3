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
import java.util.logging.Level;

import com.molinari.utility.controller.ControlloreBase;

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
		try(final FileInputStream fis = new FileInputStream(filename);
			final BufferedInputStream bis = new BufferedInputStream(fis);){
			player = new AdvancedPlayer(bis);
		} catch (final Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE,"Problem playing file " + filename, e);
		}

		// run in new thread to play in background
		new Thread() {
			@Override
			public void run() {
				try {
					player.play();
				} catch (final Exception e) {
					ControlloreBase.getLog().log(Level.SEVERE,"Problem playing file " + filename, e);
				}
			}
		}.start();

	}

	
	@Override
	public void converterUpdate(final int updateID, final int param1, final int param2) {
		// not implemented

	}

	@Override
	public void parsedFrame(final int frameNo, final Header header) {
		// not implemented

	}

	@Override
	public void readFrame(final int frameNo, final Header header) {
		// not implemented

	}

	@Override
	public void decodedFrame(final int frameNo, final Header header, final Obuffer o) {
		// not implemented

	}

	@Override
	public boolean converterException(final Throwable t) {
		// not implemented
		return false;
	}

}
