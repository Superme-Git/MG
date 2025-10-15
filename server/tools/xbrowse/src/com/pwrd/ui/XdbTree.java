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
	 * ��ѯ��ʼ���㣬�����д��ڶ��ƥ��ʱ����Ҫ���кܶ�β�ѯ���ܹ�
	 * �ҵ����ʵ�ƥ���Ϊ���ܹ������������в�ѯ����Ҫ��¼��һ�β�ѯ
	 * ��ƥ��λ�ã���һ������ͬһ����ѯ�ʽ��в�ѯʱ���ʹӸ�λ�ÿ�ʼ��
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
		/** �����ѯ����Ѿ��ı䣬�������ǵ�һ�β�ѯ����ô��ͷ��ʼ���������в�ѯ�� */
		DefaultMutableTreeNode node = (isQueryChanged || _queryStartNode == null) ? _rootNode : _queryStartNode.getNextNode();
		StringMatch match = allowPartialMatch ? new PartialStringMatch() : new WholeStringMatch();
		while(node != null)
		{
			if(match.isMatch(node.getUserObject().toString(), query))
			{
				Stack<DefaultMutableTreeNode> paths = new Stack<DefaultMutableTreeNode>();
				_queryStartNode = node; // ���汾�β�ѯ�������
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
