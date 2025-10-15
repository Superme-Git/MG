package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import mkdb.util.DatabaseMetaData;
// import javax.swing.tree.TreePath;

import com.pwrd.definition.DatabaseMetaDataWrapper;
import com.pwrd.resources.ConstantIcons;

public class XBeanPanel extends JPanel
{
	private static final long serialVersionUID = 6545166397561883609L;

	private XdbTree xbeanTree;
	
	public XBeanPanel()
	{
	}
	
	public XBeanPanel(Collection<DatabaseMetaData.Bean> beanDefs)
	{
		this();
		xbeanTree = new XdbTree();
		DefaultMutableTreeNode rootNode = xbeanTree.getRootNode();
		rootNode.setUserObject("XBeans (" + beanDefs.size() + " xbeans in total)");
		for(DatabaseMetaData.Bean beanDef : beanDefs)
		{
			DefaultMutableTreeNode xbeanNode = new DefaultMutableTreeNode(beanDef.getName());
			beanDef.getVariables();
			for(DatabaseMetaData.Bean.Variable var : beanDef.getVariables())
			{
				DefaultMutableTreeNode varNode = new DefaultMutableTreeNode(var);
				xbeanNode.add(varNode);
			}
			
			rootNode.add(xbeanNode);
		}
		
		// Create the tree model.
		TreeModel treeModel = new DefaultTreeModel(rootNode);
		
		// Create the JTree from the tree model.
		// xbeanTree = new JTree(treeModel);
		xbeanTree.setModel(treeModel);
		xbeanTree.setCellRenderer(new XBeanTreeCellRenderer());
		xbeanTree.expandRow(0);
		setLayout(new BorderLayout());
		add(new JScrollPane(xbeanTree), BorderLayout.CENTER);
	}
	
	public void find(String query, boolean isQueryChanged, boolean allowPartialMatch)
	{
		xbeanTree.find(query, isQueryChanged, allowPartialMatch);
	}
	
	static class XBeanTreeCellRenderer extends DefaultTreeCellRenderer
	{
		private static final long serialVersionUID = 954051922781749607L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			if(node.isRoot())
				setIcon(ConstantIcons.getIcon("xbeans"));
			else if(DatabaseMetaDataWrapper.getInstance().containsBean(node.getUserObject().toString()))
				setIcon(ConstantIcons.getIcon("xbean"));
			else
				setIcon(ConstantIcons.getIcon("xvar"));
			
			return this;
		}
	}
}
