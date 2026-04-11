package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADIntegerDirective extends HRADAbstractDirective<Integer> {

	public HRADIntegerDirective( String name, HRADIntegerNode value) {
		super(name, value.getValue());
	}
	public HRADIntegerDirective(String name, int value) {
		this(name, new HRADIntegerNode(value));
	}
}
