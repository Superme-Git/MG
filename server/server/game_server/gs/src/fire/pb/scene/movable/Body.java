package fire.pb.scene.movable;

import fire.pb.scene.MonsterInfo;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;

/**
 * 身体物件
 * 
 */
public abstract class Body extends SceneObject {
	
	Body(long uid, final String nm, final Scene s, final Position p, int d)
	{
		super(uid, nm, s, p);
		
		dir = d;
	}
	
	final int getDir()
	{
		return dir;
	}
	
	final public void setDir(int d)
	{
		dir = (d < 0 || d > 7) ? dir : d;
	}
	
	final public void setCurDestPos(int x, int y) {
		curDestPos = new Position(x, y);
	}
	
	final Position getCurDestPos()
	{
		return curDestPos;
	}
	
	final public void clearCurDestPos()
	{
		curDestPos = null;
	}
	
	final public void enterMyScreen()
	{
		enterMyScreen(-1);
	}
	
	final public void leaveMyScreen()
	{
		leaveMyScreen(-1);
	}
	
	public abstract void notifySeeEachother(int dir);

	public abstract void notifyForgetEachother(int dir, boolean includenpc);

	final public void enterMyScreen(int dir)
	{
		if(0 == dir)
			return;
		
		final Scene scene = getScene();
		if(null == scene)
			return;
		
		scene.addBodyToScreen(this);
		notifySeeEachother(dir);
	}
	
	final public void leaveMyScreen(int dir)
	{
		if(0 == dir)
			return;
		
		final Scene scene = getScene();
		if(null == scene)
			return;
		
		notifyForgetEachother(dir,true);
		scene.removeBodyFromScreen(this);
	}

	/**
	 * 获得当前脚下的暗雷布怪类型
	 */
	final public int getMonsterConfig() {
		if(null == getScene()) return 0;
		
		final MonsterInfo m = getScene().getMapInfo().getMonsterInfo();
		if (null == m) return 0;

		final GridPos g = getPos().toGridPos();
		return m.getMonsterConfig(g.getX(), g.getY());
	}
	
	
	public boolean updatePosition(Position p)
	{
		final Scene s = getScene();
		if(null == s)
			return false;
		if(!s.validPosition(p))
		{
			SceneManager.logger.error("updatePosition移动时坐标不合法：MapId=" + s.getMapID() + ",Pos=" + p);
			return false;
		}
		// 确定移动的屏块方向
		final int dir = getMyScreenPos().where(s.getScreenPos(p));
		
		leaveMyScreen(dir);
		setPos(p);
		enterMyScreen(dir);
		return true;
	}
	
	public boolean updatePosition(Scene ns, Position np)
	{
		if(null == ns) return false;
		if(null == np) return false;
		if(!ns.validPosition(np))
		{
			SceneManager.logger.error("updatePosition跳转时坐标不合法：MapId=" + ns.getMapID() + ",Pos=" + np);
			return false;
		}
		leaveMyScreen();
		setScene(ns);
		setPos(np);
		clearCurDestPos();
		enterMyScreen();
		return true;
	}
	
	public boolean updatePositionForce(Position p)
	{
		final Scene s = getScene();
		if(null == s)
			return false;
		if(!s.validPosition(p))
		{
			SceneManager.logger.error("updatePosition移动时坐标不合法：MapId=" + s.getMapID() + ",Pos=" + p);
			return false;
		}
		
		leaveMyScreen(-1);
		setPos(p);
		enterMyScreen(-1);
		return true;
	}
	
	final public int getStandardSpeed()
	{
		return 140;
	}
	
	final public int getSpeed()
	{
		return speed;
	}
	
	final public void setSpeed(int val)
	{
		speed = val;
	}
	
	private int dir;						// 方向
	private Position curDestPos = null;		// 移动时的目标点
	private int speed = getStandardSpeed();	// 移动的速度	
}