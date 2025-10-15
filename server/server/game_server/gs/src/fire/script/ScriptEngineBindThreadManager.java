package fire.script;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import fire.pb.battle.BattleField;

/**
 * 
 * ScriptEngine与线程绑定
 * 
 * @author liangyanpeng
 *
 */
public class ScriptEngineBindThreadManager {
	
	private static ThreadLocal<ScriptEngine> scriptEngineMap = new ThreadLocal<>();
	
	private static BlockingQueue<ScriptEngine> initScriptEngineManagerQueue;
	
	/**
	 * 初始化后, 线程首次绑定脚本引擎就不用初始化而是从{@link ScriptEngineBindThreadManager#initScriptEngineManagerQueue}池中获得的.<br>
	 * 在{@link fire.pb.main.Gs#main(String[])}初始化的.
	 */
	public static void initialization() {
		mkdb.util.TimeoutExecutor procedure = mkdb.Mkdb.getInstance().getExecutor().getProcedureTimeoutExecutor();
		int corePoolSize = procedure.getCorePoolSize();
		initScriptEngineManagerQueue = new LinkedBlockingQueue<>(corePoolSize);
		for (int i = 0; i < corePoolSize; i++) {
			initScriptEngineManagerQueue.add(createScriptEngine());
		}
	}

	private static ScriptEngine builder() {
		if (null == initScriptEngineManagerQueue) {
			return createScriptEngine();
		}
		ScriptEngine instance = initScriptEngineManagerQueue.poll();
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
	
	/**
	 * 如果未先初始化{@link ScriptEngineBindThreadManager#initialization()}那么线程首次调用都是创建出来的
	 * @return 脚本引擎
	 */
	public static ScriptEngine getScriptEngine() {
		ScriptEngine scriptEngine = scriptEngineMap.get();
		if (scriptEngine == null) {
			scriptEngineMap.set(scriptEngine = builder());
			if (BattleField.logger.isDebugEnabled()) {
				BattleField.logger.debug("绑定脚本引擎的线程:" + Thread.currentThread().getName());
			}
		}
		return scriptEngine;
	}
	
	
	
}
