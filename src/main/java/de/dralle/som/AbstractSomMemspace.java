/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public abstract class AbstractSomMemspace implements ISomMemspace {

	private static final int ACC_ADDRESS = 0;
	private static final int START_ADDRESS_START = 9;
	private static final int ADDRESS_SIZE_END = 7;
	private static final int ADDRESS_SIZE_START = 1;
	private static final int ADR_EVAL_ADDRESS = 8;

	@Override
	public int getAccumulatorAddress() {
		return ACC_ADDRESS;
	}

	@Override
	public abstract AbstractSomMemspace clone();
		
	public boolean equalContent(IMemspace obj) {
		if(obj instanceof IMemspace) {
			IMemspace cTo = (IMemspace)obj;
			if(getSize()==cTo.getSize()) {
				for (int i = 0; i < getSize(); i++) {
					if(getBit(i)!=cTo.getBit(i)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void copy(IMemspace from) {
		for (int i = 0; i < from.getSize(); i++) {
			if(i<getSize()) {
				setBit(i, from.getBit(i));
			}
		}		
	}

	@Override
	public int getAdrEvalAddress() {
		return ADR_EVAL_ADDRESS;
	}

	@Override
	public int getWriteHookEnabledAddress() {
		return getAdrEvalAddress()+getN()+1;
	}

	@Override
	public int getWriteHookDirectionAddress() {
		return getWriteHookEnabledAddress()+1;
	}

	@Override
	public int getWriteHookCommunicationAddress() {
		return getWriteHookDirectionAddress()+1;
	}

	@Override
	public int getWriteHookSelectAddress() {
		return getWriteHookCommunicationAddress()+1;
	}

	@Override
	public boolean getAccumulatorValue() {
		return getBit(getAccumulatorAddress());
	}

	@Override
	public void setAccumulatorValue(boolean value) {
		setBit(getAccumulatorAddress(), value);
	}

	@Override
	public boolean isAdrEvalSet() {
		return getBit(getAdrEvalAddress());
	}

	@Override
	public void setAdrEval(boolean value) {
		setBit(getAdrEvalAddress(), value);
	}

	@Override
	public void setAdrEval() {
		setAdrEval(true);
	}

	@Override
	public void clearAdrEval() {
		setAdrEval(false);
	}

	@Override
	public boolean isWriteHookEnabled() {
		return getBit(getWriteHookEnabledAddress());
	}

	@Override
	public boolean getWriteHookCommunicationBit() {
		return getBit(getWriteHookCommunicationAddress());
	}

	@Override
	public void setWriteHookCommunicationBit(boolean value) {
		setBit(getWriteHookCommunicationAddress(), value);		
	}

	@Override
	public void setWriteHookDirectionBit(boolean value) {
		setBit(getWriteHookDirectionAddress(), value);		
	}

	@Override
	public boolean isWriteHookReadmode() {
		return !getBit(getWriteHookDirectionAddress());
	}

	@Override
	public boolean isWriteHookWritemode() {
		return getBit(getWriteHookDirectionAddress());
	}

	@Override
	public boolean isWriteHookSelected() {
		return getBit(getWriteHookSelectAddress());
	}

	@Override
	public boolean isWriteHookSwitchmodeSelected() {
		return !getBit(getWriteHookSelectAddress());
	}

	@Override
	public int getN() {
		return getBitsUnsignedBounds(ADDRESS_SIZE_START, ADDRESS_SIZE_END + 1);
	}

	@Override
	public void setN(int n) {
		setBitsUnsignedBounds(ADDRESS_SIZE_START, ADDRESS_SIZE_END + 1, n);
		}

	@Override
	public int getNextAddress() {
		return getBitsUnsignedBounds(START_ADDRESS_START, START_ADDRESS_START + getN());
	}

	@Override
	public void setNextAddress(int address) {
		setBitsUnsignedBounds(START_ADDRESS_START, START_ADDRESS_START+getN(), address);

	}

	@Override
	public int getBitsUnsignedBounds(int lowerBound, int upperBound) {
		int bits = upperBound - lowerBound;
		int returnValue = 0;
		for (int i = 0; i < bits; i++) {
			if (getBit(lowerBound + i)) {
				int bitmask = 1 << (bits - i - 1);
				returnValue = returnValue | bitmask;
			}
		}
		return returnValue;
	}

	@Override
	public void setBitsUnsignedBounds(int lowerBound, int upperBound, int value) {
		int bits = upperBound - lowerBound;
		for (int i = 0; i < bits; i++) {
			boolean bitValue = value - Math.pow(2, bits - i - 1) >= 0 ? true : false;
			if (bitValue) {
				value -= Math.pow(2, bits - i - 1);
			}
			setBit(lowerBound + i, bitValue);
		}
	}

	@Override
	public int getBitsUnsigned(int lowerBound, int n) {
		return getBitsUnsignedBounds(lowerBound, lowerBound + n);
	}

	@Override
	public void setBitsUnsigned(int lowerBound, int n, int value) {
		setBitsUnsignedBounds(lowerBound, lowerBound + n, value);

	}

}
