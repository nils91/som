package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrad.model.HRADModel;

public abstract class HRADDualChildExpressionNode extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private HRADAbstractDirectiveExpressionTreeNode[] childs = new HRADAbstractDirectiveExpressionTreeNode[2];

	public HRADDualChildExpressionNode() {
		super();
	}

	public HRADDualChildExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super();
		this.childs = new HRADAbstractDirectiveExpressionTreeNode[] { child1, child2 };
	}

	public HRADDualChildExpressionNode(HRADAbstractDirectiveExpressionTreeNode[] childs) {
		super();
		this.childs = childs;
	}

	@Override
	public HRADDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRADDualChildExpressionNode cl = (HRADDualChildExpressionNode) super.clone();
		cl.childs=new HRADAbstractDirectiveExpressionTreeNode[childs.length];
		for (int i = 0; i < childs.length; i++) {
			HRADAbstractDirectiveExpressionTreeNode abstractExpressionNode = childs[i];
			cl.childs[i] = abstractExpressionNode.clone();
		}
		return cl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADDualChildExpressionNode) {
			HRADDualChildExpressionNode oth = (HRADDualChildExpressionNode) obj;
			return childs[0].equals(oth.childs[0]) && childs[1].equals(oth.childs[1]);
		}
		return false;
	}

	public HRADAbstractDirectiveExpressionTreeNode[] getChilds() {
		return childs;
	}



	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	
	public void setChild(HRADAbstractDirectiveExpressionTreeNode child, int i) {
		this.childs[i] = child;
	}

	public void setChilds(HRADAbstractDirectiveExpressionTreeNode[] childs) {
		this.childs = childs;
	}

}
