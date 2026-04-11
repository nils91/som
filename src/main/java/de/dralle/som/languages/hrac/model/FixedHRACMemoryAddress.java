package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACResolveDirectiveTreeVisitor;

public class FixedHRACMemoryAddress extends AbstractHRACMemoryAddress {
	private HRACAbstractDirectiveExpressionTreeNode address;

	public FixedHRACMemoryAddress() {
		super();
	}

	public FixedHRACMemoryAddress(HRACAbstractDirectiveExpressionTreeNode accept) {
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
	public void resolve(HRACModel parent, String[] directives) {
		super.resolve(parent, directives);
		if(directives!=null) {
			address=address.accept(new HRACResolveDirectiveTreeVisitor(parent, directives, false));
		}else {
			address=address.accept(new HRACResolveDirectiveTreeVisitor(parent));
		}
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

	public HRACAbstractDirectiveExpressionTreeNode getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return address.hashCode() + super.hashCode();
	}

	public void setAddress(HRACAbstractDirectiveExpressionTreeNode address) {
		this.address = address;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
