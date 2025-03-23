package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

public class HRACDirectiveNode extends HRACAbstractExpressionNode implements Cloneable {
	private String directiveName;

	public HRACDirectiveNode() {
		super();
	}

	public HRACDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}

	@Override
	public int calculateNumericalValue() {
		throw new RuntimeException("Unresolved directive node: " + directiveName);
	}

	@Override
	public HRACDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRACDirectiveNode) super.clone();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		HRACAbstractExpressionNode resolvedNode = this.getResolvedExpressionTree(parent);
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

	/**
	 * return the EXpression tree behind this directive name.
	 */
	@Override
	public HRACAbstractExpressionNode getResolvedExpressionTree(HRACModel parent) {
		return parent.getDirectiveAsExpressionTree(directiveName);
	}

	/**
	 * If strings is null, the directive is resolved regardless of name. Otherwise
	 * the directive is only reolved if its part of the strings array.
	 */
	@Override
	public HRACAbstractExpressionNode getResolvedExpressionTree(HRACModel parentClone, String[] strings) {
		if (strings != null) {
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equals(directiveName)) {
					return getResolvedExpressionTree(parentClone);
				}
			}
		}
		return this.clone();
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

	@Override
	/**
	 * return the EXpression tree behind this directive name. Since this node can
	 * not replace itsself, the return value should always be assigned to itsself;
	 */
	public HRACAbstractExpressionNode resolve(HRACModel parent) {
		return parent.getDirectiveAsExpressionTree(directiveName);
	}

	@Override
	public HRACAbstractExpressionNode resolve(HRACModel parentClone, String[] strings) {
		if (strings != null) {
			for (int i = 0; i < strings.length; i++) {
				if (strings[i].equals(directiveName)) {
					return getResolvedExpressionTree(parentClone);
				}
			}
		}
		return this.clone();
	}

	public void setdirectiveName(String directiveName) {
		this.directiveName = directiveName;
	}

	@Override
	public String toString() {
		return "$" + directiveName + "";
	}
}
