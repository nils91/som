package de.dralle.som.languages.hrbs.model.expressiontree;

import java.util.Map;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.HRBSModel;

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
	public abstract HRACAbstractExpressionNode compileToHRAC( ) ;

}
