package com.molinari.mp3.business.op.writer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;

public class WriterTest {

	private File output;

	@After
	public void cleanup() {
		if (output != null) {
			output.delete();
		}
	}

	@Test
	public void testScriveTutteLeRigheSenzaSovrascrivere() throws Exception {
		output = newOutputFile();
		Writer writer = new Writer(false, output.getAbsolutePath(), "key");

		writer.operationTag(mp3WithTag("Artista1", "Titolo1"));
		writer.operationTag(mp3WithTag("Artista2", "Titolo2"));

		List<String> lines = Files.readAllLines(output.toPath(), StandardCharsets.UTF_8);
		assertEquals(2, lines.size());
		assertEquals("ARTISTA1 - TITOLO1", lines.get(0));
		assertEquals("ARTISTA2 - TITOLO2", lines.get(1));
	}

	@Test
	public void testSovrascriveOutputDiUnRunPrecedente() throws Exception {
		output = newOutputFile();
		Files.write(output.toPath(), "VECCHIA RIGA\n".getBytes(StandardCharsets.UTF_8));

		Writer writer = new Writer(false, output.getAbsolutePath(), "key");
		writer.operationTag(mp3WithTag("Artista", "Titolo"));

		List<String> lines = Files.readAllLines(output.toPath(), StandardCharsets.UTF_8);
		assertEquals(1, lines.size());
		assertEquals("ARTISTA - TITOLO", lines.get(0));
	}

	private File newOutputFile() throws Exception {
		File file = File.createTempFile("writer-output", ".txt");
		file.deleteOnExit();
		return file;
	}

	private Mp3 mp3WithTag(final String artista, final String traccia) {
		final Tag tag = new Tag() {

			@Override
			public String getArtistaPrincipale() {
				return artista;
			}

			@Override
			public String getTitoloCanzone() {
				return traccia;
			}

			@Override
			public String getNomeAlbum() {
				return null;
			}

			@Override
			public String getGenere() {
				return null;
			}

			@Override
			public String getCommenti() {
				return null;
			}

			@Override
			public String getTraccia() {
				return traccia;
			}

			@Override
			public void setArtistaPrincipale(String artistaPrincipale) {
			}

			@Override
			public void setTitoloCanzone(String titoloCanzone) {
			}

			@Override
			public void setNomeAlbum(String nomeAlbum) {
			}

			@Override
			public void setGenere(String genere) {
			}

			@Override
			public void setCommenti(String commenti) {
			}

			@Override
			public void setTraccia(String traccia) {
			}

			@Override
			public boolean hasTitleAndArtist() {
				return artista != null && traccia != null;
			}
		};

		return new Mp3() {
			@Override
			public Tag getTag() {
				return tag;
			}
		};
	}
}
