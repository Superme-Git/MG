package com.pwrd.ui.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.javadocking.dock.TabDock;
import com.pwrd.definition.DatabaseMetaDataWrapper;
import com.pwrd.ui.QueryPanel;
import com.pwrd.ui.QueryResultPanel;
import com.pwrd.ui.XBeanPanel;
import com.pwrd.ui.XdbBrowserFrame;

/**
 * ����<code>XTablePanel</code>����¼��ļ����������û�˫��<code>XTablePanel</code>��
 * <code>XBean</code>���ʱ���ܹ���ת��<code>XBeanPanel</code>����Ӧ��<code>XBean</code>
 * ��㣬����ʾ����ϸ����Ϣ��
 * 
 * @author wp853
 *
 */
public class XTableMouseListener extends MouseAdapter
{
	@Override
	public void mousePressed(MouseEvent e)
	{
		TabDock topLeftTabDock = XdbBrowserFrame.getXTableXBeanTabDock();
		XBeanPanel xbeanPanel = XdbBrowserFrame.getXBeanPanel();
		
		if(e.getClickCount() == 2) // ��������˫��
		{
			JTree tableTree = (JTree)e.getSource();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tableTree.getLastSelectedPathComponent();
			if(DatabaseMetaDataWrapper.getInstance().containsBean(selectedNode.getUserObject().toString()))
			{
				for(int i = 0; i < topLeftTabDock.getDockableCount(); ++ i)
				{
					if(topLeftTabDock.getDockable(i).getID().equals("xbean"))
					{
						topLeftTabDock.setSelectedDockable(topLeftTabDock.getDockable(i));
						break;
					}
				}
				xbeanPanel.find(selectedNode.getUserObject().toString(), false, false);
			}
			else if(DatabaseMetaDataWrapper.getInstance().containsTable(selectedNode.getUserObject().toString())) // ���ѡ�е��Ǳ��㣬�����ɲ�ѯ���
			{
				String tableName = selectedNode.getUserObject().toString();
				TabDock tabDock = XdbBrowserFrame.getQueryResultTabDock();
				QueryResultPanel qrPanel = (QueryResultPanel) tabDock.getSelectedDockable().getContent();
				QueryPanel queryPanel = qrPanel.getQueryPanel();
				String queryStmt = "select from " + tableName + ";";
				queryPanel.setQueryStatements(queryStmt);
			}
		}
	}
}