package de.dralle.som.languages.hrac.model;

public interface IHRACRangeProvider extends Cloneable{
	public String getRunningDirectiveName();
	public void setRunningDirectiveName(String name);
	int[] getRange(HRACModel model);
}
