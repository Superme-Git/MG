package com.pwrd.xql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

import com.pwrd.reflect.model.ObjectWrapper;
import com.pwrd.util.Toolkit;

/**
 *一个<code>KVQualifiedField</code>对象表示一个字段，
 *这些字段以'.'分隔开。
 * 
 * @author 王平(wangping101853)
 *
 */
public class KVQualifiedField
{
	private static Log logger = LogFactory.getLog(KVQualifiedField.class);
	
	private int index;
	private List<String> qualifiedName;
	
	public KVQualifiedField()
	{
		index = 0;
		qualifiedName = new ArrayList<String>();
	}
	
	public void addQualification(String qualification)
	{
		qualifiedName.add(qualification);
	}
	
	public boolean hasNextQualification()
	{
		return index < qualifiedName.size();
	}
	
	public String getNextQualification()
	{
		if(!hasNextQualification())
		{
			logger.error("Please invoke hasNextQualification each time before getNextQualification");
			throw new IllegalStateException("Please invoke hasNextQualification each time before getNextQualification");
		}
		return qualifiedName.get(index ++);
	}
	
	public String getPreviousQualifiedName()
	{
		String str = "";
		for(int i = 0; i < index; ++ i)
		{
			if(i != 0)
				str += TokenType.DOT.getDescription();
			str += qualifiedName.get(i);
		}
		
		return str;
	}
	
	public int size()
	{
		return qualifiedName.size();
	}
	
	/**
	 * 重置qualified-field，使其指向field的开始。
	 */
	public void reset()
	{
		index = 0;
	}
	
	/**
	 * 判断该field是否是给定表合法的字段
	 * <p/>
	 * 语义分析阶段使用
	 * 
	 * @param table - 表名
	 * @return true 如果该field是给定表的合法字段
	 */
	public boolean isValid(String table)
	{
		return getType(table) != null;
	}
	
	/**
	 *得到给定表字段的类型。
	 * 
	 *<p/>语义分析阶段使用
	 * 
	 * @param table
	 * @return 字段的类型如果给定的表存在该字段，否则null
	 */
	public DatabaseMetaData.Type getType(String table)
	{
		if(DatabaseMetaData.getInstance().getTable(table) != null)
		{
			DatabaseMetaData.Table tableDef = DatabaseMetaData.getInstance().getTable(table);
			/*
			 * 首先检查leftOperand是否存在。
			 * 如果语法正确的话，leftOperand的长度肯定不小于1，
			 * 当其长度为1时，leftOperand只能是key或者value，
			 * 因此不需要检查其有效性，因此只考虑leftOperand长度大于1时
			 * 的有效性。
			 */
			reset();
			DatabaseMetaData.Type type = null;
			String qualification = getNextQualification();
			if("key".equalsIgnoreCase(qualification))
				type = tableDef.getKeyType();
			else if("value".equalsIgnoreCase(qualification))
				type = tableDef.getValueType();
			
			// 此时key和value必须是Bean类型
			if(size() > 1) 
				type = checkQulification(type, null, null);
				
			return type;
		}
		return null;
	}
	
