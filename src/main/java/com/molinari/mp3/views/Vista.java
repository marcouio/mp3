package com.molinari.mp3.views;

import java.awt.Container;

import javax.swing.JPanel;

import com.molinari.utility.graphic.component.container.PannelloBase;

public class Vista extends PannelloBase {

	private static final long serialVersionUID = -1455195023357489172L;
	private NewPlayList playList;
	private Pannello pannello;
	private JPanel panForGestore;
	private JPanel panForPlayer;
	
	/**
	 * Create the frame.
	 */
	public Vista(Container cont) {
		super(cont);
		initMenu();

		initGestore();
		initPlayer();

	}

	private void initPlayer() {
		panForPlayer = new JPanel();
		panForPlayer.setLayout(null);
		this.add(panForPlayer);
		panForPlayer.setBounds(283, 31, 400, 317);
		playList = NewPlayList.getSingleton();
		playList.setBounds(0, 0, 450, 257);
		panForPlayer.add(playList);
	}

	private void initGestore() {
		panForGestore = new JPanel();
		panForGestore.setLayout(null);
		panForGestore.setBounds(10, 31, 278, 300);
		this.add(panForGestore);
		pannello = new Pannello(this);
		pannello.setBounds(0, 0, 258, 257);
		panForGestore.add(pannello);
	}

	private void initMenu() {
		this.setLayout(null);
		final MyMenu menu = new MyMenu();
		menu.setBounds(0, 0, 1000, 20);
		this.add(menu);

	}

	public NewPlayList getPlayList() {
		return playList;
	}

	public void setPlayList(final NewPlayList playList) {
		this.playList = playList;
	}

	public Pannello getPannello() {
		return pannello;
	}

	public void setPannello(final Pannello pannello) {
		this.pannello = pannello;
	}

	public JPanel getPanForGestore() {
		return panForGestore;
	}

	public void setPanForGestore(final JPanel panForGestore) {
		this.panForGestore = panForGestore;
	}

	public JPanel getPanForPlayer() {
		return panForPlayer;
	}

	public void setPanForPlayer(final JPanel panForPlayer) {
		this.panForPlayer = panForPlayer;
	}
}
