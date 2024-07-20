package de.dralle.som.languages.hrac.model.expressiontree;

public class FactorialExpressionNode extends SingleChildExpressionNode implements Cloneable {

	public FactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FactorialExpressionNode(AbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FactorialExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getChild().toString()+"!";
	}

}
