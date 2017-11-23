package org.arc.megaburner;
/**
 * MegaBurner application window
 * @author mingzo@gmail.com
 * @since 2017/11/8
 */

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.io.IOException;

import org.arc.megaburner.helper.FileHelper;
import org.arc.megaburner.helper.HexHelper;
import org.arc.megaburner.helper.SerialHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MegaBurner {

	static {
	    System.loadLibrary("rxtxSerial");
	}
	
	private Widgets ui;

	private FileHelper fileHelper;
	
	private SerialHelper serialHelper;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MegaBurner window = new MegaBurner();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		//init UI widgets
		ui = Widgets.ui();

		fileHelper = new FileHelper();
		
		serialHelper = new SerialHelper();
		 
		//add events to widgets
		attachMenuEvents();
		attachEvents();
		attachCheckEvents();
		attachReadEvents();
		attachEraseEvents();
		attachFileEvents();
		attachWriteEvents();
		attachVerifyEvents();
		attachConsoleEvents();
		
		while (!ui.getShell().isDisposed()) {
			if (!Display.getDefault().readAndDispatch()) {
				Display.getDefault().sleep();
			}
		}
	}
	
	protected void listSerialPorts() {
		//Serial construct
		String[] comPorts = serialHelper.getAvailableSerialPorts();
		if (comPorts.length > 0) {
			ui.getSerialList().setItems(serialHelper.getAvailableSerialPorts());
			ui.getSerialList().select(0);
			
			Console.tip("");
		}
		else {
			Console.tip("No Serial");
		}
	}
	
	protected void openFile() {
		fileHelper.openDialog(ui.getShell());
		
		String selectedFile = fileHelper.getFilePath();
		if (selectedFile != null) {
			ui.setTxtFilePathText(selectedFile);
			ui.setTxtFilePathToolTipText(selectedFile);
			Console.log("Loaded \""+fileHelper.getFileName()+"\", size:" + Long.toString(fileHelper.getFileSize()) + " bytes.", false);
		}
	}

	/*
	 * menu item actions
	 */
	protected void attachMenuEvents() {
		//Window exit event
		ui.getShell().addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				try {
					serialHelper.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		//Menu --> File --> open
		ui.getMntmOpen().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openFile();
			}
		});
		
		//Menu --> File --> exit
		ui.getMntmExit().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ui.confirm("Exit MegaBurner now?")) {
					try {
						serialHelper.disconnect();
						ui.getShell().dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
		
		//Menu --> File --> about
		ui.getMntmAbout().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ui.info("Mega Burner EEPROM Programmer. \nmade by Max.(mingzo@gmail.com) \n\nCompatible with: \n MX29L3211(32Mbit/4MByte)");

			}
		});
	}
	
	protected void attachEvents() {
		//Serial construct
		listSerialPorts();
		
		ui.getSerialList().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				serialHelper.setSelected(ui.getSerialList().getSelectionIndex());
			}
		});
		
		//Serial refresh button
		ui.getBtnRefresh().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				listSerialPorts();
				ui.getSerialList().redraw();
			}
		});
		
		//Chip List
		ui.getChipList().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Chips.setSelected(ui.getChipList().getSelectionIndex());
			}
		});
	}

	/*
	 * check operation
	 */
	protected void attachCheckEvents() {
		// Check		
		ui.getBtnCheck().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ui.updateChipInfo(serialHelper.check());
						} catch (CommException e1) {
							Console.log(e1.getMessage(), false);
						}	
					}
				}).start();
				
			}
		});
	}
	
	/*
	 * erase operation
	 */
	protected void attachEraseEvents() {
		//Erase
		ui.getBtnErase().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				boolean result = ui.confirm("All the data on your chip will be ERASED, ARE YOU SURE?");
				if (result) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ui.disableBefore();
							try {
								if (!Chips.isChecked())
									ui.updateChipInfo(serialHelper.check());
								
								if (serialHelper.erase()) {
									ui.info("Chip erased!");
								} else {
									ui.info("Chip erase failed!");
								}
							} catch (CommException e1) {
								Console.log(e1.getMessage(), false);
							}

							ui.enableAfter();
						}
					}).start();
				}
			}
		});
	}
	
	/*
	 * read operation
	 */
	protected void attachReadEvents() {
		// Read
		ui.getBtnReadData().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						ui.disableBefore();

						try {
							if (!Chips.isChecked())
								ui.updateChipInfo(serialHelper.check());
							
							byte[] data = serialHelper.read();
							fileHelper.setDumpFileData(data);
							ui.setHexDataText(HexHelper.convertToHex(data));
						} catch (CommException e1) {
							Console.log(e1.getMessage(), false);
						}

						ui.enableAfter();
					}
				}).start();
			}
		});
	}
	
	/*
	 * file operation
	 */
	protected void attachFileEvents() {
		// Save
		ui.getBtnSave().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (fileHelper.getDumpFileData() == null) {
					ui.warning("No data to save, Read the chip first!");
				}
				else {
					if (fileHelper.saveDialog(ui.getShell())) {
						ui.info("File saved to \""+fileHelper.getDumpFilePath()+"\"");						
					}
				}
			}
		});
		
		//File dialog event
		ui.getBtnBrowse().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				openFile();
			}
		});
	}
	
	/*
	 * verify operation
	 */
	protected void attachVerifyEvents() {
		//Verify
		ui.getBtnVerify().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (fileHelper.getFileName() == null) {
					ui.warning("Load a binary file first!");
				}
				else if (fileHelper.getFileSize() > Chips.getCapacity()) {
					ui.warning("File size exceeded the maximum capacity of "+Chips.getName()+"("+Chips.getType()+")!");
				}
				else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ui.disableBefore();
							try {
								if (!Chips.isChecked())
									ui.updateChipInfo(serialHelper.check());
								
								byte[] data = serialHelper.verify(fileHelper.getFileData());
								fileHelper.setDumpFileData(data);
								ui.setHexDataText(HexHelper.convertToHex(data));
							} catch (CommException e1) {
								Console.log(e1.getMessage(), false);
							}

							ui.enableAfter();
						}
					}).start();					
				}
			}
		});		
	}

	/*
	 * write operation
	 */
	protected void attachWriteEvents() {
		//Write
		ui.getBtnWrite().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (fileHelper.getFileName() == null) {
					ui.warning("Load a binary file first!");
				}
				else if (fileHelper.getFileSize() > Chips.getCapacity()) {
					ui.warning("File size exceeded the maximum capacity of "+Chips.getName()+"("+Chips.getType()+")!");
				}
				else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							ui.disableBefore();
							try {
								if (!Chips.isChecked())
									ui.updateChipInfo(serialHelper.check());
								
								boolean erased = false;
								if (ui.getBoxEraseSelection())
									erased = serialHelper.erase();
								else 
									erased = true;
								
								if (erased)
									serialHelper.write(fileHelper.getFileData());
								else
									ui.warning("Chip erasing failed, write operation canceled!");
								
								if (ui.getBoxVerifySelection()) {
									byte[] data = serialHelper.verify(fileHelper.getFileData());
									fileHelper.setDumpFileData(data);
									ui.setHexDataText(HexHelper.convertToHex(data));
								}
								
							} catch (CommException e1) {
								Console.log(e1.getMessage(), false);
							}

							ui.enableAfter();
						}
					}).start();
				}
			}
		});		
	}

	/*
	 * Cancel the current operation
	 * Clear the text in the console message area
	 */
	protected void attachConsoleEvents() {	
		//Cancel
		ui.getBtnCancel().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean result = ui.confirm("Stop current job, ARE YOU SURE?");
				if (result) {
					try {
						serialHelper.disconnect();
						serialHelper.connect();
						
						if (serialHelper.isConnected()) {
							ui.enableAfter();
							Console.progressUpdate(0);
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CommException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		//Clear
		ui.getBtnClear().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Console.clear();
			}
		});		
	}
}
