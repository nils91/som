package de.dralle.som.languages.hrad.model.directive;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public abstract class HRADAbstractDirectiveValue<T> implements Cloneable {
	
	private HRADSourceLocation sourceLocation;
	
	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
	}

	
	@Override
	public int hashCode() {
		int hc = 1;
		hc += value == null ? 0 : value.hashCode();
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbstractDirectiveValue<?>) {
			HRADAbstractDirectiveValue<?> oth = (HRADAbstractDirectiveValue<?>) obj;
			boolean equal = true;
			if (equal) {
				if (value != null) {
					equal = value.equals(oth.value);
				} else {
					equal = value == oth.value;
				}
			}
			if(equal&&sourceLocation!=null) {
				equal=sourceLocation.equals(oth.sourceLocation);
			}
			return equal;
		}
		return super.equals(obj);
	}

	@Override
	public HRADAbstractDirectiveValue<T> clone() {
		HRADAbstractDirectiveValue<T> clone = null;
		try {
			clone = (HRADAbstractDirectiveValue<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		clone.value = value;
		clone.sourceLocation=sourceLocation;
		return clone;
	}

	@Override
	public String toString() {		
		String str="";
		if(value!=null) {
			str+=value.toString();
		}
		return str;
	}

	public HRADAbstractDirectiveValue( T value, HRADSourceLocation sourceLocation) {
		super();
		this.value = value;
		this.sourceLocation=sourceLocation;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	
	private T value;
}
