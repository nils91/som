package de.dralle.som.languages.hrac.model.expressiontree;

public class HRACModuloExpressionNode extends HRACDualChildExpressionNode implements Cloneable {

	public HRACModuloExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACModuloExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACModuloExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode()+getChilds()[0].hashCode()%getChilds()[1].hashCode();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChilds()[0].calculateNumericalValue()%getChilds()[1].calculateNumericalValue();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+getChilds()[0].toString()+" % "+getChilds()[1].toString()+" )";
	}

}
