package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public class HRADStringNamedDirectiveNode extends HRADAbstractDirectiveNode<String> implements Cloneable {
	
	public HRADStringNamedDirectiveNode() {
		super();
	}

	public HRADStringNamedDirectiveNode(String directiveName) {
		super(directiveName);
	}

	@Override
	public HRADStringNamedDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRADStringNamedDirectiveNode) super.clone();
	}


	@Override
	public String toString() {
		return "$" + getDirectiveName() + "";
	}
}
