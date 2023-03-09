package de.dralle.som.languages.hrbs.model;

public class HRBSMemoryAddressOffset implements Cloneable {
	private int offset;
	public HRBSMemoryAddressOffset() {
		this(0);
	}
	public HRBSMemoryAddressOffset(int offset) {
		this(offset,null);
	}
	public HRBSMemoryAddressOffset(int offset, String directiveAccessName) {
		super();
		this.offset = offset;
		this.directiveAccessName = directiveAccessName;
	}public HRBSMemoryAddressOffset(String directiveAccessName) {
		this(0,directiveAccessName);
	}

	private String directiveAccessName;
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getDirectiveAccessName() {
		return directiveAccessName;
	}

	public void setDirectiveAccessName(String directiveAccessName) {
		this.directiveAccessName = directiveAccessName;
	}

	public String asCode() {
		String s = "[";
		if(directiveAccessName!=null) {
			s+=String.format("$%s", directiveAccessName);
		}else {
			s+=offset+"";
		}
		return s+"]";
	}

	@Override
	public int hashCode() {
		if(directiveAccessName!=null) {
			return directiveAccessName.hashCode();
		}
		return offset;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSMemoryAddressOffset) {
			HRBSMemoryAddressOffset other=(HRBSMemoryAddressOffset) obj;
			if(directiveAccessName!=null) {
				return directiveAccessName.equals(other.directiveAccessName);
			}
			return offset==other.offset;
		}
		return super.equals(obj);
	}

	@Override
	protected HRBSMemoryAddressOffset clone()  {
		// TODO Auto-generated method stub
		HRBSMemoryAddressOffset copy = null;
		try {
			copy = (HRBSMemoryAddressOffset) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return copy;
	}

	@Override
	public String toString() {
		return asCode();
	}
}
