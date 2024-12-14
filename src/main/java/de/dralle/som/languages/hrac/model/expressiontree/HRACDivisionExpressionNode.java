package de.dralle.som.languages.hrac.model.expressiontree;

public class HRACDivisionExpressionNode extends HRACDualChildExpressionNode implements Cloneable {

	public HRACDivisionExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACDivisionExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACDivisionExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()/getChilds()[1].hashCode();
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" / "+getChilds()[1].toString()+" )";
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue()/getChilds()[1].calculateNumericalValue();
	}

}
