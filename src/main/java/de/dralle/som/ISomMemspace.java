/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils Dralle
 *
 */
public interface ISomMemspace extends IMemspace{
	int getAccumulatorAddress();
	
	int getAdrEvalAddress();
	
	int getWriteHookEnabledAddress();

	int getWriteHookDirectionAddress();

	int getWriteHookCommunicationAddress();

	int getWriteHookSelectAddress();
	
	boolean getAccumulatorValue();
	
	void setAccumulatorValue(boolean value);
	
	boolean isAdrEvalSet();
	
	void setAdrEval(boolean value);
	void setAdrEval();
	void clearAdrEval();
	
	boolean isWriteHookReadmode();

	boolean isWriteHookWritemode();

	boolean isWriteHookSelected();
	
	boolean isWriteHookSwitchmodeSelected();
	
	/**
	 * The value N says how long (in bits) each address is.
	 * @return
	 */
	int getN();
	
	void setN(int n);

	/**
	 * Address bits. If ADR_EVAL is set, these contain the next memory address, from
	 * which execution will continue. This will NOT contain always the next evaluated memory address.
	 * 
	 * @return
	 */
	int getNextAddress();
	
	void setNextAddress(int address);
	
	int getBitsUnsignedBounds(int lowerBound, int upperBound);

	void setBitsUnsignedBounds(int lowerBound, int upperBound, int value);
	
	int getBitsUnsigned(int lowerBound, int n) ;
	
	void setBitsUnsigned(int lowerBound, int n, int value) ;

}
