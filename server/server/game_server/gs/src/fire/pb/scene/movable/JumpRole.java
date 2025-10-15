package fire.pb.scene.movable;

import java.util.LinkedList;
import java.util.List;

import fire.msp.move.MRoleJump;
import fire.pb.move.SRoleJumpDrawback;
import fire.pb.scene.BlockInfo;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.sPos.Position;

public class JumpRole
{
	private static final int ENERGY_RECOVER_PER_SECOND = 5;//每秒恢复的气力值
	private static final int FIRST_JUMP_ENERGY = -30;//一段跳消耗的气力
	private static final int SECOND_JUMP_ENERGY = -40;//二段跳消耗的气力
	private static final int JUMP_MILLI_TIME = 1500; //跳跃消耗的时间（单个一段跳或者二段跳）
	public static final int STATE_NO_JUMP = 0;
	public static final int STATE_FIRST_JUMP = 1;
	public static final int STATE_SECOND_JUMP = 2;
	
	public static final short LEVEL3_SECOND_JUMP_BLOCK_TYPE = BlockInfo.JUMP_ALL_BLOCK;//三级加二段跳需要验证的阻挡类型
	public static final short LEVEL3_JUMP_BLOCK_TYPE = LEVEL3_SECOND_JUMP_BLOCK_TYPE | BlockInfo.JUMP_THIRD_BLOCK;//三级跳需要验证的
	public static final short LEVEL2_JUMP_BLOCK_TYPE = LEVEL3_JUMP_BLOCK_TYPE | BlockInfo.JUMP_SECOND_BLOCK;//二级跳需要验证的
	public static final short LEVEL1_JUMP_BLOCK_TYPE = LEVEL2_JUMP_BLOCK_TYPE | BlockInfo.JUMP_FIRST_BLOCK;//一级跳需要验证的
	
	final private Role role;
	private int level;					// 轻功等级
	private int maxdistance;			// 跳跃的最大距离，与等级相关
	private int power;					// 气力值，用于跳跃
	private int maxpower;				// 最大气力值
	
	private int state = 0;					// 跳跃状态： 0 没跳； 1 一段跳； 2 二段跳
	private long lastchangetime = 0;	// 上次修改气力值的时间	
	private long jumptime = 0;				// 跳跃开始的时间
	private Position startpos = null;		// 跳跃的开始点
	private Position firstendpos = null;    //如果在二段跳，一段跳的终点  
	private Position secondstartpos = null;	// 二段跳的转折点
	private Position endpos = null;			// 跳跃的终点
	private int powerrestore = 0;			// 气力储备
	
	public JumpRole(Role role, int jumplevel, int power, int maxpower,int qilirestore)
	{
		this.role = role;
		this.power = power;
		this.maxpower = maxpower;
		this.powerrestore = qilirestore;
		setJumpLevel(jumplevel);
	}
	
	/**
	 * 当轻功等级改变时
	 * @param level
	 */
	public void setJumpLevel(int level)
	{
		this.level = level;
		switch(level)
		{
		case 1:
			this.maxdistance = 300;
			break;
		case 2:
			this.maxdistance = 350;
			break;
		case 3:
			this.maxdistance = 450;
			break;
		}
	}
	
	public void setPowerRestore(int restore)
	{
		this.powerrestore = restore;
	}
	private boolean addPower(int added,long now)
	{
		if(addPowerRestore(added))//先消耗气力储备
			return true;
		
		int curpower = getPower(now); 
		if(curpower + added < 0)
			return false;
		power = Math.min(maxpower, curpower + added);
		lastchangetime = now;
		//TODO 向客户端发送协议，同步气力
		return true;
	}
	
