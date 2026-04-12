package de.dralle.som.languages.hrad.model;

import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirective;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public class HRADDirectiveStatement extends AbstractHRADCommand {
	private String name;
	private HRADAbstractDirectiveExpressionTreeNode value;
	public HRADDirectiveStatement( String name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(sourceLocation);
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HRADAbstractDirectiveExpressionTreeNode getValue() {
		return value;
	}
	public void setValue(HRADAbstractDirectiveExpressionTreeNode value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, value);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HRADDirectiveStatement other = (HRADDirectiveStatement) obj;
		return Objects.equals(name, other.name) && Objects.equals(value, other.value);
	}
	@Override
	public String toString() {
		return ";"+name+"="+value.toString();
	}
}
