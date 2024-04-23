package de.dralle.som.languages.hrac.model;

public class HRACForDupRange implements Cloneable {
	private int rangeStart;
	private int rangeEnd;
	private int stepSize = 1;
	private String rangeStartSpecial;
	private String rangeEndSpecial;
	private String stepSizeSpecial;
	private boolean lowerBoundExclusive = false;
	private boolean upperBoundExclusive = false

	;

	public int getStepSize() {
		return stepSize;
	}

	public boolean isLowerBoundExclusive() {
		return lowerBoundExclusive;
	}

	public void setLowerBoundExclusive(boolean lowerBoundExclusive) {
		this.lowerBoundExclusive = lowerBoundExclusive;
	}

	public boolean isUpperBoundExclusive() {
		return upperBoundExclusive;
	}

	public void setUpperBoundExclusive(boolean upperBoundExclusive) {
		this.upperBoundExclusive = upperBoundExclusive;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public String getStepSizeSpecial() {
		return stepSizeSpecial;
	}

	public void setStepSizeSpecial(String stepSizeSpecial) {
		this.stepSizeSpecial = stepSizeSpecial;
	}

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
		if (stepSizeSpecial != null) {
			stepSize = parent.getDirectiveAsInt(stepSizeSpecial);
		}
		int[] rng = null;
		if (rangeStart <= rangeEnd) {
			if (!lowerBoundExclusive && !upperBoundExclusive) {
				rng = new int[(rangeEnd - rangeStart + 1) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart + i * stepSize;
				}
			} else if (lowerBoundExclusive && upperBoundExclusive) {
				rng = new int[(rangeEnd - rangeStart - 1) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart + i * stepSize +1;
				}
			} else if (lowerBoundExclusive) {
				rng = new int[(rangeEnd - rangeStart) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart + i * stepSize + 1;
				}
			} else if (upperBoundExclusive) {
				rng = new int[(rangeEnd - rangeStart) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart + i * stepSize;
				}
			}

		} else {
			if (!lowerBoundExclusive && !upperBoundExclusive) {
				rng = new int[(rangeStart - rangeEnd + 1) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart - i * stepSize ;
				}
			} else if (lowerBoundExclusive && upperBoundExclusive) {
				rng = new int[(rangeStart - rangeEnd - 1) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart - i * stepSize +1;
				}
			} else if (lowerBoundExclusive) {
				rng = new int[(rangeStart - rangeEnd) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart - i * stepSize + 1;
				}
			} else if (upperBoundExclusive) {
				rng = new int[(rangeStart - rangeEnd) / stepSize];
				for (int i = 0; i < rng.length; i++) {
					rng[i] = rangeStart - i * stepSize;
				}
			}

		}
		return rng;
	}

	@Override
	public HRACForDupRange clone() {
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
		String s = lowerBoundExclusive?"]":"[";;
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
		s+=";";
		if (stepSizeSpecial != null) {
			s += "$" + stepSizeSpecial;
		} else {
			s += stepSize + "";
		}
		return s + (upperBoundExclusive?"[":"]");
	}

	@Override
	public String toString() {
		return asCode();
	}
}
