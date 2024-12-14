package de.dralle.som.languages.hras.model;

public abstract class HRASSingleChildExpressionNode extends HRASAbstractExpressionNode implements Cloneable{
	private HRASAbstractExpressionNode child;

	public HRASSingleChildExpressionNode(HRASAbstractExpressionNode child) {
		super();
		this.child = child;
	}
	public HRASSingleChildExpressionNode() {
		super();
	}

	public HRASAbstractExpressionNode getChild() {
		return child;
	}

	public void setChild(HRASAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public HRASSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRASSingleChildExpressionNode cl= (HRASSingleChildExpressionNode) super.clone();
		cl.child=child.clone();
		return cl;
	}

	@Override
	public int calculateNumericalValue() {
		return child.calculateNumericalValue();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRASSingleChildExpressionNode) {
			HRASSingleChildExpressionNode oth = (HRASSingleChildExpressionNode)obj;
			return child.equals(oth.child);
		}
		return false;
	}

	@Override
	public String toString() {
		return child.toString();
	}
}
