package fire.pb.scene.sPos;

/**
 * 屏块坐块
 *
 */
public final class ScreenPos extends BasicPos {

	public ScreenPos(int x, int y){
		super(x, y);
	}
	
	/**\
	 * 通过屏幕索引来构造屏幕区块坐标
	 * @param index
	 */
	public ScreenPos(int index){
		super(index >>> 16,index & 0x0000ffff);
	}
	
	public java.util.Set<Integer> getAroundScreenIndexs()
	{
		final java.util.Set<Integer> list = new java.util.HashSet<Integer>();

		for (int i = getX() - 1; i <= getX() + 1; i++)
		{
			if (i >= 0)
			{
				for (int j = getY() - 1; j <= getY() + 1; j++)
					if (j >= 0)
						list.add((i << 16) + j);
			}
		}
		list.add(getScreenIndex());
		return list;
	}
	
	/**
	 * 
	 * @return 根据屏坐标返回该坐标的索引值，该索引值高低16位分别存储x, y
	 */
	public int getScreenIndex(){
		return (getX() << 16) + getY();
	}

	public static final int SCREEN_GRID_LENGTH = 56;    
	public static final int SCREEN_WIDTH = GridPos.GRID_WIDTH * SCREEN_GRID_LENGTH;    
	public static final int SCREEN_HIGHTH = GridPos.GRID_HIGHTH * SCREEN_GRID_LENGTH;
}
