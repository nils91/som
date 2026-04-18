package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADDirectiveFunction extends HRADAbstractDirectiveName<HRADAbstractDirectiveName<?>> {
	
	private String[] params;

	public HRADDirectiveFunction( String name, HRADAbstractDirectiveName<?> value,String[] params, HRADSourceLocation sourceLocation) {
		super(name, value, sourceLocation);
		this.params=params;
	}
}
