package de.dralle.som.languages.hrad.model.directive;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public class HRADStringDirective extends HRADAbstractDirective<String> {

	@Override
	public HRADStringDirective clone() {
		// TODO Auto-generated method stub
		return (HRADStringDirective) super.clone();
	}

	@Override
	public String toString() {
		String str = ";";
		if(getName()!=null) {
			str+=getName();
		}
		str+=" = ";
		if(getValue()!=null) {
			str+="\""+ getValue().toString()+"\"";
		}
		return str;
	}

	public HRADStringDirective(String name, String value, HRADSourceLocation sourceLocation) {
		super(name, value, sourceLocation);
		// TODO Auto-generated constructor stub
	}

}
