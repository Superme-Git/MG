package com.pwrd.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import mkdb.util.DatabaseMetaData;

import com.pwrd.definition.DatabaseMetaDataWrapper;

public class PropertiesPanel extends JPanel
{
	private static final long serialVersionUID = -5902147264122782190L;

	private static final int BLANK_TABLE_ROWS = 5;
	
	private JTable _propertiesTable;
	private DefaultTableModel _propertiesModel;
	
	PropertiesPanel()
	{
		_propertiesTable = new JTable();
		String[] columnName = {"Property name", "Property value"};
		_propertiesModel = new DefaultTableModel(columnName, BLANK_TABLE_ROWS);
		_propertiesTable.setModel(_propertiesModel);
		
		JScrollPane scrollPane = new JScrollPane(_propertiesTable);
		_propertiesTable.setFillsViewportHeight(true);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public TreeSelectionListener getXTableTreeSelectionListener()
	{
		return new XTableTreeSelectionListener();
	}
	
	class XTableTreeSelectionListener implements TreeSelectionListener
	{

		@Override
		public void valueChanged(TreeSelectionEvent e)
		{
			JTree tree = (JTree)e.getSource();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			// select a table
			if(DatabaseMetaDataWrapper.getInstance().containsTable(node.getUserObject().toString()))
			{
				DatabaseMetaData.Table tableDef = DatabaseMetaData.getInstance().getTable(node.getUserObject().toString());
				tableDef.getPersistence();
				int i = 0;
				if(tableDef.getPersistence() != null)
				{
					_propertiesModel.setValueAt("persistence", i, 0);
					_propertiesModel.setValueAt(tableDef.getPersistence().toString(), i, 1);
					++ i;
				}
				if(tableDef.getForeign() != null)
				{
					_propertiesModel.setValueAt("foreign", i, 0);
					_propertiesModel.setValueAt(tableDef.getForeign().toString(), i, 1);
					++ i;
				}
				if(tableDef.getCapacity() != null)
				{
					_propertiesModel.setValueAt("capacity", i, 0);
					_propertiesModel.setValueAt(tableDef.getCapacity().toString(), i, 1);
					++ i;
				}
			}
			else
			{
				_propertiesModel.setRowCount(BLANK_TABLE_ROWS);
				for(int i = 0; i < BLANK_TABLE_ROWS; ++ i)
				{
					_propertiesModel.setValueAt("", i, 0);
					_propertiesModel.setValueAt("", i, 1);
				}
			}
		}
		
	}
}
