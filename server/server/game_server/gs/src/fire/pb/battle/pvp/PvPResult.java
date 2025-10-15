package fire.pb.battle.pvp;

/**
 * PvP某些复杂的返回结果
 * @author XGM
 */
public abstract class PvPResult {

	// 结果
	protected final int result;

	/**
	 * 构造
	 * @param result
	 */
	public PvPResult(int result) {
		this.result = result;
	}

	/**
	 * 获取结果
	 * @return
	 */
	public int get() {
		return result;
	}

	/**
	 * 处理
	 * @return
	 */
	public abstract int handle();
}
