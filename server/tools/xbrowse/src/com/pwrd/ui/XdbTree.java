package com.pwrd.ui;

import java.util.Stack;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class XdbTree extends JTree
{
	private static final long serialVersionUID = 8737600150736431366L;

	private DefaultMutableTreeNode _rootNode;
	/** 
	 * 查询起始顶点，当树中存在多个匹配时，需要进行很多次查询才能够
	 * 找到合适的匹配项，为了能够对整棵树进行查询，需要记录上一次查询
	 * 的匹配位置，下一次再用同一个查询词进行查询时，就从该位置开始。
	 */
	private DefaultMutableTreeNode _queryStartNode;
	
	public XdbTree()
	{
		_rootNode = new DefaultMutableTreeNode();
		_queryStartNode = null;
	}
	
	public DefaultMutableTreeNode getRootNode()
	{
		return _rootNode;
	}
	
	public void find(String query, boolean isQueryChanged, boolean allowPartialMatch)
	{
		assert _rootNode != null;
		// logger.info("isQueryChanged: " + isQueryChanged);
		/** 如果查询语句已经改变，或者这是第一次查询，那么从头开始遍历树进行查询。 */
		DefaultMutableTreeNode node = (isQueryChanged || _queryStartNode == null) ? _rootNode : _queryStartNode.getNextNode();
		StringMatch match = allowPartialMatch ? new PartialStringMatch() : new WholeStringMatch();
		while(node != null)
		{
			if(match.isMatch(node.getUserObject().toString(), query))
			{
				Stack<DefaultMutableTreeNode> paths = new Stack<DefaultMutableTreeNode>();
				_queryStartNode = node; // 保存本次查询结束结点
				while(node != null)
				{
					paths.push(node);
					node = (DefaultMutableTreeNode)node.getParent();
				}
				
				DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[paths.size()];
				int index = 0;
				while(!paths.empty())
				{
					nodes[index] = paths.pop();
					index ++;
				}
				TreePath treePath = new TreePath(nodes);
				setSelectionPath(treePath);
				scrollPathToVisible(treePath);
				break;
			}
			node = node.getNextNode();
		}
	}
	
	private static interface StringMatch
	{
		public boolean isMatch(String a, String b);
	}
	
	private static class PartialStringMatch implements StringMatch
	{

		@Override
		public boolean isMatch(String a, String b) 
		{
			assert a != null && b != null;
			return a.toUpperCase().contains(b.toUpperCase());
		}
		
	}
	
	private static class WholeStringMatch implements StringMatch
	{

		@Override
		public boolean isMatch(String a, String b) 
		{
			return a.equalsIgnoreCase(b);
		}
		
	}
}
