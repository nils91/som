package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASSingleChildExpressionNode;

public class HRACSingleChildExpressionNode extends HRACAbstractExpressionNode implements Cloneable {
	private HRACAbstractExpressionNode child;

	public HRACSingleChildExpressionNode(HRACAbstractExpressionNode child) {
		super();
		this.child = child;
	}

	public HRACSingleChildExpressionNode() {
		super();
	}

	public HRACAbstractExpressionNode getChild() {
		return child;
	}

	public void setChild(HRACAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public HRACSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRACSingleChildExpressionNode cl = (HRACSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}
	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChild().calculateNumericalValue();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	@Override
	public HRACAbstractExpressionNode resolve(HRACModel parent) {
		child=child.resolve(parent);
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACSingleChildExpressionNode) {
			HRACSingleChildExpressionNode oth = (HRACSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	@Override
	public String toString() {
		return child.toString();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASSingleChildExpressionNode(getChild().compileToHRAS(parent));
	}


}
