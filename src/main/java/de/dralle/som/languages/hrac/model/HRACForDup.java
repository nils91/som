package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a single command or an entire HRACChildModel
 * @author Nils
 *
 */
public class HRACForDup implements Cloneable{
	private int special;
	private HRACForDupRange range=null;
	@Override
	protected HRACForDup clone() {
		HRACForDup clone = new HRACForDup();
		if(cmd!=null) {
			clone.cmd=cmd;
		}
		return clone;
	}
	private HRACCommand cmd=null;
	public List<HRACCommand> getCommands(){
		List<HRACCommand> returnList=new ArrayList<>();
		if(cmd!=null) {
			returnList.add(cmd);
		}
		return returnList;
	}
	public HRACForDup(HRACCommand cmd) {
		super();
		this.cmd = cmd;
	}
	public HRACForDup() {
		// TODO Auto-generated constructor stub
	}
	public String asCode() {
		if(cmd!=null) {
			return cmd.asCode();
		}
		return "";
	}
	@Override
	public String toString() {
		return "HRACForDup [asCode()=" + asCode() + "]";
	}
}
