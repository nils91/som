package de.dralle.som.languages.hras.model;

public abstract class AbstractExpressionNode implements Cloneable{
	public AbstractExpressionNode() {
		
	}
	@Override
	public AbstractExpressionNode clone()  {
		// TODO Auto-generated method stub
		try {
			return (AbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public abstract int calculateNumericalValue();

}
