package com.mammoth.logger.gui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.mammoth.logger.Log;


public class UnmarkAction extends Action{
	private static Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE); 
	private TableViewer tv;
	public UnmarkAction(TableViewer tv){
		this.tv = tv;
		setText("取消标记");
	}
	
	public void run(){
		for(TableItem table:tv.getTable().getSelection()){
			table.setBackground(white);
		}	
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Log log = (Log)selection.getFirstElement();
		log.unmark();
	}

}