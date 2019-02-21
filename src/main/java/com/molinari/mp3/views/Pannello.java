package com.molinari.mp3.views;

import java.awt.Container;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.op.renamer.Renamer;
import com.molinari.mp3.business.op.tidier.Tidier;
import com.molinari.mp3.business.op.writer.Writer;
import com.molinari.mp3.business.op.writer.WriterFromFileOp;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.graphic.component.alert.Alert;
import com.molinari.utility.graphic.component.button.ButtonBase;
import com.molinari.utility.graphic.component.checkbox.CheckBoxBase;
import com.molinari.utility.graphic.component.container.PannelloBase;
import com.molinari.utility.io.ExecutorFiles;
import com.molinari.utility.io.FactoryExecutorFiles;
import com.molinari.utility.io.FileOperation;
import com.molinari.utility.io.func.CrosserFiles;

public class Pannello extends PannelloBase {

	private static final long serialVersionUID = 1L;

	private JTextField        cartellaInput;
	private JTextField        txtCartellaOutput;

	private CheckBoxBase force;

	/**
	 * Create the panel.
	 */
	public Pannello(Container c) {
		super(c);
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
		
		final ButtonBase buttonCaricaOutput = new ButtonBase("output", this);
		buttonCaricaOutput.setSize(73, 23);
		buttonCaricaOutput.posizionaADestraDi(txtCartellaOutput, 5, 0);
		add(buttonCaricaOutput);
		
		force = new CheckBoxBase("Force find tags", this);
		force.setSize(140, 20);
		force.posizionaSottoA(txtCartellaOutput, 0, 5);
		//TODO add listener to set force properties
		

		buttonCaricaOutput.addActionListener(e -> {
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				fileChooser.getSelectedFile();
				final File file = fileChooser.getSelectedFile();
				txtCartellaOutput.setText(file.getAbsolutePath());
			}
		});

		final ButtonBase buttonRinomina = new ButtonBase(this);
		buttonRinomina.setText("rinomina");
		buttonRinomina.setSize(112, 23);
		buttonRinomina.posizionaSottoA(force, 0, 5);
		add(buttonRinomina);

		final JSeparator separator = new JSeparator();
		separator.setBounds(13, 217, 244, 2);
		add(separator);

		final ButtonBase btnCrealista = new ButtonBase(this);
		btnCrealista.addActionListener(arg0 -> {
			String s = JOptionPane.showInputDialog("key?", "0B3qZnQc");
			new CrosserFiles().execute(cartellaInput.getText(), new Writer(force.isSelected(), txtCartellaOutput.getText() + Mp3ReaderUtil.slash() + "listaAlbum.odt", s)::apply);
			FileOperation fileOperation = new WriterFromFileOp(txtCartellaOutput.getText() + Mp3ReaderUtil.slash() + "listaAlbum.odt");
			ExecutorFiles executorFiles = FactoryExecutorFiles.createExecutorFiles(fileOperation);
			try {
				executorFiles.start(cartellaInput.getText());
			} catch (ParserConfigurationException | SAXException e) {
				ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
			}
		});
		btnCrealista.setText("crea lista");
		btnCrealista.setSize(110, 23);
		btnCrealista.posizionaSottoA(buttonRinomina, 0, 5);
		add(btnCrealista);

		final ButtonBase buttonOrdina = new ButtonBase(this);
		buttonOrdina.addActionListener(arg0 -> {
			try {
				String s = JOptionPane.showInputDialog("key?", "0B3qZnQc");
				new CrosserFiles().execute(cartellaInput.getText(), new Tidier(force.isSelected(), s, txtCartellaOutput.getText())::apply);
				Alert.info("Ok, file mp3 ordinati correttamente", "Perfetto");
			} catch (final Exception e) {
				Controllore.log(Level.SEVERE, e.getMessage(), e);
			}
		});
		buttonOrdina.setText("ordina");
		buttonOrdina.setSize(112, 23);
		buttonOrdina.posizionaSottoA(btnCrealista, 0, 5);
		add(buttonOrdina);

		buttonRinomina.addActionListener(e -> {
			String s = JOptionPane.showInputDialog("key?", "0B3qZnQc");
			try {
				
				new CrosserFiles().execute(cartellaInput.getText(), new Renamer(force.isSelected(), s)::apply);
				Alert.info("Ok, file mp3 rinominati correttamente", "Perfetto");

			} catch (final Exception e1) {
				ControlloreBase.getLog().log(Level.SEVERE, e1.getMessage(), e1);
			}

		});
	}

	protected String[][] creaCanzoni() {

		return new String[][] {};
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
}
