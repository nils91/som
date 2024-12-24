package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hrbs.model.HRBSModel;

public class HRBSIntegerNode extends HRBSAbstractExpressionNode implements Cloneable{
	@Override
	public HRBSIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRBSIntegerNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSIntegerNode){
		HRBSIntegerNode oth=(HRBSIntegerNode) obj;
		return value==oth.value;
		}
		if(obj instanceof Integer) {
			Integer oth = (Integer)obj;
			return value==oth.intValue();
		}
		return false;
	}
	@Override
	public String toString() {
		return value+"";
	}
	private int value;

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public HRBSIntegerNode(int value) {
		super();
		this.value = value;
	}
	public HRBSIntegerNode() {
		super();
	}
	@Override
	public HRACAbstractExpressionNode compileToHRAC() {
		return new HRACIntegerNode(value);
	}
}
