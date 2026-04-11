package de.dralle.som.languages.hrad.model.expressiontree;

public abstract class HRADCommutativeDualChildExpressionNode extends HRADDualChildExpressionNode implements Cloneable {

	public HRADCommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRADCommutativeDualChildExpressionNode(HRADAbstractExpressionNode child1,
			HRADAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public HRADCommutativeDualChildExpressionNode(HRADAbstractExpressionNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADCommutativeDualChildExpressionNode) {
			HRADCommutativeDualChildExpressionNode oth = (HRADCommutativeDualChildExpressionNode) obj;
			HRADAbstractExpressionNode[] childs = getChilds();
			HRADAbstractExpressionNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
