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

/**
 *执行XQL的事件监听器，当用户输入查询语句后，点击执行时， 系统
 *就会从当前的查询语句编辑组件得到查询语句（允许用户生成多个查
 *询结果界面，这样用户可以保留查询结果），然后 执行该查询语句并
 *返回执行结果。 
 * 
 *<p/>查询语句的执行分为五个阶段：词法分析、语法分析、中间 代码
 *生成、提交查询和返回结果。（内容有待补充） 
 * 
 * @author 王平(wangping101853)
 * 
 */
public class ExecuteActionListener implements ActionListener
{
	private static Log logger = LogFactory.getLog(ExecuteActionListener.class);

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
