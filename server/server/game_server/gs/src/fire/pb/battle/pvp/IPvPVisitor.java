package fire.pb.battle.pvp;

import java.util.Comparator;
import java.util.Map;

/**
 * PvP访问者
 * @author XGM
 */
public interface IPvPVisitor {

	/**
	 * 访问
	 * @param pvpRaceProxy
	 */
	public void visit(PvPRaceProxy pvpRaceProxy);




	/************************************************************************************/
	/**
	 * 工具集
	 * @author XGM
	 */
	public static class Util {

		/**
		 * 比较名次
		 * @author XGM
		 */
		public static abstract class ComparatorRank<T> implements Comparator<T> {

			final Map<Long, Integer> currentRankMap;

			public ComparatorRank(Map<Long, Integer> currentRankMap) {
				this.currentRankMap = currentRankMap;
			}

			public int compareRank(long roleId1, long roleId2) {
				int v = 0;
				if (currentRankMap != null) {
					Integer rank1 = currentRankMap.get(roleId1);
					Integer rank2 = currentRankMap.get(roleId2);
					if (rank1 != null && rank2 != null) {
						v = rank1.intValue() - rank2.intValue(); // 当前榜单排名在前的靠前
					} else if (rank1 == null && rank2 == null) {
						v = 0;
					} else {
						// 如果有人不在当前榜单,则靠后
						if (rank1 != null && rank2 == null) {
							v = -1;
						} else if (rank1 == null && rank2 != null) {
							v = 1;
						}
					}
				}
				return v;
			}
		}
	}
	/************************************************************************************/
}
