package de.dralle.som.languages.hrac.model;

public class HRACForDupRange implements Cloneable {
	private int rangeStart;
	private int rangeEnd;
	private String rangeStartSpecial;
	private String rangeEndSpecial;

	public int getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(int rangeStart) {
		this.rangeStart = rangeStart;
	}

	public int getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(int rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	public String getRangeStartSpecial() {
		return rangeStartSpecial;
	}

	public void setRangeStartSpecial(String rangeStartSpecial) {
		this.rangeStartSpecial = rangeStartSpecial;
	}

	public String getRangeEndSpecial() {
		return rangeEndSpecial;
	}

	public void setRangeEndSpecial(String rangeEndSpecial) {
		this.rangeEndSpecial = rangeEndSpecial;
	}

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

	@Override
	public HRACForDupRange clone(){
		// TODO Auto-generated method stub
		try {
			return (HRACForDupRange) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String asCode() {
		String s = "[";
		if (rangeStartSpecial != null) {
			s += "$" + rangeStartSpecial;
		} else {
			s += rangeStart + "";
		}
		s += ":";
		if (rangeEndSpecial != null) {
			s += "$" + rangeEndSpecial;
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
