package de.dralle.som.languages.hrad.model.expressiontree;

public abstract class HRBSDualChildExpressionNode extends HRBSAbstractExpressionNode implements Cloneable {
	private HRBSAbstractExpressionNode[] childs = new HRBSAbstractExpressionNode[2];

	public HRBSDualChildExpressionNode() {
		super();
	}

	public HRBSDualChildExpressionNode(HRBSAbstractExpressionNode child1, HRBSAbstractExpressionNode child2) {
		super();
		this.childs = new HRBSAbstractExpressionNode[] { child1, child2 };
	}

	public HRBSDualChildExpressionNode(HRBSAbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	@Override
	public HRBSDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRBSDualChildExpressionNode cl = (HRBSDualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			HRBSAbstractExpressionNode abstractExpressionNode = childs[i];
			cl.childs[i] = abstractExpressionNode.clone();
		}
		return cl;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSDualChildExpressionNode) {
			HRBSDualChildExpressionNode oth = (HRBSDualChildExpressionNode) obj;
			return childs[0].equals(oth.childs[0]) && childs[1].equals(oth.childs[1]);
		}
		return false;
	}

	public HRBSAbstractExpressionNode[] getChilds() {
		return childs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	public void setChild(HRBSAbstractExpressionNode child, int i) {
		this.childs[i] = child;
	}

	public void setChilds(HRBSAbstractExpressionNode[] childs) {
		this.childs = childs;
	}

}
