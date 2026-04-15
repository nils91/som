package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public class HRADDirectiveNode extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private String directiveName;
	private List<HRADAbstractDirectiveExpressionTreeNode> params;

	public HRADDirectiveNode() {
		super();
	}

	public HRADDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}


	@Override
	public HRADDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRADDirectiveNode) super.clone();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRADModel parent) {
		HRADAbstractDirectiveExpressionTreeNode resolvedNode = this.accept(new HRADResolveDirectiveTreeVisitor(parent.getDirectives(), true,true));
		if (resolvedNode != null) {
			return resolvedNode.compileToHRAS(parent);
		}
		return null;
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSDirectiveNode(directiveName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADDirectiveNode) {
			HRADDirectiveNode oth = (HRADDirectiveNode) obj;
			return directiveName.equals(oth.directiveName);
		}
		return false;
	}

	public String getDirectiveName() {
		return directiveName;
	}

		

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return directiveName.hashCode();
	}

	public void setDirectiveName(String directiveName) {
		this.directiveName = directiveName;
	}

	@Override
	public String toString() {
		return "$" + directiveName + "";
	}
}
