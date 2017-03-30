package com.molinari.mp3.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

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

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.player.MyBasicPlayer;

public class NewPlayList extends JPanel {

	private static final long  serialVersionUID = 1L;
	private static MyTable     table;
	final String[]             nomiColonne      = new String[] { "Titolo - Artista - Album" };
	private static JScrollPane scrollPane;
	HashMap<String, Mp3>       mappaMp3         = new HashMap<String, Mp3>();
	protected MyBasicPlayer    player           = new MyBasicPlayer(this);
	private JLabel             label;
	private JSlider            slider;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				final JFrame inst = new JFrame();
				final NewPlayList p = new NewPlayList();
				inst.setBounds(10, 10, 275, 268);
				inst.getContentPane().add(p);
				inst.setTitle("MP3 Manager");
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

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
		play = Mp3ReaderUtil.resizeImage(30, 30, play);
		ImageIcon playrosso = new ImageIcon("ImgUtil/playrosso.jpg");
		playrosso = Mp3ReaderUtil.resizeImage(30, 30, playrosso);
		final JToggleButton playButton = new JToggleButton(play);
		playButton.setRolloverIcon(playrosso);

		ImageIcon stop = new ImageIcon("ImgUtil/stop.jpg");
		stop = Mp3ReaderUtil.resizeImage(30, 30, stop);
		ImageIcon stoprosso = new ImageIcon("ImgUtil/stoprosso.jpg");
		stoprosso = Mp3ReaderUtil.resizeImage(30, 30, stoprosso);
		playButton.setBounds(10, 10, 30, 30);
		playButton.setBorder(null);
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final int riga = table.getSelectedRow();
				Mp3 fileMp3 = mappaMp3.get(Integer.toString(riga));
				if (fileMp3 != null) {
					if (player != null) {
						player.stop();
					}
					player.setMappaMp3(mappaMp3);
					MyBasicPlayer.setIndex(riga);
					label.setText(fileMp3.getNome());
					player.opener(fileMp3.getMp3file().getAbsolutePath());
					player.play();
				}
			}
		});
		add(playButton);

		final JButton stopButton = new JButton(stop);
		stopButton.setBounds(50, 10, 30, 30);
		stopButton.setBorder(null);
		stopButton.setRolloverIcon(stoprosso);
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (player != null) {
					player.stop();
				}
			}
		});
		add(stopButton);

		label = new JLabel();
		label.setBounds(90, 9, 350, 14);
		add(label);

		ImageIcon volume = new ImageIcon("ImgUtil/volume.jpg");
		volume = Mp3ReaderUtil.resizeImage(20, 8, volume);
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
				final Double valoreDouble = new Double(valore.doubleValue());
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
				        // player.stop();
			        }

		});
		add(slider);
	}

	public void creaPlayList(final String[][] canzoni) {
		final MyTable table = new MyTable(canzoni, nomiColonne);
		getScrollPane().setViewportView(table);
		table.setBounds(10, 39, 430, 250);
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

	public HashMap<String, Mp3> getMappaMp3() {
		return mappaMp3;
	}

	public void put(final String key, final Mp3 value) {
		mappaMp3.put(key, value);
	}

	public void setMappaMp3(final HashMap<String, Mp3> mappaMp3) {
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
