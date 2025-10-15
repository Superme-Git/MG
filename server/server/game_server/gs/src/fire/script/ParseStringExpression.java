/**ParseStringExpression.java*/

package fire.script;

import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptException;

/**ClassName:ParseStringExpression Function: 封装一下scriptEngine,直接用一行语句调用获得一个表达式的值*/
public class ParseStringExpression extends AbstractJSEngine {

	public Double ParseStr(String expression, Map<String, Object> paras) throws ScriptException {
		if (fire.script.JavaScript.isNumeric(expression))
			return Double.valueOf(expression);
		
		if (paras != null)
			for (Entry<String, Object> entry : paras.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		if (expression == null || "".equals(expression))
			expression = "0";
		Object o = eval(expression);
		if (o == null)
			return null;
		if (o instanceof Double)
			return (Double) o;
		if (o instanceof Integer)
			return ((Integer) o).doubleValue();
		return 0.0;
	}
}
