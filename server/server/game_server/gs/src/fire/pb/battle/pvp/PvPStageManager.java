package fire.pb.battle.pvp;

import java.util.HashMap;
import java.util.Map;

/**
 * PvP阶段管理
 * @author XGM
 */
public class PvPStageManager {

	private final Map<EPvPStage, IPvPStage> manager = new HashMap<EPvPStage, IPvPStage>();

	/**
	 * 注册
	 * @param e
	 * @param i
	 */
	public void register(EPvPStage e, IPvPStage i) {
		manager.put(e, i);
	}

	/**
	 * 获取
	 * @param e
	 * @return
	 */
	public IPvPStage get(EPvPStage e) {
		return manager.get(e);
	}
}
