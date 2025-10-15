package mytools;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

public class ClassNameUtil {
	private static Logger logger = Logger.getLogger(ClassNameUtil.class);
	private final String classname;
	private final String[] tmp;

	public static String xmlpathToNS(Element paramElement) {
		String str1 = null;

		while (paramElement != null) {
			if (paramElement.getNodeName().equals("bean")) {
				str1 = paramElement.getAttribute("name");
				if (str1.isEmpty())
					throw new RuntimeException("name==null");
			} else if (paramElement.getNodeName().equals("namespace")) {
				if (str1 == null)
					throw new RuntimeException("name==null");
				String str2 = paramElement.getAttribute("name");
				if (str2.isEmpty())
					throw new RuntimeException(new StringBuilder().append("name==null,ns=").append(str1).toString());
				str1 = str2.concat(".").concat(str1);
			}

			int i = paramElement.getParentNode().getNodeType();
			if (i == 9)
				break;
			if (i == 1)
				paramElement = (Element) paramElement.getParentNode();
			else
				return null;
		}
		return str1;
	}

	public ClassNameUtil(String paramString) {
		this.classname = paramString;
		this.tmp = paramString.split("\\.");
	}

	public String getDir() {
		StringBuilder localStringBuilder = new StringBuilder(this.tmp[0]);
		for (int i = 1; i != this.tmp.length; ++i)
			localStringBuilder.append("/").append(this.tmp[i]);
		return localStringBuilder.toString();
	}

	public String getShortName() {
		return this.tmp[(this.tmp.length - 1)];
	}

	public String getNamespace() {
		return this.classname.substring(0, this.classname.lastIndexOf("."));
	}

	public static Element getElementByClassName(String paramString,
			Element paramElement) {
		XPathFactory localXPathFactory = XPathFactory.newInstance();
		String[] arrayOfString = paramString.split("\\.");
		if (arrayOfString.length < 3)
			return null;
		StringBuilder localStringBuilder = new StringBuilder("/");
		for (int i = 0; i != arrayOfString.length - 1; ++i)
			localStringBuilder.append("/namespace[@name='").append(arrayOfString[i]).append("']");
		localStringBuilder.append("/bean[@name='").append(arrayOfString[(arrayOfString.length - 1)]).append("']");
		try {
			return ((Element) localXPathFactory.newXPath().evaluate(
					localStringBuilder.toString(),
					paramElement.getOwnerDocument(), XPathConstants.NODE));
		} catch (XPathExpressionException localXPathExpressionException) {
			logger.error("err", localXPathExpressionException);
		}
		return null;
	}

	static String capFirst(String paramString) {
		if (paramString.isEmpty())
			return paramString;
		if (paramString.length() > 1)
			return new StringBuilder().append(paramString.toUpperCase().charAt(0))
					.append(paramString.substring(1)).toString();
		return paramString.toUpperCase();
	}
}