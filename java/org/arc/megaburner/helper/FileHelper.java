/**
 * File Helper
 * @author mingzo@gmail.com
 * @since 2017/11/10
 */
package org.arc.megaburner.helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.arc.megaburner.Console;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class FileHelper {

	public static String FILE_DIALOG_PATH = "";
	public static String[] FILE_DIALOG_FILTER_NAMES = new String[] { "SFC ROM(*.sfc)", "FC ROM(*.nes)", "二进制文件(*.bin)", "所有文件(*.*)" };
	public static String[] FILE_DIALOG_FILTER_TYPES = new String[] { "*.sfc", "*.nes", "*.bin", "*.*" };
	
	//file from local
	private String fileName;
	private String filePath;
	private long fileSize;
	private byte[] fileData;
	
	//File from chip
	private String dumpFileName;
	private String dumpFilePath;
	private long dumpFileSize;
	private byte[] dumpFileData;
	
	public void openDialog(Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterPath(FILE_DIALOG_PATH);
		dialog.setText("Choose a rom image file");
		dialog.setFileName("");
		dialog.setFilterNames(FILE_DIALOG_FILTER_NAMES);
		dialog.setFilterExtensions(FILE_DIALOG_FILTER_TYPES);
		
		FILE_DIALOG_PATH = dialog.getFilterPath();
		
		filePath = dialog.open();
		
		if (filePath != null) {
			loadFile();
		}
	}
	
	public boolean saveDialog(Shell shell) {
		SaveDialog dialog = new SaveDialog(shell);
		dialog.setText("Save as...");
		dialog.setFileName("");
//		dialog.setFilterNames(FILE_DIALOG_FILTER_NAMES);
//		dialog.setFilterExtensions(FILE_DIALOG_FILTER_TYPES);
		
		dumpFilePath = dialog.open();
		
		if (dumpFilePath != null) {
			saveFile();
			return true;
		}
		return false;
	}
	
    public void loadFile() {
        try {
        	File file = new File(filePath);
        	fileSize = file.length();
        	fileName = file.getName();
        	fileData = new byte[(int)fileSize];
        	
        	BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));
            for (int i = 0; i < fileSize; i++) {
            	fileData[i] = (byte) bi.read();
            }
            bi.close();
        } catch (IOException e) {
        	Console.log(e.getMessage(), false);
        }
    }

    public void saveFile() {
        try {
        	FileOutputStream out = new FileOutputStream(dumpFilePath);
        	out.write(dumpFileData);
        	out.close();
        } catch (IOException e) {
        	Console.log(e.getMessage(), false);
        }
    }
    
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getDumpFileName() {
		return dumpFileName;
	}

	public void setDumpFileName(String dumpFileName) {
		this.dumpFileName = dumpFileName;
	}

	public String getDumpFilePath() {
		return dumpFilePath;
	}

	public void setDumpFilePath(String dumpFilePath) {
		this.dumpFilePath = dumpFilePath;
	}

	public long getDumpFileSize() {
		return dumpFileSize;
	}

	public void setDumpFileSize(long dumpFileSize) {
		this.dumpFileSize = dumpFileSize;
	}

	public byte[] getDumpFileData() {
		return dumpFileData;
	}

	public void setDumpFileData(byte[] dumpFileData) {
		this.dumpFileData = dumpFileData;
	}
}
