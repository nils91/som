package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADIntegerDirectiveValue extends HRADAbstractDirectiveValue<Integer> {

	public HRADIntegerDirectiveValue( HRADIntegerNode value, HRADSourceLocation sourceLocation) {
		super(value.getValue(), sourceLocation);
	}
	public HRADIntegerDirectiveValue(int value, HRADSourceLocation sourceLocation) {
		this(new HRADIntegerNode(value), sourceLocation);
	}
}
