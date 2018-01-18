package com.molinari.mp3.views;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.molinari.mp3.business.op.binder.Mp3File;
import com.molinari.mp3.business.player.MyBasicPlayer;
import com.molinari.utility.paint.images.UtilImage;

public class NewPlayList extends JPanel {

	private static final long serialVersionUID = 1L;
	private static MyTable table;
	private static final String[] nomiColonne = new String[] { "Titolo - Artista - Album" };
	private static JScrollPane scrollPane;
	private Map<String, Mp3File> mappaMp3 = new HashMap<>();
	protected transient MyBasicPlayer player = new MyBasicPlayer(this);
	private JLabel label;
	private JSlider slider;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {

			final JFrame inst = new JFrame();
			final NewPlayList p = new NewPlayList();
			inst.setBounds(10, 10, 275, 268);
			inst.getContentPane().add(p);
			inst.setTitle("MP3 Manager");
			inst.setLocationRelativeTo(null);
			inst.setVisible(true);
			inst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		});
	}

	private static NewPlayList singleton;

	public static NewPlayList getSingleton() {
		if (singleton == null) {
			singleton = new NewPlayList();
		}
		return singleton;
	}

	private NewPlayList() {
		setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 48, 370, 210);
		scrollPane.setBorder(null);
		add(scrollPane);
		table = new MyTable(costruisciMatriceVuota(), nomiColonne);
		table.setBorder(null);
		scrollPane.setViewportView(table);
		ImageIcon play = new ImageIcon("ImgUtil/play.jpg");
		play = UtilImage.resizeImage(30, 30, play);
		ImageIcon playrosso = new ImageIcon("ImgUtil/playrosso.jpg");
		playrosso = UtilImage.resizeImage(30, 30, playrosso);
		final JToggleButton playButton = new JToggleButton(play);
		playButton.setRolloverIcon(playrosso);

		ImageIcon stop = new ImageIcon("ImgUtil/stop.jpg");
		stop = UtilImage.resizeImage(30, 30, stop);
		ImageIcon stoprosso = new ImageIcon("ImgUtil/stoprosso.jpg");
		stoprosso = UtilImage.resizeImage(30, 30, stoprosso);
		playButton.setBounds(10, 10, 30, 30);
		playButton.setBorder(null);
		playButton.addActionListener(this::play);
		add(playButton);

		final JButton stopButton = new JButton(stop);
		stopButton.setBounds(50, 10, 30, 30);
		stopButton.setBorder(null);
		stopButton.setRolloverIcon(stoprosso);
		stopButton.addActionListener(e -> {
			if (player != null) {
				player.stop();
			}
		});
		add(stopButton);

		label = new JLabel();
		label.setBounds(90, 9, 350, 14);
		add(label);

		ImageIcon volume = new ImageIcon("ImgUtil/volume.jpg");
		volume = UtilImage.resizeImage(20, 8, volume);
		final JLabel labelImage = new JLabel();
		labelImage.setBounds(350, 9, 35, 14);
		labelImage.setIcon(volume);
		add(labelImage);

		final JCheckBox checkContinue = new JCheckBox();
		checkContinue.setBounds(327, 9, 35, 14);
		checkContinue.setSelected(false);
		checkContinue.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (checkContinue.isSelected()) {
					player.setContinua(true);
				} else {
					player.setContinua(false);
				}
			}

		});
		add(checkContinue);

		final JSlider sliderVolume = new JSlider();
		sliderVolume.setBounds(320, 21, 50, 24);
		sliderVolume.setValue(50);
		// sliderVolume.set
		sliderVolume.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent arg0) {

				final Integer valore = sliderVolume.getValue();
				final Double valoreDouble = Double.valueOf(valore.doubleValue());
				final double valoreVolume = valoreDouble / 70;
				player.setGain(valoreVolume);
			}
		});
		add(sliderVolume);
		slider = new JSlider();
		slider.setBounds(90, 21, 220, 24);
		slider.setValue(0);
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				final int valore = slider.getValue();
				player.salta(valore);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// do nothing
			}

		});
		add(slider);
	}

	public void play(ActionEvent e) {
		final int riga = table.getSelectedRow();
		Mp3File fileMp3 = mappaMp3.get(Integer.toString(riga));
		if (fileMp3 != null) {
			player.stop();
			player.setMappaMp3(mappaMp3);
			player.opener(fileMp3.getPath());
			player.play();

			player.setIndex(riga);
			label.setText(fileMp3.getNome());
		} else {
			if (player.isInPause()) {
				player.resume();
			}

		}
	}

	public void creaPlayList(final String[][] canzoni) {
		final MyTable tableLoc = new MyTable(canzoni, nomiColonne);
		getScrollPane().setViewportView(tableLoc);
		tableLoc.setBounds(10, 39, 430, 250);
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(final JScrollPane scrollPane) {
		NewPlayList.scrollPane = scrollPane;
	}

	public String[] getNomiColonne() {
		return nomiColonne;
	}

	private String[][] costruisciMatriceVuota() {
		final String[][] dati = new String[30][nomiColonne.length];
		for (int i = 0; i < 30; i++) {
			dati[i][0] = "";
		}
		return dati;
	}

	public MyTable getTable() {
		return table;
	}

	public void setTable(final MyTable table) {
		NewPlayList.table = table;
	}

	public Map<String, Mp3File> getMappaMp3() {
		return mappaMp3;
	}

	public void put(final String key, final Mp3File value) {
		mappaMp3.put(key, value);
	}

	public void setMappaMp3(final Map<String, Mp3File> mappaMp3) {
		this.mappaMp3 = mappaMp3;
	}

	public JSlider getSlider() {
		return slider;
	}

	public void setSlider(final JSlider slider) {
		this.slider = slider;
	}

	public MyBasicPlayer getPlayer() {
		return player;
	}

	public void setPlayer(final MyBasicPlayer player) {
		this.player = player;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(final JLabel label) {
		this.label = label;
	}
}
