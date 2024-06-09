package de.dralle.som.languages.hras.model;

public class IntegerNode extends AbstractExpressionNode implements Cloneable{
	@Override
	public IntegerNode clone() {
		// TODO Auto-generated method stub
		return (IntegerNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IntegerNode){
		IntegerNode oth=(IntegerNode) obj;
		return value==oth.value;
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
	public IntegerNode(int value) {
		super();
		this.value = value;
	}
	public IntegerNode() {
		super();
	}
	public IntegerNode(AbstractExpressionNode value) {
		super();
		this.value = value.calculateNumericalValue();
	}
}
