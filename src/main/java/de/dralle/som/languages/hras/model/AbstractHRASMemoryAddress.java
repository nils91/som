/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public abstract class AbstractHRASMemoryAddress implements Cloneable{

	public AbstractHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}


	public Integer getAddressOffset() {
		return addressOffset;
	}

	public void setAddressOffset(Integer addressOffset) {
		this.addressOffset = addressOffset;
	}

	private Integer addressOffset;

	public int resolve(HRASModel model) {
		if(addressOffset!=null) {
			return addressOffset.intValue();
		}
		return 0;
	}
	@Override
	public int hashCode() {
		int hashc =0;
		if(addressOffset!=null) {
			hashc+=addressOffset.hashCode();
		}
		return hashc;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof AbstractHRASMemoryAddress) {
			if(addressOffset!=null) {
				return addressOffset.equals(((AbstractHRASMemoryAddress)obj).addressOffset);
			}else {
				return ((AbstractHRASMemoryAddress)obj).addressOffset==null;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRASCode();
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
		if(addressOffset!=null) { 
			copy.addressOffset=addressOffset.intValue();
		}		
		return copy;
	}

	public String asHRASCode() {
		if(addressOffset!=null) {
			return "["+addressOffset+"]";}
		return "";
	}

	
}
