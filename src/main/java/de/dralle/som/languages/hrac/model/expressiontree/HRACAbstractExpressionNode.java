package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public abstract class HRACAbstractExpressionNode implements Cloneable {
	public HRACAbstractExpressionNode() {

	}

	public abstract int calculateNumericalValue();

	@Override
	public HRACAbstractExpressionNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRACAbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract HRASAbstractExpressionNode compileToHRAS(HRACModel parent);

	public abstract HRBSAbstractExpressionNode compileToHRBS();

	/**
	 * Returns the very same expression tree as this with its directive nodes
	 * resolved. To avoid modification of the original tree the returned one will be
	 * a clone.
	 * 
	 * @param allDirectives
	 * @return
	 */
	public HRACAbstractExpressionNode getResolvedExpressionTree(HRACModel parent) {
		return getResolvedExpressionTree(parent, null);
	}

	public HRACAbstractExpressionNode getResolvedExpressionTree(HRACModel parentClone, String[] strings) {
		HRACAbstractExpressionNode clone = this.clone();
		clone = clone.resolve(parentClone, strings);
		return this;
	}

	public Collection<String> getUsedDirectives() {
		return new ArrayList<String>();
	}

	/**
	 * Resolves all directive nodes in this expression tree.
	 * 
	 * @param allDirectives
	 * @return
	 */
	public HRACAbstractExpressionNode resolve(HRACModel parent) {
		return resolve(parent, null);
	}

	public HRACAbstractExpressionNode resolve(HRACModel parentClone, String[] strings) {
		return this;
	}

}
