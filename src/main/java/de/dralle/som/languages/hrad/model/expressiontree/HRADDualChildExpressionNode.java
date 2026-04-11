package de.dralle.som.languages.hrad.model.expressiontree;

public abstract class HRADDualChildExpressionNode extends HRADAbstractExpressionNode implements Cloneable {
	private HRADAbstractExpressionNode[] childs = new HRADAbstractExpressionNode[2];

	public HRADDualChildExpressionNode() {
		super();
	}

	public HRADDualChildExpressionNode(HRADAbstractExpressionNode child1, HRADAbstractExpressionNode child2) {
		super();
		this.childs = new HRADAbstractExpressionNode[] { child1, child2 };
	}

	public HRADDualChildExpressionNode(HRADAbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	@Override
	public HRADDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRADDualChildExpressionNode cl = (HRADDualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			HRADAbstractExpressionNode abstractExpressionNode = childs[i];
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

	public HRADAbstractExpressionNode[] getChilds() {
		return childs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	public void setChild(HRADAbstractExpressionNode child, int i) {
		this.childs[i] = child;
	}

	public void setChilds(HRADAbstractExpressionNode[] childs) {
		this.childs = childs;
	}

}
