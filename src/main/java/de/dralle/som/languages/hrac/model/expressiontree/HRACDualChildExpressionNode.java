package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrac.model.HRACModel;

public abstract class HRACDualChildExpressionNode extends HRACAbstractExpressionNode implements Cloneable {
	private HRACAbstractExpressionNode[] childs = new HRACAbstractExpressionNode[2];

	public HRACDualChildExpressionNode() {
		super();
	}

	public HRACDualChildExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super();
		this.childs = new HRACAbstractExpressionNode[] { child1, child2 };
	}

	public HRACDualChildExpressionNode(HRACAbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	@Override
	public HRACDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRACDualChildExpressionNode cl = (HRACDualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			HRACAbstractExpressionNode abstractExpressionNode = childs[i];
			cl.childs[i] = abstractExpressionNode.clone();
		}
		return cl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACDualChildExpressionNode) {
			HRACDualChildExpressionNode oth = (HRACDualChildExpressionNode) obj;
			return childs[0].equals(oth.childs[0]) && childs[1].equals(oth.childs[1]);
		}
		return false;
	}

	public HRACAbstractExpressionNode[] getChilds() {
		return childs;
	}

	@Override
	public Collection<String> getUsedDirectives() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(childs[0].getUsedDirectives());
		list.addAll(childs[1].getUsedDirectives());
		return list;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	@Override
	public HRACDualChildExpressionNode resolve(HRACModel parent) {
		for (int i = 0; i < childs.length; i++) {
			childs[i] = childs[i].resolve(parent);
		}
		return this;
	}

	public void setChild(HRACAbstractExpressionNode child, int i) {
		this.childs[i] = child;
	}

	public void setChilds(HRACAbstractExpressionNode[] childs) {
		this.childs = childs;
	}

}
