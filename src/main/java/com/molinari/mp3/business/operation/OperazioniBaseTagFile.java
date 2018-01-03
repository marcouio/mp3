package com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v2_4;
import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.acoustid.TagAudioTrack;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.lookup.LookUp;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.MyTagException;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo2_4;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.utility.io.URLUTF8Encoder;

public abstract class OperazioniBaseTagFile extends OperazioniBase {

	protected CheckFile checkFile;
	private boolean forceFindTag = true;

	public OperazioniBaseTagFile() {
		initCheckFile();
	}

	protected void initCheckFile() {
		checkFile = new CheckFile();
	}

	/**
	 * Questo metodo è solo una parte di codice ricorrente che scorre tutte le
	 * cartelle in profondità per tutti i file che raggiunge eseguo un metodo
	 * che va implementato a secondo dell'uso che se ne vuole fare
	 * 
	 * @param pathFile
	 * @return
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public boolean scorriEdEseguiSuTuttiIFile(String pathFilePar) throws ParserConfigurationException, SAXException {
		String pathFile = pathFilePar;
		setTipoOperazione();
		boolean ok = true;
		try {

			if (!pathFile.substring(pathFile.length() - 1, pathFile.length()).equals(Mp3ReaderUtil.slash())) {
				pathFile += Mp3ReaderUtil.slash();
			}
			final File dir = new File(pathFile);
			final String[] files = dir.list();
			if(files != null){
				for (int i = 0; i < files.length; i++) {
					final File f = new File(pathFile + files[i]);

					if (checkFile(f, true)) {
						if (f.isFile() && f.canRead()) {
							eseguiOperazioneSufile(pathFile, f);
						} else if (f.isDirectory()) {
							doWithDirectory(f);
						}
					}
				}
			}
			if (cartelleDaScorrere != null && !cartelleDaScorrere.isEmpty()) {
				for (int i = 0; i < cartelleDaScorrere.size(); i++) {
					final String path = cartelleDaScorrere.get(i);
					cartelleDaScorrere.remove(i);
					scorriEdEseguiSuTuttiIFile(path);
				}
			}
			operazioneFinale();
			Controllore.setOperazione(IOperazioni.DEFAULT);
		} catch (final IOException e) {
			// se il file non viene trovato o non è disponibile
			ok = false;
			Controllore.getLog().log(Level.SEVERE, "File non trovato o non disponibile", e);
		}
		return ok;
	}

	public void doWithDirectory(final File f) {
		cartelleDaScorrere.add(f.getAbsolutePath());
	}

	public void eseguiOperazioneSufile(String pathFile, final File f) throws IOException {
		try {
			Controllore.getLog().info("Sto analizzando il file: " + f.getAbsolutePath());
			operazioneDaEseguireSulFile(pathFile, f);
		} catch (final Exception e) {
			Controllore.getLog().log(Level.SEVERE, "-> Errore non controllato durante l'operazione su file", e);
		}
	}

	private boolean checkFile(final File f, final boolean b) {
		return checkFile.checkFile(f, b);
	}

	protected abstract void setTipoOperazione();

	protected abstract void operazioneFinale();

	/**
	 * Metodo generico che prende il tag da un file ed esegue delle operazioni
	 * su di esso tramite metodi astratti che vanno implementati nelle classi
	 * figlie a seconda dell'utilizzo che se ne vuole fare
	 * 
	 * @param pathFile
	 * @param f
	 * @throws IOException
	 * @throws TagException
	 */
	public void operazioniGenerica(final String pathFile, final File f) throws IOException, Exception {
		if(f.getAbsolutePath().toLowerCase().endsWith(CheckFile.ESTENSIONE_MP3)){
			workOnMp3(pathFile, f);
		}
	}
	
	public String adjust(String nome){
		return nome.replaceAll("/", "").replaceAll(":", "");
	}

	public void workOnMp3(final String pathFile, final File f) {
		try {
			final Mp3 file = new Mp3(f);
			if(f.getAbsolutePath().endsWith(".original.mp3") || f.getName().equals(".mp3")){
				f.delete();
			}else{
				final Tag tag = file.getTag();
				if (tag != null) {
					Controllore.getLog().info("- > Tag già presenti su file");
					operazioneTagPresenti(pathFile, f, tag);
				} else {
					Controllore.getLog().info(" -> Tag non presenti su file");
					operazioneTagNonPresenti(pathFile, f);
				}
			}
		} catch (IOException | TagException e) {
			throw new Mp3Exception(e);
		}
	}

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagNonPresenti(String pathFile2, File f);

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException;

	protected void operazioneDaEseguireSulFile(final String pathFile, final File f) throws IOException, MyTagException {
		try {
			operazioniGenerica(pathFile, f);
		} catch (final Exception e) {
			Controllore.getLog().log(Level.SEVERE, "-> Eccezione durante la lettura del file! Guarda i log", e);
		}
	}

	public Tag findTag(final File f) {

		Tag result = null;
		try {
			Mp3 mp3 = new Mp3(f);

			boolean hasTitleAndArtist = TagUtil.hasTitleAndArtist(mp3.getTag());
			
			if (!hasTitleAndArtist || isForceFindTag()) {
				result = findTagByWeb(f, mp3);
			}

			if(result == null) {
				result = mp3.getTag();
			}

			if (result == null || !hasTitleAndArtist) {
				final Assegnatore assegna = new Assegnatore(f, "-");
				assegna.save(f);
				result = assegna.getFile().getTag();
				if(result == null){
					result =  new TagTipo2_4(new ID3v2_4());
					
				}
			}

			mp3.setTag(result);
			mp3.save(f);
		} catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
		return result;
	}

	public Tag findTagByWeb(final File f, Mp3 mp3) {
		Tag result = null;
		try {
			LookUp lookUp = new LookUp(KeyHolder.getSingleton().getKey());
			TagAudioTrack tagFromUrl = lookUp.lookup(f);
			result = mp3.getTag();
			if(tagFromUrl != null){
				
				String trackName = tagFromUrl.getTrackName();
				String artist = tagFromUrl.getArtist();
				
				if (trackName != null && artist != null) {
					
					Tag webResult = new TagTipo2_4(new ID3v2_4());
					
					trackName = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getTrackName()));
					artist = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getArtist()));
					String album = tagFromUrl.getAlbum();
					if(album != null){
						album = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getAlbum()));
					}
					webResult.setTitoloCanzone(trackName);
					webResult.setArtistaPrincipale(artist);
					webResult.setNomeAlbum(album);
					
					if(result != null){
						if(result.getCommenti() != null){
							webResult.setCommenti(result.getCommenti());
						}
						if(result.getGenere() != null){
							webResult.setGenere(result.getGenere());
						}
						if(result.getTraccia() != null){
							webResult.setTraccia(result.getTraccia());
						}
					}
					
					
					
					mp3.setTag(webResult);
					mp3.save(f);
	
					Controllore.getLog().info("-> Memorizzazione tag");
					return webResult;
				} else {
					Controllore.getLog().info("-> Tag da url non trovato o con informazioni mancanti");
				}
			}
		} catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, "-> Eccezione durante la ricerca del tag via web", e);
		}
		return result;
	}

	public boolean isForceFindTag() {
		return forceFindTag;
	}

	public void setForceFindTag(boolean forceFindTag) {
		this.forceFindTag = forceFindTag;
	}
}
