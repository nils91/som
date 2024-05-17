package de.dralle.som.languages.hrac.model;

public interface IHRACRangeProvider extends Cloneable{
	public String getRunningDirectiveName();
	int[] getRange(HRACModel model);
}
