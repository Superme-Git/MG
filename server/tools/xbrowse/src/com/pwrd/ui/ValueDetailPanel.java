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
 * Xdb��һ����ֵ���ݿ⣬������һ���ǽ�Ϊ�򵥵����ͣ���ֵ�������Ϊ
 * ���ӣ�������ԭʼ���͡��������ͻ���<code>XBean</code>���ͣ���
 * <code>XBean</code>����ʱһ����Ͻṹ�����ڲ��ֻ�Ƕ�װ���������
 * ���ͣ����Ϊ���ܹ�ֱ����ʾֵ���ͣ�������״�ṹ��ʾ��
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
	 * �ڽ������<code>ResultPanel</code>��ʾ�ļ�-ֵ�ԣ����ֵ������ԭʼ���ͣ�
	 * ��ֱ����ʾ����������Ǽ������ͻ���<code>XBean</code>���ͣ���ֻ��ʾͼ��
	 * ���������Ƿ�Ҫ��ʾ�أ��������û�˫����ͼ��ʱ���ͻᴥ���ü��������ü�����
	 * �ͻ�����״�ṹ��ʾ��¼��ֵ��
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
				
				/** �õ�ѡ�е�ֵ����<code>ResultPanel</code>�У� */
				Object value = table.getValueAt(row, column);
				if(value != null)
				{
					_rootNode.removeAllChildren();
					getObjectTree(_rootNode, value, "");
					// ��״�ṹ�任����������ģ��
					_detailTree.setModel(new DefaultTreeModel(_rootNode));
					_detailTree.expandRow(0);
				}
			}
		}
		
		/**
		 * ����һ����<code>Object</code>����Ϊ��������
		 * 
		 * @param subRoot - ��<code>obj</code>���������ĸ���㡣
		 * @param obj - <code>fieldName</code>��ֵ��
		 * @param fieldName - ��Ա����
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
		 * ����һ����<code>XBean</code>����<code>CBean</code>����Ϊ��������
		 * 
		 * @param subRoot - ��<code>xbean</code>���������ĸ���㡣
		 * @param xbean - <code>fieldName</code>��ֵ��
		 * @param fieldName - ��Ա����
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
		 * ����һ����<code>Collection</code>����Ϊ��������
		 * 
		 * @param subRoot - ��<code>obj</code>���������ĸ���㡣
		 * @param obj - <code>fieldName</code>��ֵ��
		 * @param fieldName - ��Ա����
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
		 * ����һ����<code>Map</code>����Ϊ��������
		 * 
		 * @param subRoot - ��<code>obj</code>���������ĸ���㡣
		 * @param obj - <code>fieldName</code>��ֵ��
		 * @param fieldName - ��Ա����
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
			/** ��ʱ�õ������������Ƕ��ڼ������ͣ����ܻ����Ԫ��������Ϣ */
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
