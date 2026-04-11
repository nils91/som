package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADDirectiveFunction extends HRADAbstractDirective<HRADAbstractDirective<?>> {
	
	private String[] params;

	public HRADDirectiveFunction( String name, HRADAbstractDirective<?> value,String[] params, HRADSourceLocation sourceLocation) {
		super(name, value, sourceLocation);
		this.params=params;
	}
}
