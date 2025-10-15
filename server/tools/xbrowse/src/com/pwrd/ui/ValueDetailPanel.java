package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

import com.pwrd.definition.DatabaseMetaDataWrapper;
import com.pwrd.resources.ConstantIcons;

/**
 * Xdb是一个键值数据库，键类型一般是较为简单的类型，而值类型则较为
 * 复杂，可能是原始类型、集合类型或者<code>XBean</code>类型，而
 * <code>XBean</code>类型时一个组合结构，其内部又会嵌套包含这三种
 * 类型，因此为了能够直观显示值类型，采用树状结构显示。
 * 
 */
public class ValueDetailPanel extends JPanel
{
	private static final long serialVersionUID = 5032076255369962037L;
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(ValueDetailPanel.class);

	private JTree _detailTree;
	private DefaultMutableTreeNode _rootNode;
	
	public ValueDetailPanel()
	{
		_rootNode = new DefaultMutableTreeNode("Value Detail");
		TreeModel detailModel = new DefaultTreeModel(_rootNode);
		
		_detailTree = new JTree(detailModel);
		_detailTree.setCellRenderer(new XTableTreeCellRenderer());
		_detailTree.expandRow(0);
		JScrollPane scrollPane = new JScrollPane(_detailTree);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public ResultTableMouseListener getResultTableMouseListener()
	{
		return new ResultTableMouseListener();
	}
	
	public void reset()
	{
		_rootNode = new DefaultMutableTreeNode("Value Detail");
		_detailTree.setModel(new DefaultTreeModel(_rootNode));
	}
	
	/**
	 * 在结果界面<code>ResultPanel</code>显示的键-值对，如果值类型是原始类型，
	 * 则直接显示，否则如果是集合类型或者<code>XBean</code>类型，则只显示图标
	 * （类型名是否要显示呢？）。当用户双击该图标时，就会触发该监听器，该监听器
	 * 就会以树状结构显示记录的值。
	 * 
	 * @author wp853
	 *
	 */
	private class ResultTableMouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			if(e.getClickCount() == 2)
			{
				JTable table = (JTable)e.getSource();
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				
				/** 得到选中的值（在<code>ResultPanel</code>中） */
				Object value = table.getValueAt(row, column);
				if(value != null)
				{
					_rootNode.removeAllChildren();
					getObjectTree(_rootNode, value, "");
					// 树状结构变换后，重新设置模型
					_detailTree.setModel(new DefaultTreeModel(_rootNode));
					_detailTree.expandRow(0);
				}
			}
		}
		
		/**
		 * 建立一颗以<code>Object</code>对象为根的树。
		 * 
		 * @param subRoot - 以<code>obj</code>建立的树的根结点。
		 * @param obj - <code>fieldName</code>的值。
		 * @param fieldName - 成员名。
		 */
		private void getObjectTree(DefaultMutableTreeNode subRoot, Object obj, String fieldName)
		{
			DatabaseMetaData.Type objType = DatabaseMetaDataWrapper.getInstance().getType(obj.getClass());
			switch(objType.getTypeId())
			{
				case BEAN:
					getBeanTree(subRoot, obj, fieldName);
					break;
				case LIST:
				case VECTOR:
				case SET:
					getCollectionTree(subRoot, obj, fieldName);
					break;	
				case MAP:
				case TREEMAP:
					getMapTree(subRoot, obj, fieldName);
					break;
				case BOOLEAN:
				case SHORT:
				case INT:
				case LONG:
				case STRING:
				case FLOAT:
				{
					String sep = "";
					if(!"".equals(fieldName))
						sep = " = ";
					subRoot.setUserObject(fieldName + sep + obj.toString());
					break;
				}
				default:
					break;
			}
		}
		
		/**
		 * 建立一颗以<code>XBean</code>或者<code>CBean</code>对象为根的树。
		 * 
		 * @param subRoot - 以<code>xbean</code>建立的树的根结点。
		 * @param xbean - <code>fieldName</code>的值。
		 * @param fieldName - 成员名。
		 */
		private void getBeanTree(DefaultMutableTreeNode subRoot, Object xbean, String fieldName)
		{
			String sep = "";
			if(!"".equals(fieldName))
				sep = " = ";
			DatabaseMetaData.Type objType = DatabaseMetaDataWrapper.getInstance().getType(xbean.getClass());
			subRoot.setUserObject(fieldName + sep + objType.getName());
			Field[] fields = xbean.getClass().getDeclaredFields();
			DefaultMutableTreeNode tempNode;
			for(Field field : fields)
			{
				try 
				{
					field.setAccessible(true);
					tempNode = new DefaultMutableTreeNode();
					Object result = field.get(xbean);
					getObjectTree(tempNode, result, field.getName());
					subRoot.add(tempNode);
				} 
				catch (IllegalArgumentException e1) 
				{
					e1.printStackTrace();
				}
				catch (IllegalAccessException e1) 
				{
					e1.printStackTrace();
				} 
			}
		}
		
