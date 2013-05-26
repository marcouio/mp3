package business.operazioni.ordinatore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import oggettiWrapper.Mp3;
import oggettiWrapper.Tag;

import org.xml.sax.SAXException;

import vista.Alert;
import business.Controllore;
import business.Mp3ReaderUtil;
import business.check.CheckFile;
import business.check.CheckFileOrdina;
import business.operazioni.Assegnatore;
import business.operazioni.IOperazioni;
import business.operazioni.OperazioniBaseTagFile;

public class Ordinatore extends OperazioniBaseTagFile {

	private String input;
	private String output;
	private ArrayList<File> fileDaSpostareAllaFine = new ArrayList<File>();;
	private String pathCartellaAlbum;

	public Ordinatore(final String input, final String output) throws Exception {
		if (CheckFile.checkCartelle(input, output)) {
			setInput(input);
			setOutput(output);
			setPathFile(getOutput());
		} else {
			Alert.errore("Errori presenti nella scelta delle cartelle", "Non va");
			throw new Exception("Errori presenti nella scelta delle cartelle");
		}
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		try {
			final Assegnatore assegna = new Assegnatore(f, "-");
			if (f.getName().endsWith(CheckFile.ESTENSIONE_MP3)) {
				final Tag tag = assegna.getTag();
				assegna.save(f);
				if (tag != null) {
					if (isTagsValorizzati(tag)) {
						operazioneTagPresenti(pathFile2, f, tag);
					}
				}
			} else {
				fileDaSpostareAllaFine.add(f);
			}
		} catch (final IOException e) {
			// file non trovato
			e.printStackTrace();
		}

	}

	protected boolean isTagsValorizzati(final Tag tag) {
		return tag.getArtistaPrincipale() != null && tag.getNomeAlbum() != null && tag.getArtistaPrincipale() != "" && tag.getNomeAlbum() != "";
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag) throws IOException {
		try {
			ordinaPerTag(pathFile2, f, tag);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void setTipoOperazione() {
		Controllore.setOperazione(IOperazioni.ORDINA);
	}

	@Override
	protected void initCheckFile() {
		checkFile = new CheckFileOrdina();
	}

	@Override
	protected void operazioneFinale() {
		if (pathCartellaAlbum != null && !pathCartellaAlbum.equals("")) {
			for (int i = 0; i < fileDaSpostareAllaFine.size(); i++) {
				final File f = fileDaSpostareAllaFine.get(i);
				f.renameTo(new File(pathCartellaAlbum + Mp3ReaderUtil.slash() + f.getName()));
			}
		}
		// TODO generaReport();

	}

	public boolean ordina() {
		try {
			return scorriEdEseguiSuTuttiIFile(input);
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub

	}

	public boolean moveFile(final File origine, final File destinazione) {
		return origine.renameTo(destinazione);
	}

	private boolean ordinaPerTag(final String pathFile2, final File f, final Tag tag) throws Exception {
		Mp3 mp3 = null;
		if (checkFile.checkFile(f, true)) {
			mp3 = new Mp3(f);
			mp3.setTag(tag);
			final File cartellaAlfabeto = creaCartellaAlfabeto(tag, mp3);
			final File cartellaArtista = creaCartellaArtista(tag, mp3, cartellaAlfabeto);
			final File cartellaAlbum = creaCartellaAlbum(tag, mp3, cartellaArtista);
			pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
			if (f.renameTo(new File(pathCartellaAlbum + Mp3ReaderUtil.slash() + mp3.getNome()))) {
				return true;
			}
		}
		return false;
	}

	private File creaCartellaAlbum(final Tag tag, final Mp3 mp3, final File cartellaArtista) {
		File cartellaAlbum = null;
		final String pathCartellaArtista = cartellaArtista.getAbsolutePath();
		cartellaAlbum = new File(pathCartellaArtista + Mp3ReaderUtil.slash() + tag.getNomeAlbum());
		if (!cartellaAlbum.exists()) {
			cartellaAlbum.mkdir();
		}
		return cartellaAlbum;

	}

	private File creaCartellaArtista(final Tag tag, final Mp3 mp3, final File cartellaAlfabeto) {
		File cartellaArtista = null;
		final String pathCartellaAlfabeto = cartellaAlfabeto.getAbsolutePath();
		cartellaArtista = new File(pathCartellaAlfabeto + Mp3ReaderUtil.slash() + tag.getArtistaPrincipale());
		if (!cartellaArtista.exists()) {
			cartellaArtista.mkdir();
		}
		return cartellaArtista;

	}

	private File creaCartellaAlfabeto(final Tag tag, final Mp3 mp3) {
		File dir = null;
		final String nomeAlfa = tag.getArtistaPrincipale();
		String lettera = "";
		if (nomeAlfa != null && nomeAlfa.length() > 1) {
			lettera = nomeAlfa.substring(0, 1).toUpperCase();
			try {
				Integer.parseInt(lettera);
				dir = new File(output + Mp3ReaderUtil.slash() + "0_9");
				if (!dir.exists()) {
					dir.mkdir();
				}
			} catch (final NumberFormatException e) {
				dir = new File(output + Mp3ReaderUtil.slash() + lettera);
				if (!dir.exists()) {
					dir.mkdir();
				}
			}
		} else {
			dir = new File(output + Mp3ReaderUtil.slash() + "NoLetter");
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return dir;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getInput() {
		return input;
	}

}
