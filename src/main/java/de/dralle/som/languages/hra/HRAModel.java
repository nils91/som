/**
 * 
 */
package de.dralle.som.languages.hra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.languages.hra.model.Command;

/**
 * @author Nils
 *
 */
public class HRAModel {
	private int n;
	private int startAdress;
	private int heap;
	private Map<String,Integer> symbols;
	private Map<String,Integer> builtins;
	private List<Command> commands;
	private void setupBuiltins() {
		builtins=new HashMap<>();
		builtins.put("ACC",AbstractSomMemspace.ACC_ADDRESS);
		builtins.put("ADR_EVAL",AbstractSomMemspace.ADR_EVAL_ADDRESS);
		builtins.put("WH_EN",AbstractSomMemspace.WH_EN);
		builtins.put("N", AbstractSomMemspace.ADDRESS_SIZE_START);
		builtins.put("WH_COM",AbstractSomMemspace.WH_COM);
		builtins.put("WH_DIR",AbstractSomMemspace.WH_DIR);
		builtins.put("WH_SEL",AbstractSomMemspace.WH_SEL);
		builtins.put("ADR",AbstractSomMemspace.START_ADDRESS_START);
	}
	private int recalculateN() {
		int minN=Integer.MAX_VALUE;
		int minBitCount=getFixedBitCount()+symbols.size()+getCommandBits()+heap;
		int i=0;
		while(i<10000&&minBitCount>Math.pow(2, n)) {
			n++;
			 minBitCount=getFixedBitCount()+symbols.size()+getCommandBits()+heap;
			 if(minBitCount<getHighestTgtAddress()+1) {
				 minBitCount=getHighestTgtAddress()+1;
			 }
		}
		return n;
	}
	private int getHighestTgtAddress(){
		int high=0;
		for (Command command : commands) {
			if(command.getTgtAddress()>high) {
				high=command.getTgtAddress();
			}
		}
		return high;
	}
	private int getCommandBits() {
		int commandSize=getCommandSize();
		return commands.size()*commandSize;
	}
	private int getCommandSize() {
		return 1+n;
	}
	private int getFixedBitCount() {
		return 11+n;
	}
	private int getExitAddress() {
		return (int) (Math.pow(2, n)-getCommandSize());
	}
}
