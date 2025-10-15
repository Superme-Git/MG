package fire.pb.scene.movable;

import gnet.link.Onlines;

import com.locojoy.base.Octets;

import fire.pb.scene.MapHelper;
import fire.pb.scene.Scene;
import fire.pb.scene.SendProtocolThread;
import fire.pb.scene.sPos.BasicPos;
import fire.pb.scene.sPos.Position;
import fire.pb.scene.sPos.ScreenPos;


/**
 * 场景物件
 */
public abstract class SceneObject extends BasicObject {
	
	public SceneObject(long uid, final String nm, final Scene s, final Position p){
		super(uid, nm);
		
		scene = s;
		pos = p;
		/*if(!s.validPosition(p))
		{
			SceneManager.logger.error("生成SceneObject时坐标超出了地图"+s.getMapID()+"范围，坐标" + p);
		}*/
	}
	
	public Position getPos(){
		return pos;
	}
	
	public void setPos(Position p){
		pos = p;
	}
	
	public Scene getScene(){
		return scene;
	}
	
	public void setScene(Scene s){
		scene = s;
	}
	
	public boolean checkVisible()
	{
		return visible;
	}
	
	public void setVisible(boolean tf)
	{
		visible = tf;
	}

	/**
	 * 检测两个物件间的距离是否在一定范围内
	 */
	public boolean checkDistance(SceneObject o, int distance){
		if(! scene.equals(o.scene))	return false;
		
		if(pos.getDistance(o.pos) > distance) return false;
		
		return true;
	}
	
	/**
	 *  在场景s 上 从当前位置p延dir方向 向前的屏索引
	 */
	public java.util.Set<Integer> getForwardScreenIndex(int dir)
	{
		final ScreenPos sp = getMyScreenPos();
		return MapHelper.getForwardScreenIndex(sp.getX(), sp.getY(), getScene().getScreenWidth(), getScene().getScreenHighth(), dir);
	}
	
	/**
	 * 在场景s 上 从当前位置p延dir方向 向后的屏索引
	 */
	public java.util.Set<Integer> getBackScreenIndex(int dir)
	{
		return getForwardScreenIndex(BasicPos.getReverseDir(dir));
	}
	
	/**
	 * 获取周边的屏块索引
	 */
	java.util.Set<Integer> getAroundScreenIndexs()
	{
		return getMyScreenPos().getAroundScreenIndexs();
	}
	
	/**
	 * 获取所有场景坐标所在的屏块
	 */
	public ScreenPos getMyScreenPos()
	{
		return scene.getScreenPos(pos);
	}
	
	public int getMyScreenIndex()
	{
		return getMyScreenPos().getScreenIndex();
	}
	
	/**
	 * 获取周边的角色（全部）
	 */
	public java.util.Map<Long, Role> getAroundRoles()
	{
		return getScene().getRoleInScreensByNum(getAroundScreenIndexs(),Integer.MAX_VALUE,null);
	}
	
	/**
	 * 获取周边角色的role id(全部)
	 */
	public java.util.Set<Long> getAroundRoleIDs()
	{
		return getAroundRoles().keySet();
	}
	
	/**
	 * 向周围的所有角色发送协议
	 */
	public void sendAround(mkio.Protocol p2) {	
		SendProtocolThread.getInstance().send(getAroundRoleIDs(), p2);
	}
	
	/**
	 * 向周围的所有角色发送数据
	 */
	public void sendOctetsAround(int ptype, Octets poctets)
	{
		Onlines.getInstance().sendOctets(getAroundRoleIDs(), ptype, poctets);
	}

	private Scene scene;			// 所在场景
	private Position pos;			// 坐标
	private boolean visible = true;	// 是否可见
}
