package com.mammoth.logger.gui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

import com.mammoth.logger.Log;
import com.mammoth.logger.SearchCondition;
import com.mammoth.util.TimeStamp;

public class LogShell extends Shell {
 
	private static LogShell shell;
    public static boolean isSearchStop=false;
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			shell = new LogShell(display, SWT.SHELL_TRIM);
			shell.setImage(new Image(display,"icon.gif"));
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			shell.wait.dispose();
			e.printStackTrace();
		}
	}

	public LogShell(Display display, int style) {
		super(display, style);
		createContents();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("LogViewer");
		setSize(800, 600);

		setLayout(new FillLayout());
		wait = new WaitDialog();
		
		final SashForm sashForm = new SashForm(this, SWT.NONE);

		
		tree = createTreeViewer(sashForm);

		final TabFolder tabFolder = new TabFolder(sashForm, SWT.NONE);

		final TabItem currentItem = new TabItem(tabFolder, SWT.NONE);
		currentItem.setText("当前");

		final TabItem searchItem = new TabItem(tabFolder, SWT.NONE);
		searchItem.setText("搜索");

		final Composite comp = new Composite(tabFolder,SWT.NONE);
		comp.setLayout(new FillLayout());
		final Composite comp2 = new Composite(tabFolder,SWT.NONE);
		comp2.setLayout(new FillLayout());
		currentItem.setControl(comp);
		searchItem.setControl(comp2);
		
		
		currntTable = createTableViewer(comp, currentLog);
		searchTable = createTableViewer(comp2, searchLog);
		
		tabFolder.setSelection(0);
				
		sashForm.setWeights(new int[] {25, 80 });
		
		
		final Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		menuItem.setText("文件(&F)");

		final Menu menu_1 = new Menu(menuItem);
		menuItem.setMenu(menu_1);

		final MenuItem openDirectory = new MenuItem(menu_1, SWT.NONE);
		openDirectory.setText("打开目录");
		openDirectory.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialog = new DirectoryDialog(shell);
				dialog.setText("打开目录");
				dialog.setMessage("请选择一个目录");
				String dir = dialog.open();
				if(dir != null){
					openDirectory(dir);
				}
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		final MenuItem openFile = new MenuItem(menu_1, SWT.NONE);
		openFile.setText("打开文件");
		openFile.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setText("打开文件");              
				dialog.setFilterNames(new String[]{"日志文件(*.log)","文本文件(*.txt)"});
				dialog.setFilterExtensions(new String[]{"*.log","*.txt"});
				String file = dialog.open();
				if(file != null){
					openFile(file);
				}
			}

			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		new MenuItem(menu_1, SWT.SEPARATOR);

		final MenuItem exit = new MenuItem(menu_1, SWT.NONE);
		exit.setText("退出");
		exit.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		final MenuItem settings = new MenuItem(menu, SWT.CASCADE);
		settings.setText("设置");

		final Menu menu_3 = new Menu(settings);
		settings.setMenu(menu_3);

		final MenuItem utfChoose = new MenuItem(menu_3, SWT.RADIO);
		utfChoose.setSelection(true);
		utfChoose.setText("UTF-8");
		utfChoose.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				setEncoding("UTF-8");
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});


		final MenuItem GBKChoose = new MenuItem(menu_3, SWT.RADIO);
		GBKChoose.setText("GBK");
		GBKChoose.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				setEncoding("GBK");
                
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		final MenuItem search = new MenuItem(menu, SWT.NONE);
		search.setText("搜索");
		search.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent arg0) {
				searchDialog.setSearchCondition(condition);
				searchDialog.open();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		final MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dialog = new FileDialog(shell);
				dialog.setText("导出文件");
				dialog.setFilterNames(new String[]{"日志文件(*.log)"});
				dialog.setFilterExtensions(new String[]{"*.log"});
				String file = dialog.open();
				if(file != null){
					if(file.contains(".log")){
						export(file);
					} else {
						export(file + ".log");
					}
				}
			}


		});
		menuItem_1.setText("导出");

		final MenuItem menuItem_6 = new MenuItem(menu, SWT.CASCADE);
		menuItem_6.setText("帮助");

		final Menu menu_2 = new Menu(menuItem_6);
		menuItem_6.setMenu(menu_2);

		final MenuItem menuItem_7 = new MenuItem(menu_2, SWT.NONE);
		menuItem_7.setText("帮助");

		final MenuItem menuItem_8 = new MenuItem(menu_2, SWT.NONE);
		menuItem_8.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				showBuildTime();
			}
		});
		menuItem_8.setText("关于");	
	}
	
	private void setEncoding(String encoding){
		if(encoding == null) return;
		wait.open();
		logEntity.setEncoding(encoding);
		tree.refresh();
		currntTable.refresh();
		search();
		searchTable.refresh();
		wait.dispose();
	}
	
	private void openDirectory(String directory) {
		wait.open();
		logEntity.setDirectory(directory);
		logEntity.initDirectory();
		tree.refresh();
		search();
		searchTable.refresh();
		wait.dispose();
	}
	
	private void openFile(String file){
		wait.open();
		long start = System.currentTimeMillis();
		logEntity.setFile(file);
		logEntity.initFile();
		tree.refresh();
		long used = System.currentTimeMillis() - start;
		System.out.println("USE TIME:" + used);
		wait.dispose();
	}
	
	protected TreeViewer createTreeViewer(Composite parent){
		final TreeViewer tree = new TreeViewer(parent,SWT.BORDER);
		tree.setContentProvider(new TreeViewerContentProvider());
		tree.setLabelProvider(new TreeViewerLabelProvider());
		tree.setInput(logEntity.getChildren());

		IDoubleClickListener listener = new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
		        TreeEntity te;
		        IStructuredSelection selection = (IStructuredSelection)tree.getSelection();
		        te = (TreeEntity)selection.getFirstElement();
		        if(te instanceof LogFileEntity){
		        	LogFileEntity log = (LogFileEntity)te;
		        	wait.open();
		        	long start = System.currentTimeMillis();
		        	log.init();
		        	tree.refresh();
		        	long used = System.currentTimeMillis() - start;
		        	System.out.println("USED TIME:" + used);
		        	wait.dispose();
		        } else if(te instanceof LineEntity){
		        	wait.open();
		        	try{
		        		LineEntity line = (LineEntity)te;
			        	currentLog = line.getViewList();
			        	currntTable.setInput(currentLog);
			        	tree.refresh();
			        	currntTable.refresh();
                        long current=System.currentTimeMillis();
			        	search();
                        System.out.println("Search　Used Time :"  + (System.currentTimeMillis()-current));
			        	searchTable.refresh();
		        	} finally{
		        		wait.dispose();
		        	}
		        }
			}
		};
		tree.addDoubleClickListener(listener);
		return tree;
	}
	

	protected TableViewer createTableViewer(Composite parent,List input){
		final TableViewer tableViewer = new TableViewer(parent, SWT.MULTI|SWT.BORDER | SWT.FULL_SELECTION);
		
		Table table = tableViewer.getTable();
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableLayout tLayout = new TableLayout();
		table.setLayout(tLayout);
		tLayout.addColumnData(new ColumnWeightData(8));
		new TableColumn(table,SWT.NONE).setText("行号");
		
		tLayout.addColumnData(new ColumnWeightData(15));
		new TableColumn(table,SWT.NONE).setText("日期");
		
		tLayout.addColumnData(new ColumnWeightData(15));
		new TableColumn(table,SWT.NONE).setText("时间");
		
		tLayout.addColumnData(new ColumnWeightData(8));
		new TableColumn(table,SWT.NONE).setText("级别");
		
		tLayout.addColumnData(new ColumnWeightData(20));
		new TableColumn(table,SWT.NONE).setText("资源");
		
		tLayout.addColumnData(new ColumnWeightData(47));
		new TableColumn(table,SWT.NONE).setText("消息");
		
		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new TableViewerLabelPRovider());

		tableViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				Log log = (Log)selection.getFirstElement();
				if(log != null){
					ViewLogDialog dlg = new ViewLogDialog(shell,log);
					dlg.open();
				}
			}
			
		});
		tableViewer.setInput(input);
		
		//右键菜单
		MenuManager menuManager = new MenuManager();
		menuManager.add(new MarkAction(tableViewer));
		menuManager.add(new UnmarkAction(tableViewer));
		Menu menu = menuManager.createContextMenu(tableViewer.getTable());
		tableViewer.getTable().setMenu(menu);
		
		
		return tableViewer;
	}
	
	protected void search(){
		if(condition == null){
			return;
		}
		searchLog.clear();
        isSearchStop=false;
		for(Log log:currentLog){
			log.unmarkSearch();
			if(condition.match(log)&&isSearchStop==false){
				log.markSearch();
				searchLog.add(log);
			}
		}
		//设置为颜色
		Table t = this.currntTable.getTable();
		t.selectAll();
		for(TableItem item:t.getSelection()){
			Log log = (Log)item.getData();
			if(log.isMarked() == false && log.isSearched()){
				item.setBackground(MarkAction.yellow);
			}
            else
            {
                item.setBackground(MarkAction.white);
            }
           
		}
		t.deselectAll();
	}
	
	protected TreeViewer tree;
	protected TableViewer currntTable;
	protected TableViewer searchTable;
	protected WaitDialog wait;
	
	protected SearchDialog searchDialog = new SearchDialog(this);
	
	LogEntity logEntity = new LogEntity();
	List<Log> currentLog = new ArrayList<Log>();
	List<Log> searchLog = new ArrayList<Log>();
	

	protected void checkSubclass() {
	}

	private void export(String file) {
		System.out.println("Export File:" + file);
		wait.open();
		try{
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));			
			for(Object o:logEntity.getChildren()){
				if(o instanceof LogFileEntity){
					LogFileEntity log = (LogFileEntity)o;
					for(Object ob:log.getChildren()){
						if(ob instanceof LineEntity){
							LineEntity te = (LineEntity)ob;
							List<Log> logs = te.getViewList();
							for(Log l:logs){
								if(condition.match(l)){
									writer.println(l.getOrigin());
								}
							}
						}
					}
				}
			}
			writer.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		wait.dispose();	
	}
	
	private SearchCondition condition;
	public void setCondition(SearchCondition c) {
		condition = c;
		wait.open();
		search();
		searchTable.refresh();
		wait.dispose();
	}	
	
	private void showBuildTime() {
		MessageDialog.openInformation(shell, "版本信息", TimeStamp.getTimeStampInformation());
	}
}

