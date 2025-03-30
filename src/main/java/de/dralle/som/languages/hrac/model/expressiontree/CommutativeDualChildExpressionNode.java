package de.dralle.som.languages.hrac.model.expressiontree;

public abstract class CommutativeDualChildExpressionNode extends HRACDualChildExpressionNode implements Cloneable {

	public CommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(HRACAbstractExpressionNode child1, HRACAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(HRACAbstractExpressionNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommutativeDualChildExpressionNode) {
			CommutativeDualChildExpressionNode oth = (CommutativeDualChildExpressionNode) obj;
			HRACAbstractExpressionNode[] childs = getChilds();
			HRACAbstractExpressionNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
