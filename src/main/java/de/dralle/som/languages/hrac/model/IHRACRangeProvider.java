package de.dralle.som.languages.hrac.model;

public interface IHRACRangeProvider extends Cloneable{
	int[] getRange(HRACModel model);
}
