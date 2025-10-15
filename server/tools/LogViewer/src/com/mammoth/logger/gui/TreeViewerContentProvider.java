package com.mammoth.logger.gui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import java.util.List;

public class TreeViewerContentProvider implements ITreeContentProvider {
	
	public Object[] getElements(Object obj) {
		if(obj instanceof List){
			List list = (List)obj;
			return list.toArray();
		} else {
			return new Object[0];
		}
	}

	public Object[] getChildren(Object obj) {
		TreeEntity entry = (TreeEntity)obj;
		List list = entry.getChildren();
		if(list == null || list.isEmpty()){
			return new Object[0];
		} else {
			return list.toArray();
		}
	}
	
	public boolean hasChildren(Object obj) {
		TreeEntity entry = (TreeEntity)obj;
		List list = entry.getChildren();
		if(list == null || list.isEmpty()){
			return false;
		} else {
			return true;
		}
	}
	
	
	public Object getParent(Object obj) {
		return null;
	}


	public void dispose() {
	}

	public void inputChanged(Viewer obj, Object arg1, Object arg2) {
	}

}
