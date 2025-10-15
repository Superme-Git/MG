package fire.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;


public class TestJSEngine extends AbstractJSEngine
{
	public static void main(String[] args) throws ScriptException
	{		
		javax.script.ScriptEngineManager em = new javax.script.ScriptEngineManager();
		javax.script.ScriptEngine se = em.getEngineByName("JavaScript");
		System.out.println(se.getFactory().getParameter("THREADING"));
		Bindings bindings = se.createBindings();
		bindings.put("a", 1);
		bindings.put("b", 2);
		se.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		String js = "with(Math){a}";
		Object o = se.eval(js);
		System.out.println("a:" + o);
		System.out.println("****************");
		long l = ((long)Integer.MAX_VALUE+1)  + Integer.MAX_VALUE;
		System.out.println(l);
		System.out.println((int)l);
	}
}
