package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

/**
 * Provides a range of values (as an arry) via getRange().
 */
public class HRACForDupFixedRangeProvider implements IHRACRangeProvider, Cloneable {
	private List<HRACAbstractDirectiveExpressionTreeNode> values;
	private String runningDirectiveName = "i";

	public void addValue(HRACAbstractDirectiveExpressionTreeNode value) {
		if (values == null) {
			values = new ArrayList<HRACAbstractDirectiveExpressionTreeNode>();
		}
		values.add(value);
	}

	public void addValue(int value) {
		if (values == null) {
			values = new ArrayList<HRACAbstractDirectiveExpressionTreeNode>();
		}
		values.add(new HRACIntegerNode(value));
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
	public HRACForDupFixedRangeProvider clone() {
		HRACForDupFixedRangeProvider clone = new HRACForDupFixedRangeProvider();
		try {
			clone = (HRACForDupFixedRangeProvider) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.values = new ArrayList<HRACAbstractDirectiveExpressionTreeNode>();
		for (HRACAbstractDirectiveExpressionTreeNode v : values) {
			clone.values.add(v.clone());
		}
		return null;
	}

	public HRACAbstractDirectiveExpressionTreeNode[] getRange(HRACModel parent) {
		HRACAbstractDirectiveExpressionTreeNode[] rng = new HRACAbstractDirectiveExpressionTreeNode[values.size()];
		for (int i = 0; i < values.size(); i++) {
			HRACAbstractDirectiveExpressionTreeNode value = new HRACIntegerNode(0);
			if (values.get(i) != null) {
				value = values.get(i);
			}
			rng[i] = value;
		}
		return rng;
	}

	@Override
	public String getRunningDirectiveName() {
		return runningDirectiveName;
	}

	@Override
	public void setRunningDirectiveName(String name) {
		runningDirectiveName = name;
	}

	public void setValues(List<HRACAbstractDirectiveExpressionTreeNode> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return asCode();
	}
}
