package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public abstract class HRACAbstractDirectiveExpressionTreeNode implements Cloneable {
	public HRACAbstractDirectiveExpressionTreeNode() {

	}

	public <T> T accept(HRACDirectiveExpressionTreeVisitorInterface<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRACAbstractDirectiveExpressionTreeNode) super.clone();
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
	public HRACAbstractDirectiveExpressionTreeNode getResolvedExpressionTree(HRACModel parent) {
		return getResolvedExpressionTree(parent, null);
	}

	public HRACAbstractDirectiveExpressionTreeNode getResolvedExpressionTree(HRACModel parentClone, String[] strings) {
		HRACAbstractDirectiveExpressionTreeNode clone = this.clone();
		clone = clone.resolve(parentClone, strings);
		return clone;
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
	public HRACAbstractDirectiveExpressionTreeNode resolve(HRACModel parent) {
		return resolve(parent, null);
	}

	public HRACAbstractDirectiveExpressionTreeNode resolve(HRACModel parentClone, String[] strings) {
		return this;
	}

}
