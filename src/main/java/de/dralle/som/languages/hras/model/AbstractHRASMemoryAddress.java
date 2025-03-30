/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public abstract class AbstractHRASMemoryAddress implements Cloneable {

	private HRASAbstractExpressionNode addressOffset;

	public AbstractHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public String asHRASCode() {
		if (addressOffset != null) {
			return "[" + addressOffset + "]";
		}
		return "";
	}

	@Override
	public AbstractHRASMemoryAddress clone() {
		AbstractHRASMemoryAddress copy = null;
		try {
			copy = (AbstractHRASMemoryAddress) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (addressOffset != null) {
			copy.addressOffset = addressOffset.clone();
		}
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AbstractHRASMemoryAddress) {
			if (addressOffset != null) {
				return addressOffset.equals(((AbstractHRASMemoryAddress) obj).addressOffset);
			} else {
				return ((AbstractHRASMemoryAddress) obj).addressOffset == null;
			}
		}
		return false;
	}

	public HRASAbstractExpressionNode getAddressOffset() {
		return addressOffset;
	}

	@Override
	public int hashCode() {
		int hashc = 0;
		if (addressOffset != null) {
			hashc += addressOffset.hashCode();
		}
		return hashc;
	}

	public int resolve(HRASModel model) {
		if (addressOffset != null) {
			return addressOffset.calculateNumericalValue();
		}
		return 0;
	}

	public void setAddressOffset(HRASAbstractExpressionNode addressOffset) {
		if (addressOffset == null) {
			addressOffset = new HRASIntegerNode(0);
		}
		this.addressOffset = addressOffset;
	}

	public void setAddressOffset(Integer addressOffset) {
		if (addressOffset == null) {
			addressOffset = 0;
		}
		this.addressOffset = new HRASIntegerNode(addressOffset);
	}

	@Override
	public String toString() {
		return asHRASCode();
	}

}
