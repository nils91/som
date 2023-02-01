/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils
 *
 */
public abstract class SOMFileWriter<SOURCE,TARGET> {
	
	private SOMFormats sourceFormat;
	private SOMFormats targetFormat;
	public SOMFileWriter(SOMFormats sourceFormat, SOMFormats targetFormat) {
		super();
		this.sourceFormat = sourceFormat;
		this.targetFormat = targetFormat;
	}
	public abstract TARGET compile(SOURCE sourceModel);}
