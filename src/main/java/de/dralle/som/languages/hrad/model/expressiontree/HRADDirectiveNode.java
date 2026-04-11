package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;

public class HRADDirectiveNode extends HRADAbstractExpressionNode implements Cloneable {
	private String directiveName;

	public HRADDirectiveNode() {
		super();
	}

	public HRADDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}

	@Override
	public HRADDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRADDirectiveNode) super.clone();
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACDirectiveNode(directiveName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADDirectiveNode) {
			HRADDirectiveNode oth = (HRADDirectiveNode) obj;
			return directiveName.equals(oth.directiveName);
		}
		return false;
	}

	public String getDirectiveName() {
		return directiveName;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return directiveName.hashCode();
	}

	public void setdirectiveName(String directiveName) {
		this.directiveName = directiveName;
	}

	@Override
	public String toString() {
		return "$" + directiveName + "";
	}
}
