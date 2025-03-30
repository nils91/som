/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

/**
 * @author Nils
 *
 */
public class HRBSSymbol implements Cloneable {
	private String name;

	/**
	 * Potential target symbol. Might be null.
	 */
	private AbstractHRBSMemoryAddress targetSymbol;

	private HRBSSymbolType type;

	private HRBSAbstractExpressionNode bitCnt;

	public HRBSSymbol() {
	}

	public HRBSSymbol(String name) {
		this.name = name;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		if (type != null) {
			sb.append(type + " ");
		}
		sb.append(name);
		if (bitCnt != null) {
			sb.append(String.format("[%s]", bitCnt));
		}

		if (targetSymbol != null) {
			sb.append(String.format(" %s", targetSymbol.asHRBSCode()));
		}
		return sb.toString();
	}

	@Override
	public HRBSSymbol clone() {
		HRBSSymbol clone = new HRBSSymbol();
		clone.setName(name);
		clone.setBitCnt(bitCnt != null ? bitCnt.clone() : null);
		clone.setType(type);
		if (targetSymbol != null) {
			clone.setTargetSymbol(targetSymbol.clone());
		}
		return clone;
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
			if (bitCnt == null) {
				equals &= bitCnt == other.bitCnt;
			} else {
				equals &= bitCnt.equals(other.bitCnt);
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

	public HRBSAbstractExpressionNode getBitCnt() {
		return bitCnt;
	}

	public String getName() {
		return name;
	}

	public AbstractHRBSMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}

	public HRBSSymbolType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int hashc = bitCnt != null ? bitCnt.hashCode() : 0;
		if (name != null) {
			hashc += name.hashCode();
		}
		hashc += type.toString().hashCode();
		if (targetSymbol != null) {
			hashc += targetSymbol.hashCode() * 97;
		}
		return hashc;
	}

	public void setBitCnt(HRBSAbstractExpressionNode hrbsAbstractExpressionNode) {
		bitCnt = hrbsAbstractExpressionNode;
	}

	public void setBitCnt(int bitCnt) {
		this.bitCnt = new HRBSIntegerNode(bitCnt);
	}

	public void setBitCntDirective(String text) {
		bitCnt = new HRBSDirectiveNode(text);

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetSymbol(AbstractHRBSMemoryAddress mirrorSymbol) {
		this.targetSymbol = mirrorSymbol;
	}

	public void setType(HRBSSymbolType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return asCode();
	}

}
