package com.pwrd.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

/** 
 * 查询和结果应该是一一对应的 
 */
public class QueryResultPanel extends JPanel
{
	private static final long serialVersionUID = -5268295783978052536L;
	
	private QueryPanel queryPanel;
	private ResultPanel resultPanel;
	
	public QueryResultPanel()
	{
		super(new BorderLayout());
		queryPanel = new QueryPanel();
		resultPanel = new ResultPanel();
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(queryPanel);
		splitPane.setRightComponent(resultPanel);
		splitPane.setDividerLocation(200);
		add(splitPane, BorderLayout.CENTER);
	}
	
	public QueryPanel getQueryPanel()
	{
		return queryPanel;
	}
	
	public ResultPanel getResultPanel()
	{
		return resultPanel;
	}
}
