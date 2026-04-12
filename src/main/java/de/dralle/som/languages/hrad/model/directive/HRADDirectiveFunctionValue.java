package de.dralle.som.languages.hrad.model.directive;
import java.util.LinkedHashMap;
import java.util.Map;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADDirectiveFunctionValue extends HRADAbstractDirectiveValue<HRADAbstractDirectiveValue<?>> {
	
	private Map<String,String> params=new LinkedHashMap<String, String>();

	public HRADDirectiveFunctionValue( HRADAbstractDirectiveValue<?> value,Map<String, String> params, HRADSourceLocation sourceLocation) {
		super(value, sourceLocation);
		this.params=params;
	}
}
