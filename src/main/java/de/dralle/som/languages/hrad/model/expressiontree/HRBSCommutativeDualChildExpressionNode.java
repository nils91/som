package de.dralle.som.languages.hrad.model.expressiontree;

public abstract class HRBSCommutativeDualChildExpressionNode extends HRBSDualChildExpressionNode implements Cloneable {

	public HRBSCommutativeDualChildExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSCommutativeDualChildExpressionNode(HRBSAbstractExpressionNode child1,
			HRBSAbstractExpressionNode child2) {
		super(child1, child2);
		// TODO Auto-generated constructor stub
	}

	public HRBSCommutativeDualChildExpressionNode(HRBSAbstractExpressionNode[] childs) {
		super(childs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSCommutativeDualChildExpressionNode) {
			HRBSCommutativeDualChildExpressionNode oth = (HRBSCommutativeDualChildExpressionNode) obj;
			HRBSAbstractExpressionNode[] childs = getChilds();
			HRBSAbstractExpressionNode[] othChilds = oth.getChilds();
			return (childs[0].equals(othChilds[0]) && childs[1].equals(othChilds[1]))
					|| (childs[0].equals(othChilds[1]) && childs[1].equals(othChilds[0]));
		}
		return false;
	}

}
