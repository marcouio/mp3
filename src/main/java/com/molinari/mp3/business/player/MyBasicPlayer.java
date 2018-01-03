package com.molinari.mp3.business.player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.operation.binder.Raccoglitore.Mp3File;
import com.molinari.mp3.views.NewPlayList;
import com.molinari.utility.controller.ControlloreBase;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

/**
 * * This class implements a simple player based on BasicPlayer. * BasicPlayer
 * is a threaded class providing most features * of a music player. BasicPlayer
 * works with underlying JavaSound * SPIs to support multiple audio formats.
 * Basically JavaSound supports * WAV, AU, AIFF audio formats. Add MP3 SPI and
 * Vorbis * SPI in your CLASSPATH to play MP3 and Ogg Vorbis file.
 */
public class MyBasicPlayer implements BasicPlayerListener {
	private BasicPlayer control = new BasicPlayer();
	private Map<String, Mp3File> mappaMp3 = new HashMap<>();
	private Map<String, Object> properties;
	private int lunghezzaFrameMp3;
	private Integer lunghezzaByteMp3;
	protected boolean continua = false;
	private int index;
	private static int size = 1;
	private NewPlayList playList;


	/** * Contructor. */
	public MyBasicPlayer(final NewPlayList playList) {
		this.playList = playList;
	}
	
	public boolean isInPause(){
		return control.getStatus() == BasicPlayer.PAUSED;
	}

	/**
	 * @return lo stato del player
	 */
	public int getStato() {
		int stato = 0;
		if (control != null) {
			stato = control.getStatus();
		}
		return stato;
	}

	/**
	 * manda il player in pausa
	 */
	public void stop() {
		try {
			if (control != null) {
				control.pause();
			}
		} catch (final BasicPlayerException e) {
			ControlloreBase.getLog().log(Level.SEVERE,e.getMessage(), e);
		}
	}

	/**
	 * setta il volume (da 0.0 a 1.0)
	 * 
	 * @param volume
	 */
	public void setGain(final double volume) {
		try {
			if (control != null) {
				control.setGain(volume);
			}
		} catch (final BasicPlayerException e) {
			ControlloreBase.getLog().log(Level.SEVERE,e.getMessage(), e);
		}
	}

	/**
	 * manda il player alla posizione indicata dallo slider passato come
	 * parametro
	 * 
	 * @param slider
	 */
	public void salta(final int slider) {
		try {
			if (control != null) {
				control.seek(setPosizioneCanzone(slider));
				control.resume();
			}
		} catch (final BasicPlayerException e) {
			ControlloreBase.getLog().log(Level.SEVERE,e.getMessage(), e);
		}
	}

	/**
	 * @return il basicPlayer
	 */
	public BasicPlayer getControl() {
		return control;
	}

	/**
	 * Apre un file preparandolo ad essere suonato. chiamare il metodo prima di
	 * play()
	 * 
	 * @param filename
	 */
	public void opener(final String filename) {
		setController(control);
		// Register BasicPlayerTest to BasicPlayerListener events.
		// It means that this object will be notified on BasicPlayer
		// events such as : opened(...), progress(...), stateUpdated(...)
		control.addBasicPlayerListener(this);

		try { // Open file, or URL or Stream (shoutcast, icecast) to play.
			control.open(new File(filename));

			// control.open(new URL("http://yourshoutcastserver.com:8000"))
			// Start playback in a thread. control.play()
			// If you want to pause/resume/pause the played file then
			// write a Swing player and just call control.pause(),
			// control.resume() or control.stop().
			// Use control.seek(bytesToSkip) to seek file
			// (i.e. fast forward and rewind). seek feature will
			// work only if underlying JavaSound SPI implements
			// skip(...). True for MP3SPI and SUN SPI's
			// (WAVE, AU, AIFF). // Set Volume (0 to 1.0).
			// control.setGain(0.85)
			// Set Pan (-1.0 to 1.0).
			control.setPan(1.0);
			// control.setGain(0.5)
		} catch (final BasicPlayerException e) {
			ControlloreBase.getLog().log(Level.SEVERE,e.getMessage(), e);
		}
	}

