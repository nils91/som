package de.dralle.som.languages.hras.model;

public abstract class CommutativeDualChildExpressionNode extends HRASDualChildExpressionNode implements Cloneable {

	public CommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(HRASAbstractExpressionNode child1, HRASAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public CommutativeDualChildExpressionNode(HRASAbstractExpressionNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommutativeDualChildExpressionNode) {
			CommutativeDualChildExpressionNode oth = (CommutativeDualChildExpressionNode) obj;
			HRASAbstractExpressionNode[] childs = getChilds();
			HRASAbstractExpressionNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
