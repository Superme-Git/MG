package com.mammoth.logger.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mammoth.logger.SearchCondition;

public class SearchDialog extends Dialog {

	private Text txtKeyWord;
	private Text txtClass;
	private Combo endMinute;
	private Combo endHour;
	private Combo startMinute;
	private Combo startHour;
	protected Object result;

	protected LogShell parent;
	protected Shell shell;
	
	public SearchDialog(LogShell shell){
		super(shell,SWT.NONE);
		this.parent = shell;
	}
	
	protected SearchCondition condition;
	public void setSearchCondition(SearchCondition condition){
		this.condition = condition;
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
		shell.setSize(400, 272);
		shell.setText("设置搜索条件");

		final Group group = new Group(shell, SWT.NONE);
		group.setText("选择日志出现时间");
		group.setBounds(10, 10, 380, 69);

		startHour = new Combo(group, SWT.NONE);
		startHour.setItems(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"});
		startHour.select(0);
		startHour.setBounds(10, 38, 53, 20);

		final Label label = new Label(group, SWT.NONE);
		label.setText("起始时间");
		label.setBounds(10, 20, 53, 12);

		startMinute = new Combo(group, SWT.NONE);
		startMinute.setItems(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
		startMinute.select(0);
		startMinute.setBounds(87, 38, 53, 20);
		
		final Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("：");
		label_1.setBounds(70, 40, 12, 12);

		final Label label_2 = new Label(group, SWT.NONE);
		label_2.setText("结束时间");
		label_2.setBounds(222, 20, 53, 12);

		endHour = new Combo(group, SWT.NONE);
		endHour.setItems(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"});
		endHour.select(23);
		endHour.setBounds(222, 38, 53, 20);

		final Label label_1_1 = new Label(group, SWT.NONE);
		label_1_1.setBounds(282, 40, 12, 12);
		label_1_1.setText("：");

		endMinute = new Combo(group, SWT.NONE);
		endMinute.setItems(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
		endMinute.select(59);
		endMinute.setBounds(299, 38, 53, 20);

		final Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("选择日志级别");
		group_1.setBounds(10, 85, 380, 48);

		final Button infoButton = new Button(group_1, SWT.CHECK);
		infoButton.setSelection(true);
		infoButton.setText("INFO");
		infoButton.setBounds(10, 21, 45, 16);

		final Button errorButton = new Button(group_1, SWT.CHECK);
		errorButton.setSelection(true);
		errorButton.setText("ERROR");
		errorButton.setBounds(61, 21, 51, 16);

		final Button debugButton = new Button(group_1, SWT.CHECK);
		debugButton.setSelection(true);
		debugButton.setText("DEBUG");
		debugButton.setBounds(137, 21, 61, 16);

		final Button warnButton = new Button(group_1, SWT.CHECK);
		warnButton.setSelection(true);
		warnButton.setText("WARN");
		warnButton.setBounds(204, 21, 61, 16);

		final Button fatalButton = new Button(group_1, SWT.CHECK);
		fatalButton.setSelection(true);
		fatalButton.setText("FATAL");
		fatalButton.setBounds(271, 21, 93, 16);

		final Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("类名");
		label_3.setBounds(10, 143, 50, 25);

		final Label label_3_1 = new Label(shell, SWT.NONE);
		label_3_1.setBounds(10, 175, 50, 25);
		label_3_1.setText("关键字");

		txtClass = new Text(shell, SWT.BORDER);
		txtClass.setBounds(70, 140, 320, 25);

		txtKeyWord = new Text(shell, SWT.BORDER);
		txtKeyWord.setBounds(70, 175, 245, 25);

		if(condition != null){		
			startHour.select(condition.getStartHour());
			startMinute.select(condition.getStartMinute());
			endHour.select(condition.getEndHour());
			endMinute.select(condition.getEndMinute());
			infoButton.setSelection(condition.infoSelect);
			errorButton.setSelection(condition.errorSelect);
			debugButton.setSelection(condition.debugSelect);
			warnButton.setSelection(condition.warnSelect);
			fatalButton.setSelection(condition.fatalSelect);
			txtClass.setText(condition.getClassName().trim());
			txtKeyWord.setText(condition.getKeyWord().trim());
		}
		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setText("OK");
		okButton.setBounds(10, 210, 90, 22);
		
		okButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				int startHH = startHour.getSelectionIndex();
				int startMin = startMinute.getSelectionIndex();
				boolean isConvers=true;//是否转换成功
                if (startHH == -1) {
                    try {
                        startHH = Integer.parseInt(startHour.getText());
                    } catch (NumberFormatException e) {
                        isConvers = false;
                    }
                    if (isConvers == false)
                        startHH = 0;
                }
                isConvers = true;
                if (startMin == -1) {
                    try {
                        startMin = Integer.parseInt(startMinute.getText());
                    } catch (NumberFormatException e) {
                        isConvers = false;
                    }
                    if (isConvers == false)
                        startMin = 0;
                }
				int endHH = endHour.getSelectionIndex();
				int endMin = endMinute.getSelectionIndex();
				isConvers=true;
                if (endHH == -1) {
                    try {
                        endHH = Integer.parseInt(endHour.getText());
                    } catch (NumberFormatException e) {
                        isConvers = false;
                    }
                    if (isConvers == false)
                        endHH = 23;
                }
                isConvers = true;
                if (endMin == -1) {
                    try {
                        endMin = Integer.parseInt(endMinute.getText());
                    } catch (NumberFormatException e) {
                        isConvers = false;
                    }
                    if (isConvers == false)
                        endMin = 59;
                }
				boolean info = infoButton.getSelection();
				boolean debug = debugButton.getSelection();
				boolean warn = warnButton.getSelection();
				boolean fatal = fatalButton.getSelection();
				boolean error = errorButton.getSelection();

				String clazz = txtClass.getText().trim();
				String keyword = txtKeyWord.getText().trim();
				
				SearchCondition condition = new SearchCondition();
				
				condition.setStartHour(startHH);
				condition.setStartMinute(startMin);
				condition.setEndHour(endHH);
				condition.setEndMinute(endMin);
				
				condition.setInfoSelect(info);
				condition.setDebugSelect(debug);
				condition.setWarnSelect(warn);
				condition.setFatalSelect(fatal);
				condition.setErrorSelect(error);
				
				condition.setClassName(clazz);
                if (keyword.startsWith("&") || keyword.startsWith("|")) {
                    MessageDialog.openWarning(null, "错误", "条件表达式不能以＆或｜开始。");
                    return;
                }
				condition.setKeyWord(keyword);
				
				condition.rebuild();
				shell.dispose();
				parent.setCondition(condition);
				
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {				
			}
		});
		
		
		final Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(290, 210, 100, 22);
		
		cancelButton.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				shell.dispose();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {				
			}
		});

		final Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(final SelectionEvent arg0) {
                String str="关键字规则\n  ＆:与，只能出现在表达式中间.\n｜:或,只能出现在表达式中间.\n！:非,可以出现在表达式中间或开头，如果出现在中间不需要其它操作符辅助。\n如果字符串中包括上述三中条件符，请以双引号括起.\n如搜索记录包含a但不包含b:!b＆a\n记录包含a或b:a|b\n对过于复杂的条件表达式，可能出现错误结果，最终解释权归MT_G项目组！";
                MessageDialog.openWarning(null, "错误", str);
           
            }
		});
		button.setText("表达式规则");
		button.setBounds(321, 174, 69, 26);
	}
	
	

}
