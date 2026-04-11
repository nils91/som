package de.dralle.som.languages.hrad.model.directive;

public abstract class HRADAbstractDirective<T> implements Cloneable {
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		int hc = 1;
		hc += name == null ? 0 : name.hashCode();
		hc += value == null ? 0 : value.hashCode();
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbstractDirective<?>) {
			HRADAbstractDirective<?> oth = (HRADAbstractDirective<?>) obj;
			boolean equal = name == oth.name;
			if (name != null) {
				equal = name.equals(oth.name);
			}
			if (equal) {
				if (value != null) {
					equal = value.equals(oth.value);
				} else {
					equal = value == oth.value;
				}
			}
			return equal;
		}
		return super.equals(obj);
	}

	@Override
	public HRADAbstractDirective<T> clone() {
		HRADAbstractDirective<T> clone = null;
		try {
			clone = (HRADAbstractDirective<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		clone.name = name;
		clone.value = value;
		return clone;
	}

	@Override
	public String toString() {
		String str = ";";
		
		if(name!=null) {
			str+=name;
		}
		str+=" = ";
		if(value!=null) {
			str+=value.toString();
		}
		return str;
	}

	public HRADAbstractDirective(String name, T value) {
		super();
		this.name = name;
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	private String name;
	private T value;
}
