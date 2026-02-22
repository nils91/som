package de.dralle.som.languages.hrac.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public abstract class HRACAbstractDirectiveExpressionTreeNode implements Cloneable{
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

	
	public Collection<String> getUsedDirectives() {
		return new ArrayList<String>();
	}

	

}
