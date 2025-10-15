package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

import com.pwrd.definition.DatabaseMetaDataWrapper;
import com.pwrd.resources.ConstantIcons;

public class ResultPanel extends JPanel
{
	private static final long serialVersionUID = 4379973992436087469L;
	private static Log logger = LogFactory.getLog(ResultPanel.class);
	
	private JTable _resultTable;
	private DefaultTableModel _resultModel;
	
	/** 分页相关 */
	@SuppressWarnings("rawtypes")
	private List<Map.Entry> resultList;
	private JButton prevBtn;
	private JLabel pageLabel;
	private JButton nextBtn;
	Page page = new Page();
	
	public ResultPanel()
	{
		_resultTable = new JTable();
		String[] columnNames = {"Key", "Value"};
		_resultModel = new DefaultTableModel(columnNames, page.getPageRows())
		{
			private static final long serialVersionUID = -7829667720362731257L;

			@Override
			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}
		};
		_resultTable.setModel(_resultModel);
		_resultTable.setDefaultRenderer(Object.class, new ResultTableCellRenderer());
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_resultTable.setFillsViewportHeight(true);
		
		JPanel pagePanel = new JPanel();
		JToolBar pageTB = new JToolBar();
		prevBtn = new JButton(ConstantIcons.getIcon("previous"));
		prevBtn.addActionListener(new PageActionListener(PageActionType.PREVIOUS));
		prevBtn.setEnabled(false);
		pageLabel = new JLabel(0 + " - " + "0");
		nextBtn = new JButton(ConstantIcons.getIcon("next"));
		nextBtn.addActionListener(new PageActionListener(PageActionType.NEXT));
		nextBtn.setEnabled(false);
		pageTB.add(prevBtn);
		pageTB.add(pageLabel);
		pageTB.add(nextBtn);
		pageTB.addSeparator();
		pagePanel.add(pageTB);
		pagePanel.setAlignmentX(CENTER_ALIGNMENT);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(pagePanel, BorderLayout.PAGE_END);
	}
	
	public void addResultTableMouseListener(MouseListener listener)
	{
		_resultTable.addMouseListener(listener);
	}
	
	@SuppressWarnings("rawtypes")
	public void display(List<Map.Entry> data)
	{
		assert data != null;
		logger.info("本次查询共返回" + data.size() + "条记录。");
		resultList = data;
		page.setCurrentPage(1);
		page.setTotalRows(resultList.size());
		XdbBrowserFrame.getValueDetailPanel().reset();
		// 删除时一定要清除掉ValueDetailPanel上的数据
		for(int i = _resultModel.getRowCount() - 1; i >= 0; -- i)
			_resultModel.removeRow(i);
		_resultModel.setRowCount(page.getPageRows());
		updatePageButtonStatus();
		display();
	}
	
	@SuppressWarnings("rawtypes")
	private void display()
	{
		/** 分页所使用的区间[startIndex, endIndex) */
		int row = 0;
		for(int i = page.getPageStartIndex(); i < page.getPageEndIndex(); ++ i)
		{
			Map.Entry entry = resultList.get(i);
			_resultModel.setValueAt(entry.getKey(), row, 0);
			_resultModel.setValueAt(entry.getValue(), row, 1);
			++ row;
		}
		pageLabel.setText((page.getPageStartIndex() + 1) + " - " + (page.getPageEndIndex()));
	}
	
	private void updatePageButtonStatus()
	{
		prevBtn.setEnabled(page.hasPreviousPage());
		nextBtn.setEnabled(page.hasNextPage());
	}
	
	public class PageActionListener implements ActionListener
	{
		private PageActionType type;
		
		public PageActionListener(PageActionType _type)
		{
			type = _type;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			switch(type)
			{
				case PREVIOUS:
				{
					page.toPreviousPage();
					break;
				}
				case NEXT:
				{
					page.toNextPage();
					break;
				}
			}
			updatePageButtonStatus();
			display();
		}
	}
	
	private static enum PageActionType
	{
		PREVIOUS, NEXT;
	}
	
	/**
	 * for Xbean 
	 */
	public static class ResultTableCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 7392824627861022009L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) 
		{
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if(value == null)
			{
				setIcon(null);
				setText("");
			}
			else
			{
				if(column == 0)
				{
					setIcon(null);
					setText(value.toString());
				}
				else if(column == 1)
				{
					DatabaseMetaData.Type objType = DatabaseMetaDataWrapper.getInstance().getType(value.getClass());
					switch(objType.getTypeId())
					{
						case BEAN:
							setIcon(ConstantIcons.getIcon("xbean"));
							setText(objType.getName());
							break;
						case LIST:
						case VECTOR:
						case SET:
							setIcon(ConstantIcons.getIcon("set"));
							setText(objType.getName());
							break;	
						case MAP:
						case TREEMAP:
							setIcon(ConstantIcons.getIcon("map"));
							setText(objType.getName());
							break;
						case BOOLEAN:
						case SHORT:
						case INT:
						case LONG:
						case STRING:
						case FLOAT:
						default:
							setIcon(null);
							setText(value.toString());
							break;
					}
				}
			}
			
			return this;
		}
	}
}
