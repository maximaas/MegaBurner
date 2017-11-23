/**
 * Message system
 * 
 * @author mingzo@gmail.com
 * @since 2017/11/9
 */
package org.arc.megaburner;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class Console {

	private StyledText logArea;
	private CLabel tipArea;
	private ProgressBar progressBar;
	
	private static Console console;
	
	private Console() {
//		logInfo = new HashMap<>();
	}

	public static void init(StyledText log, CLabel tip, ProgressBar progress) {
		if (console == null)
			console = new Console();
		console.logArea = log;
		console.tipArea = tip;
		console.progressBar = progress;
	}
	
	private void addLog(String text, boolean append) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!append && logArea.getText().length() > 0)
					logArea.append("\r\n");
				
				logArea.append("- ");
				logArea.append(text);
				
				logArea.setSelection(logArea.getText().length());
				
//				StyleRange styleRange = new StyleRange();
//				styleRange.start = logArea.getCharCount();
//				styleRange.length = text.length();
//				styleRange.fontStyle = SWT.BOLD;
//				styleRange.foreground = new Color(Display.getDefault(), 255, 127, 0);;
//				logArea.setStyleRange(styleRange);
			}
		});
	}
	
	private void clearLog() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				logArea.setText("");				
			}
		});
	}
	
	private void resetProgress(int maxValue) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				if (!progressBar.isDisposed()) {
					progressBar.setMaximum(maxValue);
					progressBar.setMinimum(0);
					progressBar.setSelection(0);	
				}
			}
		});
	}
	
	private void updateProgress(int currentValue) {
		Display.getDefault().syncExec(new Runnable() {
			
			//(progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT)
			
			@Override
			public void run() {
				if (!progressBar.isDisposed()) {
					progressBar.setSelection(currentValue);
				}
			}
		});
	}
	
	private void showTip(String text) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				tipArea.setText(text);		
			}
		});
	}

	public static void clear() {
		console.clearLog();
	}
	
	public static void log(String log, boolean append) {
		console.addLog(log, append);
	}
	
	public static void log(String log, long start, long end) {
		console.addLog(log + " (Time taken " + (end - start) + "ms)", true);
	}
	
	public static void tip(String tip) {
		console.showTip(tip);
	}

	public static void progressReset(int max) {
		console.resetProgress(max);
	}
	
	public static void progressUpdate(int value) {
		console.updateProgress(value);
	}
}
