package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

/**
 * Provides a range of values (as an arry) via getRange().
 */
public class HRACForDupFixedRangeProvider implements IHRACRangeProvider, Cloneable {
	private List<HRACAbstractExpressionNode> values;
	private String runningDirectiveName = "i";

	public void setValues(List<HRACAbstractExpressionNode> values) {
		this.values = values;
	}

	public void addValue(HRACAbstractExpressionNode value) {
		if (values == null) {
			values = new ArrayList<HRACAbstractExpressionNode>();
		}
		values.add(value);
	}

	public void addValue(int value) {
		if (values == null) {
			values = new ArrayList<HRACAbstractExpressionNode>();
		}
		values.add(new HRACIntegerNode(value));
	}

	public HRACAbstractExpressionNode[] getRange(HRACModel parent) {
		HRACAbstractExpressionNode[] rng = new HRACAbstractExpressionNode[values.size()];
		for (int i = 0; i < values.size(); i++) {
			HRACAbstractExpressionNode value = new HRACIntegerNode(0);
			if (values.get(i) != null) {
				value = values.get(i);
			}
			rng[i] = value;
		}
		return rng;
	}

	@Override
	public HRACForDupFixedRangeProvider clone() {
		HRACForDupFixedRangeProvider clone = new HRACForDupFixedRangeProvider();
		try {
			clone = (HRACForDupFixedRangeProvider) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.values = new ArrayList<HRACAbstractExpressionNode>();
		for (HRACAbstractExpressionNode v : values) {
			clone.values.add(v.clone());
		}
		return null;
	}

	public String asCode() {
		String s = "";
		if (runningDirectiveName != null) {
			s = "$" + runningDirectiveName + " = ";
		}
		s += "{";
		for (int i = 0; i < values.size(); i++) {

			if (values.get(i) != null) {
				s += values.get(i);
			}
			if (i < values.size() - 1) {
				s += ", ";
			}
		}
		return s + "}";
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	public String getRunningDirectiveName() {
		return runningDirectiveName;
	}

	@Override
	public void setRunningDirectiveName(String name) {
		runningDirectiveName = name;
	}
}
