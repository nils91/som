/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveTreeCalculateIntegerValueVisitor;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACResolveDirectiveTreeVisitor;

/**
 * @author Nils
 *
 */
public class HRACSymbol implements Cloneable {
	private String name;
	/**
	 * Op means 'overwrite parent'. Controls wether this symbol should overwrite a
	 * symbol of the same name in the parent scope when merging
	 */
	private boolean op = false;
	/**
	 * Potential target symbol. Might be null.
	 */
	private AbstractHRACMemoryAddress targetSymbol;
	private HRACAbstractDirectiveExpressionTreeNode bitCnt;

	public HRACSymbol() {
		// TODO Auto-generated constructor stub
	}

	public HRACSymbol(String generateHRACSymbolName) {
		name = generateHRACSymbolName;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		if (op) {
			sb.append("op ");
		}
		if (targetSymbol != null) {
			sb.append("symbol ");
		} else {
			sb.append("alloc ");
		}
		sb.append(name);

		if (bitCnt == null) {

		} else {
			sb.append(String.format("[%s]", bitCnt.toString()));
		}

		if (targetSymbol != null) {
			sb.append(String.format(" %s", targetSymbol));
		}
		sb.append(";");
		return sb.toString();
	}

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
		if (bitCnt != null) {
			clone.bitCnt = bitCnt.clone();
		}
		clone.op = op;
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACSymbol) {
			HRACSymbol oth = (HRACSymbol) obj;
			boolean equals = equalsName(oth);
			if (equals) {
				equals = bitCnt == oth.bitCnt || bitCnt.equals(oth.bitCnt);
			}
			if (equals) {
				equals = targetSymbol == oth.targetSymbol || targetSymbol.equals(oth.targetSymbol);
			}
			if (equals) {
				equals = op == oth.op;
			}
			return equals;
		}
		return super.equals(obj);
	}

	public boolean equalsName(HRACSymbol oth) {
		return name == oth.name || name.equals(oth.name);
	}

	public HRACAbstractDirectiveExpressionTreeNode getBitCnt() {
		return bitCnt;
	}

	/**
	 * Shortcut to get the bitcnt as int.
	 * 
	 * @param model
	 * @return
	 */
	public int getBitCntAsInt(HRACModel model) {
		return bitCnt.accept(new HRACResolveDirectiveTreeVisitor(model, true)).accept(new HRACDirectiveTreeCalculateIntegerValueVisitor()).intValue();
	}

	public String getName() {
		return name;
	}

	public AbstractHRACMemoryAddress getTargetSymbol() {
		return targetSymbol;
	}

	public void setBitCnt(HRACAbstractDirectiveExpressionTreeNode bitCnt) {
		this.bitCnt = bitCnt;
	}

	public void setBitCnt(int bitCnt) {
		this.bitCnt = new HRACIntegerNode(bitCnt);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTargetSymbol(AbstractHRACMemoryAddress mirrorSymbol) {
		this.targetSymbol = mirrorSymbol;
	}

	@Override
	public String toString() {
		return asCode();
	}

	public boolean isOp() {
		return op;
	}

	public void setOp(boolean op) {
		this.op = op;
	}
}
