package com.mammoth.logger.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mammoth.logger.Log;

public class ViewLogDialog extends Dialog {

	private Text text_1;
	private Text text_4;
	private Text text_3;
	private Text text_2;
	private Text text;
	private Text text_5;
	protected Object result;

	protected Shell shell;
	protected Log log;

	/**
	 * Create the dialog
	 * @param parent
	 * @param style
	 */
	public ViewLogDialog(Shell parent, Log log) {
		super(parent, SWT.NONE);
		this.log = log;
	}


	/**
	 * Open the dialog
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(426, 375);
		shell.setText("查看日志");

		final Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("行号");
		label.setBounds(23, 21, 95, 25);

		final Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(23, 52, 95, 25);
		label_1.setAlignment(SWT.RIGHT);
		label_1.setText("日期");
		
		final Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(23, 52, 95, 25);
		label_5.setAlignment(SWT.RIGHT);
		label_5.setText("时间");

		final Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(23, 83, 95, 25);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setText("日志级别");

		final Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(23, 114, 95, 25);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setText("资源");

		final Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(23, 174, 95, 25);
		label_4.setAlignment(SWT.RIGHT);
		label_4.setText("信息");

		text = new Text(shell, SWT.BORDER);
		text.setEditable(false);
		text.setBounds(124, 21, 289, 25);
		text.setText(log.getLineNumber().toString());
		
		text_5 = new Text(shell, SWT.BORDER);
		text_5.setEditable(false);
		text_5.setBounds(124, 21, 289, 25);
		text_5.setText(log.getDate().toString());

		text_2 = new Text(shell, SWT.BORDER);
		text_2.setEditable(false);
		text_2.setBounds(124, 52, 289, 25);
		text_2.setText(log.getTime());

		text_3 = new Text(shell, SWT.BORDER);
		text_3.setEditable(false);
		text_3.setBounds(124, 83, 289, 25);
		text_3.setText(log.getLevel());

		text_4 = new Text(shell, SWT.BORDER | SWT.WRAP);
		text_4.setEditable(false);
		text_4.setBounds(124, 114, 289, 54);
		text_4.setText(log.getSource());

		text_1 = new Text(shell, SWT.BORDER | SWT.WRAP);
		text_1.setEditable(false);
		text_1.setBounds(124, 174, 289, 108);
		text_1.setText(log.getMessage());

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setText("OK");
		okButton.setBounds(365, 311, 48, 22);
		
		okButton.addSelectionListener(new SelectionListener(){

			public void widgetSelected(SelectionEvent arg0) {	
				shell.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {			
			}
			
		});
	}

}
