package com.pwrd.ui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.javadocking.dock.TabDock;
import com.javadocking.dockable.Dockable;

import com.pwrd.ui.QueryPanel;
import com.pwrd.ui.QueryResultPanel;
import com.pwrd.ui.ResultPanel;
import com.pwrd.ui.XdbBrowserFrame;
import com.pwrd.xql.XQLExecutor;

public class JsonActionListener implements ActionListener
{
	private static Log logger = LogFactory.getLog(JsonActionListener.class);

	@Override
	public void actionPerformed(ActionEvent e)
	{
		TabDock queryResultTabDock = XdbBrowserFrame.getQueryResultTabDock();
		Dockable dockable = queryResultTabDock.getSelectedDockable();

		/**
		 * 在执行与该语句相关的查询时，一定要记得本次查询用于显示结果的 <code>ResultPanel</code>。
		 */
		QueryResultPanel queryResultPanel = (QueryResultPanel) dockable
				.getContent();
		QueryPanel queryPanel = queryResultPanel.getQueryPanel();
		String query = queryPanel.getQueryStatements();
		try
		{
			
			XQLExecutor stmt = new XQLExecutor(query);
			if(stmt.execute())
			{
				
				switch(stmt.getOperationType())
				{
				case XQL_OPERATION_SELECT:
				{
					ResultPanel resultPanel = queryResultPanel.getResultPanel();
					resultPanel.display(stmt.getResultList());
					break;
				}
				case XQL_OPERATION_UPDATE:
				case XQL_OPERATION_DELETE:
					logger.info("There are " + stmt.getCount() + " records updated or deleted.");
					break;
				}
			}
		} catch (IllegalArgumentException e1)
		{
			e1.printStackTrace();
		}
	}

}
