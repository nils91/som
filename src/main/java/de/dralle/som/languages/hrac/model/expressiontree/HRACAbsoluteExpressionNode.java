package de.dralle.som.languages.hrac.model.expressiontree;

public class HRACAbsoluteExpressionNode extends HRACSingleChildExpressionNode implements Cloneable {

	public HRACAbsoluteExpressionNode(HRACAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACAbsoluteExpressionNode) {
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
		return "| "+getChild().toString()+" |";
	}

}
