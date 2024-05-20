package de.dralle.som.languages.hrbs.model;

public class HRBSBoundsRange extends AbstractHRBSRange implements Cloneable{
	private HRBSMemoryAddressOffset start;
	private HRBSMemoryAddressOffset end;
	private HRBSMemoryAddressOffset step;
	public HRBSMemoryAddressOffset getStep() {
		return step;
	}
	public void setStep(HRBSMemoryAddressOffset step) {
		this.step = step;
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
	public HRBSMemoryAddressOffset getStart() {
		return start;
	}
	public void setStart(HRBSMemoryAddressOffset start) {
		this.start = start;
	}
	public HRBSMemoryAddressOffset getEnd() {
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
		rstr=rstr.substring(0, rstr.length()-1)+":";
		if(end!=null) {
			rstr+=end.toString().substring(1);
		}
		if(step!=null) {
			rstr=rstr.substring(0, rstr.length()-1)+";";
			rstr+=";"+step.toString().substring(1);
		}
		if(startBoundExclusive) {
			rstr="]"+rstr.substring(1);
		}
		if(endBoundExclusive) {
			rstr=rstr.substring(0, rstr.length()-1)+"[";
		}
		return rstr;
	}
	public void setEnd(HRBSMemoryAddressOffset end) {
		this.end = end;
	}

}
