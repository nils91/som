package de.dralle.som.languages.hrbs.model.expressiontree;

public class HRBSAbsoluteExpressionNode extends HRBSSingleChildExpressionNode implements Cloneable {

	public HRBSAbsoluteExpressionNode(HRBSAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSAbsoluteExpressionNode) {
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
