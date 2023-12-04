package de.dralle.som.languages.hrac.model;

public class FixedHRACMemoryAddress extends AbstractHRACMemoryAddress {
	private int address;	
	
	public FixedHRACMemoryAddress(int address) {
		super();
		this.address = address;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public FixedHRACMemoryAddress() {
		super();
	}
	@Override
	public int hashCode() {
		return address+ super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FixedHRACMemoryAddress) {
			FixedHRACMemoryAddress other = (FixedHRACMemoryAddress)obj;
			boolean equaL=address==other.address;
			return equaL&&super.equals(obj);
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public FixedHRACMemoryAddress clone() {
		FixedHRACMemoryAddress copy=(FixedHRACMemoryAddress) super.clone();
		copy.address=address;
		return copy;
	}

	@Override
	public String asHRACCode() {
		return "@"+address+super.asHRACCode();
	}
}
