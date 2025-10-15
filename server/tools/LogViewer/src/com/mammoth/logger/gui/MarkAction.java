package com.mammoth.logger.gui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

import com.mammoth.logger.Log;


public class MarkAction extends Action{
	public static Color blue = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN); 
	public static Color yellow = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
    public static Color white=Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	private TableViewer tv;
	public MarkAction(TableViewer tv){
		this.tv = tv;
		setText("±ê¼Ç");
	}
	
	public void run(){
		for(TableItem table:tv.getTable().getSelection()){
			table.setBackground(blue);
		}				
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		Log log = (Log)selection.getFirstElement();
		log.mark();
	}

}
