package de.dralle.som.languages.hrac.model;

public class NamedHRACMemoryAddress extends AbstractHRACMemoryAddress {
	private String name;

	public NamedHRACMemoryAddress() {
		super();
	}

	public NamedHRACMemoryAddress(String name) {
		this();
		this.name = name;
	}

	@Override
	public String asHRACCode() {
		return name + super.asHRACCode();
	}

	@Override
	public NamedHRACMemoryAddress clone() {
		NamedHRACMemoryAddress copy = (NamedHRACMemoryAddress) super.clone();
		copy.name = name;
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NamedHRACMemoryAddress) {
			NamedHRACMemoryAddress other = (NamedHRACMemoryAddress) obj;
			boolean equaL = name.equals(other.name);
			return equaL && super.equals(obj);
		}
		return false;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + super.hashCode();
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
