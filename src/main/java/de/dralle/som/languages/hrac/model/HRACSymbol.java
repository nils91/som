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
public class HRACSymbol implements Cloneable {
	@Override
	public HRACSymbol clone() {
		HRACSymbol clone;
		try {
			clone = (HRACSymbol) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			clone = new HRACSymbol();
		}
		if (targetSymbol != null) {
			clone.targetSymbol = targetSymbol.clone();
		}
		return clone;
	}

	private String name;
	/**
	 * Potential target symbol. Might be null.
	 */
	private AbstractHRACMemoryAddress targetSymbol;
	private HRACAbstractExpressionNode bitCnt;

@Deprecated
/**
 * bitCnt is now a type which can be a directive (directiveNode in expression tree) so this class is no longer required to differentiate between int and directive
 * @param bitCntISN
 */
	private boolean bitCntSpecial;
	/**
	 * If bitCnt is special, contains the special (directive) name.
	 */
	@Deprecated
	/**
	 * bitCnt is now a type which can be a directive (directiveNode in expression tree) so this class is no longer required to differentiate between int and directive
	 * @param bitCntISN
	 */
	private String specialName;

@Deprecated
/**
 * bitCnt is now a type which can be a directive (directiveNode in expression tree) so this class is no longer required to differentiate between int and directive
 * @param bitCntISN
 */
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public HRACSymbol(String generateHRACSymbolName) {
		name = generateHRACSymbolName;
	}

	public HRACSymbol() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AbstractHRACMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}

	public void setTargetSymbol(AbstractHRACMemoryAddress mirrorSymbol) {
		this.targetSymbol = mirrorSymbol;
	}
	@Deprecated
	public int getBitCntAsInt() {
		return bitCnt.calculateNumericalValue();
	}
	public HRACAbstractExpressionNode getBitCnt() {
		return bitCnt;
	}

	public void setBitCnt(int bitCnt) {
		this.bitCnt = new HRACIntegerNode(bitCnt);
	}
	public void setBitCnt(HRACAbstractExpressionNode bitCnt) {
		this.bitCnt = bitCnt;
	}
@Deprecated
/**
 * bitCnt is now a type which can be a directive (directiveNode in expression tree) so this class is no longer required to differentiate between int and directive
 * @param bitCntISN
 */
	public boolean isBitCntSpecial() {
		return bitCntSpecial;
	}
@Deprecated
/**
 * bitCnt is now a type which can be a directive (directiveNode in expression tree) so this class is no longer required to differentiate between int and directive
 * @param bitCntISN
 */
	public void setBitCntSpecial(boolean bitCntISN) {
		this.bitCntSpecial = bitCntISN;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		if (targetSymbol != null) {
			sb.append("symbol ");
		} else {
			sb.append("alloc ");
		}
		sb.append(name);
		if (bitCntSpecial) {
			sb.append(String.format("[$%s]", specialName));
		} else {
			if(bitCnt==null) {
				
			}else {
				sb.append(String.format("[%s]", bitCnt.toString()));
			}
		}
		if (targetSymbol != null) {
			sb.append(String.format(" %s", targetSymbol));
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRACSymbol) {
			HRACSymbol oth = (HRACSymbol)obj;
			boolean equals = name==oth.name||name.equals(oth.name);
			if(equals) {
				equals=bitCnt==oth.bitCnt||bitCnt.equals(oth.bitCnt);
			}
			if(equals) {
				equals=targetSymbol==oth.targetSymbol||targetSymbol.equals(oth.targetSymbol)
;			}
			return equals;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
	return asCode();
	}

	public String getSpecialName() {
		return specialName;
	}
}
