package com.molinari.mp3.views;

import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.binder.Binder;
import com.molinari.mp3.business.op.binder.Mp3File;
import com.molinari.mp3.business.player.MyBasicPlayer;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.func.ParallelCrosserFile;
import com.molinari.utility.io.func.CrosserFiles;

public class MyMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;

	public MyMenu() {
		init();
	}

	private void init() {
		this.setBounds(0, 0, 1000, 20);

		// crea un menu
		final JMenu file = new JMenu("File");
		this.add(file);

		final JMenuItem daFile = new JMenuItem("Apri File");
		daFile.addActionListener(e -> apriFile());
		file.add(daFile);

		final JMenuItem daCartella = new JMenuItem("Apri Cartella");
		daCartella.addActionListener(e -> apriCartella());
		file.add(daCartella);

		final JMenuItem chiudi = new JMenuItem("Chiudi");
		chiudi.addActionListener(e -> System.exit(0));
		file.add(chiudi);

		final JMenu finestre = new JMenu("Finestre");
		add(finestre);

		final JCheckBoxMenuItem gestore = new JCheckBoxMenuItem("Gestore");
		gestore.setState(true);
		gestore.addActionListener(arg0 -> gestore());
		finestre.add(gestore);

		final JCheckBoxMenuItem player = new JCheckBoxMenuItem("Player");
		player.setState(true);
		player.addActionListener(arg0 -> player());
		finestre.add(player);

		final JMenu help = new JMenu("Help");
		add(help);

		final JMenuItem info = new JMenuItem("Info");
		help.add(info);

		final JMenuItem manuale = new JMenuItem("Manuale");
		help.add(manuale);

	}

	public void player() {
		final Vista vista = Controllore.getSingleton().getVista();
		final JPanel pannello = Controllore.getSingleton().getVista().getPanForPlayer();
		final JPanel pannelloGestore = Controllore.getSingleton().getVista().getPanForGestore();
		if (pannello.isVisible()) {
			pannello.setVisible(false);
			final Double larghezza1 = Double.valueOf(vista.getPanForGestore().getSize().getWidth());
			final Double altezza1 = Double.valueOf(vista.getPanForGestore().getSize().getHeight());
			vista.setSize(larghezza1.intValue(), altezza1.intValue());
			vista.getPanForGestore().setLocation(new Point(0, 20));
			vista.invalidate();
			vista.repaint();
		} else {
			pannello.setVisible(true);
			if (pannelloGestore.isVisible()) {
				vista.setSize(vista.getPreferredSize());
				pannello.setLocation(new Point(283, 31));
				pannelloGestore.setLocation(new Point(10, 31));
			} else {
				final Double larghezza2 = Double.valueOf(pannello.getSize().getWidth());
				final Double altezza2 = Double.valueOf(pannello.getSize().getHeight());
				vista.setSize(larghezza2.intValue(), altezza2.intValue());
				vista.getPanForGestore().setLocation(new Point(0, 20));
				vista.invalidate();
				vista.repaint();
			}
		}
	}

	public void gestore() {
		final Vista vista = Controllore.getSingleton().getVista();
		final JPanel pannello = Controllore.getSingleton().getVista().getPanForGestore();
		final JPanel pannelloPlayer = Controllore.getSingleton().getVista().getPanForPlayer();
		// se il pannello è visibile lo nascondo e ridimensiono la vista
		// prendendo le dimensioni dell'altro pannello
		if (pannello.isVisible()) {
			pannello.setVisible(false);
			final Double larghezza1 = Double.valueOf(vista.getPanForPlayer().getSize().getWidth());
			final Double altezza1 = Double.valueOf(vista.getPanForPlayer().getSize().getHeight());
			vista.setSize(larghezza1.intValue(), altezza1.intValue());
			vista.getPanForPlayer().setLocation(new Point(0, 20));
			vista.invalidate();
			vista.repaint();
		} else {
			pannello.setVisible(true);
			if (pannelloPlayer.isVisible()) {
				vista.setSize(vista.getPreferredSize());
				pannello.setLocation(new Point(10, 31));
				pannelloPlayer.setLocation(new Point(283, 31));
			} else {
				final Double larghezza2 = Double.valueOf(vista.getPanForGestore().getSize().getWidth());
				final Double altezza2 = Double.valueOf(vista.getPanForGestore().getSize().getHeight());
				vista.setSize(larghezza2.intValue(), altezza2.intValue());
				vista.getPanForPlayer().setLocation(new Point(0, 20));
				vista.invalidate();
				vista.repaint();
			}
		}
	}

	public void apriCartella() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fileChooser.getSelectedFile();
			final File file1 = fileChooser.getSelectedFile();
			final Pannello pannello = Controllore.getSingleton().getVista().getPannello();
			pannello.getCartellaInput().setText(file1.getAbsolutePath());
			
			Binder binder = new Binder();
			new ParallelCrosserFile().execute(pannello.getCartellaInput().getText(), binder::apply);
			
			Controllore.getSingleton().getVista();
			final String[] nomiColonne = Controllore.getSingleton().getVista().getPlayList().getNomiColonne();
			final JScrollPane scroll = Controllore.getSingleton().getVista().getPlayList().getScrollPane();
			MyTable table = new MyTable(makeMatrix(binder), nomiColonne);
			Controllore.getSingleton().getVista().getPlayList().setTable(table);
			scroll.setViewportView(table);
			Controllore.getSingleton().getVista().repaint();

		}
	}

	public Mp3File[][] makeMatrix(Binder binderOp) {
		List<Mp3File> listaFile = binderOp.getListaFile();
		final Mp3File[][] dati = new Mp3File[listaFile.size()][1];
		
		
		MyBasicPlayer.setSize(listaFile.size());
		for (int i = 0; i < listaFile.size(); i++) {
			final Mp3File mp3 = listaFile.get(i);
			dati[i][0] = mp3;
			NewPlayList.getSingleton().put(Integer.toString(i), mp3);
		}
		return dati;
	}

	public void apriFile() {
		final NewPlayList playlist = Controllore.getSingleton().getVista().getPlayList();
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		MyBasicPlayer player;
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fileChooser.getSelectedFile();
			final File file1 = fileChooser.getSelectedFile();
			Mp3 fileMp3 = null;
			try {
				if (file1.isFile()) {
					fileMp3 = new Mp3(file1);
				}
			} catch (final Exception e1) {
				ControlloreBase.getLog().log(Level.SEVERE, e1.getMessage(), e1);
			}
			if (fileMp3 != null) {
				player = playlist.getPlayer();
				if (player != null) {
					player.stop();
					player.opener(fileMp3.getMp3file().getAbsolutePath());
					player.play();
				}
				playlist.getLabel().setText(fileMp3.getNome());
			}
		}
	}

}
