package com.molinari.mp3.business.acoustid;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.Assert;

public class TagAudioTrackTest {

	@Test
	public void test() {
		Result rMax = new Result();
		rMax.setScore(10.0);
		rMax.setId("Max");
		Result rMin = new Result();
		rMin.setScore(1.0);
		rMin.setId("Min");
		Result bestResult = new TagAudioTrack(null).getBestResult(Arrays.asList(rMax, rMin));
		
		Assert.assertEquals("Max", bestResult.getId());
	}

}
