/**
 * Chip
 * 
 * read and write block are defined here
 * 
 * @author mingzo@gmail.com
 * @since 2017/11/11
 */
package org.arc.megaburner.chips;

public abstract class Chip {

	protected int readBlock = 4096;
	
	protected int writeBlock = 4096;
	
	public abstract String getId();
	
	public abstract String getName();
	
	public abstract String getDisplayName();

	public abstract String getType();

	public abstract int getCapacity();

	public abstract int getPageSize();
	
	public abstract int getReadBlockCount();

	public int getReadBlockSize() {
		return readBlock;
	}

	public int getWriteBlockSize() {
		return writeBlock;
	}
}
