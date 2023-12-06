package de.dralle.som.languages.hrbs.model;

public class NamedHRBSMemoryAddress extends AbstractHRBSMemoryAddress {
	private String name;

	public NamedHRBSMemoryAddress(String name) {
		super();
		this.name = name;
	}

	@Override
	public int hashCode() {
		if (name != null) {
			return name.hashCode() + super.hashCode() * 97;
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NamedHRBSMemoryAddress) {
			boolean equals=super.equals(obj);
			if(name!=null) {
				return equals&&name.equals(((NamedHRBSMemoryAddress)obj).getName());
			}
			return equals&&name==((NamedHRBSMemoryAddress)obj).name;
		}
		return super.equals(obj);
	}

	@Override
	public NamedHRBSMemoryAddress clone() {
		NamedHRBSMemoryAddress clone= (NamedHRBSMemoryAddress) super.clone();
		clone.name=name;
		return clone;
	}

	@Override
	public String asHRBSCode() {
		return super.getFirstPartHRBSCode()+name+super.getSecondPartHRBSCode();
	}

	public NamedHRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
