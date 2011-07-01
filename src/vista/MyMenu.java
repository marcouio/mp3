package vista;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import oggettiWrapper.Mp3;
import business.Controllore;
import business.operazioni.raccoglitore.Raccoglitore;
import business.player.MyBasicPlayer;

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
						}
						playlist.getLabel().setText(fileMp3.getNome());
						player.opener(fileMp3.getMp3file().getAbsolutePath());
						player.play();
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
					final Raccoglitore raccogli = new Raccoglitore();

					raccogli.raccogli(pannello.getCartellaInput().getText());
					Controllore.getSingleton().getVista();
					final String[] nomiColonne = Controllore.getSingleton().getVista().getPlayList().getNomiColonne();
					final JScrollPane scroll = Controllore.getSingleton().getVista().getPlayList().getScrollPane();
					MyTable table = Controllore.getSingleton().getVista().getPlayList().getTable();
					table = new MyTable(raccogli.getCanzoni(), nomiColonne);
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
		// info.addActionListener(new AscoltatoreInfo());
		help.add(info);

		final JMenuItem manuale = new JMenuItem("Manuale");
		help.add(manuale);

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JFrame frame = new JFrame();
				frame.setSize(1000, 50);
				frame.getContentPane().add(new MyMenu());
				frame.setVisible(true);
			}
		});
	}

}
