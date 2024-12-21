package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;

public class HRACIntegerNode extends HRACAbstractExpressionNode implements Cloneable{
	@Override
	public HRACIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRACIntegerNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRACIntegerNode){
		HRACIntegerNode oth=(HRACIntegerNode) obj;
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
	public HRACIntegerNode(int value) {
		super();
		this.value = value;
	}
	public HRACIntegerNode() {
		super();
	}
	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASIntegerNode(value);
	}
}
