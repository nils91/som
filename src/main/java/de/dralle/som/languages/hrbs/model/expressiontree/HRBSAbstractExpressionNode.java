package de.dralle.som.languages.hrbs.model.expressiontree;

import java.util.Map;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;

public abstract class HRBSAbstractExpressionNode implements Cloneable{
	public HRBSAbstractExpressionNode() {
		
	}
	@Override
	public HRBSAbstractExpressionNode clone()  {
		// TODO Auto-generated method stub
		try {
			return (HRBSAbstractExpressionNode) super.clone();
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
	public HRBSAbstractExpressionNode getResolvedExpressionTree(HRACModel parent) {
		HRBSAbstractExpressionNode clone = this.clone();
		clone=clone.resolve(parent);
		return this;
	}
	/**
	 * Resolves all directive nodes in this expression tree.
	 * @param allDirectives
	 * @return
	 */
	public HRBSAbstractExpressionNode resolve(HRACModel parent) {
		return this;
	}
	public abstract HRASAbstractExpressionNode compileToHRAS(HRACModel parent) ;

}
