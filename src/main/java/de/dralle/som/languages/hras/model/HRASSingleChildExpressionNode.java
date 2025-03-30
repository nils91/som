package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACSingleChildExpressionNode;

public class HRASSingleChildExpressionNode extends HRASAbstractExpressionNode implements Cloneable {
	private HRASAbstractExpressionNode child;

	public HRASSingleChildExpressionNode() {
		super();
	}

	public HRASSingleChildExpressionNode(HRASAbstractExpressionNode child) {
		super();
		this.child = child;
	}

	@Override
	public int calculateNumericalValue() {
		return child.calculateNumericalValue();
	}

	@Override
	public HRASSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRASSingleChildExpressionNode cl = (HRASSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}

	@Override
	public HRACSingleChildExpressionNode compileToHRAC() {
		return new HRACSingleChildExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRASSingleChildExpressionNode) {
			HRASSingleChildExpressionNode oth = (HRASSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	public HRASAbstractExpressionNode getChild() {
		return child;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	public void setChild(HRASAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return child.toString();
	}
}
