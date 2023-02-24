/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils
 *
 */
public abstract class AbstractUnconditionalDebugPoint {
	private String name;
	public AbstractUnconditionalDebugPoint(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean update(int cmdAddress,Opcode op,int tgtAddress,ISomMemspace memspace) {
		return trigger(cmdAddress,op,tgtAddress,memspace);
	}

	public abstract boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace);
}
