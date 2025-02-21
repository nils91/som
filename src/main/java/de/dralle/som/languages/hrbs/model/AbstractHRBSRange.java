package de.dralle.som.languages.hrbs.model;

public abstract class AbstractHRBSRange implements Cloneable {
	private String runningDirectiveName;

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
	public boolean equals(Object obj) {
		if (obj instanceof AbstractHRBSRange) {
			AbstractHRBSRange oth = (AbstractHRBSRange) obj;
			if (runningDirectiveName != null) {
				return runningDirectiveName.equals(oth.runningDirectiveName);
			}
		}
		return super.equals(obj);
	}

	public String getRunningDirectiveName() {
		if (runningDirectiveName == null) {
			return "i";// return default if not set otherwise
		}
		return runningDirectiveName;
	}

	@Override
	public int hashCode() {
		return runningDirectiveName.hashCode();
	}

	public void setRunningDirectiveName(String runningDirectiveName) {
		this.runningDirectiveName = runningDirectiveName;
	}

	@Override
	public String toString() {
		String str = "";
		if (runningDirectiveName != null) {
			str += "$" + runningDirectiveName + " = ";
		}
		return str;
	}

	/**
	 * Tries to calculate the size of this range. If directives were used to define
	 * this range the result might not be accurate.
	 * 
	 * @return
	 */
	public abstract int tryGetRangeSize();

}
