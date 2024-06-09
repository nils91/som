package de.dralle.som.languages.hras.model;

public abstract class DualChildExpressionNode extends AbstractExpressionNode implements Cloneable{
	private AbstractExpressionNode[] childs=new AbstractExpressionNode[2];
	public DualChildExpressionNode() {
		super();
	}
	public DualChildExpressionNode(AbstractExpressionNode child1,AbstractExpressionNode child2) {
		super();
		this.childs = new AbstractExpressionNode[] {child1,child2};
	}
	public DualChildExpressionNode(AbstractExpressionNode[] childs) {
		super();
		this.childs = childs;
	}

	public AbstractExpressionNode[] getChilds() {
		return childs;
	}

	public void setChilds(AbstractExpressionNode[] childs) {
		this.childs = childs;
	}
	public void setChild(AbstractExpressionNode child,int i) {
		this.childs[i] = child;
	}
	@Override
	public DualChildExpressionNode clone() {
		// TODO Auto-generated method stub
		DualChildExpressionNode cl= (DualChildExpressionNode) super.clone();
		for (int i = 0; i < childs.length; i++) {
			AbstractExpressionNode abstractExpressionNode = childs[i];
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
		if(obj instanceof DualChildExpressionNode) {
			DualChildExpressionNode oth = (DualChildExpressionNode)obj;
			return childs[0].equals(oth.childs[0])&&childs[1].equals(oth.childs[1]);
		}
		return false;
	}

}
