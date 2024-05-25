package de.dralle.som.languages.hras.model;

public abstract class MultiplicationExpression extends CommutativeDualChildExpressionNode implements Cloneable {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiplicationExpression) {
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
		return getChilds()[0].calculateNumericalValue()*getChilds()[1].calculateNumericalValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" * "+getChilds()[1].toString()+" )";
	}

}
