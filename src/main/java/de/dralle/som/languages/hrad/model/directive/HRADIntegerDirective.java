package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADIntegerDirective extends HRADAbstractDirectiveName<Integer> {

	public HRADIntegerDirective( String name, HRADIntegerNode value, HRADSourceLocation sourceLocation) {
		super(name, value.getValue(), sourceLocation);
	}
	public HRADIntegerDirective(String name, int value, HRADSourceLocation sourceLocation) {
		this(name, new HRADIntegerNode(value), sourceLocation);
	}
}
