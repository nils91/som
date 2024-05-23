package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.List;

public class HRBSValueRange extends AbstractHRBSRange implements Cloneable {

	private List<HRBSMemoryAddressOffset> values = new ArrayList<HRBSMemoryAddressOffset>();

	public List<HRBSMemoryAddressOffset> getValues() {
		return values;
	}

	public void setValues(List<HRBSMemoryAddressOffset> values) {
		this.values = values;
	}

	public void addValue(HRBSMemoryAddressOffset value) {
		values.add(value);
	}

	@Override
	public int hashCode() {
		int hc = super.hashCode();
		for (int i = 0; i < values.size(); i++) {
			HRBSMemoryAddressOffset array_element = values.get(i);
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
					HRBSMemoryAddressOffset array_element = values.get(i);
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
		for (HRBSMemoryAddressOffset hrbsMemoryAddressOffset : values) {
			cl.values.add(hrbsMemoryAddressOffset.clone());
		}
		return cl;
	}

	@Override
	public String toString() {
		String str = super.toString();
		str+="{";
		for (HRBSMemoryAddressOffset hrbsMemoryAddressOffset : values) {
			str+=hrbsMemoryAddressOffset.toString()+", ";
		}
		str=str.substring(0, str.length()-2)+"}";
		return str;
	}

}
