package com.molinari.mp3.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.operation.binder.Raccoglitore;
import com.molinari.mp3.business.operation.binder.Raccoglitore.Mp3File;
import com.molinari.mp3.business.operation.renamer.Rinominatore;
import com.molinari.mp3.business.operation.tidier.Ordinatore;
import com.molinari.mp3.business.operation.writer.Scrittore;
import com.molinari.mp3.business.operation.writer.ScrittoreListaAlbumDaCartelle;
import com.molinari.mp3.business.operation.writer.ScrittoreListaAlbumDaFile;
import com.molinari.utility.graphic.component.alert.Alert;

public class Pannello extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField        cartellaInput;
	private JTextField        txtCartellaOutput;
	private static Pannello   singleton;
	private boolean           perFile          = false;

	private JCheckBox         chckbxPerFile;

	public static Pannello getSingleton() {
		if (singleton == null) {
			singleton = new Pannello();
		}
		return singleton;
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				final JFrame inst = new JFrame();
				final Pannello p = new Pannello();
				inst.setBounds(10, 10, 275, 268);
				inst.getContentPane().add(p);
				inst.setTitle("MP3 Manager");
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}

		});
	}

	/**
	 * Create the panel.
	 */
	private Pannello() {
		setLayout(null);
		final JButton buttonInput = new JButton();
		buttonInput.setText("input");
		buttonInput.setBounds(181, 13, 73, 23);
		add(buttonInput);

		buttonInput.addActionListener(e -> {
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				final File file = fileChooser.getSelectedFile();
				cartellaInput.setText(file.getAbsolutePath());
				final Raccoglitore raccogli = new Raccoglitore();

				raccogli.raccogli(cartellaInput.getText());
				Controllore.getSingleton().getVista();
				final String[] nomiColonne = Controllore.getSingleton().getVista().getPlayList().getNomiColonne();
				final JScrollPane scroll = Controllore.getSingleton().getVista().getPlayList().getScrollPane();
				MyTable table = Controllore.getSingleton().getVista().getPlayList().getTable();
				Mp3File[][] canzoni = raccogli.getCanzoni();
				table = new MyTable(canzoni, nomiColonne);
				Controllore.getSingleton().getVista().getPlayList().setTable(table);
				scroll.setViewportView(table);
				Controllore.getSingleton().getVista().repaint();
			}
		});

		cartellaInput = new JTextField();
		cartellaInput.setBounds(22, 11, 149, 27);
		cartellaInput.setText("Cartella Input");
		add(cartellaInput);

		txtCartellaOutput = new JTextField();
		txtCartellaOutput.setText("Cartella Output");
		txtCartellaOutput.setBounds(22, 57, 149, 27);
		add(txtCartellaOutput);

		final JButton buttonCaricaOutput = new JButton();
		buttonCaricaOutput.setText("output");
		buttonCaricaOutput.setBounds(181, 59, 73, 23);
		add(buttonCaricaOutput);

		buttonCaricaOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileChooser.getSelectedFile();
					final File file = fileChooser.getSelectedFile();
					txtCartellaOutput.setText(file.getAbsolutePath());
				}
			}
		});

		final JButton buttonRinomina = new JButton();
		buttonRinomina.setText("rinomina");
		buttonRinomina.setBounds(22, 140, 112, 23);
		add(buttonRinomina);

		final JSeparator separator = new JSeparator();
		separator.setBounds(13, 217, 244, 2);
		add(separator);

		final JButton btnCrealista = new JButton();
		btnCrealista.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				Scrittore scrittore = null;
				if (isPerFile()) {
					scrittore = new ScrittoreListaAlbumDaFile();
				} else {
					scrittore = new ScrittoreListaAlbumDaCartelle();
				}
				scrittore.write(cartellaInput.getText(), txtCartellaOutput.getText() + Mp3ReaderUtil.slash() + "listaAlbum.odt");
			}
		});
		btnCrealista.setText("crea lista");
		btnCrealista.setBounds(22, 106, 110, 23);
		add(btnCrealista);

		chckbxPerFile = new JCheckBox("Per File");
		chckbxPerFile.setBounds(133, 106, 97, 23);
		add(chckbxPerFile);

		final JButton buttonOrdina = new JButton();
		buttonOrdina.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				try {
					final Ordinatore ordina = new Ordinatore(cartellaInput.getText(), txtCartellaOutput.getText());
					if (ordina.ordina()) {
						Alert.info("Ok, file mp3 ordinati correttamente", "Perfetto");
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		buttonOrdina.setText("ordina");
		buttonOrdina.setBounds(22, 174, 112, 23);
		add(buttonOrdina);

		buttonRinomina.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				String s = JOptionPane.showInputDialog("key?", "0B3qZnQc");
				try {
					final Rinominatore rinomina = new Rinominatore(s);
					if (rinomina.scorriEdEseguiSuTuttiIFile(cartellaInput.getText())) {
						Alert.info("Ok, file mp3 rinominati correttamente", "Perfetto");
					}

				} catch (final Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	protected String[][] creaCanzoni() {

		return null;
	}

	/**
	 * @return the cartellaInput
	 */
	public JTextField getCartellaInput() {
		return cartellaInput;
	}

	/**
	 * @param cartellaInput
	 *            the cartellaInput to set
	 */
	public void setCartellaInput(final JTextField cartellaInput) {
		this.cartellaInput = cartellaInput;
	}

	/**
	 * @return the txtCartellaOutput
	 */
	public JTextField getTxtCartellaOutput() {
		return txtCartellaOutput;
	}

	/**
	 * @param txtCartellaOutput
	 *            the txtCartellaOutput to set
	 */
	public void setTxtCartellaOutput(final JTextField txtCartellaOutput) {
		this.txtCartellaOutput = txtCartellaOutput;
	}

	public void setPerFile(final boolean perFile) {
		this.perFile = perFile;
	}

	public boolean isPerFile() {
		if (chckbxPerFile.isSelected()) {
			perFile = true;
		} else {
			perFile = false;
		}
		return perFile;
	}
}
