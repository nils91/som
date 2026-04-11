package de.dralle.som.languages.hrac.model.directive;

public abstract class AbstractDirective<T> implements Cloneable {
	private boolean global;

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

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
		hc *= global ? 3 : 2;
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractDirective<?>) {
			AbstractDirective<?> oth = (AbstractDirective<?>) obj;
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
			if (equal) {
				equal = global == oth.global;
			}
			return equal;
		}
		return super.equals(obj);
	}

	@Override
	public AbstractDirective<T> clone() {
		AbstractDirective<T> clone = null;
		try {
			clone = (AbstractDirective<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.name = name;
		clone.value = value;
		clone.global = global;
		return clone;
	}

	@Override
	public String toString() {
		String str = ";";
		if(global) {
			str+=" global ";
		}
		if(name!=null) {
			str+=name;
		}
		str+=" = ";
		if(value!=null) {
			str+=value.toString();
		}
		return str;
	}

	public AbstractDirective(boolean global, String name, T value) {
		super();
		this.global=global;
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