	public boolean addPowerRestore(int addvalue)
	{
		if(powerrestore <= 0)
			return false;
		powerrestore =  Math.max(0, powerrestore + addvalue);
		if(powerrestore <= 0)
		{
			//TODO remove buff
		}
		return true;
	}
	/**
	 * 验证并且跳跃
	 * @param topos 目标点
	 * @param first true：一段跳；false：二段跳
	 * @return
	 */
	public boolean jump(Position spos, Position topos, boolean first)
	{
		if(level == 0/* || (level == 1 && !first)*/)
			return false;
		
		long now = System.currentTimeMillis();
		
		if(!validState(first))//验证状态合法
		{
			Scene.LOG.debug("角色 " + role.getRoleID()+ " 跳跃时，状态不合法");
			return false;
		}
		
		if(!validStartPos(first))//验证起跳位置合法
		{
			Scene.LOG.debug("角色 " + role.getRoleID()+ " 跳跃时，起跳位置不合法");
			return false;
		}
		
		if(!validDistance(spos, topos))//验证距离合法
		{
			Scene.LOG.debug("角色 " + role.getRoleID()+ " 跳跃时，距离不合法");
			return false;
		}
		
		if(!validTeam())//验证队伍合法
		{
			Scene.LOG.debug("角色 " + role.getRoleID()+ " 跳跃时，队伍不合法");
			return false;
		}
		
		if(!addPower(first? FIRST_JUMP_ENERGY : SECOND_JUMP_ENERGY, now))
		{
			Scene.LOG.debug("角色 " + role.getRoleID()+ " 跳跃时，气力不合法");
			return false;
		}
		
		setJumpInfo(first,spos, topos, now);
		role.clearCurDestPos();
		return true;
	}
	
	/**
	 * 验证通过后设置跳跃信息
	 * @param first
	 * @param topos
	 * @param now
	 */
	private void setJumpInfo(boolean first, Position spos, Position topos, long time)
	{
		if(first)
		{
			setState(STATE_FIRST_JUMP);
			startpos = spos;
			endpos = topos;
		}
		else
		{
			setState(STATE_SECOND_JUMP);
			firstendpos = endpos;
			secondstartpos = spos;
			endpos = topos;
		}
		jumptime = time;
	}
	
	/**
	 * stop前的checkmove
	 * @return
	 */
	private boolean checkmove()
	{
		if(getState() == STATE_NO_JUMP)
			return false;
		//一段跳
		List<Position> firstposes = new LinkedList<Position>();
		firstposes.add(startpos);
		Position destPos = (getState() == STATE_FIRST_JUMP) ? endpos : secondstartpos;
		if (!role.getScene().checkPath(firstposes, destPos, getJumpBlockType(true,role.getPos().getZ())))
		{
			Scene.LOG.info("角色 " +role.getName()+ " 一段跳时，路径上 ：" + firstposes.toString() + " , " +destPos+ "有跳跃阻挡点，被拉回（地图ID ="+ role.getScene().getMapID() +" ）");
			return false;
		}
		if(getState() == STATE_FIRST_JUMP)
			return true;
		
		//二段跳
		List<Position> secondposes = new LinkedList<Position>();
		secondposes.add(secondstartpos);
		if (!role.getScene().checkPath(secondposes, endpos, getJumpBlockType(false,role.getPos().getZ())))
		{
			Scene.LOG.info("角色 " +role.getName()+ " 二段跳时，路径上 ：" + firstposes.toString() + " , " +destPos+ "有跳跃阻挡点，被拉回（地图ID ="+ role.getScene().getMapID() +" ）");
			return false;
		}
		return true;
	}
	
	/**
	 * 跳完，只清状态不置坐标
	 * @return
	 */
	public boolean stop(Position stoppos)
	{
		long now = System.currentTimeMillis();
		if(now - jumptime < JUMP_MILLI_TIME * 0.7)//必须大于最小跳跃时间，才能跳完
		{
			Scene.LOG.error("停止跳跃时出错，小于最小跳跃时间。");
			//不再直接拉回，但至少相当于两次checkmove超速
			role.checkValidFrequency(now);
			role.checkValidFrequency(now);
			role.checkValidFrequency(now);
			//confirmPositionToStart();
			//return false;
		}
		if(endpos == null)
		{
			confirmPositionToStart();
			return false;
		}
		if(stoppos.toGridPos().getX() != endpos.toGridPos().getX() || stoppos.toGridPos().getY() != endpos.toGridPos().getY())
		{
			Scene.LOG.error(role.getRoleID() + "停止跳跃时出错，"+role.getScene().getMapID()+" 停止时的点"+ stoppos.toGridPos() +"与开始跳时的最终点 "+endpos.toGridPos()+"不同 " );
			confirmPositionToStart();
			return false;
		}
		
		//checkmove，验证
		if(!checkmove())
		{
			Scene.LOG.error(role.getRoleID() + "停止跳跃CheckMove时出错" );
			confirmPositionToStart();
			return false;
		}
		
		//验证停止的目标点不是阻挡点
		if(role.getScene().checkBlock(stoppos, stoppos.getWalkBlockType()))
		{
			Scene.LOG.error(role.getRoleID() + "停止跳跃时出错，"+role.getScene().getMapID()+" 坐标是阻挡点 : " + stoppos.toGridPos());
			confirmPositionToStart();
			return false;
		}
		
		clear();//先清除跳跃状态， XXX 先/后清除的区别在于，别人看到自己跳入自己视野内的角色，带（后清）不带（先清）跳跃的状态和坐标
		
		//将角色及队友从跳跃的起点移到终点
		role.move(stoppos);
		/*role.updatePosition(stoppos);
		if(role.getTeam() != null)
		{
			for(Role member :role.getTeam().getMembers())
				member.updatePosition(stoppos);
		}*/
		//clear();//后清除跳跃状态
		return true;
	}
	
