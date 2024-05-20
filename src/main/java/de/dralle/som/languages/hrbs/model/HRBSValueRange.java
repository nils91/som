package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.List;

public class HRBSValueRange extends AbstractHRBSRange implements Cloneable{

	private List<HRBSMemoryAddressOffset> values=new ArrayList<HRBSMemoryAddressOffset>();
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
			hc+=array_element.hashCode()*(i+1);
			
		}
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public AbstractHRBSRange clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
