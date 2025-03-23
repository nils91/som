package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;

public interface IHRACRangeProvider extends Cloneable {
	public String asCode();

	public IHRACRangeProvider clone();

	HRACAbstractExpressionNode[] getRange(HRACModel model);

	public String getRunningDirectiveName();

	public void setRunningDirectiveName(String name);
}
