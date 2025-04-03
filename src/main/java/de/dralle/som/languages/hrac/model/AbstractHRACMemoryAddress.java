/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

/**
 * @author Nils
 *
 */
public class AbstractHRACMemoryAddress implements Cloneable {
	private HRACAbstractExpressionNode offset;

	protected AbstractHRACMemoryAddress() {

	}

	public String asHRACCode() {
		if (offset != null) {
			return String.format("[%s]", offset);
		} else {
			return "";
		}
	}

	@Override
	public AbstractHRACMemoryAddress clone() {
		AbstractHRACMemoryAddress copy = new AbstractHRACMemoryAddress();
		try {
			copy = (AbstractHRACMemoryAddress) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		if (offset != null) {
			copy.offset = offset.clone();
		}
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AbstractHRACMemoryAddress) {
			AbstractHRACMemoryAddress other = (AbstractHRACMemoryAddress) obj;
			boolean equal = true;
			if (equal && offset != null) {
				return offset.equals(((AbstractHRACMemoryAddress) obj).offset);
			}
			return super.equals(obj);
		}
		return false;
	}
	/**
	 * Resolve used directives, if any
	 * @param parent
	 */
	public void resolve(HRACModel parent) {
		resolve(parent, null);
	}

	public void resolve(HRACModel parent,String[] directives) {
		if(offset!=null) {
			if(directives!=null) {
				offset=offset.resolve(parent,directives);
			}else {
				offset=offset.resolve(parent);
			}
		}
	}
	public HRACAbstractExpressionNode getOffset() {
		return offset;
	}

	@Override
	public int hashCode() {
		int hashc = 0;
		if (offset != null) {
			hashc += offset.hashCode();
		}
		return hashc;
	}

	public void setOffset(HRACAbstractExpressionNode offset) {
		this.offset = offset;
	}

	public void setOffset(int offset) {
		this.offset = new HRACIntegerNode(offset);
	}

	@Override
	public String toString() {
		return asHRACCode();
	}
}
