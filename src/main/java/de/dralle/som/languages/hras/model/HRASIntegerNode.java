package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class HRASIntegerNode extends HRASAbstractExpressionNode implements Cloneable{
	@Override
	public HRASIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRASIntegerNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRASIntegerNode){
		HRASIntegerNode oth=(HRASIntegerNode) obj;
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
	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return value;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public HRASIntegerNode(int value) {
		super();
		this.value = value;
	}
	public HRASIntegerNode() {
		super();
	}
	public HRASIntegerNode(HRASAbstractExpressionNode value) {
		super();
		this.value = value.calculateNumericalValue();
	}
	@Override
	public HRACAbstractExpressionNode compileToHRAC() {
		return new HRACIntegerNode(value);
	}
}
