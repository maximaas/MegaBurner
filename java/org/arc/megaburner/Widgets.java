/**
 * Build widgets in the window layout
 * 
 * @author mingzo@gmail.com
 * @since 2017/11/9
 */
package org.arc.megaburner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class Widgets {

	private static Widgets ui;
	
	private Widgets() {
		shlMegaBurner = new Shell(SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlMegaBurner.setImage(SWTResourceManager.getImage(Widgets.class, "/logo.png"));
		shlMegaBurner.setText("Mega Burner by Max.");		
		shlMegaBurner.setSize(790, 705);
		shlMegaBurner.setLayout(new GridLayout(1, true));
		
		createMenu();
		createFlashInfo();
		createFlashData();
		createRomFile();
		createConsole();
		
		shlMegaBurner.open();
		shlMegaBurner.layout();
	}
	
	public static Widgets ui() {
		if (ui == null)
			ui = new Widgets();
		return ui;
	}
	
	private Shell shlMegaBurner;
	
	public Shell getShell() {
		return shlMegaBurner;
	}

	//Menu
	private MenuItem mntmOpen;
	private MenuItem mntmExit;
	private MenuItem mntmAbout;
	
	//Flash Infos
	private Combo serialList;
	private Button btnRefresh;
	private Combo chipList;
	private Button btnCheck;
	private Button btnErase;
	private Button btnReadData;
	
	//Flash Data
	private CLabel chipStatus;
	private CLabel chipId;
	private CLabel chipType;
	private StyledText hexData;
	private Button btnSave;
	
	//Rom File
	private Text   txtFilePath;
	private Button btnBrowse;
	private Button btnVerify;
	private Button btnWrite;
	private Button boxErase;
	private Button boxVerify;
	
	//Console
	private StyledText consoleMsg;
	private CLabel tipMsg;
	private ProgressBar progressBar;
	private ToolItem btnCancel;
	private ToolItem btnClear;
	
	
	protected boolean confirm(String msg) {
		return MessageDialog.openConfirm(shlMegaBurner, "Confirm?", msg);
	}

	protected void warning(String msg) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openWarning(shlMegaBurner, "Warning!", msg);
			}
		});
	}
	
	protected void info(String msg) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(shlMegaBurner, "Information.", msg);
			}
		});
	}
	
	protected void createMenu() {
		Menu menu = new Menu(shlMegaBurner, SWT.BAR);
		shlMegaBurner.setMenuBar(menu);
		
		MenuItem menuItemFile = new MenuItem(menu, SWT.CASCADE);
		menuItemFile.setText("File");
		
		Menu menuFile = new Menu(menuItemFile);
		menuItemFile.setMenu(menuFile);
		
		mntmOpen = new MenuItem(menuFile, SWT.NONE);
		mntmOpen.setText("Open");
		
		new MenuItem(menuFile, SWT.SEPARATOR);
		
		mntmExit = new MenuItem(menuFile, SWT.NONE);
		mntmExit.setText("Exit");
		
		MenuItem menuHelp = new MenuItem(menu, SWT.CASCADE);
		menuHelp.setText("Help");
		
		Menu menu_2 = new Menu(menuHelp);
		menuHelp.setMenu(menu_2);
		
		mntmAbout = new MenuItem(menu_2, SWT.NONE);
		mntmAbout.setText("About");	
	}
	
	protected void createFlashInfo() {
		Label lblChipInfo = new Label(shlMegaBurner, SWT.NONE);
		lblChipInfo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblChipInfo.setText("Flash EEPROM: ");
		
		Label labelOne = new Label(shlMegaBurner, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelOne.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		labelOne.setText("labelOne");
		
		Composite compsiteChip = new Composite(shlMegaBurner, SWT.NONE);
		RowLayout rl_compsiteChip = new RowLayout(SWT.HORIZONTAL);
		rl_compsiteChip.fill = true;
		compsiteChip.setLayout(rl_compsiteChip);
		compsiteChip.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblSerial = new CLabel(compsiteChip, SWT.NONE);
		lblSerial.setText("Serial :");
		
		serialList = new Combo(compsiteChip, SWT.READ_ONLY);
		serialList.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NONE));
		serialList.setLayoutData(new RowData(45, SWT.DEFAULT));
		
		btnRefresh = new Button(compsiteChip, SWT.NONE);
		btnRefresh.setText("R");
		
		CLabel lblChoose = new CLabel(compsiteChip, SWT.NONE);
		lblChoose.setText("  Chip :");
		
		chipList = new Combo(compsiteChip, SWT.READ_ONLY);
		chipList.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		chipList.setItems(Chips.getNameList());
		chipList.select(0);
		
		Label splitter1 = new Label(compsiteChip, SWT.NONE);
		splitter1.setText(" ");
		
		btnCheck = new Button(compsiteChip, SWT.NONE);
		btnCheck.setText(" Check ");
		
		new Label(compsiteChip, SWT.NONE);
		
		btnErase = new Button(compsiteChip, SWT.NONE);
		btnErase.setText(" Erase ");
		
		new Label(compsiteChip, SWT.NONE);
		
		btnReadData = new Button(compsiteChip, SWT.NONE);
		btnReadData.setText(" Read Data ");
		
		new Label(compsiteChip, SWT.NONE);
	}

	protected void createFlashData() {
		Group groupFlashData = new Group(shlMegaBurner, SWT.NONE);
		groupFlashData.setLayout(new RowLayout(SWT.HORIZONTAL));
		groupFlashData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		groupFlashData.setText("Data");
		
		CLabel labelChipId = new CLabel(groupFlashData, SWT.NONE);
		labelChipId.setText("ID:");
		
		chipId = new CLabel(groupFlashData, SWT.NONE);
		chipId.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		chipId.setText("         ");
		
		CLabel labelChipStatus = new CLabel(groupFlashData, SWT.NONE);
		labelChipStatus.setText("Status:");
		
		chipStatus = new CLabel(groupFlashData, SWT.NONE);
		chipStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		chipStatus.setText("               ");

		CLabel labelChipType = new CLabel(groupFlashData, SWT.NONE);
		labelChipType.setText("Type:");
		
		chipType = new CLabel(groupFlashData, SWT.NONE);
		chipType.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		chipType.setText("                                              ");
		
		Label label = new Label(groupFlashData, SWT.NONE);
		label.setText("                                                                       ");
		
		btnSave = new Button(groupFlashData, SWT.NONE);
		btnSave.setText(" Save... ");
		
		hexData = new StyledText(groupFlashData, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		hexData.setFont(SWTResourceManager.getFont("Consolas", 11, SWT.NORMAL));
		hexData.setEditable(false);
		hexData.setLayoutData(new RowData(740, 285));
	}
	
	protected void createRomFile() {
		//vertical split
		Composite compositeSplit = new Composite(shlMegaBurner, SWT.NONE);
		GridData gd_compositeSplit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compositeSplit.heightHint = 2;
		compositeSplit.setLayoutData(gd_compositeSplit);
		
		//------------------------------------------------------------------------------
		Label lblRomFile = new Label(shlMegaBurner, SWT.NONE);
		lblRomFile.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.BOLD));
		lblRomFile.setText("Binary rom image: ");
		
		Label labelTwo = new Label(shlMegaBurner, SWT.SEPARATOR | SWT.HORIZONTAL);
		labelTwo.setText("labelTwo");
		labelTwo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeFile = new Composite(shlMegaBurner, SWT.NONE);
		RowLayout rl_compositeFile = new RowLayout(SWT.HORIZONTAL);
		rl_compositeFile.fill = true;
		compositeFile.setLayout(rl_compositeFile);
		compositeFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblFile = new CLabel(compositeFile, SWT.NONE);
		lblFile.setText("File:");
		
		txtFilePath = new Text(compositeFile, SWT.BORDER);
		txtFilePath.setEditable(false);
		txtFilePath.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		txtFilePath.setLayoutData(new RowData(366, SWT.DEFAULT));
		
		btnBrowse = new Button(compositeFile, SWT.NONE);
		btnBrowse.setText("Browse");
		
		new Label(compositeFile, SWT.NONE);
		
		btnVerify = new Button(compositeFile, SWT.NONE);
		btnVerify.setText(" Verify ");
		
		new Label(compositeFile, SWT.NONE);
		
		btnWrite = new Button(compositeFile, SWT.NONE);
		btnWrite.setText(" Write ");
		
		Label splitter8 = new Label(compositeFile, SWT.NONE);
		splitter8.setText("    ");
		
		boxErase = new Button(compositeFile, SWT.CHECK);
		boxErase.setSelection(true);
		boxErase.setText("erase");
		
		boxVerify = new Button(compositeFile, SWT.CHECK);
		boxVerify.setSelection(true);
		boxVerify.setText("verify");
	}
	
	protected void createConsole() {
		//vertical split
		Composite compositeSplit2 = new Composite(shlMegaBurner, SWT.NONE);
		GridData gd_compositeSplit2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_compositeSplit2.heightHint = 2;
		compositeSplit2.setLayoutData(gd_compositeSplit2);
		
		//console
		Group groupConsole = new Group(shlMegaBurner, SWT.NONE);
		groupConsole.setLayout(new RowLayout(SWT.HORIZONTAL));
		groupConsole.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		groupConsole.setText("Console");
		
		consoleMsg = new StyledText(groupConsole, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL);
		consoleMsg.setDoubleClickEnabled(false);
		consoleMsg.setEditable(false);
		consoleMsg.setLayoutData(new RowData(740, 88));
		
		//error
		Composite compositeProgress = new Composite(shlMegaBurner, SWT.NONE);
		RowLayout rl_compositeProgress = new RowLayout(SWT.HORIZONTAL);
		//rl_compositeProgress.fill = true;
		compositeProgress.setLayout(rl_compositeProgress);
		compositeProgress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tipMsg = new CLabel(compositeProgress, SWT.NONE);
		tipMsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipMsg.setLayoutData(new RowData(65, SWT.DEFAULT));
		tipMsg.setText("");
		
		//progress bar
		progressBar = new ProgressBar(compositeProgress, SWT.NONE);
		//progressBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		progressBar.setLayoutData(new RowData(645, 20));
		
		//init console 
		Console.init(consoleMsg, tipMsg, progressBar);
		
		
		ToolBar toolBar = new ToolBar(compositeProgress, SWT.FLAT | SWT.RIGHT);
		//toolBar.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		toolBar.setLayoutData(new RowData(55, SWT.DEFAULT));
		
		btnCancel = new ToolItem(toolBar, SWT.NONE);
		btnCancel.setImage(SWTResourceManager.getImage(this.getClass(), "/org/eclipse/jface/action/images/stop.png"));
		btnCancel.setToolTipText("Cancel current job");
		btnCancel.setEnabled(false);

		btnClear = new ToolItem(toolBar, SWT.NONE);
		btnClear.setImage(SWTResourceManager.getImage(this.getClass(), "/org/eclipse/jface/dialogs/images/message_error.png"));
		btnClear.setToolTipText("Clear console messages");
	}

	public void updateChipInfo(String chipId) {
		if (Chips.check(chipId)) {
			setChipStatusText(Chips.STATUS_MATCHED);
			setChipIdText(chipId);
			setChipTypeText(Chips.getType());
		} else {
			setChipStatusText(Chips.STATUS_MISMATCHED);
			setChipIdText(chipId);
		}
	}
	
	
	public MenuItem getMntmOpen() {
		return mntmOpen;
	}
	
	public MenuItem getMntmExit() {
		return mntmExit;
	}
	
	public MenuItem getMntmAbout() {
		return mntmAbout;
	}
	
	public Combo getSerialList() {
		return serialList;
	}

	public Button getBtnCheck() {
		return btnCheck;
	}

	public Button getBtnErase() {
		return btnErase;
	}

	public Button getBtnReadData() {
		return btnReadData;
	}

	public Button getBtnSave() {
		return btnSave;
	}
	
	public Text getTxtFilePath() {
		return txtFilePath;
	}

	public Button getBtnBrowse() {
		return btnBrowse;
	}

	public StyledText getConsoleMsg() {
		return consoleMsg;
	}

	public CLabel getTipMsg() {
		return tipMsg;
	}

	public Combo getChipList() {
		return chipList;
	}

	public Button getBtnRefresh() {
		return btnRefresh;
	}

	public CLabel getChipStatus() {
		return chipStatus;
	}

	public CLabel getChipId() {
		return chipId;
	}

	public CLabel getChipType() {
		return chipType;
	}

	public StyledText getHexData() {
		return hexData;
	}

	public Button getBtnVerify() {
		return btnVerify;
	}

	public Button getBtnWrite() {
		return btnWrite;
	}
	
	public ToolItem getBtnCancel() {
		return btnCancel;
	}

	public ToolItem getBtnClear() {
		return btnClear;
	}
	
	public boolean getBoxEraseSelection() {
		final boolean[] checked = new boolean[] {false};
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				checked[0] = boxErase.getSelection();
			}
		});
		return checked[0];
	}

	public boolean getBoxVerifySelection() {
		final boolean[] checked = new boolean[] {false};
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				checked[0] = boxVerify.getSelection();
			}
		});
		return checked[0];
	}

	public void setChipStatusText(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				chipStatus.setText(str);
			}
		});
	}

	public void setChipIdText(String str ) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				chipId.setText(str);
			}
		});
	}

	public void setChipTypeText(String str ) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				chipType.setText(str);
			}
		});
	}

	public void setHexDataText(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				hexData.setText(str);
			}
		});
	}

	public void appendHexDataText(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				hexData.append(str);
			}
		});
	}
	
	public void setTxtFilePathText(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				txtFilePath.setText(str);
			}
		});
	}

	public void setTxtFilePathToolTipText(String str) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				txtFilePath.setToolTipText(str);
			}
		});
	}
	
	public void setConsoleMsg(StyledText consoleMsg) {
		this.consoleMsg = consoleMsg;
	}

	public void enableAfter() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				btnCheck.setEnabled(true);
				btnErase.setEnabled(true);
				btnReadData.setEnabled(true);
				btnVerify.setEnabled(true);
				btnWrite.setEnabled(true);
				boxErase.setEnabled(true);
				boxVerify.setEnabled(true);
				
				btnBrowse.setEnabled(true);
				
				btnCancel.setEnabled(false);
			}
		});
	}
	
	public void disableBefore() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				btnCheck.setEnabled(false);
				btnErase.setEnabled(false);
				btnReadData.setEnabled(false);
				btnVerify.setEnabled(false);
				btnWrite.setEnabled(false);
				boxErase.setEnabled(false);
				boxVerify.setEnabled(false);
				
				btnBrowse.setEnabled(false);
				
				btnCancel.setEnabled(true);
			}
		});
	}
	
}
