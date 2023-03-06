package de.dralle.som.languages.hrac.model;

public class HRACForDupRange {
	private int rangeStart;
	private int rangeEnd;
	private boolean rangeStartSpecial;
	private boolean rangeEndSpecial;
	public int[] getRange(int special) {
		if(rangeEndSpecial) {
			rangeEnd=special;
		}
		if(rangeStartSpecial) {
			rangeStart=special;
		}
		int[] rng=null;
		if(rangeStart<=rangeEnd) {
			rng=new int[rangeEnd-rangeStart+1];
			for (int i = 0; i < rng.length; i++) {
				rng[i]=rangeStart+i;
			}
		}else {
			rng=new int[rangeStart-rangeEnd+1];
			for (int i = 0; i < rng.length; i++) {
				rng[i]=rangeStart-i;
			}
		}
		return rng;
	}
}
