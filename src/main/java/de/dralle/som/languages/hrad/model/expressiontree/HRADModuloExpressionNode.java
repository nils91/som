package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASModuloExpression;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSModuloExpressionNode;

public class HRADModuloExpressionNode extends HRADDualChildExpressionNode implements Cloneable {

	public HRADModuloExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADModuloExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}


	

	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADModuloExpressionNode) {
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
