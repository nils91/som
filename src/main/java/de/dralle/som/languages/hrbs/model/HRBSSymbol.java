/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

/**
 * @author Nils
 *
 */
public class HRBSSymbol implements Cloneable {
	public HRBSSymbol(String name) {
		this.name = name;
	}

	public HRBSSymbol() {
	}

	@Override
	public int hashCode() {
		int hashc = bitCnt;
		if (name != null) {
			hashc += name.hashCode();
		}
		if (bitCntSpecialName != null) {
			hashc += bitCntSpecialName.hashCode() * 255;
		}
		hashc += type.hashCode();
		if (targetSymbol != null) {
			hashc += targetSymbol.hashCode() * 97;
		}
		return hashc;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSSymbol) {
			boolean equals;
			HRBSSymbol other = (HRBSSymbol) obj;
			if (name == null) {
				equals = name == other.name;
			} else {
				equals = name.equals(other.name);
			}
			if (bitCntSpecialName != null) {
				equals = equals && bitCntSpecialName.equals(other.bitCntSpecialName);
			} else {
				equals = equals && bitCntSpecialName == other.bitCntSpecialName;
				equals = equals && bitCnt == other.bitCnt;
			}
			equals = equals && type.equals(other.type);
			if (targetSymbol != null) {
				equals = equals && targetSymbol.equals(other.targetSymbol);
			} else {
				equals = equals && targetSymbol == other.targetSymbol;
			}
			return equals;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	public HRBSSymbol clone() {
		HRBSSymbol clone = new HRBSSymbol();
		clone.setName(name);
		clone.setBitCnt(bitCnt);
		clone.setBitCntISSpecial(bitCntSpecialName);
		clone.setType(type);
		if (targetSymbol != null) {
			clone.setTargetSymbol(targetSymbol.clone());
		}
		return clone;
	}

	private String name;
	/**
	 * Potential target symbol. Might be null.
	 */
	private AbstractHRBSMemoryAddress targetSymbol;
	private HRBSSymbolType type;
	private int bitCnt;
	private String bitCntSpecialName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AbstractHRBSMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}

	public void setTargetSymbol(AbstractHRBSMemoryAddress mirrorSymbol) {
		this.targetSymbol = mirrorSymbol;
	}

	public int getBitCnt() {
		return bitCnt;
	}

	public void setBitCnt(int bitCnt) {
		this.bitCnt = bitCnt;
	}

	public String isBitCntISSpecial() {
		return bitCntSpecialName;
	}

	public void setBitCntISSpecial(String bitCntISN) {
		this.bitCntSpecialName = bitCntISN;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		if (type != null) {
			sb.append(type + " ");
		}
		sb.append(name);
		if (bitCntSpecialName != null) {
			sb.append(String.format("[$%s]", bitCntSpecialName));
		} else {
			sb.append(String.format("[%d]", bitCnt));
		}
		if (targetSymbol != null) {
			sb.append(String.format(" %s", targetSymbol.asHRBSCode()));
		}
		return sb.toString();
	}

	public HRBSSymbolType getType() {
		return type;
	}

	public void setType(HRBSSymbolType type) {
		this.type = type;
	}
}
