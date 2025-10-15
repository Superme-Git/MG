package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

import com.pwrd.resources.ConstantIcons;

public class XTablePanel extends JPanel /*implements DraggableContent*/
{
	private static final long serialVersionUID = -3271752927338419761L;
	
	private XdbTree tableTree;
	
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(XTablePanel.class);
	
	public XTablePanel()
	{
	}
	
	public XTablePanel(Collection<DatabaseMetaData.Table> tableDefs)
	{
		this();
		// 对这些定义进行排序吧
		tableTree = new XdbTree();
		DefaultMutableTreeNode rootNode = tableTree.getRootNode();
		rootNode.setUserObject("Xdb (" + tableDefs.size() + " xtables in total)");
		for(DatabaseMetaData.Table tableDef : tableDefs)
		{
			DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableDef.getName());
			
			// XTableDefinition tableDef = entry.getValue();
			DefaultMutableTreeNode keyNode = new DefaultMutableTreeNode(tableDef.getKeyType().getName());
			DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(tableDef.getValueType().getName());
			
			tableNode.add(keyNode);
			tableNode.add(valueNode);
			
			rootNode.add(tableNode);
		}
		
		// Create the tree model.
		TreeModel treeModel = new DefaultTreeModel(rootNode);
		
		// Create the JTree from the tree model.
		// tableTree = new JTree(treeModel);
		tableTree.setModel(treeModel);
		tableTree.setCellRenderer(new MetaTableTreeCellRenderer());
		tableTree.expandRow(0);
		// Expand the tree.
		// Add the tree in a scroll pane.
		setLayout(new BorderLayout());
		add(new JScrollPane(tableTree), BorderLayout.CENTER);
	}
	
	public void find(String query, boolean isQueryChanged, boolean allowPartialMatch)
	{
		tableTree.find(query, isQueryChanged, allowPartialMatch);
	}
	
	public void addTreeSelectionListener(TreeSelectionListener listener)
	{
		tableTree.addTreeSelectionListener(listener);
	}
	
	public void addMouseListener(MouseListener listener)
	{
		tableTree.addMouseListener(listener);
	}
	
	static class MetaTableTreeCellRenderer extends DefaultTreeCellRenderer 
	{
		private static final long serialVersionUID = -8199847668922727182L;
		
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			
			if(node.isRoot())
				setIcon(ConstantIcons.getIcon("xdb"));
			// It's a table
			else if(DatabaseMetaData.getInstance().getTable(node.getUserObject().toString()) != null)
				setIcon(ConstantIcons.getIcon("xtable"));
			else
			{
				if(node.getNextSibling() != null) // It's a key
					setIcon(ConstantIcons.getIcon("xkey"));
				else
				{
					// It's a xbean
					if(DatabaseMetaData.getInstance().getBean(node.getUserObject().toString()) != null)
						setIcon(ConstantIcons.getIcon("xbean"));
					else // It's a value except xbean 
						setIcon(ConstantIcons.getIcon("xvalue"));
				}
			}
			
			return this;
		}
	}
}
