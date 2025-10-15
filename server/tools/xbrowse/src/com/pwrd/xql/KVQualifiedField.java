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
 *һ��<code>KVQualifiedField</code>�����ʾһ���ֶΣ�
 *��Щ�ֶ���'.'�ָ�����
 * 
 * @author ��ƽ(wangping101853)
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
	 * ����qualified-field��ʹ��ָ��field�Ŀ�ʼ��
	 */
	public void reset()
	{
		index = 0;
	}
	
	/**
	 * �жϸ�field�Ƿ��Ǹ�����Ϸ����ֶ�
	 * <p/>
	 * ��������׶�ʹ��
	 * 
	 * @param table - ����
	 * @return true �����field�Ǹ�����ĺϷ��ֶ�
	 */
	public boolean isValid(String table)
	{
		return getType(table) != null;
	}
	
	/**
	 *�õ��������ֶε����͡�
	 * 
	 *<p/>��������׶�ʹ��
	 * 
	 * @param table
	 * @return �ֶε�������������ı���ڸ��ֶΣ�����null
	 */
	public DatabaseMetaData.Type getType(String table)
	{
		if(DatabaseMetaData.getInstance().getTable(table) != null)
		{
			DatabaseMetaData.Table tableDef = DatabaseMetaData.getInstance().getTable(table);
			/*
			 * ���ȼ��leftOperand�Ƿ���ڡ�
			 * ����﷨��ȷ�Ļ���leftOperand�ĳ��ȿ϶���С��1��
			 * ���䳤��Ϊ1ʱ��leftOperandֻ����key����value��
			 * ��˲���Ҫ�������Ч�ԣ����ֻ����leftOperand���ȴ���1ʱ
			 * ����Ч�ԡ�
			 */
			reset();
			DatabaseMetaData.Type type = null;
			String qualification = getNextQualification();
			if("key".equalsIgnoreCase(qualification))
				type = tableDef.getKeyType();
			else if("value".equalsIgnoreCase(qualification))
				type = tableDef.getValueType();
			
			// ��ʱkey��value������Bean����
			if(size() > 1) 
				type = checkQulification(type, null, null);
				
			return type;
		}
		return null;
	}
	
	/**
	 *���ֶεĳ��ȴ���1ʱ��ʹ�ô˷����õ��ֶε����͡�
	 * 
	 *<p/>��������׶�ʹ�á�
	 * 
	 * @param type - ��ǰ����
	 * @param keyType - �����ͣ�����ǰ�����ǹ�������ʱ����
	 * @param valueType - ֵ����
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
			case BEAN:	// ��ʱqualification��ʾBean�е�һ������
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
						logger.error("����" + type + "��������Ա" + qualification);
						logger.error(getPreviousQualifiedName() + " is incorrect.");
						return null;
					}
				}
				else
				{
					logger.error("����" + type + "�Ǽ��������ͣ���������Ա" + qualification);
					return null;
				}
			}
			case LIST:	// ��ʱqualification��ʾ˳������������
			case VECTOR:
			case SET:
			{
				// ˳����������ֻ��������
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
			case MAP:	// ��ʱqualification��ʾ���������ļ�ֵ
			case TREEMAP:
			{
				// ��������������������keyType��Ӧ
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
				logger.error("�����Ͳ���ӵ�г�Ա");
				return null;
			}
		}
		
		return type;
	}
	
	/**
	 *��������¼�ֶε�ֵ����Ϊ������ֵ��
	 * 
	 *<p/>ִ�н׶�ʹ�á�
	 * 
	 * @param record - ��¼
	 * @param value - �����õ�ֵ
	 * @return true������óɹ�������false
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
	 *��ȡ������¼�ֶε�ֵ��
	 *
	 *<p/>ִ�н׶�ʹ�á�
	 *
	 * @param record
	 * @return - ������¼�ֶε�ֵ
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
				logger.error(getPreviousQualifiedName() + "������");
		}
		return leftValue;
	}
	
	/**
	 * �õ�<code>field</code>�ڶ���<code>obj</code>�е�ֵ���÷�������
	 * �ݹ�ͷ�����ɡ�
	 * 
	 * @param obj - ����
	 * @param field - ���Ա
	 * @return - ���Ա�ڶ����е�ֵ����ɹ������򷵻�null
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
