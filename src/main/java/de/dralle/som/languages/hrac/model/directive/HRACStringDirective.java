package de.dralle.som.languages.hrac.model.directive;

public class HRACStringDirective extends AbstractDirective<String> {

	@Override
	public HRACStringDirective clone() {
		// TODO Auto-generated method stub
		return (HRACStringDirective) super.clone();
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

	public HRACStringDirective(boolean global, String name, String value) {
		super(global, name, value);
		// TODO Auto-generated constructor stub
	}

}
