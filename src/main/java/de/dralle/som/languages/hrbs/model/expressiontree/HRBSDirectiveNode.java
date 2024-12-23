package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.HRBSModel;

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
	public HRACAbstractExpressionNode compileToHRAC(HRBSModel parent) {
		HRBSAbstractExpressionNode resolvedNode = this.getResolvedExpressionTree(parent);
		if(resolvedNode!=null) {
			return resolvedNode.compileToHRAC(parent);
		}
		return null;
	}
}
