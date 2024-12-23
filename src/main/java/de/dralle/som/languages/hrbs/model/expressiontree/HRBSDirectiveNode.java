package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;

public class HRBSDirectiveNode extends HRBSAbstractExpressionNode implements Cloneable{
	@Override
	public HRBSDirectiveNode clone() {
		// TODO Auto-generated method stub
		return (HRBSDirectiveNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return directiveName.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HRBSDirectiveNode){
		HRBSDirectiveNode oth=(HRBSDirectiveNode) obj;
		return directiveName.equals(oth.directiveName);
		}
		return false;
	}
	@Override
	public String toString() {
		return "$"+directiveName+"";
	}
	private String directiveName;

	public String getdirectiveName() {
		return directiveName;
	}
	public void setdirectiveName(String directiveName) {
		this.directiveName = directiveName;
	}
	public HRBSDirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}
	public HRBSDirectiveNode() {
		super();
	}
	@Override
	public int calculateNumericalValue() {
		throw new RuntimeException("Unresolved directive node: "+directiveName);
	}
	/**
	 * return the EXpression tree behind this directive name.
	 */
	@Override
	public HRBSAbstractExpressionNode getResolvedExpressionTree(HRACModel parent) {
		return parent.getDirectiveAsExpressionTree(directiveName);
	}
	@Override
	/**
	 * return the EXpression tree behind this directive name. Since this node can not replace itsself, the return value should always be assigned to itsself;
	 */
	public HRBSAbstractExpressionNode resolve(HRACModel parent) {
		return parent.getDirectiveAsExpressionTree(directiveName);
	}
	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		HRBSAbstractExpressionNode resolvedNode = this.getResolvedExpressionTree(parent);
		if(resolvedNode!=null) {
			return resolvedNode.compileToHRAS(parent);
		}
		return null;
	}
}
