package de.dralle.som.languages.hrad.model.expressiontree;

public class HRADAbsoluteExpressionNode extends HRADSingleChildExpressionNode implements Cloneable {

	public HRADAbsoluteExpressionNode(HRADAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbsoluteExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "| " + getChild().toString() + " |";
	}

}
