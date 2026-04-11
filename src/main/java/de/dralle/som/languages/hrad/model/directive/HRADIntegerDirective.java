package de.dralle.som.languages.hrad.model.directive;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;

public class HRADIntegerDirective extends HRADExpressionTreeDirective {

	public HRADIntegerDirective(boolean global, String name, HRADIntegerNode value) {
		super(global, name, value);
	}
	public HRADIntegerDirective(boolean global, String name, int value) {
		this(global, name, new HRADIntegerNode(value));
	}
}
