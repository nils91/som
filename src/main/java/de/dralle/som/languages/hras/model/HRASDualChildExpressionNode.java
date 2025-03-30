package de.dralle.som.languages.hras.model;

public abstract class HRASDualChildExpressionNode extends HRASAbstractExpressionNode implements Cloneable {
	private HRASAbstractExpressionNode[] childs = new HRASAbstractExpressionNode[2];

	public HRASDualChildExpressionNode() {
		super();
	}

	public HRASDualChildExpressionNode(HRASAbstractExpressionNode child1, HRASAbstractExpressionNode child2) {
		super();
		this.childs = new HRASAbstractExpressionNode[] { child1, child2 };
	}

	public HRASDualChildExpressionNode(HRASAbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	@Override
	public HRASDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRASDualChildExpressionNode cl = (HRASDualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			HRASAbstractExpressionNode abstractExpressionNode = childs[i];
			cl.childs[i] = abstractExpressionNode.clone();
		}
		return cl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRASDualChildExpressionNode) {
			HRASDualChildExpressionNode oth = (HRASDualChildExpressionNode) obj;
			return childs[0].equals(oth.childs[0]) && childs[1].equals(oth.childs[1]);
		}
		return false;
	}

	public HRASAbstractExpressionNode[] getChilds() {
		return childs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	public void setChild(HRASAbstractExpressionNode child, int i) {
		this.childs[i] = child;
	}

	public void setChilds(HRASAbstractExpressionNode[] childs) {
		this.childs = childs;
	}

}
