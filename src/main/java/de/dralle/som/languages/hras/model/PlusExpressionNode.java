package de.dralle.som.languages.hras.model;

public class PlusExpressionNode extends CommutativeDualChildExpressionNode implements Cloneable {

	public PlusExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlusExpressionNode(HRASAbstractExpressionNode child1, HRASAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue() + getChilds()[1].calculateNumericalValue();
	}

	@Override
	public de.dralle.som.languages.hrac.model.expressiontree.HRACPlusExpressionNode compileToHRAC() {
		return new de.dralle.som.languages.hrac.model.expressiontree.HRACPlusExpressionNode(
				getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PlusExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() + getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " + " + getChilds()[1].toString() + " )";
	}

}
