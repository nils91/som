package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.Map;

import de.dralle.som.languages.hrac.model.HRACModel;

public abstract class HRACAbstractExpressionNode implements Cloneable{
	public HRACAbstractExpressionNode() {
		
	}
	@Override
	public HRACAbstractExpressionNode clone()  {
		// TODO Auto-generated method stub
		try {
			return (HRACAbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract int calculateNumericalValue();
	/**
	 * Returns the very same expression tree as this with its directive nodes resolved. To avoid modification of the original tree the returned one will be a clone.
	 * @param allDirectives
	 * @return
	 */
	public HRACAbstractExpressionNode getResolvedExperessionTree(HRACModel parent) {
		HRACAbstractExpressionNode clone = this.clone();
		clone=clone.resolve(parent);
		return this;
	}
	/**
	 * Resolves all directive nodes in this expression tree.
	 * @param allDirectives
	 * @return
	 */
	public HRACAbstractExpressionNode resolve(HRACModel parent) {
		return this;
	}

}
