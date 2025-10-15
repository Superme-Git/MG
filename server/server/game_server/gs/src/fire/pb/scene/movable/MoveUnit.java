package fire.pb.scene.movable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import fire.pb.KickErrConst;
import fire.pb.main.ConfigManager;
import fire.pb.move.SRoleMove;
import fire.pb.scene.Scene;
import fire.pb.scene.SendProtocolThread;
import fire.pb.scene.sPos.Position;
import fire.pb.util.DateValidate;
import fire.pb.util.FireProp;

/**
 * 可移动单元
 */
public abstract class MoveUnit extends Body {
	public static int taiwan = 1;
	
	MoveUnit(long uid, String nm, Scene s, Position p, int d)
	{
		super(uid, nm, s, p, d);
	}
	
	public abstract long getRoleID(); 

	public boolean checkNeedCheckPos() {
		return needCheckPos;
	}

	public void setNeedCheckPos() {
		needCheckPos = true;
	}

	public void clearNeedCheckPos() {
		needCheckPos = false;
	}

	/**
	 * 检测角色移动速度是否合法
	 */
	private boolean checkValidSpeed(int cruiseid, long curTick, int speed)
	{
		if(! checkVisible()) 
			return true;
		if (speed >= MAX_MOVE_SPEED) {
			Scene.LOG.debug("这速度太大了\t" + speed);
			return false;
		}

		if (speed <= STANDARD_MOVE_SPEED)
			return true;
		
		if (cruiseid > 0)
		{
			return true;
		}

		if (0 == overSpeedNum)
			firstOverSpeedTime = curTick;
		
		overSpeedNum ++;
		if (overSpeedNum >= MAX_OVERSPEED_NUM) 
		{
			overSpeedNum = 0;
			if (( curTick - firstOverSpeedTime) < MAX_OVERSPEED_ELAPSE) 
			{
				Scene.LOG.error("角色 " +getRoleID()+" 累计超速了，踢掉。");
				firstOverSpeedTime = 0;
				gnet.link.Onlines.getInstance().kick(((Role)this).getRoleID(), KickErrConst.ERR_RUN_TOO_FAST);
				return false;
			}
			firstOverSpeedTime = 0;
		}
		return true;
	}
	
	/**
	 * 检测是否为合法的移动
	 * @param posList	移动过程中经历的点
	 * @param destPos	移动过程最终目的点
	 * @param curTick	当前的时刻
	 * @param length	路线长度
	 * @return
	 */
	public boolean checkValidMove(int cruiseid, final java.util.List<Position> posList, final Position destPos, long curTick, int length) {

		if(! checkVisible()) return true;
		if(null == getScene()) return false;

//		if (!getScene().checkPath(posList, destPos, getWalkBlockType()))
//		{
//			Scene.logger.info("角色 " +getRoleID()+ "路径上 ：" + posList.toString() + "有阻挡点，被拉回（地图ID ="+ getScene().getMapID() +" ）");
//			return false;
//		}

		// 根据路线长度进行速度判断
		if(! checkValidSpeed(cruiseid, curTick, length))
			return false;

		return true;
	}
	
	public short getWalkBlockType()
	{
		return getPos().getWalkBlockType();
	}

	/**
	 * 累计统计，角色的移动检测合法性验证, 验证角色发送处理的频率
	 */
	public boolean checkValidFrequency(long curTick)
	{
		if ((curTick - moveCheckTime) < CHECK_INTERVAL)
		{			
			if (0 == badFrequencyNum)
				firstBadFrequencyTime = curTick;
			
			    badFrequencyNum ++;
			if (badFrequencyNum >= MAX_BADSPEED_NUM*taiwan)
			{
				badFrequencyNum = 0;
				if ((curTick - firstBadFrequencyTime) < CHECK_INTERVAL * MAX_BADSPEED_NUM * taiwan * 3)
				{
					if (!DateValidate.inTheSameDay(badFrequencyLastTime, curTick)) 
						badFrequencyCheckMove = 0;
					badFrequencyCheckMove ++;
					if (badFrequencyCheckMove >= 50){
				
						Integer kick = fire.pb.scene.movable.MoveUnit.NEED_KICK.get();
						if (kick != null && kick == 1)
						{
							Scene.LOG.error("角色 " +getRoleID()+" checkmove频率过快，踢掉。");
							gnet.link.Onlines.getInstance().kick(((Role)this).getRoleID(), KickErrConst.ERR_RUN_TOO_FAST);							
						}
						
						badFrequencyCheckMove = 0;
					}
					badFrequencyLastTime = curTick;
					firstBadFrequencyTime = 0;
					return false;
				}
				firstBadFrequencyTime = 0;
			}
		}
		moveCheckTime = curTick;
		return true;
	}
	
