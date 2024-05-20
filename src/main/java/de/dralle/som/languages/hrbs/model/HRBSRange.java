package de.dralle.som.languages.hrbs.model;

public class HRBSRange implements Cloneable{
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
		int n = 0;
		if(start!=null) {
			n+=start.hashCode();
		}
		if(end!=null) {
			n*=end.hashCode();
		}
		return n;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSRange) {
			boolean eq=true;
			HRBSRange other=(HRBSRange) obj;
			if(eq&&start!=null) {
				eq= start.equals(other.getStart());
			}
			if(eq&&end!=null) {
				eq=end.equals(other.end);
			}
			return eq;
		}
		return super.equals(obj);
	}
	@Override
	public HRBSRange clone()  {
		HRBSRange c = null;
		try {
			c = (HRBSRange) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(start!=null) {
			c.start=start.clone();
		}
		if(end!=null) {
			c.end=end.clone();
		}
		return c;
	}
	@Override
	public String toString() {
		String rstr = "";
		if(start!=null) {
			rstr+=start.toString();
		}
		rstr=rstr.substring(0, rstr.length()-1)+":";
		if(end!=null) {
			rstr+=end.toString().substring(1);
		}
		return rstr;
	}
	public void setEnd(HRBSMemoryAddressOffset end) {
		this.end = end;
	}

}
