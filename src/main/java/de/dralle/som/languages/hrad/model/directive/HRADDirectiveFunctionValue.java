package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADDirectiveFunctionValue extends HRADAbstractDirectiveValue<HRADAbstractDirectiveValue<?>> {
	
	private String[] params;

	public HRADDirectiveFunctionValue( HRADAbstractDirectiveValue<?> value,String[] params, HRADSourceLocation sourceLocation) {
		super(value, sourceLocation);
		this.params=params;
	}
}
