package de.dralle.som.languages.hras.model;

public abstract class AbsoluteExpressionNode extends SingleChildExpressionNode implements Cloneable {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbsoluteExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		return Math.abs(getChild().calculateNumericalValue());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "| "+getChild().toString()+" |";
	}

}