	/**
	 * Open callback, stream is ready to play.
	 * 
	 * properties map includes audio format dependant features such as bitrate,
	 * duration, frequency, channels, number of frames, vbr flag, ...
	 * 
	 * @param stream
	 *            could be File, URL or InputStream
	 * @param properties
	 *            audio stream properties.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void opened(final Object stream, final Map properties) {
		// Pay attention to properties. It's useful to get duration,
		// bitrate, channels, even tag such as ID3v2.
		lunghezzaByteMp3 = (Integer) properties.get("audio.length.bytes");
		lunghezzaFrameMp3 = (Integer) properties.get("audio.length.frames");
		display("opened : " + properties.toString());

	}

	/**
	 * * Progress callback while playing.
	 * 
	 * This method is called severals time per seconds while playing. properties
	 * map includes audio format features such as instant bitrate, microseconds
	 * position, current frame number, ...
	 * 
	 * @param bytesread
	 *            from encoded stream.
	 * @param microseconds
	 *            elapsed (<b>reseted after a seek !</b>).
	 * @param pcmdata
	 *            PCM samples.
	 * @param properties
	 *            audio stream parameters.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void progress(final int bytesread, final long microseconds, final byte[] pcmdata, final Map properties) {
		this.properties = properties;
		// Pay attention to properties. It depends on underlying JavaSound SPI
		// MP3SPI provides mp3.equalizer.
		// display("progress : " + properties.toString())
		// System.out.println(properties.get("mp3.frame"))
		// System.out.println(properties.get("mp3.frame.size.bytes"))
		if (properties.get("mp3.frame") != null && lunghezzaFrameMp3 != 0) {
			NewPlayList.getSingleton().getSlider().setValue(setLivelloSlider());
		}
	}

	public void resume() {
		try {
			control.resume();
		} catch (final BasicPlayerException e) {
			ControlloreBase.getLog().log(Level.SEVERE,e.getMessage(), e);
		}
	}

	/**
	 * trasforma la posizione della canzone in posizione dello slider della
	 * durata
	 * 
	 * @return
	 */
	public int setLivelloSlider() {
		final Long numeroFrame = (Long) properties.get("mp3.frame");
		// lunghezzaMp3 : 100 = numeroFrame : x
		// System.out.println("valore slider: " + numeroFrame.intValue() * 100 /
		// lunghezzaMp3)
		return numeroFrame.intValue() * 100 / lunghezzaFrameMp3;
	}

	public int setPosizioneCanzone(final int posizioneSlider) {
		// 100: lunghezzaMp3 = slider : x
		properties.get("position.byte");
		return lunghezzaByteMp3 * posizioneSlider / 100;

	}

	/**
	 * Notification callback for basicplayer events such as opened, eom ...
	 * 
	 * @param event
	 */
	@Override
	public void stateUpdated(final BasicPlayerEvent event) {
		// Notification of BasicPlayer states (opened, playing, end of media,
		// ...)
		display("stateUpdated : " + event.toString());
		if (continua && event.toString().equals("STOPPED:-1") && ((Long) properties.get("mp3.position.byte")).intValue() >= (lunghezzaByteMp3 - 150000)) {
			if (index < size) {
				index++;
			} else {
				index = 0;
			}
			final Mp3File mp3 = mappaMp3.get(Integer.toString(index));
			opener(mp3.getPath());
			playList.getLabel().setText(mp3.getNome());
			play();
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public static int getSize() {
		return size;
	}

	public static void setSize(final int size) {
		MyBasicPlayer.size = size;
	}

	/**
	 * A handle to the BasicPlayer, plugins may control the player through the
	 * controller (play, stop, ...)
	 * 
	 * @param controller
	 *            : a handle to the player
	 */
	@Override
	public void setController(final BasicController controller) {
		display("setController : " + controller);
	}

	public void display(final String msg) {
		Controllore.getLog().info(msg);
	}

	public void play() {
		try {
			control.play();
		} catch (final BasicPlayerException e) {
			Controllore.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(final Map<String, Object> properties) {
		this.properties = properties;
	}

	public boolean isContinua() {
		return continua;
	}

	public void setContinua(final boolean continua) {
		this.continua = continua;
	}

	public Map<String, Mp3File> getMappaMp3() {
		return mappaMp3;
	}

	public void setMappaMp3(final Map<String, Mp3File> mappaMp3) {
		this.mappaMp3 = mappaMp3;
	}

	public NewPlayList getPlayList() {
		return playList;
	}

	public void setPlayList(final NewPlayList playList) {
		this.playList = playList;
	}
}