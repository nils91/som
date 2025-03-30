package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.Collection;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASSingleChildExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSSingleChildExpressionNode;

public class HRACSingleChildExpressionNode extends HRACAbstractExpressionNode implements Cloneable {
	private HRACAbstractExpressionNode child;

	public HRACSingleChildExpressionNode() {
		super();
	}

	public HRACSingleChildExpressionNode(HRACAbstractExpressionNode child) {
		super();
		this.child = child;
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getChild().calculateNumericalValue();
	}

	@Override
	public HRACSingleChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRACSingleChildExpressionNode cl = (HRACSingleChildExpressionNode) super.clone();
		cl.child = child.clone();
		return cl;
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASSingleChildExpressionNode(getChild().compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSSingleChildExpressionNode(child.compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACSingleChildExpressionNode) {
			HRACSingleChildExpressionNode oth = (HRACSingleChildExpressionNode) obj;
			return child.equals(oth.child);
		}
		return false;
	}

	public HRACAbstractExpressionNode getChild() {
		return child;
	}

	@Override
	public Collection<String> getUsedDirectives() {
		return child.getUsedDirectives();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return child.hashCode();
	}

	@Override
	public HRACAbstractExpressionNode resolve(HRACModel parent) {
		child = child.resolve(parent);
		return this;
	}

	public void setChild(HRACAbstractExpressionNode child) {
		this.child = child;
	}

	@Override
	public String toString() {
		return child.toString();
	}

}
