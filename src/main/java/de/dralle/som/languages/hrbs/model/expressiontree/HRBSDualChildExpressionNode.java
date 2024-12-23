package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;

public abstract class HRBSDualChildExpressionNode extends HRBSAbstractExpressionNode implements Cloneable{
	private HRBSAbstractExpressionNode[] childs=new HRBSAbstractExpressionNode[2];
	public HRBSDualChildExpressionNode() {
		super();
	}
	public HRBSDualChildExpressionNode(HRBSAbstractExpressionNode child1,HRBSAbstractExpressionNode child2) {
		super();
		this.childs = new HRBSAbstractExpressionNode[] {child1,child2};
	}
	public HRBSDualChildExpressionNode(HRBSAbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	public HRBSAbstractExpressionNode[] getChilds() {
		return childs;
	}

	public void setChilds(HRBSAbstractExpressionNode[] childs) {
		this.childs = childs;
	}
	@Override
	public HRBSDualChildExpressionNode resolve(HRACModel parent) {
		for (int i = 0; i < childs.length; i++) {
			childs[i] = childs[i].resolve(parent);
		}
		return this;
	}
	public void setChild(HRBSAbstractExpressionNode child,int i) {
		this.childs[i] = child;
	}
	@Override
	public HRBSDualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		HRBSDualChildExpressionNode cl= (HRBSDualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			HRBSAbstractExpressionNode abstractExpressionNode = childs[i];
			cl.childs[i]=abstractExpressionNode.clone();
		}
		return cl;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return childs.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSDualChildExpressionNode) {
			HRBSDualChildExpressionNode oth = (HRBSDualChildExpressionNode)obj;
			return childs[0].equals(oth.childs[0])&&childs[1].equals(oth.childs[1]);
		}
		return false;
	}

}
