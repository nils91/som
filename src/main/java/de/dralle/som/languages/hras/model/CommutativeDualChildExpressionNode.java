package de.dralle.som.languages.hras.model;

public abstract class CommutativeDualChildExpressionNode extends DualChildExpressionNode implements Cloneable{
	
	public CommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(AbstractExpressionNode child1, AbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(AbstractExpressionNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CommutativeDualChildExpressionNode) {
			CommutativeDualChildExpressionNode oth = (CommutativeDualChildExpressionNode)obj;
			AbstractExpressionNode[] childs = getChilds();
			AbstractExpressionNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0])&&childs[1].equals(othChilds[1]))||(childs[0].equals(othChilds[1])&&childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
