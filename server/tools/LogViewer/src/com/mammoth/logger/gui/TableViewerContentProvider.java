package com.mammoth.logger.gui;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewerContentProvider  implements IStructuredContentProvider{

	public Object[] getElements(Object obj) {
		if(obj instanceof List){
			return ((List)obj).toArray();
		}
		return null;
	}

	public void dispose() {		
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

}
