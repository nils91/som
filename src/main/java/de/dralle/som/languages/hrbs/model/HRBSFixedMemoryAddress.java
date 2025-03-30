package de.dralle.som.languages.hrbs.model;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRBSFixedMemoryAddress extends AbstractHRBSMemoryAddress {
	private HRBSAbstractExpressionNode address;

	public HRBSFixedMemoryAddress(HRBSAbstractExpressionNode hrbsAbstractExpressionNode) {
		super();
		this.address = hrbsAbstractExpressionNode;
	}

	public HRBSFixedMemoryAddress(int address) {
		super();
		this.address = new HRBSIntegerNode(address);
	}

	@Override
	public String asHRBSCode() {
		return getFirstPartHRBSCode() + "@(" + address + ")" + getSecondPartHRBSCode();
	}

	@Override
	public HRBSFixedMemoryAddress clone() {
		// TODO Auto-generated method stub
		HRBSFixedMemoryAddress clone = (HRBSFixedMemoryAddress) super.clone();
		clone.address = address;
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSFixedMemoryAddress) {
			return address == ((HRBSFixedMemoryAddress) obj).address && super.equals(obj);
		}
		return super.equals(obj);
	}

	public HRBSAbstractExpressionNode getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + address.hashCode();
	}

	public void setAddress(HRBSAbstractExpressionNode address) {
		this.address = (address);
	}

	public void setAddress(int address) {
		this.address = new HRBSIntegerNode(address);
	}
}
