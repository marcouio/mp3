package com.molinari.mp3.business.op;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilOpTest {

	@Test
	public void adjustNullReturnsEmpty() {
		assertEquals("", UtilOp.adjust(null));
	}

	@Test
	public void adjustRemovesIllegalCharacters() {
		assertEquals("ABC", UtilOp.adjust("A/B\\C:*?\"<>|"));
	}

	@Test
	public void adjustReplacesApostropheWithSpace() {
		assertEquals("Bob s", UtilOp.adjust("Bob's"));
	}

	@Test
	public void adjustTrimsTrailingDotsAndSpaces() {
		assertEquals("Artist", UtilOp.adjust("Artist... "));
	}

	@Test
	public void adjustPrefixesReservedNames() {
		assertEquals("_CON", UtilOp.adjust("CON"));
	}
}
