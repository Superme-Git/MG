package fire.script;

import java.util.concurrent.BlockingQueue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class ScriptEngineBindThreadMgr {
	
	private static ThreadLocal<ScriptEngine> scriptEngineMap = new ThreadLocal<>();
	private static BlockingQueue<ScriptEngine> initScriptEngineMgrQueue;
	public static void initialization() {
		createScriptEngine();
	}

	private static ScriptEngine builder() {
		if (null == initScriptEngineMgrQueue) {
			return createScriptEngine();
		}
		ScriptEngine instance = initScriptEngineMgrQueue.poll();
		if (null == instance) {
			return createScriptEngine();
		}
		return instance;
	} 
	
	private static ScriptEngine createScriptEngine() {
		ScriptEngine instance = new ScriptEngineManager().getEngineByName("nashorn");
		Module.getInstance().registerJavaScriptExpr();
		Module.getInstance().registerAllExprToEngine(instance);
		return instance;
	}
	
	
	public static ScriptEngine getScriptEngine() {
		ScriptEngine scriptEngine = scriptEngineMap.get();
		if (scriptEngine == null) {
			scriptEngineMap.set(scriptEngine = builder());
		}
		return scriptEngine;
	}
	
	
	
}
