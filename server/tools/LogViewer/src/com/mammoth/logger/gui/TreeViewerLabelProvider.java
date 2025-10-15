package com.mammoth.logger.gui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class TreeViewerLabelProvider implements ILabelProvider{

	public Image getImage(Object arg0) {
		return null;
	}
	
	public void addListener(ILabelProviderListener arg0) {		
	}

	public void dispose() {		
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}
	
	public String getText(Object obj) {
		TreeEntity entry = (TreeEntity)obj;
		return entry.getName();
	}
}
