package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Provides a range of values (as an arry) via getRange() if bounds and stepsize are specified.
 */
public class HRACForDupBoundingRangeProvider implements IHRACRangeProvider,Cloneable {
	private int rangeStart;
	private int rangeEnd;
	private int stepSize = 1;
	private String rangeStartSpecial;
	private String rangeEndSpecial;
	private String stepSizeSpecial;
	// next 2 refer to the start and end of a range, regardless of the range countin
	// down or up. for[1:2] 1 would be start and 2 would be end, [2:1] would be
	// start 2 and end 1
	private boolean rangeStartBoundExclusive = false;
	private boolean rangeEndBoundExclusive = false

	;

	public boolean isRangeStartBoundExclusive() {
		return rangeStartBoundExclusive;
	}

	public boolean isRangeEndBoundExclusive() {
		return rangeEndBoundExclusive;
	}

	public int getStepSize() {
		return stepSize;
	}

	public boolean isLowerBoundExclusive() {
		return rangeStartBoundExclusive;
	}

	public void setRangeStartBoundExclusive(boolean lowerBoundExclusive) {
		this.rangeStartBoundExclusive = lowerBoundExclusive;
	}

	public boolean isUpperBoundExclusive() {
		return rangeEndBoundExclusive;
	}

	public void setRangeEndBoundExclusive(boolean upperBoundExclusive) {
		this.rangeEndBoundExclusive = upperBoundExclusive;
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
		if (rangeStart <= rangeEnd) {// range counts up
			// calculate "real" range limits (taking into account upper and lower
			// exclusivity)
			int realRangeStart = rangeStart;
			int realRangeEnd = rangeEnd;
			if (rangeStartBoundExclusive) {
				realRangeStart += 1;
			}
			if (rangeEndBoundExclusive) {
				realRangeEnd -= 1;
			}
			List<Integer> range = new ArrayList<Integer>();
			int currentValue = realRangeStart;
			while (currentValue <= realRangeEnd) {
				range.add(currentValue);
				currentValue += stepSize;
			}
			rng = new int[range.size()];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = range.get(i);
			}
		} else {// range counts down
			// calculate "real" range limits (taking into account upper and lower
			// exclusivity)
			int realRangeStart = rangeStart;
			int realRangeEnd = rangeEnd;
			if (rangeStartBoundExclusive) {
				realRangeStart -= 1;
			}
			if (rangeEndBoundExclusive) {
				realRangeEnd += 1;
			}
			List<Integer> range = new ArrayList<Integer>();
			int currentValue = realRangeStart;
			while (currentValue >= realRangeEnd) {
				range.add(currentValue);
				currentValue -= stepSize;
			}
			rng = new int[range.size()];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = range.get(i);
			}
		}
		return rng;
	}

	@Override
	public HRACForDupBoundingRangeProvider clone() {
		// TODO Auto-generated method stub
		try {
			return (HRACForDupBoundingRangeProvider) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String asCode() {
		String s = rangeStartBoundExclusive ? "]" : "[";
		;
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
		s += ";";
		if (stepSizeSpecial != null) {
			s += "$" + stepSizeSpecial;
		} else {
			s += stepSize + "";
		}
		return s + (rangeEndBoundExclusive ? "[" : "]");
	}

	@Override
	public String toString() {
		return asCode();
	}
}
