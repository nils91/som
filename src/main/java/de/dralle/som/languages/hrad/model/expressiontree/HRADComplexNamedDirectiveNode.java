package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public class HRADComplexNamedDirectiveNode extends HRADAbstractDirectiveNode<HRADAbstractDirectiveExpressionTreeNode> implements Cloneable {
	
	public HRADComplexNamedDirectiveNode() {
		super();
	}

	public HRADComplexNamedDirectiveNode(HRADAbstractDirectiveExpressionTreeNode directiveName) {
		super(directiveName);
	}

	@Override
	public HRADComplexNamedDirectiveNode clone() {
		HRADComplexNamedDirectiveNode rn = (HRADComplexNamedDirectiveNode) super.clone();
		rn.setDirectiveName(rn.getDirectiveName().clone());
		return rn;
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSDirectiveNode(directiveName);
	}

	@Override
	public String toString() {
		return "$(" + getDirectiveName() + ")";
	}
}
