package de.dralle.som.languages.hrac.model.expressiontree;

public abstract class HRACCommutativeDualChildExpressionNode extends HRACDualChildExpressionNode implements Cloneable {

	public HRACCommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACCommutativeDualChildExpressionNode(HRACAbstractDirectiveExpressionTreeNode child1, HRACAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public HRACCommutativeDualChildExpressionNode(HRACAbstractDirectiveExpressionTreeNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACCommutativeDualChildExpressionNode) {
			HRACCommutativeDualChildExpressionNode oth = (HRACCommutativeDualChildExpressionNode) obj;
			HRACAbstractDirectiveExpressionTreeNode[] childs = getChilds();
			HRACAbstractDirectiveExpressionTreeNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
