package de.dralle.som.languages.hrbs.model;

public class AbstractHRBSRange implements Cloneable {
	private String runningDirectiveName;

	@Override
	public int hashCode() {
		return runningDirectiveName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractHRBSRange) {
			AbstractHRBSRange oth = (AbstractHRBSRange) obj;
			if (runningDirectiveName != null) {
				return runningDirectiveName.equals(oth.runningDirectiveName);
			}
		}
		return super.equals(obj);
	}

	@Override
	public AbstractHRBSRange clone() {
		// TODO Auto-generated method stub
		try {
			return (AbstractHRBSRange) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		String str = "";
		if(runningDirectiveName!=null) {
			str+="$"+runningDirectiveName+" = ";
		}
		return str;
	}

	public String getRunningDirectiveName() {
		if (runningDirectiveName == null) {
			return "i";// return default if not set otherwise
		}
		return runningDirectiveName;
	}

	public void setRunningDirectiveName(String runningDirectiveName) {
		this.runningDirectiveName = runningDirectiveName;
	}

}
