package de.dralle.som.languages.hrbs.model;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRBSBoundsRange extends AbstractHRBSRange implements Cloneable{
	private HRBSAbstractExpressionNode start;
	private HRBSAbstractExpressionNode end;
	private HRBSAbstractExpressionNode step;
	public HRBSAbstractExpressionNode getStep() {
		return step;
	}
	public void setStep(HRBSAbstractExpressionNode step) {
		this.step = step;
	}
	public void setStep(int step) {
		this.step = new HRBSIntegerNode(step);
	}
	public boolean isStartBoundExclusive() {
		return startBoundExclusive;
	}
	public void setStartBoundExclusive(boolean startBoundExclusive) {
		this.startBoundExclusive = startBoundExclusive;
	}
	public boolean isEndBoundExclusive() {
		return endBoundExclusive;
	}
	public void setEndBoundExclusive(boolean endBoundExclusive) {
		this.endBoundExclusive = endBoundExclusive;
	}
	private boolean startBoundExclusive=false;
	private boolean endBoundExclusive=false;
	public HRBSAbstractExpressionNode getStart() {
		return start;
	}
	public void setStart(HRBSAbstractExpressionNode start) {
		this.start = start;
	}
	public void setStart(int start) {
		this.start = new HRBSIntegerNode(start);
	}
	public void setEnd(int end) {
		this.end = new HRBSIntegerNode(end);
	}
	public HRBSAbstractExpressionNode getEnd() {
		return end;
	}
	@Override
	public int hashCode() {
		int n = super.hashCode();
		if(start!=null) {
			n+=start.hashCode();
		}
		if(end!=null) {
			n*=end.hashCode();
		}
		if(step!=null) {
			n/=step.hashCode();
		}
		if(startBoundExclusive) {
			n-=31;
		}
		if(endBoundExclusive) {
			n-=97;
		}
		return n;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSBoundsRange) {
			boolean eq=super.equals(obj);
			HRBSBoundsRange other=(HRBSBoundsRange) obj;
			if(eq&&start!=null) {
				eq= start.equals(other.getStart());
			}
			if(eq&&end!=null) {
				eq=end.equals(other.end);
			}
			if(eq&&step!=null) {
				eq=step.equals(other.step);
			}
			return eq;
		}
		return super.equals(obj);
	}
	@Override
	public HRBSBoundsRange clone()  {
		HRBSBoundsRange c = null;
		c = (HRBSBoundsRange) super.clone();
		if(start!=null) {
			c.start=start.clone();
		}
		if(end!=null) {
			c.end=end.clone();
		}
		if(step!=null) {
			c.step=step.clone();
		}
		return c;
	}
	@Override
	public String toString() {
		String rstr = super.toString();
		if(start!=null) {
			rstr+=start.toString();
		}
		rstr=rstr+":";
		if(end!=null) {
			rstr+=end.toString();
		}
		if(step!=null) {
			rstr+=";"+step.toString();
		}
		if(startBoundExclusive) {
			rstr="]"+rstr;
		}else {
			rstr="["+rstr;
		}
		if(endBoundExclusive) {
			rstr=rstr+"[";
		}else {
			rstr+="]";
		}
		return rstr;
	}
	public void setEnd(HRBSAbstractExpressionNode end) {
		this.end = end;
	}

}
