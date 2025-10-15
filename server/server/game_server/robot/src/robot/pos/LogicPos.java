package robot.pos;

/**
 * 逻辑坐标 (当前角色坐标)
 */
public final class LogicPos extends BasePos {

	public LogicPos(int x, int y) {
		super(x, y);
	}

	/**
	 * @return 获取邻近点，最多八个
	 */
	public java.util.List<LogicPos> getAdjacentPos() {
		final java.util.List<LogicPos> ret = new java.util.ArrayList<LogicPos>();
		for (int i = getX() - 1; i <= getX() + 1; i++) {
			for (int j = getY() - 1; j <= getY() + 1; j++) {
				if (i >= 0 && j >= 0) {
					if (i != getX() || j != getY()) {
						ret.add(new LogicPos(i, j));
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 获取中点
	 * 
	 * @param o
	 *            终点
	 */
	public LogicPos getMiddleLogicPos(final LogicPos o) {
		return new LogicPos((getX() + o.getX()) / 2, (getY() + o.getY()) / 2);
	}

	/**
	 * 
	 * 获取两点间的一点，这一点与this的距离为len 如果 len 大于this与to的距离，那么返回 to
	 * 
	 */
	public LogicPos getPositionBetween(final LogicPos to, int len) {
		final int dis = this.getDirectDistance(to);
		if (0 == dis)
			return to;

		final float k = (len < dis) ? (float) len / dis : 1;

		final int x = (int) ((to.getX() - this.getX()) * k + this.getX());
		final int y = (int) ((to.getY() - this.getY()) * k + this.getY());

		return new LogicPos(x, y);
	}

	/**
	 * @return 两个角色是否在同一个格子坐标上
	 */
	public boolean isInSameGrid(final LogicPos p2) {
		return this.toGridPos().isSamePos(p2.toGridPos());
	}

	/**
	 * @return 对应的格子坐标
	 */
	public GridPos toGridPos() {
		return new GridPos(getX() / GridPos.GRID_WIDTH, getY()
				/ GridPos.GRID_HIGHTH);
	}

	public fire.pb.move.Pos toProtocolPos() {
		if (getX() > Short.MAX_VALUE || getY() > Short.MAX_VALUE)
			throw new IllegalArgumentException("坐标超出最大值：" + toString());
		return new fire.pb.move.Pos((short) getX(), (short) getY());
	}

	@Override
	public String toString() {
		return new StringBuilder("(").append(getX()).append(",").append(getY())
				.append(")").append("(").append(")").toString();
	}
}
