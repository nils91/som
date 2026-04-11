package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACSingleChildExpressionNode;

public class HRADSingleChildExpressionNode extends HRADAbstractExpressionNode implements Cloneable {
	private HRADAbstractExpressionNode child;

	public HRADSingleChildExpressionNode() {
		super();
	}

	public HRADSingleChildExpressionNode(HRADAbstractExpressionNode child) {
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
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACSingleChildExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADSingleChildExpressionNode) {
			HRADSingleChildExpressionNode oth = (HRADSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	public HRADAbstractExpressionNode getChild() {
		return child;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	public void setChild(HRADAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return child.toString();
	}

}