	/**
	 * 根据时间和状态来验证动作是否合法，验证超时
	 * @param first
	 * @param now
	 * @return
	 */
	private boolean validState(boolean first)
	{
		if(first)
		{
			if(getState() == STATE_NO_JUMP)
				return true;
			return false;
		}
		else
		{
			if(getState() == STATE_FIRST_JUMP/* && (now - jumptime) < JUMP_MILLI_TIME * 1.3*/)
				return true;
			return false;
		}
	}
	
	/**
	 * 根据时间和状态来验证动作是否合法，验证超时
	 * @param first
	 * @param now
	 * @return
	 */
	private boolean validTeam()
	{
		if(!role.canMove())
			return false;
		if(role.getTeam() == null)
			return true;
		if(role.getTeamIndex() > 1)
			return false;
		for(Role member : role.getTeam().getMembers())
		{
			if(member.getLevel() < 10)
				return false;
		}
		return true;
	}
	
	/**
	 * 验证起跳位置合法
	 * @param first
	 * @return
	 */
	private boolean validStartPos(boolean first)
	{
		short blocktype = 0;
		if(first)
			blocktype = role.getWalkBlockType();//起跳点不能是行走阻挡点
		else
			blocktype = getJumpBlockType(false,role.getPos().getZ());//二段跳起跳点的阻挡类型必须与等级符合
		
		if(role.getScene().checkBlock(role.getPos(), blocktype))
			return false;
		
		return true;
	}
	
	/**
	 * 获取与角色跳跃等级相关的阻挡类型
	 * @param first
	 * @return
	 */
	private short getJumpBlockType(boolean first, int z)
	{
		int blocklevel = level + z + (first? 0 : 1);
		switch(blocklevel)
		{
		case 1:
			return LEVEL1_JUMP_BLOCK_TYPE;
		case 2:
			return LEVEL2_JUMP_BLOCK_TYPE;
		case 3:
			return LEVEL3_JUMP_BLOCK_TYPE;
		case 4:
		case 5:
			return LEVEL3_SECOND_JUMP_BLOCK_TYPE;
		}
		throw new IllegalArgumentException("角色 " +role.getName()+ " 轻功等级错误：" + level);
	}
	
	/**
	 * 验证跳跃距离
	 * @return
	 */
	private boolean validDistance(Position spos,Position topos)
	{
		int distace = spos.getDirectDistance(topos);
		if(distace < maxdistance * 1.1)
			return true;
		else
			return false;
	}
	/**
	 * 设置状态，同时通知逻辑添加buff
	 * @param jumpstate
	 */
	public void setState(int jumpstate)
	{
		this.state = jumpstate;
		if(this.state == STATE_NO_JUMP)
			SceneClient.pSend(new MRoleJump(role.getRoleID(), (byte)0 , (byte)0));//向逻辑发送协议删除buff
		else if(this.state == STATE_FIRST_JUMP)
			SceneClient.pSend(new MRoleJump(role.getRoleID(), (byte)1 , (byte)-FIRST_JUMP_ENERGY));//向逻辑发送协议1段跳
		else if(this.state == STATE_SECOND_JUMP)
			SceneClient.pSend(new MRoleJump(role.getRoleID(), (byte)2 , (byte)-SECOND_JUMP_ENERGY));//向逻辑发送协议2段跳
	}
	
	/**
	 * 是否在跳跃中（不区分一段还是二段，不验证超时）
	 * @return
	 */
	public boolean isJumping()
	{
		return getState() != STATE_NO_JUMP;
	}
	/**
	 * 获取跳跃状态，参考JumpRole.STATE_XXX_XXX，不验证超时
	 * @return
	 */
	public int getState()
	{
		return state;
	}
	
