package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveTreeCalculateIntegerValueVisitor;

/**
 * Provides a range of values (as an array) via getRange() if bounds and
 * stepsize are specified.
 */
public class HRACForDupBoundingRangeProvider implements IHRACRangeProvider, Cloneable {
	private HRACAbstractDirectiveExpressionTreeNode rangeStart;
	private HRACAbstractDirectiveExpressionTreeNode rangeEnd;
	private HRACAbstractDirectiveExpressionTreeNode stepSize = new HRACIntegerNode(1);
	// next 2 refer to the start and end of a range, regardless of the range countin
	// down or up. for[1:2] 1 would be start and 2 would be end, [2:1] would be
	// start 2 and end 1
	private boolean rangeStartBoundExclusive = false;
	private boolean rangeEndBoundExclusive = false;
	private String runningDirectiveName = "i"; // name of the running compiler directive to be injected into child loops

	public String asCode() {
		String s = "";
		if (runningDirectiveName != null) {
			s = "$" + runningDirectiveName + " = ";
		}
		s = rangeStartBoundExclusive ? "]" : "[";
		{
			s += rangeStart + "";
		}
		s += ":";
		{
			s += rangeEnd + "";
		}
		s += ";";
		{
			s += stepSize + "";
		}
		return s + (rangeEndBoundExclusive ? "[" : "]");
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

	public HRACAbstractDirectiveExpressionTreeNode[] getRange(HRACModel parent) {

		int[] rng = getRangeAsIntArray(parent);
		HRACAbstractDirectiveExpressionTreeNode[] rngNodes = new HRACAbstractDirectiveExpressionTreeNode[rng.length];
		for (int i = 0; i < rng.length; i++) {
			int hracAbstractExpressionNode = rng[i];
			rngNodes[i] = new HRACIntegerNode(hracAbstractExpressionNode);

		}
		return rngNodes;
	}

	public int[] getRangeAsIntArray(HRACModel parent) {
		HRACAbstractDirectiveExpressionTreeNode rangeStartResolved = rangeStart.getResolvedExpressionTree(parent);
		HRACAbstractDirectiveExpressionTreeNode rangeEndResolved = rangeEnd.getResolvedExpressionTree(parent);
		HRACAbstractDirectiveExpressionTreeNode stepSizeResolved = stepSize.getResolvedExpressionTree(parent);
		int rangeStartResolvedInt = rangeStartResolved.accept(new HRACDirectiveTreeCalculateIntegerValueVisitor()).intValue();
		int rangeEndResolvedInt = rangeEndResolved.accept(new HRACDirectiveTreeCalculateIntegerValueVisitor()).intValue();
		int stepSizeResolvedInt = stepSizeResolved.accept(new HRACDirectiveTreeCalculateIntegerValueVisitor()).intValue();
		int[] rng = null;
		if (rangeStartResolvedInt <= rangeEndResolvedInt) {// range counts up
			// calculate "real" range limits (taking into account upper and lower
			// exclusivity)
			int realRangeStart = rangeStartResolvedInt;
			int realRangeEnd = rangeEndResolvedInt;
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
				currentValue += stepSizeResolvedInt;
			}
			rng = new int[range.size()];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = range.get(i);
			}
		} else {// range counts down
			// calculate "real" range limits (taking into account upper and lower
			// exclusivity)
			int realRangeStart = rangeStartResolvedInt;
			int realRangeEnd = rangeEndResolvedInt;
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
				currentValue -= stepSizeResolvedInt;
			}
			rng = new int[range.size()];
			for (int i = 0; i < rng.length; i++) {
				rng[i] = range.get(i);
			}
		}
		return rng;
	}

	public HRACAbstractDirectiveExpressionTreeNode getRangeEnd() {
		return rangeEnd;
	}

	public HRACAbstractDirectiveExpressionTreeNode getRangeStart() {
		return rangeStart;
	}

	public String getRunningDirectiveName() {
		return runningDirectiveName;
	}

	public HRACAbstractDirectiveExpressionTreeNode getStepSize() {
		return stepSize;
	}

	public boolean isLowerBoundExclusive() {
		return rangeStartBoundExclusive;
	}

	public boolean isRangeEndBoundExclusive() {
		return rangeEndBoundExclusive;
	}

	public boolean isRangeStartBoundExclusive() {
		return rangeStartBoundExclusive;
	}

	public boolean isUpperBoundExclusive() {
		return rangeEndBoundExclusive;
	}

	public void setRangeEnd(HRACAbstractDirectiveExpressionTreeNode rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	public void setRangeEnd(int rangeEnd) {
		this.rangeEnd = new HRACIntegerNode(rangeEnd);
	}

	public void setRangeEndBoundExclusive(boolean upperBoundExclusive) {
		this.rangeEndBoundExclusive = upperBoundExclusive;
	}

	public void setRangeStart(HRACAbstractDirectiveExpressionTreeNode rangeStart) {
		this.rangeStart = rangeStart;
	}

	public void setRangeStart(int rangeStart) {
		this.rangeStart = new HRACIntegerNode(rangeStart);
	}

	public void setRangeStartBoundExclusive(boolean lowerBoundExclusive) {
		this.rangeStartBoundExclusive = lowerBoundExclusive;
	}

	public void setRunningDirectiveName(String runningDirectiveName) {
		this.runningDirectiveName = runningDirectiveName;
	}

	public void setStepSize(HRACAbstractDirectiveExpressionTreeNode stepSize) {
		this.stepSize = stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = new HRACIntegerNode(stepSize);
	}

	@Override
	public String toString() {
		return asCode();
	}
}
