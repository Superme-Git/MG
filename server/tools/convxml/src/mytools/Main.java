package mytools;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
	private static XPathFactory xpathFactory = XPathFactory.newInstance();

	private static Logger logger = Logger.getLogger(Main.class);
	
//	private static HashMap<Object, HashMap<Object, Object>> publicBeanMap = new HashMap<Object, HashMap<Object, Object>>();

	private static Configuration initFreeMarker(String paramString) {
		Configuration localConfiguration;
		try {
			localConfiguration = new Configuration();
			localConfiguration.setDirectoryForTemplateLoading(new File(paramString));
		} catch (Exception localException) {
			logger.error("err", localException);
			return null;
		}
		localConfiguration.setObjectWrapper(new DefaultObjectWrapper());
		return localConfiguration;
	}

	public static void main(String[] paramArrayOfString) throws Exception {
		DOMConfigurator.configure("log4j.xml");
		if (paramArrayOfString.length < 3) {
			return;
		}
		String str1 = paramArrayOfString[0];

		String str2 = paramArrayOfString[1];
		String str3 = paramArrayOfString[2];
		boolean bool;
		if (paramArrayOfString.length == 4)
			bool = true;
		else
			bool = false;

		Configuration localConfiguration = initFreeMarker(str3);

		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();

		localDocumentBuilderFactory.setXIncludeAware(true);
		localDocumentBuilderFactory.setNamespaceAware(true);
		Document localDocument;
		try {
			DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
			localDocument = localDocumentBuilder.parse(str1);
		} catch (Exception localException) {
			logger.error("parse doc fail", localException);
			return;
		}
		if (localDocument == null) {
			return;
		}

		Object localObject1 = xpathFactory.newXPath().evaluate("//bean",
				localDocument, XPathConstants.NODESET);

		if (localObject1 == null) {
			logger.error(str1 + "文件的格式不正确");
			return;
		}
		NodeList localNodeList = (NodeList) localObject1;

		List<Object> localArrayList1 = new ArrayList<Object>();
		List<Object> localArrayList2 = new ArrayList<Object>();
		
		//先找出公用的bean
		/*for (int i = 0; i != localNodeList.getLength(); ++ i) {
			Object localObject2 = (Element) localNodeList.item(i);
			Object localObject3 = ClassNameUtil.xmlpathToNS((Element) localObject2);
			
			if (((Element) localObject2).getAttribute("genxml").equals("")) {
				savePublicBean(localConfiguration, (String) localObject3, (Element) localObject2, bool);
			}
		}*/
		
		for (int i = 0; i != localNodeList.getLength(); ++i) {
			Object localObject2 = (Element) localNodeList.item(i);
			Object localObject3 = ClassNameUtil.xmlpathToNS((Element) localObject2);
			if (((Element) localObject2).getAttribute("genxml").equals("client")) {
				// localArrayList2.add(localObject3);
			} else {
				logger.info("writing " + ((String) localObject3));
				writeJavaFile(localConfiguration, str2, (String) localObject3,
						(Element) localObject2, bool);
				
				if (((Element) localObject2).getAttribute("genxml").equals("server")) {
					localArrayList1.add(localObject3);
				}
			}
		}
		Template localTemplate = localConfiguration.getTemplate("main.ftl");
		Object localObject2 = str2 + "/mytools/ConvMain.java";
		new File((String) localObject2).getParentFile().mkdirs();
		Object localObject3 = new OutputStreamWriter(new FileOutputStream((String) localObject2), "utf-8");

		HashMap<Object, Object> localHashMap = new HashMap<Object, Object>();
		localHashMap.put("serverClassList", localArrayList1);
		localHashMap.put("clientClassList", localArrayList2);
		localHashMap.put("defineOnly", Boolean.valueOf(bool));
		localTemplate.process(localHashMap, (Writer) localObject3);
		((Writer) localObject3).close();

		logger.error("java文件生成成功");
	}

	private static boolean isInt(String paramString) {
		try {
			Integer.parseInt(paramString);
		} catch (NumberFormatException localNumberFormatException) {
			return false;
		}
		return true;
	}

	private static boolean isFloat(String paramString) {
		try {
			Float.parseFloat(paramString);
		} catch (NumberFormatException localNumberFormatException) {
			return false;
		}
		return true;
	}

	private static boolean isDouble(String paramString) {
		try {
			Double.parseDouble(paramString);
		} catch (NumberFormatException localNumberFormatException) {
			return false;
		}
		return true;
	}

	private static boolean isLong(String paramString) {
		try {
			Long.parseLong(paramString);
		} catch (NumberFormatException localNumberFormatException) {
			return false;
		}
		return true;
	}
	
	/*private static boolean savePublicBean(Configuration paramConfiguration, String className, Element paramElement,
			boolean paramBoolean) {

		ClassNameUtil localClassNameUtil = new ClassNameUtil(className);
		HashMap<Object, Object> localBeanMap = new HashMap<Object, Object>();
		localBeanMap.put("namespace", localClassNameUtil.getNamespace());
		localBeanMap.put("classname", localClassNameUtil.getShortName());
		localBeanMap.put("defineOnly", Boolean.valueOf(paramBoolean));
		String[] arrayOfString1 = paramElement.getAttribute("from").split(",");

		boolean bool = arrayOfString1[0].isEmpty();
		localBeanMap.put("xlsfiles", arrayOfString1);
		localBeanMap.put("baseclass", paramElement.getAttribute("baseclass"));

		NodeList localNodeList = paramElement.getElementsByTagName("variable");

		ArrayList<VarInfo> localArrayList = new ArrayList<VarInfo>(localNodeList.getLength());
		Object localBean;
		for (int i = 0; i != localNodeList.getLength(); ++i) {
			localBean = (Element) localNodeList.item(i);

			VarInfo localVarInfo = new VarInfo();
			localArrayList.add(localVarInfo);
			localVarInfo.name = ((Element) localBean).getAttribute("name");

			String strType = ((Element) localBean).getAttribute("type");
			String strMin = ((Element) localBean).getAttribute("min");
			String strMax = ((Element) localBean).getAttribute("max");
			String strRef = ((Element) localBean).getAttribute("ref");
			String strFromcol = ((Element) localBean).getAttribute("fromCol");

			if (!(strFromcol.isEmpty())) {
				String[] localObject2 = strFromcol.split("\\|");
				for (String localObject4 : localObject2) {
					String[] arrayOfString2 = localObject4.split(":");
					if (arrayOfString2.length == 2) {
						localVarInfo.prefixMapping.put(arrayOfString2[0], arrayOfString2[1].split(","));
					} else if (arrayOfString2.length == 1) {
						localVarInfo.prefixMapping.put("", arrayOfString2[0].split(","));
					} else
						throw new RuntimeException("错误的值,fromcol=" + strFromcol);
				}
			} else {
				throw new RuntimeException("未指定fromcol属性");
			}
			if (strType.isEmpty()) {
				logger.error(className + "'s variable need type");
			} else {
				if ((strType.equalsIgnoreCase("string"))
						|| (strType.equalsIgnoreCase("int"))
						|| (strType.equalsIgnoreCase("long"))
						|| (strType.equalsIgnoreCase("double"))
						|| (strType.equalsIgnoreCase("bool"))
						|| (strType.equalsIgnoreCase("float"))) {
					String localObject2 = "";
					if (strType.equalsIgnoreCase("string")) {
						localObject2 = "null";
						localVarInfo.type = "String";
						if ((!(strMin.isEmpty())) && (!(isInt(strMin))))
							throw new RuntimeException("min属性错误");
						if ((!(strMax.isEmpty())) && (!(isInt(strMax))))
							throw new RuntimeException("max属性错误");
						localVarInfo.minValue = strMin;
						localVarInfo.maxValue = strMax;
					} else if (strType.equalsIgnoreCase("int")) {
						localObject2 = "0";
						localVarInfo.type = "int";
						if ((!(strMin.isEmpty())) && (!(isInt(strMin))))
							throw new RuntimeException("min属性错误");
						if ((!(strMax.isEmpty())) && (!(isInt(strMax))))
							throw new RuntimeException("max属性错误");
						localVarInfo.minValue = strMin;
						localVarInfo.maxValue = strMax;
					} else if (strType.equalsIgnoreCase("long")) {
						localObject2 = "0L";
						localVarInfo.type = "long";
						if (!(strMin.isEmpty()))
							if (isLong(strMin))
								localVarInfo.minValue = strMin + "L";
							else
								throw new RuntimeException("min属性错误");
						if (!(strMax.isEmpty()))
							if (isLong(strMax))
								localVarInfo.maxValue = strMax + "L";
							else
								throw new RuntimeException("max属性错误");
					} else if (strType.equalsIgnoreCase("double")) {
						localObject2 = "0.0";
						localVarInfo.type = "double";
						if (!(strMin.isEmpty()))
							if (isDouble(strMin))
								localVarInfo.minValue = strMin + "d";
							else
								throw new RuntimeException("min属性错误");
						if (!(strMax.isEmpty()))
							if (isDouble(strMax))
								localVarInfo.maxValue = strMax + "d";
							else
								throw new RuntimeException("max属性错误");
					} else if (strType.equalsIgnoreCase("float")) {
						localObject2 = "0.0f";
						localVarInfo.type = "float";
						if (!(strMin.isEmpty()))
							if (isFloat(strMin))
								localVarInfo.minValue = strMin + "f";
							else
								throw new RuntimeException("min属性错误");
						if (!(strMax.isEmpty()))
							if (isFloat(strMax))
								localVarInfo.maxValue = strMax + "f";
							else
								throw new RuntimeException("max属性错误");
					} else if (strType.equalsIgnoreCase("bool")) {
						localObject2 = "false";
						localVarInfo.type = "boolean";
					}
					localVarInfo.initValue = ((String) localObject2);
				} else if ((strType.equalsIgnoreCase("vector")) || (strType.equalsIgnoreCase("set"))) {
					String localValueObj = ((Element) localBean).getAttribute("value").trim();

					if ((((String) localValueObj).equalsIgnoreCase("Integer"))
							|| (((String) localValueObj).equalsIgnoreCase("int"))) {
						localVarInfo.valueType = "Integer";
						localValueObj = "Integer";
					} else if (((String) localValueObj).equalsIgnoreCase("String")) {
						localVarInfo.valueType = "String";
						localValueObj = "String";
					} else if (((String) localValueObj).equalsIgnoreCase("Long")) {
						localVarInfo.valueType = "Long";
						localValueObj = "Long";
					} else if (((String) localValueObj).equalsIgnoreCase("double")) {
						localVarInfo.valueType = "Double";
						localValueObj = "Double";
					} else if (((String) localValueObj).equalsIgnoreCase("float")) {
						localVarInfo.valueType = "Float";
						localValueObj = "Float";
					} else if (((String) localValueObj).equalsIgnoreCase("bool")) {
						localVarInfo.valueType = "Boolean";
						localValueObj = "Boolean";
					} else {
						localVarInfo.valueType = ((String) localValueObj);
					}
					if (strType.equalsIgnoreCase("vector"))
						localVarInfo.type = "java.util.ArrayList<" + ((String) localValueObj) + ">";
					else if (strType.equalsIgnoreCase("set")) {
						localVarInfo.type = "java.util.TreeSet<" + ((String) localValueObj) + ">";
					}
					localVarInfo.initValue = "new " + localVarInfo.type + "()";
				} else {
					throw new RuntimeException("不认识的类型,type=" + strType);
				}

				String localObject2 = "";
				Object tempObject = ((Element) localBean).getNextSibling();
				if ((tempObject != null) && (3 == ((Node) tempObject).getNodeType())) {
					localObject2 = ((Node) tempObject).getTextContent().trim().replaceAll("[\r\n]", "");
				}

				localVarInfo.comment = ((String) localObject2);

				localVarInfo.ref = strRef;
			}
		}
		localBeanMap.put("varList", localArrayList);
		publicBeanMap.put(localBeanMap.get("classname"), localBeanMap);
		return bool;
	
	}*/

	private static boolean writeJavaFile(Configuration paramConfiguration,
			String paramString1, String paramString2, Element paramElement,
			boolean paramBoolean) {
		ClassNameUtil localClassNameUtil = new ClassNameUtil(paramString2);
		String str1 = paramString1 + "/" + localClassNameUtil.getDir() + ".java";
		HashMap<Object, Object> localHashMap = new HashMap<Object, Object>();
		localHashMap.put("namespace", localClassNameUtil.getNamespace());
		localHashMap.put("classname", localClassNameUtil.getShortName());
		localHashMap.put("defineOnly", Boolean.valueOf(paramBoolean));
		String[] arrayOfString1 = paramElement.getAttribute("from").split(",");

		boolean bool = arrayOfString1[0].isEmpty();
		localHashMap.put("xlsfiles", arrayOfString1);
		localHashMap.put("baseclass", paramElement.getAttribute("baseclass"));
		localHashMap.put("pbean", Boolean.valueOf(bool));

		String str2 = paramElement.getAttribute("genxml");
		if ((!(str2.isEmpty())) && (!(str2.equals("server")))
				&& (!(str2.equals("client")))) {
			throw new RuntimeException("错误的值,genxml=" + str2);
		}
		if (bool != str2.isEmpty())
			throw new RuntimeException("错误的值,genxml=" + str2);
		localHashMap.put("genxml", str2);

		NodeList localNodeList = paramElement.getElementsByTagName("variable");

		ArrayList<VarInfo> localArrayList = new ArrayList<VarInfo>(localNodeList.getLength());
		Object localObject1;
		for (int i = 0; i != localNodeList.getLength(); ++i) {
			localObject1 = (Element) localNodeList.item(i);

			VarInfo localVarInfo = new VarInfo();
			localArrayList.add(localVarInfo);
			localVarInfo.name = ((Element) localObject1).getAttribute("name");

			String str3 = ((Element) localObject1).getAttribute("type");
			String str4 = ((Element) localObject1).getAttribute("min");
			String str5 = ((Element) localObject1).getAttribute("max");
			String str6 = ((Element) localObject1).getAttribute("ref");
			String str7 = ((Element) localObject1).getAttribute("fromCol");

			if (!(str7.isEmpty())) {
				String[] localObject2 = str7.split("\\|");
				for (String localObject4 : localObject2) {
					String[] arrayOfString2 = localObject4.split(":");
					if (arrayOfString2.length == 2) {
						localVarInfo.prefixMapping.put(arrayOfString2[0], arrayOfString2[1].split(","));
					} else if (arrayOfString2.length == 1) {
						localVarInfo.prefixMapping.put("", arrayOfString2[0].split(","));
					} else
						throw new RuntimeException("错误的值,fromcol=" + str7);
				}
			} else {
				if (bool) {
					throw new RuntimeException("未指定fromcol属性");
				}
				String localObject2 = ((Element) localObject1).getAttribute("prefix");
				if (!localObject2.isEmpty()) {
					localVarInfo.prefixMapping.put("", localObject2.split(","));
				}
			}
			if (str3.isEmpty()) {
				logger.error(paramString2 + "'s variable need type");
			} else {
				if ((str3.equalsIgnoreCase("string"))
						|| (str3.equalsIgnoreCase("int"))
						|| (str3.equalsIgnoreCase("long"))
						|| (str3.equalsIgnoreCase("double"))
						|| (str3.equalsIgnoreCase("bool"))
						|| (str3.equalsIgnoreCase("float"))) {
					String localObject2 = "";
					if (str3.equalsIgnoreCase("string")) {
						localObject2 = "null";
						localVarInfo.type = "String";
						if ((!(str4.isEmpty())) && (!(isInt(str4))))
							throw new RuntimeException("min属性错误");
						if ((!(str5.isEmpty())) && (!(isInt(str5))))
							throw new RuntimeException("max属性错误");
						localVarInfo.minValue = str4;
						localVarInfo.maxValue = str5;
					} else if (str3.equalsIgnoreCase("int")) {
						localObject2 = "0";
						localVarInfo.type = "int";
						if ((!(str4.isEmpty())) && (!(isInt(str4))))
							throw new RuntimeException("min属性错误");
						if ((!(str5.isEmpty())) && (!(isInt(str5))))
							throw new RuntimeException("max属性错误");
						localVarInfo.minValue = str4;
						localVarInfo.maxValue = str5;
					} else if (str3.equalsIgnoreCase("long")) {
						localObject2 = "0L";
						localVarInfo.type = "long";
						if (!(str4.isEmpty()))
							if (isLong(str4))
								localVarInfo.minValue = str4 + "L";
							else
								throw new RuntimeException("min属性错误");
						if (!(str5.isEmpty()))
							if (isLong(str5))
								localVarInfo.maxValue = str5 + "L";
							else
								throw new RuntimeException("max属性错误");
					} else if (str3.equalsIgnoreCase("double")) {
						localObject2 = "0.0";
						localVarInfo.type = "double";
						if (!(str4.isEmpty()))
							if (isDouble(str4))
								localVarInfo.minValue = str4 + "d";
							else
								throw new RuntimeException("min属性错误");
						if (!(str5.isEmpty()))
							if (isDouble(str5))
								localVarInfo.maxValue = str5 + "d";
							else
								throw new RuntimeException("max属性错误");
					} else if (str3.equalsIgnoreCase("float")) {
						localObject2 = "0.0f";
						localVarInfo.type = "float";
						if (!(str4.isEmpty()))
							if (isFloat(str4))
								localVarInfo.minValue = str4 + "f";
							else
								throw new RuntimeException("min属性错误");
						if (!(str5.isEmpty()))
							if (isFloat(str5))
								localVarInfo.maxValue = str5 + "f";
							else
								throw new RuntimeException("max属性错误");
					} else if (str3.equalsIgnoreCase("bool")) {
						localObject2 = "false";
						localVarInfo.type = "boolean";
					}
					localVarInfo.initValue = ((String) localObject2);
				} else if ((str3.equalsIgnoreCase("vector")) || (str3.equalsIgnoreCase("set"))) {
					String listValue = ((Element) localObject1).getAttribute("value").trim();

					if ((((String) listValue).equalsIgnoreCase("Integer")) || (((String) listValue).equalsIgnoreCase("int"))) {
						localVarInfo.valueType = "Integer";
						listValue = "Integer";
					} else if (((String) listValue).equalsIgnoreCase("String")) {
						localVarInfo.valueType = "String";
						listValue = "String";
					} else if (((String) listValue).equalsIgnoreCase("Long")) {
						localVarInfo.valueType = "Long";
						listValue = "Long";
					} else if (((String) listValue).equalsIgnoreCase("double")) {
						localVarInfo.valueType = "Double";
						listValue = "Double";
					} else if (((String) listValue).equalsIgnoreCase("float")) {
						localVarInfo.valueType = "Float";
						listValue = "Float";
					} else if (((String) listValue).equalsIgnoreCase("bool")) {
						localVarInfo.valueType = "Boolean";
						listValue = "Boolean";
					} else {
						localVarInfo.valueType = ((String) listValue);
//						HashMap<Object, Object> beanMap = publicBeanMap.get((String) listValue);
//						localVarInfo.prefixMapping = beanMap.get("varList");
					}
					if (str3.equalsIgnoreCase("vector"))
						localVarInfo.type = "java.util.ArrayList<" + ((String) listValue) + ">";
					else if (str3.equalsIgnoreCase("set")) {
						localVarInfo.type = "java.util.TreeSet<" + ((String) listValue) + ">";
					}
					localVarInfo.initValue = "new " + localVarInfo.type + "()";
				} else {
					throw new RuntimeException("不认识的类型,type=" + str3);
				}

				String localObject2 = "";
				Object tempObject = ((Element) localObject1).getNextSibling();
				if ((tempObject != null) && (3 == ((Node) tempObject).getNodeType())) {
					localObject2 = ((Node) tempObject).getTextContent().trim().replaceAll("[\r\n]", "");
				}

				localVarInfo.comment = ((String) localObject2);

				localVarInfo.ref = str6;
			}
		}
		localHashMap.put("varList", localArrayList);
		try {
			Template localTemplate = paramConfiguration.getTemplate("bean.ftl");

			new File(str1).getParentFile().mkdirs();
			localObject1 = new OutputStreamWriter(new FileOutputStream(str1), "utf-8");

			localTemplate.process(localHashMap, (Writer) localObject1);
			((Writer) localObject1).close();
		} catch (Exception localException) {
			logger.error("err", localException);
			return bool;
		}
		return bool;
	}

	public static class VarInfo {
		public String name;
		public String type;
		public String initValue;
		public String comment;
		public String[] tablelist;
		public Map<String, String[]> prefixMapping;
		public String valueType;
		public String minValue;
		public String maxValue;
		public String ref;

		public VarInfo() {
			this.prefixMapping = new HashMap<String, String[]>();
		}

		public String getValueType() {
			return this.valueType;
		}

		public final String getComment() {
			return this.comment;
		}

		public final String getInitValue() {
			return this.initValue;
		}

		public final String getName() {
			return this.name;
		}

		public final String getType() {
			return this.type;
		}

		public String[] getTablelist() {
			return this.tablelist;
		}

		public boolean isCollectionValue() {
			return (!(this.valueType.isEmpty()));
		}

		public Map<String, String[]> getPrefixMapping() {
			return this.prefixMapping;
		}

		public String getMinValue() {
			return this.minValue;
		}

		public String getMaxValue() {
			return this.maxValue;
		}

		public String getRef() {
			return this.ref;
		}
	}
}