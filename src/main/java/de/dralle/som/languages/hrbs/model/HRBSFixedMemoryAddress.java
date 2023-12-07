package de.dralle.som.languages.hrbs.model;

public class HRBSFixedMemoryAddress extends AbstractHRBSMemoryAddress {
	@Override
	public int hashCode() {
		return super.hashCode()+address;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSFixedMemoryAddress) {
			return address==((HRBSFixedMemoryAddress)obj).address&&super.equals(obj);
		}
		return super.equals(obj);
	}

	@Override
	public HRBSFixedMemoryAddress clone() {
		// TODO Auto-generated method stub
		HRBSFixedMemoryAddress clone= (HRBSFixedMemoryAddress) super.clone();
		clone.address=address;
		return clone;
	}

	@Override
	public String asHRBSCode() {
		return getFirstPartHRBSCode()+"@"+address+getSecondPartHRBSCode();
	}

	private int address;

	public HRBSFixedMemoryAddress(int address) {
		super();
		this.address = address;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
}
