package de.dralle.som.languages.hrac.model.expressiontree;

public class DirectiveNode extends AbstractExpressionNode implements Cloneable{
	@Override
	public DirectiveNode clone() {
		// TODO Auto-generated method stub
		return (DirectiveNode) super.clone();
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return directiveName.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DirectiveNode){
		DirectiveNode oth=(DirectiveNode) obj;
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
	public DirectiveNode(String directiveName) {
		super();
		this.directiveName = directiveName;
	}
	public DirectiveNode() {
		super();
	}
	@Override
	public int calculateNumericalValue() {
		throw new RuntimeException("Unresolved directive node: "+directiveName);
	}
}
