package fire.pb.battle.pvp;

/**
 * PvP赛场代理
 * @author XGM
 */
public abstract class PvPRaceProxy {

	/**
	 * 接受访问
	 * @param visitor
	 */
	public void acceptVisit(IPvPVisitor visitor) {
		visitor.visit(this);
	}
}
