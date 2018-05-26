package com.molinari.mp3.business.op;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TagOpTest {

	@Test
	public void testGetNamePlusOriginal() {
		String plusOriginal = TagOp.getNamePlusOriginal("prova.mp3");
		assertEquals("Err!", "prova.original.mp3", plusOriginal);
		plusOriginal = TagOp.getNamePlusOriginal("prova.MP3");
		assertEquals("Err!", "prova.original.MP3", plusOriginal);
	}

}
