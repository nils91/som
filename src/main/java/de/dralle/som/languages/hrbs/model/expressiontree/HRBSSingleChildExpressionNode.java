package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACSingleChildExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASSingleChildExpressionNode;

public class HRBSSingleChildExpressionNode extends HRBSAbstractExpressionNode implements Cloneable {
	private HRBSAbstractExpressionNode child;

	public HRBSSingleChildExpressionNode(HRBSAbstractExpressionNode child) {
		super();
		this.child = child;
	}

	public HRBSSingleChildExpressionNode() {
		super();
	}

	public HRBSAbstractExpressionNode getChild() {
		return child;
	}

	public void setChild(HRBSAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public HRBSSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRBSSingleChildExpressionNode cl = (HRBSSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSSingleChildExpressionNode) {
			HRBSSingleChildExpressionNode oth = (HRBSSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	@Override
	public String toString() {
		return child.toString();
	}

	@Override
	public HRACAbstractExpressionNode compileToHRAC() {
		return new HRACSingleChildExpressionNode(getChild().compileToHRAC());
	}


}
