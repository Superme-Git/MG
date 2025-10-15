package fire.pb.battle.pvp;

/**
 * PvP服务接口
 * @author XGM
 */
public interface IPvPServiceHandle {

	/**
	 * 对服务的处理
	 * @param roleId
	 * @param params
	 */
	void handle(final long roleId, int serviceId);
}