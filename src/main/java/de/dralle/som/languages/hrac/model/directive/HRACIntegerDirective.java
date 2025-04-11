package de.dralle.som.languages.hrac.model.directive;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class HRACIntegerDirective extends HRACExpressionTreeDirective {

	public HRACIntegerDirective(boolean global, String name, HRACIntegerNode value) {
		super(global, name, value);
	}
	public HRACIntegerDirective(boolean global, String name, int value) {
		this(global, name, new HRACIntegerNode(value));
	}
}
