package fire.pb.scene.sPos;

/**
 *  逻辑坐标 (当前角色坐标就是逻辑坐标)
 *  服务器与客户端通信的坐标均是该类型
 *  
 */
public final class Position extends BasicPos {

	private GridPos togpos;
	
	public Position(int x, int y, int z){
		super(x, y, z);
	}
	public Position(int x, int y){
		super(x, y);
	}
	
	/**
	 *  @return 逻辑坐标对应的格子坐标
	 *  
	 */
	public GridPos toGridPos(){
		if(togpos == null)
			togpos = new GridPos(getX() / GridPos.GRID_WIDTH, getY() / GridPos.GRID_HIGHTH, getZ());
		return togpos;
	}
	
	/**
	 *  @param this 起点
	 *  @param o 终点
	 *  @return 中间点
	 */
	public Position getMidPos(final Position o){
		return new Position((getX()+o.getX()) /2, (getY()+o.getY()) /2, getZ());
	}

	/**
	 *  @return 两个角色（逻辑）坐标是是否在同一个格子坐标上
	 *  
	 */
	public boolean checkInSameGrid(final Position p2){

		if((getX() / GridPos.GRID_WIDTH) != (p2.getX() / GridPos.GRID_WIDTH))
			return false;
		
		if((getY() / GridPos.GRID_HIGHTH) != (p2.getY() / GridPos.GRID_HIGHTH))
			return false;
		
		if(getZ() != p2.getZ())
			return false;
		
		return true;
	}
	
	/**
	 * @return 获取邻近点，最多八个
	 */
	public java.util.List<Position> getAdjacentPos(){
		final java.util.List<Position> ret  = new java.util.ArrayList<Position>();
		for(int i=getX()-1; i<=getX()+1; i++){
			for(int j=getY()-1; j<=getY()+1; j++)
			{
				if(i>=0 && j>=0)
				{
					if(i!=getX() || j!=getY()){
						ret.add(new Position(i, j, getZ()));
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * 获取两点间的一点，这一点与this的距离为len
	 * 如果 len 大于this与to的距离，那么返回 to
	 * 
	 */
	public Position getPositionBetween(final Position to, int len)
	{
		final int dis = this.getDirectDistance(to);
		if(0 == dis)
			return to;
		
		final float k = (len < dis) ? (float)len/dis : 1;
		
		final int x = (int) ((to.getX() - this.getX()) * k + this.getX());
		final int y = (int) ((to.getY() - this.getY()) * k + this.getY());
		
		return new Position(x, y, getZ());
	}
	
	@Override
	public String toString()
	{
		return new StringBuilder("Pos(").append(getX()).append(",").append(getY()).append(",").append(getZ()).append(")").toString();
	}
	
	public fire.pb.move.Pos toProtocolPos()
	{
		if(getX() > Short.MAX_VALUE || getY() > Short.MAX_VALUE)
			throw new IllegalArgumentException("内存中的坐标超出了最大值：" + toString());
		
		return new fire.pb.move.Pos((short)getX(),(short)getY());
	}
}
