package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACModuloExpressionNode;

public class HRBSModuloExpressionNode extends HRBSDualChildExpressionNode implements Cloneable {

	public HRBSModuloExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSModuloExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HRACModuloExpressionNode compileToHRAC() {

		return new HRACModuloExpressionNode(getChilds()[0].compileToHRAC(), getChilds()[1].compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSModuloExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + getChilds()[0].hashCode() % getChilds()[1].hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( " + getChilds()[0].toString() + " % " + getChilds()[1].toString() + " )";
	}

}
