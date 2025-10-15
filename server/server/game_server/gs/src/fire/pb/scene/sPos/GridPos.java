package fire.pb.scene.sPos;

/**
 *  每个格子坐标描述一个阻挡点信息
 *  主要用来进行阻挡判断，无其它功能
 *  
 */
public class GridPos extends BasicPos {
	public GridPos(int x, int y, int z){
		super(x, y, z);
	}	
	public GridPos(int x, int y){
		super(x, y);
	}	
	

	/**
	 * 获取格子中点
	 * @param dest
	 * @return
	 */
	public GridPos getMidPos(GridPos dest){
		return  new GridPos((getX()+dest.getX()) /2, (getY()+dest.getY()) /2, getZ());
	}
	
	/**
	 * 
	 * @return 获取邻近格子坐标点，最多八个
	 */
	public java.util.List<GridPos> getAdjacentGridPos(){
		final java.util.List<GridPos> ret  = new java.util.ArrayList<GridPos>();
		for(int i=getX()-1; i<=getX()+1; i++){
			for(int j=getY()-1; j<=getY()+1; j++)
			{
				if(i>=0 && j>=0)
				{
					if(i!=getX() || j!=getY()){
						ret.add(new GridPos(i, j, getZ()));
					}
				}
			}
		}
		return ret;
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder("GPos(").append(getX()).append(",").append(getY()).append(",").append(getZ()).append(")").toString();
	}
	
	/**
	 * 获取其顶点的逻辑坐标
	 * @return
	 */
	public Position toPosition(){
		if(topos == null)
			topos = new Position(getX()*GRID_WIDTH, getY()*GRID_HIGHTH, getZ());
		return topos;
	}
	
	private Position topos;
	public static final int GRID_WIDTH = 16;		// 每个格子所占坐标宽度
	public static final int GRID_HIGHTH = 16;		// 每个格子所占坐标高度
}
