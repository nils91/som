package de.dralle.som.languages.hrbs.model;

public class NamedHRBSMemoryAddress extends AbstractHRBSMemoryAddress {
	private String targetSymbolName;

	public NamedHRBSMemoryAddress(String name) {
		super();
		this.targetSymbolName = name;
	}

	@Override
	public int hashCode() {
		if (targetSymbolName != null) {
			return targetSymbolName.hashCode() + super.hashCode() * 97;
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NamedHRBSMemoryAddress) {
			boolean equals=super.equals(obj);
			if(targetSymbolName!=null) {
				return equals&&targetSymbolName.equals(((NamedHRBSMemoryAddress)obj).getTargetSymbolName());
			}
			return equals&&targetSymbolName==((NamedHRBSMemoryAddress)obj).targetSymbolName;
		}
		return super.equals(obj);
	}

	@Override
	public NamedHRBSMemoryAddress clone() {
		NamedHRBSMemoryAddress clone= (NamedHRBSMemoryAddress) super.clone();
		clone.targetSymbolName=targetSymbolName;
		return clone;
	}

	@Override
	public String asHRBSCode() {
		return super.getFirstPartHRBSCode()+targetSymbolName+super.getSecondPartHRBSCode();
	}

	public NamedHRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public String getTargetSymbolName() {
		return targetSymbolName;
	}

	public void setTargetSymbolName(String name) {
		this.targetSymbolName = name;
	}

}
