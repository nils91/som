package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class FixedHRACMemoryAddress extends AbstractHRACMemoryAddress {
	private HRACAbstractExpressionNode address;

	public FixedHRACMemoryAddress() {
		super();
	}

	public FixedHRACMemoryAddress(HRACAbstractExpressionNode accept) {
		super();
		this.address = accept;
	}

	public FixedHRACMemoryAddress(int address) {
		super();
		this.address = new HRACIntegerNode(address);
	}

	@Override
	public String asHRACCode() {
		return "@" + address + super.asHRACCode();
	}

	@Override
	public FixedHRACMemoryAddress clone() {
		FixedHRACMemoryAddress copy = (FixedHRACMemoryAddress) super.clone();
		copy.address = address.clone();
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FixedHRACMemoryAddress) {
			FixedHRACMemoryAddress other = (FixedHRACMemoryAddress) obj;
			boolean equaL = address.equals(other.address);
			return equaL && super.equals(obj);
		}
		return false;
	}

	public HRACAbstractExpressionNode getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return address.hashCode() + super.hashCode();
	}

	public void setAddress(HRACAbstractExpressionNode address) {
		this.address = address;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
