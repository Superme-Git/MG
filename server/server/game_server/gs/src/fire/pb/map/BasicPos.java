package fire.pb.map;

import fire.pb.scene.BlockInfo;

public abstract class BasicPos {
	private int x;
	private int y;
	private int z;

	public BasicPos(int x0, int y0, int z0) {
		x = x0;
		y = y0;
		z = z0;
	}

	public BasicPos(int x0, int y0) {
		x = x0;
		y = y0;
		z = 0;
	}

	public BasicPos() {
		x = y = z = 0;
	}

	@Override
	public int hashCode() {
		return (x << 16) + y;
	}

	public boolean equals(BasicPos o) {

		if (this == o)
			return true;

		if (!this.getClass().equals(o.getClass()))
			return false;

		return x == o.x && y == o.y && z == o.z;
	}

	public boolean isSamePos(BasicPos o) {
		return this.equals(o);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getDistance(final BasicPos o) {
		final int width = Math.abs((getX() - o.getX()));
		final int highth = Math.abs(getY() - o.getY());

		return width > highth ? width : highth;
	}

	public int getDirectDistance(BasicPos to) {
		int decx = this.getX() - to.getX();
		int decy = this.getY() - to.getY();
		return (int) Math.sqrt((double) (decx * decx + decy * decy));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public short getWalkBlockType() {
		if (getZ() == 0)
			return BlockInfo.WALK_BLOCK;
		else
			return BlockInfo.WALK_1_BLOCK;
	}

	public static int getReverseDir(int dir) {
		return (dir + 4) % BasicPos.DIR_NUM;
	}

	public static final int DIR_UNKNOW = -1; // 错误
	public static final int DIR_SELF = 0; // 自己
	public static final int DIR_UP = 1; // 上
	public static final int DIR_RIGHTUP = 2; // 右上
	public static final int DIR_RIGHT = 3; // 右
	public static final int DIR_RIGHTDOWN = 4; // 右下
	public static final int DIR_DOWN = 5; // 下
	public static final int DIR_LEFTDOWN = 6; // 左下
	public static final int DIR_LEFT = 7; // 左
	public static final int DIR_LEFTUP = 8; // 左上
	public static final int DIR_NUM = 8; // 所有方向

	public static void main(String[] args) {

	}
}
