package de.dralle.som.languages.hrac.model.expressiontree;

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

}
