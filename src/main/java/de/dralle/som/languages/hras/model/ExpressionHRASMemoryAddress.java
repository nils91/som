/**
 * 
 */
package de.dralle.som.languages.hras.model;

/**
 * @author Nils
 *
 */
public class ExpressionHRASMemoryAddress extends AbstractHRASMemoryAddress implements Cloneable {
	private HRASAbstractExpressionNode expression;

	public ExpressionHRASMemoryAddress() {
		// TODO Auto-generated constructor stub
	}

	public ExpressionHRASMemoryAddress(HRASAbstractExpressionNode expression) {
		this.expression = expression;
	}

	public ExpressionHRASMemoryAddress(int accAddress) {
		this.expression = new HRASIntegerNode(accAddress);
	}

	public String asHRASCode() {
		return expression + super.asHRASCode();

	}

	@Override
	public ExpressionHRASMemoryAddress clone() {
		ExpressionHRASMemoryAddress copy = (ExpressionHRASMemoryAddress) super.clone();
		copy.expression = expression.clone();
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ExpressionHRASMemoryAddress) {
			boolean equal = expression.equals(((ExpressionHRASMemoryAddress) obj).expression) && super.equals(obj);
			return equal;
		}
		return false;
	}

	public HRASAbstractExpressionNode getexpression() {
		return expression;
	}

	@Override
	public int hashCode() {
		int hashc = expression.hashCode();
		return hashc + super.hashCode();
	}

	public int resolve(HRASModel model) {
		int address = expression.calculateNumericalValue();

		return address + super.resolve(model);
	}

	public void setexpression(HRASAbstractExpressionNode expression) {
		this.expression = expression;
	}

	public void setexpression(int address) {
		this.expression = new HRASIntegerNode(address);
	}

	@Override
	public String toString() {
		return asHRASCode();
	}
}
