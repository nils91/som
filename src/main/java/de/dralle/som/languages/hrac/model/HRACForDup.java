package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a single command or an entire HRACChildModel
 * @author Nils
 *
 */
public class HRACForDup {
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
