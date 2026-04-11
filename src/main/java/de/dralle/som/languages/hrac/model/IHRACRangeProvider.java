package de.dralle.som.languages.hrac.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;

public interface IHRACRangeProvider extends Cloneable {
	public String asCode();

	public IHRACRangeProvider clone();

	HRACAbstractDirectiveExpressionTreeNode[] getRange(HRACModel model);

	public String getRunningDirectiveName();

	public void setRunningDirectiveName(String name);
}
