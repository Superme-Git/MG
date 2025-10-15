package com.mammoth.logger.gui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import com.mammoth.logger.Log;

public class TableViewerLabelPRovider implements ITableLabelProvider {

	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	public void dispose() {		
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}
	
	public String getColumnText(Object o, int col) {
		Log log = (Log) o;
		if (col == 0) {
			return log.getLineNumber().toString();
		} else if (col == 1) {
			return log.getDate();
		} else if (col == 2) {
			return log.getTime();
		} else if (col == 3) {
			return log.getLevel();
		} else if (col == 4) {
			return log.getSource();
		} else if (col == 5) {
			return log.getMessage();
		}
		return null;
	}

	public void addListener(ILabelProviderListener arg0) {
	}

	public void removeListener(ILabelProviderListener arg0) {
		
	}

}
