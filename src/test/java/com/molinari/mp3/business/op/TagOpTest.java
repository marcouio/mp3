package com.molinari.mp3.business.op;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import com.molinari.mp3.business.objects.Mp3;

public class TagOpTest {

	@Test
	public void testGetNamePlusOriginal() {
		String plusOriginal = TagOp.getNamePlusOriginal("prova.mp3");
		assertEquals("Err!", "prova.original.mp3", plusOriginal);
		plusOriginal = TagOp.getNamePlusOriginal("prova.MP3");
		assertEquals("Err!", "prova.original.MP3", plusOriginal);
	}

	@Test
	public void testApplyRestituisceMp3() throws Exception {
		Mp3 expected = new Mp3();
		TagOp op = new StubTagOp(expected);
		File file = createTempMp3();
		try {
			assertSame(expected, op.apply(file));
		} finally {
			file.delete();
		}
	}

	@Test
	public void testApplyRestituisceNullSenzaTag() throws Exception {
		TagOp op = new StubTagOp(null);
		File file = createTempMp3();
		try {
			assertNull(op.apply(file));
		} finally {
			file.delete();
		}
	}

	private File createTempMp3() throws Exception {
		File file = File.createTempFile("tagop", ".mp3");
		file.deleteOnExit();
		return file;
	}

	@Test
	public void testApplyNonCancellaBackupOriginal() throws Exception {
		Path dir = Files.createTempDirectory("tagop");
		File file = new File(dir.toFile(), "song.mp3");
		Files.write(file.toPath(), new byte[0]);
		File backup = new File(dir.toFile(), "song.original.mp3");
		Files.write(backup.toPath(), new byte[0]);
		try {
			TagOp op = new StubTagOp(new Mp3());
			op.apply(file);
			assertTrue("Il backup .original.mp3 non deve essere cancellato", backup.exists());
		} finally {
			backup.delete();
			file.delete();
			dir.toFile().delete();
		}
	}

	private static class StubTagOp extends TagOp {

		private final Mp3 result;

		StubTagOp(Mp3 result) {
			super(false);
			this.result = result;
		}

		@Override
		public Mp3 execute(File f) {
			return result;
		}

		@Override
		protected Mp3 operationTag(Mp3 mp3) {
			return mp3;
		}
	}
}
