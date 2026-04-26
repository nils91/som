package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public abstract class HRADAbstractDirectiveNode<T> extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private T directiveName;
	private List<HRADAbstractDirectiveExpressionTreeNode> paramValues;

	public List<HRADAbstractDirectiveExpressionTreeNode> getParamValues() {
		return paramValues;
	}

	public void setParamValues(List<HRADAbstractDirectiveExpressionTreeNode> paramValues) {
		this.paramValues = paramValues;
	}

	public HRADAbstractDirectiveNode() {
		super();
	}

	public HRADAbstractDirectiveNode(T directiveName) {
		super();
		this.directiveName = directiveName;
	}

	@Override
	public HRADAbstractDirectiveNode<T> clone() {
		HRADAbstractDirectiveNode<T> clone = (HRADAbstractDirectiveNode<T>) super.clone();
		if(paramValues!=null) {
			clone.paramValues=new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
			for (HRADAbstractDirectiveExpressionTreeNode hradAbstractDirectiveExpressionTreeNode : paramValues) {
				clone.paramValues.add(hradAbstractDirectiveExpressionTreeNode.clone());
			}
		}
		return clone;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbstractDirectiveNode) {
			HRADAbstractDirectiveNode oth = (HRADAbstractDirectiveNode) obj;
			return directiveName.equals(oth.directiveName);
		}
		return false;
	}

	public T getDirectiveName() {
		return directiveName;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return directiveName.hashCode();
	}

	public void setDirectiveName(T directiveName) {
		this.directiveName = directiveName;
	}

	@Override
	public String toString() {
		return "$" + directiveName + "";
	}
}
