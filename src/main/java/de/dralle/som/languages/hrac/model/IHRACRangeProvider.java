package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;

public interface IHRACRangeProvider extends Cloneable{
	public String getRunningDirectiveName();
	public void setRunningDirectiveName(String name);
	HRACAbstractExpressionNode[] getRange(HRACModel model);
	public IHRACRangeProvider clone();
	public String asCode();
}
