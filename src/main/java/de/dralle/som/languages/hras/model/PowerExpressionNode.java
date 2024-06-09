package de.dralle.som.languages.hras.model;

public class PowerExpressionNode extends DualChildExpressionNode implements Cloneable {

	public PowerExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PowerExpressionNode(AbstractExpressionNode child1, AbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PowerExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()*getChilds()[1].hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return (int) Math.pow(getChilds()[0].calculateNumericalValue(), getChilds()[1].calculateNumericalValue());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" ^ "+getChilds()[1].toString()+" )";
	}

}
