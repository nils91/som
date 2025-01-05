package de.dralle.som.languages.hrbs.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRBSMemoryAddressOffset implements Cloneable {
	private HRBSAbstractExpressionNode offset;

	public HRBSMemoryAddressOffset() {
		this(0);
	}

	public HRBSMemoryAddressOffset(int offset) {
		this(new HRBSIntegerNode(offset));
	}

	public HRBSMemoryAddressOffset(HRBSAbstractExpressionNode offset) {
		this.offset = offset;
	}

	public HRBSMemoryAddressOffset(String directiveAccessName) {
		this(new HRBSDirectiveNode(directiveAccessName));
	}

	public HRBSAbstractExpressionNode getOffset() {
		return offset;
	}

	public void setOffset(HRBSAbstractExpressionNode offset) {
		this.offset = offset;
	}

	public String asCode() {
		String s = "[";
		{
			s += offset + "";
		}
		return s + "]";
	}

	@Override
	public int hashCode() {

		return offset.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSMemoryAddressOffset) {
			HRBSMemoryAddressOffset other = (HRBSMemoryAddressOffset) obj;
			return offset.equals(other.offset);
		}
		return super.equals(obj);
	}

	@Override
	protected HRBSMemoryAddressOffset clone() {
		// TODO Auto-generated method stub
		HRBSMemoryAddressOffset copy = null;
		try {
			copy = (HRBSMemoryAddressOffset) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		copy.offset = offset.clone();
		return copy;
	}

	@Override
	public String toString() {
		return asCode();
	}
}
