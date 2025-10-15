package fire.script;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import fire.pb.PropConf;
import fire.pb.battle.BattleField;

/**
 * 
 * ParamMap与线程绑定
 * 
 * @author liangyanpeng
 *
 */
public class ParamMapBindThreadManager {
	
	private static ThreadLocal<HashMap<String, Object>> ParamMapMap = new ThreadLocal<>();	
	private static BlockingQueue<HashMap<String, Object>> initParamMapManagerQueue;

	/**
	 * 初始化后, 线程首次绑定脚本引擎就不用初始化而是从{@link ParamMapBindThreadManager#initParamMapManagerQueue}池中获得的.<br>
	 * 在{@link fire.pb.main.Gs#main(String[])}初始化的.
	 */
	public static void initialization() {
		mkdb.util.TimeoutExecutor procedure = mkdb.Mkdb.getInstance().getExecutor().getProcedureTimeoutExecutor();
		int corePoolSize = procedure.getCorePoolSize();
			initParamMapManagerQueue = new LinkedBlockingQueue<>(corePoolSize);
			for (int i = 0; i < corePoolSize; i++) {
				initParamMapManagerQueue.add(new HashMap<String, Object>());
			}
			
	}

	private static HashMap<String, Object> builder() {
		if (null == initParamMapManagerQueue) {
			return createParamMap();
		}
		HashMap<String, Object> instance = initParamMapManagerQueue.poll();
		if (null == instance) {
			return createParamMap();
		}
		return instance;
	} 
	
	private static HashMap<String, Object> createParamMap() {
		return new HashMap<String, Object>();
	}
	
	/**
	 * 如果未先初始化{@link ParamMapBindThreadManager#initialization()}那么线程首次调用都是创建出来的
	 * @return 脚本引擎
	 */
	public static HashMap<String, Object> getMap() {
		HashMap<String, Object> paramMap = ParamMapMap.get();
		if (paramMap == null) {
			ParamMapMap.set(paramMap = builder());
			if (BattleField.logger.isDebugEnabled()) {
				BattleField.logger.debug("绑定脚本引擎的线程:" + Thread.currentThread().getName());
			}
		}
		return paramMap;
	}
	
	
	
}