	public int getPower(long now)
	{
		return (int)Math.min(maxpower, power + (ENERGY_RECOVER_PER_SECOND * (now - lastchangetime)) / 1000); 
	}
	
	/**
	 * 检测是否为合法的跳跃移动
	 * @param posList	移动过程中经历的点
	 * @param destPos	移动过程最终目的点
	 * @return
	 */
	public boolean checkValidJump(final java.util.List<Position> posList, final Position destPos)
	{
		if(null == role.getScene())
			return false;
		if(getState() == STATE_NO_JUMP)
			return false;
		/*if(System.currentTimeMillis() - jumptime > JUMP_MILLI_TIME * 1.3)//超时也不能再以跳跃形式移动了
		{
			Scene.logger.info("角色 " +role.getName()+ " 跳跃时，超时被拉回（地图ID ="+ role.getScene().getMapID() +" ）");
			return false;
		}*/
		if (!role.getScene().checkPath(posList, destPos, getJumpBlockType(getState() == STATE_FIRST_JUMP,role.getPos().getZ())))
		{
			Scene.LOG.info("角色 " +role.getName()+ " 跳跃时，路径上 ：" + posList.toString() + " , " +destPos+ "有跳跃阻挡点，被拉回（地图ID ="+ role.getScene().getMapID() +" ）");
			return false;
		}

		// 不用判断速度
		return true;
	}
	
	
	/**
	 * 出错时重置位置到起跳位置，同时清理跳跃状态
	 */
	public void confirmPositionToStart()
	{
		/*if(startpos == null)
		{
			Scene.logger.error("把坐标重置到起跳点时没有起跳点");
			return;
		}*/
		//confirmPosition(role.getPos());
		
		//清理跳跃状态
		clear();
		//重置位置
		confirmPosition(role);
	}
	
	/**
	 * 出错时重置位置，同时清理跳跃状态
	 * @param pos
	 */
	/*public void confirmPosition(Position pos)
	{
		//清理跳跃状态
		clear();
		//重置位置
		confirmPosition(role);
	}*/
	
	public static void confirmPosition(Role role)
	{
		//重置位置，不需要了，因为跳跃在stop前都在起跳点
		/*role.updatePosition(pos);
		if(role.getTeam() != null)
		{
			for(Role member :role.getTeam().getMembers())
				member.updatePosition(pos);
		}*/
		//通知客户端
		SRoleJumpDrawback snd = new SRoleJumpDrawback();
		snd.roleid = role.getRoleID();
		snd.srcpos = role.getPos().toProtocolPos();
		snd.srcz = (byte)role.getPos().getZ();
		role.send(snd);
		role.sendWhoSeeMe(snd);
	}
	
	/**
	 * 清理跳跃状态
	 */ 
	public void clear()
	{
		setState(STATE_NO_JUMP);
		startpos = null;
		firstendpos = null;
		secondstartpos = null;
		endpos = null;
		jumptime = 0;
	}
	
	public void quitTeamWhileJump(SceneTeam team, int oldteamindex)
	{
		//队长离开
		if(oldteamindex == 1)
		{
			if(isJumping())
			{//队长跳跃中离开，一般发生在解散队伍，重置所有人的位置到跳跃开始
				confirmPositionToStart();
			}
		}
		//正常队员离开
		if(oldteamindex > 1)
		{
			if(team.getCapitan() == null)
			{
				Scene.LOG.error("正常队员离队时没有队长，角色Id = "+ this.role.getRoleID());
			}
			else if(team.getCapitan().isJumping())
			{//队长跳跃中，正常队员离开，设置此队员的位置为队长起跳位置
				this.role.updatePosition(team.getCapitan().getJumpRole().getStartPos());
			}
		}
	}
	
	public Position getStartPos()
	{
		return startpos;
	}

	public Position getEndPos()
	{
		return endpos;
	}

	public long getJumpTime()
	{
		return jumptime;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getJumpDistance()
	{
		return maxdistance;
	}
	
	/**
	 * 获取跳跃的坐标
	 * @return
	 */
	public List<Position> getJumpPositions()
	{
		List<Position> poses = new LinkedList<Position>();
		if(getState() == STATE_FIRST_JUMP)
		{
			poses.add(startpos);
			poses.add(endpos);
		}
		else if(getState() == STATE_SECOND_JUMP)
		{
			poses.add(startpos);
			poses.add(firstendpos);
			poses.add(secondstartpos);
			poses.add(endpos);
		}
		return poses;
	}
}