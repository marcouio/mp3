package com.molinari.mp3.views;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.binder.BinderOp;
import com.molinari.mp3.business.player.MyBasicPlayer;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.ExecutorFiles;
import com.molinari.utility.io.FactoryExecutorFiles;

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
		daFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final NewPlayList playlist = Controllore.getSingleton().getVista().getPlayList();
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				MyBasicPlayer player;
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileChooser.getSelectedFile();
					final File file = fileChooser.getSelectedFile();
					Mp3 fileMp3 = null;
					try {
						if (file.isFile()) {
							fileMp3 = new Mp3(file);
						}
					} catch (final Exception e1) {
						e1.printStackTrace();
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
		});
		file.add(daFile);

		final JMenuItem daCartella = new JMenuItem("Apri Cartella");
		daCartella.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileChooser.getSelectedFile();
					final File file = fileChooser.getSelectedFile();
					final Pannello pannello = Controllore.getSingleton().getVista().getPannello();
					pannello.getCartellaInput().setText(file.getAbsolutePath());
					
					BinderOp binderOp = new BinderOp();
					ExecutorFiles executorFiles = FactoryExecutorFiles.createExecutorFiles(binderOp);
					try {
						executorFiles.start(pannello.getCartellaInput().getText());
					} catch (ParserConfigurationException | SAXException e1) {
						ControlloreBase.getLog().log(Level.SEVERE, e1.getMessage(), e1);
					}
					
					Controllore.getSingleton().getVista();
					final String[] nomiColonne = Controllore.getSingleton().getVista().getPlayList().getNomiColonne();
					final JScrollPane scroll = Controllore.getSingleton().getVista().getPlayList().getScrollPane();
					MyTable table = new MyTable(binderOp.getCanzoni(), nomiColonne);
					Controllore.getSingleton().getVista().getPlayList().setTable(table);
					scroll.setViewportView(table);
					Controllore.getSingleton().getVista().repaint();

				}

			}
		});
		file.add(daCartella);

		final JMenuItem chiudi = new JMenuItem("Chiudi");
		chiudi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);

			}
		});
		file.add(chiudi);

		final JMenu finestre = new JMenu("Finestre");
		add(finestre);

		final JCheckBoxMenuItem gestore = new JCheckBoxMenuItem("Gestore");
		gestore.setState(true);
		gestore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final Vista vista = Controllore.getSingleton().getVista();
				final JPanel pannello = Controllore.getSingleton().getVista().getPanForGestore();
				final JPanel pannelloPlayer = Controllore.getSingleton().getVista().getPanForPlayer();
				// se il pannello è visibile lo nascondo e ridimensiono la vista
				// prendendo le dimensioni dell'altro pannello
				if (pannello.isVisible()) {
					pannello.setVisible(false);
					final Double larghezza = new Double(vista.getPanForPlayer().getSize().getWidth());
					final Double altezza = new Double(vista.getPanForPlayer().getSize().getHeight());
					vista.setSize(larghezza.intValue(), altezza.intValue());
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
						final Double larghezza = new Double(vista.getPanForGestore().getSize().getWidth());
						final Double altezza = new Double(vista.getPanForGestore().getSize().getHeight());
						vista.setSize(larghezza.intValue(), altezza.intValue());
						vista.getPanForPlayer().setLocation(new Point(0, 20));
						vista.invalidate();
						vista.repaint();
					}
				}
			}
		});
		finestre.add(gestore);

		final JCheckBoxMenuItem player = new JCheckBoxMenuItem("Player");
		player.setState(true);
		player.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final Vista vista = Controllore.getSingleton().getVista();
				final JPanel pannello = Controllore.getSingleton().getVista().getPanForPlayer();
				final JPanel pannelloGestore = Controllore.getSingleton().getVista().getPanForGestore();
				if (pannello.isVisible()) {
					pannello.setVisible(false);
					final Double larghezza = new Double(vista.getPanForGestore().getSize().getWidth());
					final Double altezza = new Double(vista.getPanForGestore().getSize().getHeight());
					vista.setSize(larghezza.intValue(), altezza.intValue());
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
						final Double larghezza = new Double(pannello.getSize().getWidth());
						final Double altezza = new Double(pannello.getSize().getHeight());
						vista.setSize(larghezza.intValue(), altezza.intValue());
						vista.getPanForGestore().setLocation(new Point(0, 20));
						vista.invalidate();
						vista.repaint();
					}
				}
			}
		});
		finestre.add(player);

		final JMenu help = new JMenu("Help");
		add(help);

		final JMenuItem info = new JMenuItem("Info");
		help.add(info);

		final JMenuItem manuale = new JMenuItem("Manuale");
		help.add(manuale);

	}

}
