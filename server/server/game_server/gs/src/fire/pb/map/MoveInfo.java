package fire.pb.map;

/**
 * 用来计算在两点间行走的目标点的数据结构
 * 
 *
 */
public class MoveInfo {

	//路程的起始点
	private Position startPoint;

	//路程的终点
	private Position endPoint;
	
	//行走的距离
	private int length;
	
	//线段处于的象限
	private int quadrant;

	public MoveInfo(Position startPoint, Position endPoint, int length,
			int quadrant) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.length = length;
		this.quadrant = quadrant;
	}
	
	public Position getStartPoint() {
		return startPoint;
	}

	public Position getEndPoint() {
		return endPoint;
	}

	public int getLength() {
		return length;
	}
	
	public int getQuadrant() {
		return quadrant;
	}
	
}
