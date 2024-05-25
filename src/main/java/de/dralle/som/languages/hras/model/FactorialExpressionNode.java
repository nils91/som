package de.dralle.som.languages.hras.model;

public abstract class FactorialExpressionNode extends SingleChildExpressionNode implements Cloneable {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FactorialExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

private int getFac(int n) {
	if(n==1) {
		return n;
	}
	return n*getFac(n-1);
}
	
	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getFac(getChild().calculateNumericalValue());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getChild().toString()+"!";
	}

}
