package fire.pb.scene.sPos;

import fire.pb.scene.BlockInfo;

/**
 *  基本坐标信息
 *  
 */
public abstract class BasicPos {
	private int x;
	private int y;
	private int z;

	public BasicPos(int x0, int y0, int z0){
		x = x0; y = y0;	z = z0;
	}
	
	/**
	 * 默认z=0
	 * @param x0
	 * @param y0
	 */
	public BasicPos(int x0, int y0){
		x = x0; y = y0; z = 0;		
	}
	
	@Override
	public int hashCode(){
		return (x << 16) + y;
	}
	
	public boolean equals(BasicPos o){
		
		if(this == o) return true;
		
		if(!this.getClass().equals(o.getClass())) return false;
		
		return x==o.x && y==o.y && z==o.z;  
	}
	
	public boolean isSamePos(BasicPos o)	{
		return this.equals(o);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	/**
	 *  @param this 起点
	 *  @param o 终点
	 *  @return 两点间距离
	 */
	public int getDistance(final BasicPos o){
		if (!o.getClass().equals(this.getClass()))
			throw new IllegalArgumentException("不同的坐标类型");
		final int width = Math.abs((getX() - o.getX()));
		final int highth = Math.abs(getY() - o.getY());

		return width > highth ? width : highth;
	}
	
	/**
	 * 获取到另一个点的像素直线距离
	 * @param to
	 * @return
	 */
	public int getDirectDistance(BasicPos to){
		if (!to.getClass().equals(this.getClass()))
			throw new IllegalArgumentException("不同的坐标类型");
		int decx = this.getX() - to.getX();
		int decy = this.getY() - to.getY();
		return (int)Math.sqrt((double)(decx*decx + decy*decy));
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + "," + z+ ")";
	}
	
	/**
	 * 判断相邻的一个点在所在的方向
	 * @param w 给定的参考点
	 * @return w 在 this 的哪个方向上
	 */
	public int where(final BasicPos w){
		if(x==w.x && w.y==y)
			return DIR_SELF;
		else if(x==w.x && w.y+1==y)
			return DIR_UP;
		else if(x+1==w.x && w.y+1==y)
			return DIR_RIGHTUP;
		else if(x+1==w.x && w.y==y)
			return DIR_RIGHT;
		else if(x+1==w.x && y+1==w.y)
			return DIR_RIGHTDOWN;
		else if(x==w.x && y+1==w.y)
			return DIR_DOWN;
		else if(w.x+1==x && y+1==w.y)
			return DIR_LEFTDOWN;
		else if(w.x+1==x && w.y==y)
			return DIR_LEFT;
		else if(w.x+1==x && w.y+1==y)
			return DIR_LEFTUP;
		
		//对于不相邻点
		return DIR_UNKNOW;
	}
	
	public boolean isAround(final BasicPos o){
		if(isSamePos(o))
			return true;
		
		final int dir = where(o);
		if(dir < 0)
			return false;
		if(dir > DIR_NUM)
			return false;
		
		return true;
	}
	
	public short getWalkBlockType()
	{
		if(getZ() == 0)
			return BlockInfo.WALK_BLOCK;
		else
			return BlockInfo.WALK_1_BLOCK;
	}
	
	/**
	 * 获取某方向的相反方向
	 * @param dir 给定的方向
	 * @return 相反方向
	 */
	public static int getReverseDir(int dir)
	{
		return (dir + 4) % BasicPos.DIR_NUM;
	}
	
	public static final int DIR_UNKNOW = -1;	// 出错
	public static final int DIR_SELF = 0;		// 就是自己
	public static final int DIR_UP = 1;			// 上
	public static final int DIR_RIGHTUP = 2;	// 右上
	public static final int DIR_RIGHT = 3;		// 右
	public static final int DIR_RIGHTDOWN = 4;	// 右下
	public static final int DIR_DOWN = 5;		// 下
	public static final int DIR_LEFTDOWN = 6;	// 左下
	public static final int DIR_LEFT = 7;		// 左
	public static final int DIR_LEFTUP = 8;		// 左上	
	public static final int DIR_NUM = 8;	    // 全部方向的数目		
	
	
	public static void main(String[] args) {
		
	}
}
