package de.dralle.som.languages.hrad.model.directive;

public class HRADStringDirective extends HRADAbstractDirective<String> {

	@Override
	public HRADStringDirective clone() {
		// TODO Auto-generated method stub
		return (HRADStringDirective) super.clone();
	}

	@Override
	public String toString() {
		String str = ";";
		if(isGlobal()) {
			str+=" global ";
		}
		if(getName()!=null) {
			str+=getName();
		}
		str+=" = ";
		if(getValue()!=null) {
			str+="\""+ getValue().toString()+"\"";
		}
		return str;
	}

	public HRADStringDirective(boolean global, String name, String value) {
		super(global, name, value);
		// TODO Auto-generated constructor stub
	}

}
