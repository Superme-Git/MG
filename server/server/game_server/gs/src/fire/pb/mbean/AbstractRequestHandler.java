

package fire.pb.mbean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


public abstract class AbstractRequestHandler implements IRequestHandler {

	String cmdName;
	static Map<Object, Object> successMsgMap = new HashMap<Object, Object>();
	
	static{
		successMsgMap.put("return", "true");
	}

	public AbstractRequestHandler(String name) {
		this.cmdName = name;
	}

	@Override
	public String process(Element root) {

		Map<?, ?> paras = parseParameters(root);
		
		Map<?, ?> resultMap = handleRequest(paras);
		
		return buildXmlString(resultMap);
	}

	//默认构造xml的方法,子类可以修改
	protected String buildXmlString(Map<?, ?> resultMap) {

		String head = "<cmd_command cmd_data=\""+cmdName+"\" ";
		StringBuilder result = new StringBuilder(head);
		for (Entry<?, ?> entry : resultMap.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			result.append(key).append("=\"").append(value).append("\" ");
		}
		String tail = "/>";
		result.append(tail);
		return result.toString();
	}
	
	protected abstract Map<Object, Object> handleRequest(Map<?, ?> paras);
	
	protected  Map<Object,Object> parseParameters(Element root) {

		Map<Object,Object> result = new HashMap<Object,Object>();
		int length = root.getAttributes().getLength();
		for (int i = 0; i < length; i++) {
			Node node = root.getAttributes().item(i);
			if (node.getNodeType() == Node.ATTRIBUTE_NODE) 
			   result.put((Object)node.getNodeName(), (Object)node.getNodeValue());
		}
		return result;
	}
	
	protected Map<Object, Object> successMsg(){
		
		return successMsgMap;
	}
	protected Map<Object, Object> successMsg(String desc){
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("return", "true");
		result.put("desc", desc);
		return result;
	}

	protected Map<Object, Object> failedMsg(String desc){
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("return", "false");
		result.put("desc", desc);
		return result;
	}
}

