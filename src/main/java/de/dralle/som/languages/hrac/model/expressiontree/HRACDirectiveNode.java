package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public class HRACDirectiveNode extends HRACAbstractDirectiveExpressionTreeNode implements Cloneable {
	private String directiveName;

	public HRACDirectiveNode() {
		super();
	}

	public HRACDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}


	@Override
	public HRACDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRACDirectiveNode) super.clone();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		HRACAbstractDirectiveExpressionTreeNode resolvedNode = this.accept(new HRACResolveDirectiveTreeVisitor(parent, true));
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
		if (obj instanceof HRACDirectiveNode) {
			HRACDirectiveNode oth = (HRACDirectiveNode) obj;
			return directiveName.equals(oth.directiveName);
		}
		return false;
	}

	public String getDirectiveName() {
		return directiveName;
	}

		
	@Override
	public Collection<String> getUsedDirectives() {
		List<String> list = new ArrayList<String>();
		list.add(directiveName);
		return list;
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
