package de.dralle.som.languages.hrac.model;

public class HRACForDupRange {
	private int rangeStart;
	private int rangeEnd;
	private String rangeStartSpecial;
	private String rangeEndSpecial;

	public int[] getRange(HRACModel parent) {
		if (rangeEndSpecial != null) {
			rangeEnd = parent.getDirectiveAsInt(rangeEndSpecial);
		}
		if (rangeStartSpecial != null) {
			rangeStart = parent.getDirectiveAsInt(rangeStartSpecial);
		}
		int[] rng = null;
		if (rangeStart <= rangeEnd) {
			rng = new int[rangeEnd - rangeStart + 1];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = rangeStart + i;
			}
		} else {
			rng = new int[rangeStart - rangeEnd + 1];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = rangeStart - i;
			}
		}
		return rng;
	}

	public String asCode() {
		String s = "[";
		if (rangeStartSpecial != null) {
			s += ";" + rangeStartSpecial;
		} else {
			s += rangeStart + "";
		}
		s += ":";
		if (rangeEndSpecial != null) {
			s += ";" + rangeEndSpecial;
		} else {
			s += rangeEnd + "";
		}
		return s + "]";
	}

	@Override
	public String toString() {
		return asCode();
	}
}