	/**
	 * 通知角色确认位置
	 */
	public void confirmCurPosition() {
		if(!confirmEverytime)
		{
			long now = System.currentTimeMillis();
			if ((now - confirmtime) < CONFIRM_PERIOD)
				return;
			else
				confirmtime = now;
		}
		final SRoleMove snd = new SRoleMove();
		snd.roleid = getRoleID();
		/*snd.srcpos.x = p.getX();
		snd.srcpos.y = p.getY();*/
		snd.destpos = getPos().toProtocolPos();
		SendProtocolThread.getInstance().send(getRoleID(), snd);
	}
	
	/**
	 * 通知角色确认位置，从当前位置，确认到destpos位置
	 */
	public void confirmPositionTo(final Position destpos) {
		if(!confirmEverytime)
		{
			long now = System.currentTimeMillis();
			if ((now - confirmtime) < CONFIRM_PERIOD)
				return;
			else
				confirmtime = now;
		}
		final SRoleMove snd = new SRoleMove();
		snd.roleid = getRoleID();
		/*snd.srcpos.x = getPos().getX();
		snd.srcpos.y = getPos().getY();*/
		snd.destpos = destpos.toProtocolPos();
		SendProtocolThread.getInstance().send(getRoleID(), snd);
	}
	
	public java.util.Map<Long, Role> getIcansee()
	{
		return icansee;
	}
	
	public java.util.Map<Long, Role> getWhoseeme()
	{
		return whoseeme;
	}
	
	void addIcansee(Role r)
	{
		icansee.put(r.getRoleID(), r);
	}
	
	void rmIcansee(Role r)
	{
		icansee.remove(r.getRoleID());
	}
	
	void addWhoseeme(Role r)
	{
		whoseeme.put(r.getRoleID(), r);
	}

	void rmIcanseeByID(long roleid)
	{
		icansee.remove(roleid);
	}
	
	void rmWhoseeme(Role r)
	{
		whoseeme.remove(r.getRoleID());
	}

	void rmWhoseemeByID(long roleid)
	{
		whoseeme.remove(roleid);
	}
	
	void clearIcansee()
	{
		icansee.clear();
	}
	
	void clearWhoseeme()
	{
		whoseeme.clear();
	}
	
	/**
	 * 获取周围那些看不到自己的角色
	 * @return
	 */
	public java.util.Map<Long, Role> getWhoNotseeme()
	{
		Map<Long,Role> aroundroles = getAroundRoles();
		for(long rid : this.getWhoseeme().keySet())
		{
			aroundroles.remove(rid);
		}
		return aroundroles;
	}
	
	/**
	 * 获取周围那些自己看不到的角色
	 * @return
	 */
	public java.util.Map<Long, Role> getIcanNotsee()
	{
		return getIcanNotsee(getAroundScreenIndexs(), Integer.MAX_VALUE);
	}
	
	public int getbadFrequencyNum(){
		return badFrequencyNum;
	}
	
	/**
	 * 获取屏块中那些自己应该看见却看不到的角色
	 * @return
	 */
	protected java.util.Map<Long, Role> getIcanNotsee(java.util.Set<Integer> indexes, int maxnum)
	{
		if(!(this instanceof Role))
			return new HashMap<Long, Role>();
		return getScene().getRoleCannotSee(indexes, maxnum, (Role)this);
	}
	
	private final java.util.Map<Long, Role> icansee = new java.util.HashMap<Long, Role>();
	private final java.util.Map<Long, Role> whoseeme = new java.util.HashMap<Long, Role>();

	private int overSpeedNum = 0;							// 超速次数
	private long firstOverSpeedTime;	 					// 第一次超速时间
	public boolean needCheckPos; 		 					// 用于检查是否进行调整位置的判断

	public long confirmtime = 0;
	public long moveCheckTime = 0;
	public long firstBadFrequencyTime = 0;					// 发送验证频率过快，第一次计时
	public int badFrequencyCheckMove = 0;               //为台湾版做的优化,ccheckmove频率过快一天内超过3次后再踢
	public long badFrequencyLastTime = 0;
	public int badFrequencyNum = 0;						// 发送过快 的计数
	
	public long CRoleMove_CheckTime = 0;               //检验玩家发CRoleMove的频率
	
	private static boolean confirmEverytime 		= FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.pos.confirm") == 1;
	private static final int CONFIRM_PERIOD				= 5000;
	private static final int MAX_MOVE_SPEED 		= 230;		// 每次检测间隔最多可走多少坐标	
	private static final int MAX_OVERSPEED_NUM 		= 10; 		// 允许的超速次数
	private static final int MAX_OVERSPEED_ELAPSE	= 50000; 	// 从第一次超速到超速次满的时间消耗
	private static final int CHECK_INTERVAL 		= 250;		// 检测时间间隔（ms）
	private static final int MAX_BADSPEED_NUM 		= 20;		// 发送检测消息太快的最大次数
	
	public static final int STANDARD_MOVE_SPEED		= 110;		// 标准速度
	public static final int PERSTEP_PIX 			= 60;      	// 每步 约 60像素(客户端与策划设定，服务器用于按步遇怪计算)
	
	public static java.util.concurrent.atomic.AtomicInteger NEED_KICK = new java.util.concurrent.atomic.AtomicInteger(1); 
}

