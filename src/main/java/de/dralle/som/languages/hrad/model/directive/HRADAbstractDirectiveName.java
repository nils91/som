package de.dralle.som.languages.hrad.model.directive;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public abstract class HRADAbstractDirectiveName implements Cloneable {
	
	private HRADSourceLocation sourceLocation;
	
	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
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
		return hc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADAbstractDirectiveName) {
			HRADAbstractDirectiveName oth = (HRADAbstractDirectiveName) obj;
			boolean equal = name == oth.name;
			if (name != null) {
				equal = name.equals(oth.name);
			}			
			if(equal&&sourceLocation!=null) {
				equal=sourceLocation.equals(oth.sourceLocation);
			}
			return equal;
		}
		return super.equals(obj);
	}

	@Override
	public HRADAbstractDirectiveName clone() {
		HRADAbstractDirectiveName clone = null;
		try {
			clone = (HRADAbstractDirectiveName) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		clone.name = name;
		clone.sourceLocation=sourceLocation;
		return clone;
	}

	@Override
	public String toString() {
		String str = ";";
		
		if(name!=null) {
			str+=name;
		}		
		return str;
	}

	public HRADAbstractDirectiveName(String name, HRADSourceLocation sourceLocation) {
		super();
		this.name = name;
		this.sourceLocation=sourceLocation;
	}

	

	private String name;
}
