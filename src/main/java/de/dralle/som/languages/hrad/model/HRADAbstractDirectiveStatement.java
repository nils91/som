package de.dralle.som.languages.hrad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;

public abstract class HRADAbstractDirectiveStatement<T> extends AbstractHRADCommand {
	private T name;
	private List<HRADAbstractDirectiveExpressionTreeNode> paramNames;
	public List<HRADAbstractDirectiveExpressionTreeNode> getParams() {
		return paramNames;
	}
	public void setParams(List<HRADAbstractDirectiveExpressionTreeNode> params) {
		this.paramNames = params;
	}
	private HRADAbstractDirectiveExpressionTreeNode value;
	public HRADAbstractDirectiveStatement( T name,
			HRADAbstractDirectiveExpressionTreeNode value,HRADSourceLocation sourceLocation) {
		super(sourceLocation);
		this.name = name;
		this.value = value;
	}
	public T getName() {
		return name;
	}
	public void setName(T name) {
		this.name = name;
	}
	public HRADAbstractDirectiveExpressionTreeNode getValue() {
		return value;
	}
	@Override
	public HRADAbstractDirectiveStatement<?> clone() {
		// TODO Auto-generated method stub
		HRADAbstractDirectiveStatement<?> clone = (HRADAbstractDirectiveStatement<?>) super.clone();
		if(value!=null) clone.value=value.clone();
		if(paramNames!=null) {
			clone.paramNames=new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
			for (HRADAbstractDirectiveExpressionTreeNode hradAbstractDirectiveExpressionTreeNode : paramNames) {
				clone.paramNames.add(hradAbstractDirectiveExpressionTreeNode.clone());
			}
		}
		return clone;
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
		HRADAbstractDirectiveStatement<?> other = (HRADAbstractDirectiveStatement<?>) obj;
		return Objects.equals(name, other.name) && Objects.equals(value, other.value);
	}
	@Override
	public String toString() {
		return ";"+name+"="+value.toString();
	}
}
