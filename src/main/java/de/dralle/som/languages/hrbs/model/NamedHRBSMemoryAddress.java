package de.dralle.som.languages.hrbs.model;

public class NamedHRBSMemoryAddress extends AbstractHRBSMemoryAddress {
	private String targetSymbolName;

	public NamedHRBSMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public NamedHRBSMemoryAddress(String name) {
		super();
		this.targetSymbolName = name;
	}

	@Override
	public String asHRBSCode() {
		return super.getFirstPartHRBSCode() + targetSymbolName + super.getSecondPartHRBSCode();
	}

	@Override
	public NamedHRBSMemoryAddress clone() {
		NamedHRBSMemoryAddress clone = (NamedHRBSMemoryAddress) super.clone();
		clone.targetSymbolName = targetSymbolName;
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NamedHRBSMemoryAddress) {
			boolean equals = super.equals(obj);
			if (targetSymbolName != null) {
				return equals && targetSymbolName.equals(((NamedHRBSMemoryAddress) obj).getTargetSymbolName());
			}
			return equals && targetSymbolName == ((NamedHRBSMemoryAddress) obj).targetSymbolName;
		}
		return super.equals(obj);
	}

	public String getTargetSymbolName() {
		return targetSymbolName;
	}

	@Override
	public int hashCode() {
		if (targetSymbolName != null) {
			return targetSymbolName.hashCode() + super.hashCode() * 97;
		}
		return super.hashCode();
	}

	public void setTargetSymbolName(String name) {
		this.targetSymbolName = name;
	}

}
