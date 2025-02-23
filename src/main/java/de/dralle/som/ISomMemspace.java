/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public interface ISomMemspace extends IMemspace, ISetN {

	static final int ACC_ADDRESS = 0;
	static final int START_ADDRESS_START = 11;
	static final int ADDRESS_SIZE_BIT_COUNT = 5;
	public static final int ADDRESS_SIZE_START = 3;
	public static final int ADDRESS_SIZE_OFFSET = 4;
	public static final int WH_EN = 2;
	public static final int WH_COM = 8;
	public static final int WH_SEL = 10;
	public static final int WH_DIR = 9;
	public static final int ADR_EVAL_ADDRESS = 1;
	public static final int MINIMUM_ADDRESS_SIZE = ADDRESS_SIZE_OFFSET;
	public static final int MAXIMUM_ADDRESS_SIZE = (int) (Math.pow(2, ADDRESS_SIZE_BIT_COUNT) + ADDRESS_SIZE_OFFSET);

	void clearAdrEval();

	@Override
	ISomMemspace clone();

	int getAccumulatorAddress();

	boolean getAccumulatorValue();

	int getAdrEvalAddress();

	int getBitsUnsigned(int lowerBound, int n);

	int getBitsUnsignedBounds(int lowerBound, int upperBound);

	/**
	 * The value N says how long (in bits) each address is.
	 * 
	 * @return
	 */
	int getN();

	/**
	 * Address bits. If ADR_EVAL is set, these contain the next memory address, from
	 * which execution will continue. This will NOT contain always the next
	 * evaluated memory address.
	 * 
	 * @return
	 */
	int getNextAddress();

	int getWriteHookCommunicationAddress();

	boolean getWriteHookCommunicationBit();

	int getWriteHookDirectionAddress();

	int getWriteHookEnabledAddress();

	int getWriteHookSelectAddress();

	boolean isAdrEvalSet();

	boolean isWriteHookEnabled();

	boolean isWriteHookReadmode();

	boolean isWriteHookSelected();

	boolean isWriteHookSwitchmodeSelected();

	boolean isWriteHookWritemode();

	void setAccumulatorValue(boolean value);

	void setAdrEval();

	void setAdrEval(boolean value);

	void setBitsUnsigned(int lowerBound, int n, int value);

	void setBitsUnsignedBounds(int lowerBound, int upperBound, int value);

	void setN(int n);

	void setNextAddress(int address);

	void setWriteHookCommunicationBit(boolean value);

	void setWriteHookDirectionBit(boolean value);

}
