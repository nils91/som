package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public class HRBSValueRange extends AbstractHRBSRange implements Cloneable {

	private List<HRBSAbstractExpressionNode> values = new ArrayList<HRBSAbstractExpressionNode>();

	public List<HRBSAbstractExpressionNode> getValues() {
		return values;
	}

	public void setValues(List<HRBSAbstractExpressionNode> values) {
		this.values = values;
	}

	public void addValue(HRBSAbstractExpressionNode value) {
		values.add(value);
	}

	@Override
	public int hashCode() {
		int hc = super.hashCode();
		for (int i = 0; i < values.size(); i++) {
			HRBSAbstractExpressionNode array_element = values.get(i);
			hc += array_element.hashCode() * (i + 1);

		}
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSValueRange) {
			HRBSValueRange oth = (HRBSValueRange) obj;
			boolean eq = super.equals(obj);
			eq = eq && values.size() == oth.values.size();
			if (eq) {
				for (int i = 0; i < values.size(); i++) {
					HRBSAbstractExpressionNode array_element = values.get(i);
					eq = eq && array_element.equals(oth.values.get(i));

				}
				return eq;
			}

		}
		return false;
	}

	@Override
	public HRBSValueRange clone() {
		HRBSValueRange cl=(HRBSValueRange) super.clone();
		cl.values=new ArrayList<>();
		for (HRBSAbstractExpressionNode hrbsMemoryAddressOffset : values) {
			cl.values.add(hrbsMemoryAddressOffset.clone());
		}
		return cl;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str+="{";
		for (HRBSAbstractExpressionNode hrbsMemoryAddressOffset : values) {
			str+=hrbsMemoryAddressOffset.toString()+", ";
		}
		str=str.substring(0, str.length()-2)+"}";
		return str;
	}

}
