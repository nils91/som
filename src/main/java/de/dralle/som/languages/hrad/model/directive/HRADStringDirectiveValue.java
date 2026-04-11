package de.dralle.som.languages.hrad.model.directive;

import de.dralle.som.languages.hrad.HRADSourceLocation;

public class HRADStringDirectiveValue extends HRADAbstractDirectiveValue<String> {

	@Override
	public HRADStringDirectiveValue clone() {
		// TODO Auto-generated method stub
		return (HRADStringDirectiveValue) super.clone();
	}

	@Override
	public String toString() {
		String str = "";
		
		if(getValue()!=null) {
			str+="\""+ getValue().toString()+"\"";
		}
		return str;
	}

	public HRADStringDirectiveValue(String value, HRADSourceLocation sourceLocation) {
		super(value, sourceLocation);
		// TODO Auto-generated constructor stub
	}

}
