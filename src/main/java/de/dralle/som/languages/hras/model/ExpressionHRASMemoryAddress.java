/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public class ExpressionHRASMemoryAddress extends AbstractHRASMemoryAddress implements Cloneable{
	private HRASAbstractExpressionNode expression;

	public ExpressionHRASMemoryAddress(int accAddress) {
		this.expression = new HRASIntegerNode(accAddress);
	}

	public ExpressionHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public ExpressionHRASMemoryAddress(HRASAbstractExpressionNode expression) {
		this.expression=expression;
	}

	public HRASAbstractExpressionNode getexpression() {
		return expression;
	}

	public void setexpression(HRASAbstractExpressionNode expression) {
		this.expression = expression;
	}
	public int resolve(HRASModel model) {
		int address = expression.calculateNumericalValue();
		
		return address + super.resolve(model);
	}
	@Override
	public int hashCode() {
		int hashc = expression.hashCode();
		return hashc+super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj!=null&&obj instanceof ExpressionHRASMemoryAddress) {
			boolean equal = expression.equals(((ExpressionHRASMemoryAddress)obj).expression)&&super.equals(obj);			
			return equal;
		}
		return false;
	}

	@Override
	public String toString() {
		return asHRASCode();
	}

	@Override
	public ExpressionHRASMemoryAddress clone() {
		ExpressionHRASMemoryAddress copy = (ExpressionHRASMemoryAddress) super.clone();
		copy.expression=expression.clone();	
		return copy;
	}

	public String asHRASCode() {
					return expression+super.asHRASCode();
		
	}

	public void setexpression(int address) {
		this.expression=new HRASIntegerNode(address);
	}
}
