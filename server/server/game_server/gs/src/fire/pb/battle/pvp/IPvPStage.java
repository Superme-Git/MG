package fire.pb.battle.pvp;

/**
 * PvP阶段接口定义
 * @author XGM
 */
public interface IPvPStage {

	/**
	 * 获得当前阶段
	 * @return
	 */
	public EPvPStage getStage();

	/**
	 * 进入阶段
	 */
	public void onEnter(PvPControl c);

	/**
	 * 离开阶段
	 */
	public void onLeave(PvPControl c);

	/**
	 * 心跳
	 * @param c
	 */
	public default void onTick(PvPControl c) {
		
	}

	/**
	 * 申请进入场景
	 * @param roleId
	 * @param gotoType
	 */
	public default boolean onApplyEnter(PvPControl c, final long roleId, int gotoType) {
		return false;
	}

	/**
	 * 申请离开场景
	 * @param roleId
	 */
	public default void onApplyLeave(PvPControl c, final long roleId) {
		
	}

	/**
	 * 领取奖励
	 * @param c
	 * @param roleId
	 * @param boxType
	 */
	public default boolean onGetAward(PvPControl c, final long roleId, final int boxType) {
		return c.doGetAward(roleId, boxType);
	}
}
