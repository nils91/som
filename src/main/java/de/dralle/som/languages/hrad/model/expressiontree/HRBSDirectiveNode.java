package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;

public class HRBSDirectiveNode extends HRBSAbstractExpressionNode implements Cloneable {
	private String directiveName;

	public HRBSDirectiveNode() {
		super();
	}

	public HRBSDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}

	@Override
	public HRBSDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRBSDirectiveNode) super.clone();
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACDirectiveNode(directiveName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSDirectiveNode) {
			HRBSDirectiveNode oth = (HRBSDirectiveNode) obj;
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
