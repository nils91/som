package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.Collection;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASSingleChildExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSSingleChildExpressionNode;

public class HRADSingleChildExpressionNode extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private HRADAbstractDirectiveExpressionTreeNode child;

	public HRADSingleChildExpressionNode() {
		super();
	}

	public HRADSingleChildExpressionNode(HRADAbstractDirectiveExpressionTreeNode child) {
		super();
		this.child = child;
	}

	@Override
	public HRADSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRADSingleChildExpressionNode cl = (HRADSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}

	

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSSingleChildExpressionNode(child.compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADSingleChildExpressionNode) {
			HRADSingleChildExpressionNode oth = (HRADSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	public HRADAbstractDirectiveExpressionTreeNode getChild() {
		return child;
	}



	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	

	public void setChild(HRADAbstractDirectiveExpressionTreeNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return child.toString();
	}

}