	/**
	 *当字段的长度大于1时，使用此方法得到字段的类型。
	 * 
	 *<p/>语义分析阶段使用。
	 * 
	 * @param type - 当前类型
	 * @param keyType - 键类型，当当前类型是关联容器时有用
	 * @param valueType - 值类型
	 * @return
	 */
	private DatabaseMetaData.Type checkQulification(DatabaseMetaData.Type type, DatabaseMetaData.Type keyType, DatabaseMetaData.Type valueType)
	{
		assert type != null;
		assert !"key".equalsIgnoreCase(type.getName());
		assert !"value".equalsIgnoreCase(type.getName());
		
		if(hasNextQualification())
		{
			String qualification = getNextQualification();
			switch(type.getTypeId())
			{
			case BEAN:	// 此时qualification表示Bean中的一个变量
			{
				if(DatabaseMetaData.getInstance().getBean(type.getName()) != null)
				{
					DatabaseMetaData.Bean beanDef = DatabaseMetaData.getInstance().getBean(type.getName());
					if(beanDef.getVariable(qualification) != null)
					{
						DatabaseMetaData.Bean.Variable varDef = beanDef.getVariable(qualification);
						// DatabaseMetaData.Type subType = varDef.getTypeType();
						return checkQulification(varDef.getTypeType(), varDef.getKeyType(), varDef.getValueType());
					}
					else
					{
						logger.error("类型" + type + "不包含成员" + qualification);
						logger.error(getPreviousQualifiedName() + " is incorrect.");
						return null;
					}
				}
				else
				{
					logger.error("类型" + type + "是简单数据类型，不包含成员" + qualification);
					return null;
				}
			}
			case LIST:	// 此时qualification表示顺序容器的索引
			case VECTOR:
			case SET:
			{
				// 顺序容器索引只能是整数
				try
				{
					Integer.parseInt(qualification);
					return checkQulification(valueType, null, null);
				}
				catch(NumberFormatException e)
				{
					logger.error("The index of sequence container must be integer.");
					return null;
				}
			}
			case MAP:	// 此时qualification表示关联容器的键值
			case TREEMAP:
			{
				// 关联容器的索引必须与keyType对应
				if(keyType == null)
				{
					logger.error("The key type of map is null.");
					return null;
				}
				switch(keyType.getTypeId())
				{
				case BEAN:
					logger.error("xbrowse doesn't support for operating on map with key type BEAN.");
					return null;
				case LIST:
				case VECTOR:
				case SET:
				case MAP:
				case TREEMAP:
					logger.error("Container is not a valid key type for map");
					return null;
				case SHORT:
				case INT:
				case LONG:
					try
					{
						Long.parseLong(qualification);
					}
					catch(NumberFormatException e)
					{
						logger.error("Invalid integer value: " + qualification + " for key type");
						return null;
					}
					break;
				case STRING:
					break;
				case FLOAT:
					try
					{
						Double.parseDouble(qualification);
					}
					catch(NumberFormatException e)
					{
						logger.error("Invalid float value: " + qualification + " for key type");
						return null;
					}
					break;
				case BOOLEAN: //
				case BINARY:
				case ANY:
					logger.error("Invalid key type for map.");
					return null;
				}
				return checkQulification(valueType, null, null);
			}
			case BOOLEAN:
			case SHORT:
			case INT:
			case LONG: 
			case STRING:
			case FLOAT:
			case BINARY:
			case ANY:
				logger.error("简单类型不能拥有成员");
				return null;
			}
		}
		
		return type;
	}
	
	/**
	 *将给定记录字段的值设置为给定的值。
	 * 
	 *<p/>执行阶段使用。
	 * 
	 * @param record - 记录
	 * @param value - 待设置的值
	 * @return true如果设置成功，否则false
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean setValue(Map.Entry record, Token value)
	{
		reset();
		String qualification = getNextQualification();
		if(qualification.equals("key"))
		{
			logger.error("The key is forbidden to be changed.");
			return false;
		}
		Object  obj = null;
		if(qualification.equals("value"))
			obj = record.getValue();
		if(size() > 1)
		{
			ObjectWrapper ow = ObjectWrapper.createObjectWrapper(obj.getClass().getName(), obj, null, null);
			while(hasNextQualification())
			{
				if(ow == null)
					return false;
				qualification = getNextQualification();
				Map<String, ObjectWrapper> childObjects = ow.getFields();
				ow = childObjects.get(qualification);
			}
			ow.getParent().setChildValue(ow, Toolkit.convert(value.getText(), ow.getValue()));
		}
		else // size() == 1
		{
			if(value.getType() == TokenType.STRING)
				obj = value.getText();
			else if(value.getType() == TokenType.LONG)
				obj = Long.valueOf(value.getText());
			else if(value.getType() == TokenType.FLOAT)
				obj = Double.valueOf(value.getText());
		}
		record.setValue(obj);
		return true;
	}
	
	/**
	 *获取给定记录字段的值。
	 *
	 *<p/>执行阶段使用。
	 *
	 * @param record
	 * @return - 给定记录字段的值
	 */
	@SuppressWarnings("rawtypes")
	public Object getValue(Map.Entry record)
	{
		reset();
		Object leftValue = null;
		String qualification = getNextQualification();
		if(qualification.equals("key"))
			leftValue = record.getKey();
		else if(qualification.equals("value"))
			leftValue = record.getValue();
		if(size() > 1)
		{
			leftValue = getFieldValue(leftValue);
			if(leftValue == null)
				logger.error(getPreviousQualifiedName() + "不存在");
		}
		return leftValue;
	}
	
	/**
	 * 得到<code>field</code>在对象<code>obj</code>中的值。该方法采用
	 * 递归和反射完成。
	 * 
	 * @param obj - 对象
	 * @param field - 类成员
	 * @return - 类成员在对象中的值如果成功，否则返回null
	 */
	private Object getFieldValue(Object obj)
	{
		ObjectWrapper ow = ObjectWrapper.createObjectWrapper(obj.getClass().getName(), obj, null, null);
		while(hasNextQualification())
		{
			if(ow == null)
				return null;
			String qualification = getNextQualification();
			Map<String, ObjectWrapper> childObjects = ow.getFields();
			ow = childObjects.get(qualification);
		}
		
		return ow.getValue();
	}
}