		/**
		 * 建立一颗以<code>Collection</code>对象为根的树。
		 * 
		 * @param subRoot - 以<code>obj</code>建立的树的根结点。
		 * @param obj - <code>fieldName</code>的值。
		 * @param fieldName - 成员名。
		 */
		private void getCollectionTree(DefaultMutableTreeNode subRoot, Object obj, String fieldName)
		{
			assert obj != null;
			DatabaseMetaData.Type objType = DatabaseMetaDataWrapper.getInstance().getType(obj.getClass());
			String sep = "";
			if(!"".equals(fieldName))
				sep = " = ";
			Collection<?> col = (Collection<?>)obj;
			String info = " [" + col.size() + " entry";
			info = col.size() > 1 ? info + "s]" : info + "]";
			subRoot.setUserObject(fieldName + sep + objType.getName() + info);
			DefaultMutableTreeNode tempNode = null;
			for(Object o : col)
			{
				tempNode = new DefaultMutableTreeNode();
				getObjectTree(tempNode, o, "");
				subRoot.add(tempNode);
			}
		}
		
		/**
		 * 建立一颗以<code>Map</code>对象为根的树。
		 * 
		 * @param subRoot - 以<code>obj</code>建立的树的根结点。
		 * @param obj - <code>fieldName</code>的值。
		 * @param fieldName - 成员名。
		 */
		private void getMapTree(DefaultMutableTreeNode subRoot, Object obj, String fieldName)
		{
			assert obj != null;
			DatabaseMetaData.Type objType = DatabaseMetaDataWrapper.getInstance().getType(obj.getClass());
			String sep = "";
			if(!"".equals(fieldName))
				sep = " = ";
			Map<?, ?> map = (Map<?, ?>)obj;
			String info = " [" + map.size() + " entry";
			info = map.size() > 1 ? info + "s]" : info + "]";
			subRoot.setUserObject(fieldName + sep + objType.getName() + info);
			DefaultMutableTreeNode tempNode = null;
			for(Map.Entry<?, ?> kv : map.entrySet())
			{
				tempNode = new DefaultMutableTreeNode();
				getObjectTree(tempNode, kv.getValue(), kv.getKey().toString());
				subRoot.add(tempNode);
			}
		}
	}
	
	static class XTableTreeCellRenderer extends DefaultTreeCellRenderer 
	{
		private static final long serialVersionUID = -8199847668922727182L;
		
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			
			if(node.isRoot())
				setIcon(ConstantIcons.getIcon("xdb"));
			
			String label = node.getUserObject().toString();
			if(label.contains("="))
			{
				Properties p = new Properties();
				try 
				{
					p.load(new StringReader(label));
					String  pName = (String) p.propertyNames().nextElement();
					label = p.getProperty(pName);
					// System.out.println("label: " + label);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			/** 此时得到类型名，但是对于集合类型，可能会包含元素数量信息 */
			int infoIndex = label.indexOf(" [");
			label  = infoIndex == -1 ? label : label.substring(0, infoIndex);
			
			DatabaseMetaData.Type objType = null;
			try
			{
				objType = DatabaseMetaData.getInstance().getType(label);
			}
			catch(Exception e)
			{				
			}
			if(objType == null)
			{
				setIcon(ConstantIcons.getIcon("xvar"));
				return this;
			}
			switch(objType.getTypeId())
			{
				case BEAN:
					setIcon(ConstantIcons.getIcon("xbean"));
					break;
				case LIST:
				case VECTOR:
				case SET:
					setIcon(ConstantIcons.getIcon("set"));
					break;	
				case MAP:
				case TREEMAP:
					setIcon(ConstantIcons.getIcon("map"));
					break;
				case BOOLEAN:
				case SHORT:
				case INT:
				case LONG:
				case STRING:
				case FLOAT:
				default:
					setIcon(ConstantIcons.getIcon("xvar"));
					break;
			}
			/*
			if(XdbDefinition.containsType(label)) // XBean
				setIcon(ConstantIcons.getIcon("xbean"));
			else if(XdbDataType.isValidXdbCollectionType(label)) // List or Set
				setIcon(ConstantIcons.getIcon("set"));
			else if(XdbDataType.isValidXdbMapType(label)) // Map
				setIcon(ConstantIcons.getIcon("map"));
			else // Simple type
				setIcon(ConstantIcons.getIcon("xvar"));
			*/
			
			return this;
		}
	}
}
