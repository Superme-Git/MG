package fire.pb.scene;

import fire.pb.scene.sPos.Position;

/**
 * 地图跳转信息，必须是静态地图
 */
public class GotoInfo {
	public String toString(){
		return new Position(posx, posy) + "跳转到\t" +destmap+ "地图的" +new Position(destx, desty);
	}
	public int getX(){return posx;}
	public int getY(){return posy;}
	public int getDestMapID(){return destmap;}
	public int getDestX(){return destx;}
	public int getDestY(){return desty;}
	
	private int posx;
	private int posy;
	private int destmap;
	private int destx;
	private int desty;
}
