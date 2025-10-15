
package xbean;

public interface MazeInfo extends mkdb.Bean {
	public MazeInfo copy(); // deep clone
	public MazeInfo toData(); // a Data instance
	public MazeInfo toBean(); // a Bean instance
	public MazeInfo toDataIf(); // a Data instance If need. else return this
	public MazeInfo toBeanIf(); // a Bean instance If need. else return this

	public int getMapid(); // 地图id
	public int getPos(); // 位置编号或者npcid，待定

	public void setMapid(int _v_); // 地图id
	public void setPos(int _v_); // 位置编号或者npcid，待定
}
