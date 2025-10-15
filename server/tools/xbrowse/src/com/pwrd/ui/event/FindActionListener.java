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
 * ����<code>FindPanel</code>�����¼��ļ����������û�������Ҵʣ�
 * �������ʱ��ϵͳ�ͻ�����û��Ĳ��Ҵʲ���ƥ���<code>XTable</code>
 * ��<code>XBean</code>��
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
