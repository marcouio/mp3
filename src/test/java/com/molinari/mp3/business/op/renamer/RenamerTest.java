package com.molinari.mp3.business.op.renamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;

public class RenamerTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void buildRenamedFileWithNullTagKeepsOriginalName() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		assertEquals(original, Renamer.buildRenamedFile(dir, original, null));
	}

	@Test
	public void buildRenamedFileWithMissingArtistKeepsOriginalName() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		assertEquals(original, Renamer.buildRenamedFile(dir, original, new TestTag(null, "Song")));
	}

	@Test
	public void buildRenamedFileWithBlankAfterSanitizationKeepsOriginalName() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		assertEquals(original, Renamer.buildRenamedFile(dir, original, new TestTag("///", "Song")));
	}

	@Test
	public void buildRenamedFileSanitizesIllegalCharacters() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		File result = Renamer.buildRenamedFile(dir, original, new TestTag("A/B\\C:*?\"<>|", "Tit:le?"));

		assertEquals(new File(dir, "ABC - Title.mp3"), result);
	}

	@Test
	public void buildRenamedFileRemovesTrailingDotsAndSpaces() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		File result = Renamer.buildRenamedFile(dir, original, new TestTag("Artist... ", "Title . "));

		assertEquals(new File(dir, "Artist - Title.mp3"), result);
	}

	@Test
	public void buildRenamedFilePrefixesReservedWindowsName() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		File result = Renamer.buildRenamedFile(dir, original, new TestTag("CON", "Song"));

		assertEquals(new File(dir, "_CON - Song.mp3"), result);
	}

	@Test
	public void buildRenamedFilePreservesExtensionCase() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.MP3");

		File result = Renamer.buildRenamedFile(dir, original, new TestTag("Artist", "Title"));

		assertEquals(new File(dir, "Artist - Title.MP3"), result);
	}

	@Test
	public void buildRenamedFilePathTraversalStaysInParent() throws IOException {
		File dir = folder.newFolder("music");
		File original = new File(dir, "track.mp3");

		File result = Renamer.buildRenamedFile(dir, original, new TestTag("..\\..\\evil", "Song"));

		assertEquals(dir.getCanonicalFile(), result.getParentFile().getCanonicalFile());
		assertFalse(result.getName().contains("\\"));
		assertFalse(result.getName().contains("/"));
	}

	@Test
	public void safeRenameOverwritesExistingTarget() throws IOException {
		File dir = folder.newFolder("music");
		File source = new File(dir, "track.mp3");
		Files.write(source.toPath(), "source".getBytes(StandardCharsets.UTF_8));
		File target = new File(dir, "Artist - Title.mp3");
		Files.write(target.toPath(), "old".getBytes(StandardCharsets.UTF_8));

		File result = Renamer.safeRename(dir, source, new TestTag("Artist", "Title"));

		assertEquals(target, result);
		assertFalse(source.exists());
		assertTrue(target.exists());
		assertEquals("source", new String(Files.readAllBytes(target.toPath()), StandardCharsets.UTF_8));
	}

	@Test
	public void safeRenameWithSameNameIsNoOp() throws IOException {
		File dir = folder.newFolder("music");
		File source = new File(dir, "Artist - Title.mp3");
		Files.write(source.toPath(), "content".getBytes(StandardCharsets.UTF_8));

		File result = Renamer.safeRename(dir, source, new TestTag("Artist", "Title"));

		assertEquals(source, result);
		assertTrue(source.exists());
	}

	private static final class TestTag implements Tag {

		private String artista;
		private String titolo;

		private TestTag(String artista, String titolo) {
			this.artista = artista;
			this.titolo = titolo;
		}

		@Override
		public String getArtistaPrincipale() {
			return artista;
		}

		@Override
		public String getTitoloCanzone() {
			return titolo;
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
			return null;
		}

		@Override
		public void setArtistaPrincipale(String artistaPrincipale) {
			this.artista = artistaPrincipale;
		}

		@Override
		public void setTitoloCanzone(String titoloCanzone) {
			this.titolo = titoloCanzone;
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
			return TagUtil.hasTitleAndArtist(this);
		}
	}
}
