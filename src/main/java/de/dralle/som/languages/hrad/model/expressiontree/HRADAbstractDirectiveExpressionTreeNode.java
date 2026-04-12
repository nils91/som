package de.dralle.som.languages.hrad.model.expressiontree;

import java.util.ArrayList;
import java.util.Collection;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;

public abstract class HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	
	private HRADSourceLocation sourceLocation;
	
	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public HRADAbstractDirectiveExpressionTreeNode(HRADSourceLocation sourceLocation) {
		super();
		this.sourceLocation = sourceLocation;
	}

	public HRADAbstractDirectiveExpressionTreeNode() {

	}

	public <T> T accept(HRADDirectiveExpressionTreeVisitorInterface<T> visitor) {
		return visitor.visitSwitch(this);
	}

	public <T> T postAccept(HRADDirectiveExpressionTreeVisitorInterface<T> visitor, T rv) {
		return rv;

	}

	public boolean preAccept(HRADDirectiveExpressionTreeVisitorInterface<?> visitor) {
		return true;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRADAbstractDirectiveExpressionTreeNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract HRASAbstractExpressionNode compileToHRAS(HRADModel parent);

	public abstract HRBSAbstractExpressionNode compileToHRBS();


}
