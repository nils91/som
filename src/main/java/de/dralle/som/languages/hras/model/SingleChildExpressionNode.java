package de.dralle.som.languages.hras.model;

public abstract class SingleChildExpressionNode extends AbstractExpressionNode implements Cloneable{
	private AbstractExpressionNode child;

	public AbstractExpressionNode getChild() {
		return child;
	}

	public void setChild(AbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public SingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		SingleChildExpressionNode cl= (SingleChildExpressionNode) super.clone();
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
		if(obj instanceof SingleChildExpressionNode) {
			SingleChildExpressionNode oth = (SingleChildExpressionNode)obj;
			return child.equals(oth.child);
		}
		return false;
	}

	@Override
	public String toString() {
		return child.toString();
	}
}
