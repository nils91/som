/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils
 *
 */
public abstract class AbstractCommandAddressListenerDP extends AbstractUnconditionalDebugPoint {
	private int trgAdr=-1;
	public AbstractCommandAddressListenerDP(String name,int trgAdr) {
		super(name);
		this.trgAdr = trgAdr;
	}

	@Override
	public abstract boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace);

	@Override
	public boolean update(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
		if(cmdAddress==trgAdr) {
			return super.update(cmdAddress, op, tgtAddress, memspace);
		}else {
			return true;
		}
		
	}
}
