package de.dralle.som.languages.hrad.model.expressiontree;

public abstract class HRADCommutativeDualChildExpressionNode extends HRADDualChildExpressionNode implements Cloneable {

	public HRADCommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADCommutativeDualChildExpressionNode(HRADAbstractDirectiveExpressionTreeNode child1, HRADAbstractDirectiveExpressionTreeNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public HRADCommutativeDualChildExpressionNode(HRADAbstractDirectiveExpressionTreeNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADCommutativeDualChildExpressionNode) {
			HRADCommutativeDualChildExpressionNode oth = (HRADCommutativeDualChildExpressionNode) obj;
			HRADAbstractDirectiveExpressionTreeNode[] childs = getChilds();
			HRADAbstractDirectiveExpressionTreeNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
