package com.pwrd.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.javadocking.dock.TabDock;
import com.javadocking.dockable.Dockable;
import com.pwrd.ui.FindPanel;
import com.pwrd.ui.XBeanPanel;
import com.pwrd.ui.XTablePanel;
import com.pwrd.ui.XdbBrowserFrame;

/**
 * 监听<code>FindPanel</code>查找事件的监听器，当用户输入查找词，
 * 点击查找时，系统就会根据用户的查找词查找匹配的<code>XTable</code>
 * 或<code>XBean</code>。
 * 
 * @author wp853
 *
 */
public class FindActionListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		TabDock xtableXBeanTabDock = XdbBrowserFrame.getXTableXBeanTabDock();
		FindPanel findPanel = XdbBrowserFrame.getFindPanel();
		
		Dockable dockable = xtableXBeanTabDock.getSelectedDockable();
		if("xbean".equals(dockable.getID()))
		{
			XBeanPanel xbeanPanel = (XBeanPanel)dockable.getContent();
			xbeanPanel.find(findPanel.getQuery(), findPanel.isQueryChanged(), true);
		}
		else if("xtable".equals(dockable.getID()))
		{
			XTablePanel xtablePanel = (XTablePanel)dockable.getContent();
			xtablePanel.find(findPanel.getQuery(), findPanel.isQueryChanged(), true);
		}
	}
	
}
