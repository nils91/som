package de.dralle.som.languages.hrbs.model;

import java.util.logging.Logger;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRBSBoundsRange extends AbstractHRBSRange implements Cloneable {
	private static final Logger logger = Logger.getLogger(HRBSBoundsRange.class.getName());
	private HRBSAbstractExpressionNode start;
	private HRBSAbstractExpressionNode end;
	private HRBSAbstractExpressionNode step;

	private boolean startBoundExclusive = false;

	private boolean endBoundExclusive = false;

	@Override
	public HRBSBoundsRange clone() {
		HRBSBoundsRange c = null;
		c = (HRBSBoundsRange) super.clone();
		if (start != null) {
			c.start = start.clone();
		}
		if (end != null) {
			c.end = end.clone();
		}
		if (step != null) {
			c.step = step.clone();
		}
		return c;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSBoundsRange) {
			boolean eq = super.equals(obj);
			HRBSBoundsRange other = (HRBSBoundsRange) obj;
			if (eq && start != null) {
				eq = start.equals(other.getStart());
			}
			if (eq && end != null) {
				eq = end.equals(other.end);
			}
			if (eq && step != null) {
				eq = step.equals(other.step);
			}
			return eq;
		}
		return super.equals(obj);
	}

	public HRBSAbstractExpressionNode getEnd() {
		return end;
	}

	public HRBSAbstractExpressionNode getStart() {
		return start;
	}

	public HRBSAbstractExpressionNode getStep() {
		return step;
	}

	@Override
	public int hashCode() {
		int n = super.hashCode();
		if (start != null) {
			n += start.hashCode();
		}
		if (end != null) {
			n *= end.hashCode();
		}
		if (step != null) {
			n /= step.hashCode();
		}
		if (startBoundExclusive) {
			n -= 31;
		}
		if (endBoundExclusive) {
			n -= 97;
		}
		return n;
	}

	public boolean isEndBoundExclusive() {
		return endBoundExclusive;
	}

	public boolean isStartBoundExclusive() {
		return startBoundExclusive;
	}

	public void setEnd(HRBSAbstractExpressionNode end) {
		this.end = end;
	}

	public void setEnd(int end) {
		this.end = new HRBSIntegerNode(end);
	}

	public void setEndBoundExclusive(boolean endBoundExclusive) {
		this.endBoundExclusive = endBoundExclusive;
	}

	public void setStart(HRBSAbstractExpressionNode start) {
		this.start = start;
	}

	public void setStart(int start) {
		this.start = new HRBSIntegerNode(start);
	}

	public void setStartBoundExclusive(boolean startBoundExclusive) {
		this.startBoundExclusive = startBoundExclusive;
	}

	public void setStep(HRBSAbstractExpressionNode step) {
		this.step = step;
	}

	public void setStep(int step) {
		this.step = new HRBSIntegerNode(step);
	}

	@Override
	public String toString() {
		String rstr = super.toString();
		if (start != null) {
			rstr += start.toString();
		}
		rstr = rstr + ":";
		if (end != null) {
			rstr += end.toString();
		}
		if (step != null) {
			rstr += ";" + step.toString();
		}
		if (startBoundExclusive) {
			rstr = "]" + rstr;
		} else {
			rstr = "[" + rstr;
		}
		if (endBoundExclusive) {
			rstr = rstr + "[";
		} else {
			rstr += "]";
		}
		return rstr;
	}

	@Override
	public int tryGetRangeSize() {
		int startValue = 0;
		int endValue = 0;
		int stepValue = 1;
		try {
			startValue = start.compileToHRAC().calculateNumericalValue();
		} catch (Exception e) {
			logger.warning("Could not calculate range size." + e);
		}
		try {
			endValue = end.compileToHRAC().calculateNumericalValue();
		} catch (Exception e) {
			logger.warning("Could not calculate range size." + e);
		}
		try {
			stepValue = step.compileToHRAC().calculateNumericalValue();
		} catch (Exception e) {
			logger.warning("Could not calculate range size." + e);
		}
		if (startBoundExclusive) {
			startValue += 1;
		}
		if (endBoundExclusive) {
			endValue -= 1;
		}
		return (endValue - startValue) / stepValue;
	}

}
