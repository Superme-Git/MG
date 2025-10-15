package robot.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import mkio.Protocol;
import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.Main;
import robot.clan.Clan;
import robot.clan.ClanMgr;
import fire.pb.clan.ApplyClan;
import fire.pb.clan.CAcceptOrRefuseInvitation;
import fire.pb.clan.CApplyClan;
import fire.pb.clan.CCancelApplyClan;
import fire.pb.clan.CChangePosition;
import fire.pb.clan.CClanInvitation;
import fire.pb.clan.CCreateClan;
import fire.pb.clan.CEnterClanMap;
import fire.pb.clan.CFireMember;
import fire.pb.clan.CLeaveClan;
import fire.pb.clan.COpenAutoJoinClan;
import fire.pb.clan.CRequestRuneInfo;
import fire.pb.clan.CRuneGive;
import fire.pb.clan.CRuneRequest;
import fire.pb.clan.CRuneRequestView;
import fire.pb.clan.SApplyClanList;
import fire.pb.clan.SClanInvitation;
import fire.pb.clan.SFireMember;
import fire.pb.clan.SLeaveClan;
import fire.pb.clan.SOpenClan;
import fire.pb.clan.SRefreshPosition;
import fire.pb.clan.SRuneRequest;
import fire.pb.clan.SRuneRequestView;
import fire.pb.move.SRoleEnterScene;
import fire.pb.skill.CUpdateInborn;

/**
 * 公会机器人处理：创建公会，申请加入，邀请，主动退出，踢人，改变职位
 * 
 * @author yt
 */
public class ClanTask extends Task_RoleBase {

	public long lastTime = 0l;

	public long timesTimeDelay = 30000l;

	public long currentExceNum = 0l;

	boolean isProcessProtocols = false;

	// 是否需要离开公会
	boolean isNotLeaveClan = false;

	boolean isRuneRequest = false;

	// 公会副本
	boolean isEnterClanCopy = false;

	// 公会领地或者副本
	boolean isEnterClan = true;

	// 是否走路
	private MoveBattle moveBattle = null;

	boolean isCanMove = false;

	// 是否已经进入副本
	boolean isEnterCopy = false;

	boolean isReset = false;

	// 公会数量比例
	int clanNumRate = 200;

	// 申请比例
	double applyClanRate = 0.2;

	// 公会最大数量
	int clanMaxNum = 1;

	// 是否组队
	private TeamTask randomTeamTask = null;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	public ClanTask(LoginRole role) {
		super(role);
		moveBattle = new MoveBattle(role, false, false);
		init();
	}

	public ClanTask(LoginRole role, boolean isNotLeaveClan) {
		super(role);
		moveBattle = new MoveBattle(role, false, false, false);
		this.isNotLeaveClan = isNotLeaveClan;
		init();
	}

	public ClanTask(LoginRole role, boolean isNotLeaveClan, boolean isSay) {
		super(role);
		moveBattle = new MoveBattle(role, false, isSay, false);
		this.isNotLeaveClan = isNotLeaveClan;
		init();
	}

	public ClanTask(LoginRole role, boolean isNotLeaveClan, boolean isSay, boolean isEnterClanCopy) {
		super(role);
		moveBattle = new MoveBattle(role, false, isSay, false);
		this.isNotLeaveClan = isNotLeaveClan;
		this.isEnterClanCopy = isEnterClanCopy;
		init();
	}

	public ClanTask(LoginRole role, boolean isNotLeaveClan, boolean isSay, boolean isEnterClanCopy, boolean isEnterClan) {
		super(role);
		moveBattle = null;
		this.isNotLeaveClan = isNotLeaveClan;
		this.isEnterClanCopy = isEnterClanCopy;
		this.isEnterClan = isEnterClan;
		init();
	}

	/**
	 * 初始化数据满足进入公会或者创建公会的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addgold " + 1000000);
			role.sendCommand("//addhyd " + 1000000);
			role.sendCommand("//strong");
		}
		if (isEnterClanCopy) {
			ClanMgr.getInstance().timeDelay = 2000;
		} else {
			clanNumRate = 50;
			clanMaxNum = 20;
		}
	}

	public void createClan() {
		// init();
		// mkdb.Trace.error("开始创建公会" + role.rolename);
		role.sendProtocol(new CCreateClan(role.rolename, "yt" + role.roleId));
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 临时添加
		if (this.isEnterClanCopy) {
			enterClanCopyMap(false);
		}
		// 能移动的时候在调用
		if (isCanMove && moveBattle != null) {
			moveBattle.run();
		}
		// 协议处理
		if (!protocols.isEmpty()) {
			// 解析协议
			jieXiProtocols();
		}
		if (isProcessProtocols) {
			// mkdb.Trace.error("**********isProcessProtocols"
			// + isProcessProtocols + "role.roleId" + role.roleId);
			isProcessProtocols = false;
			return;
		}
		Long now = System.currentTimeMillis();
		// // 间隔一定的时间处理
		if (now - lastTime < ClanMgr.getInstance().timeDelay) {
			// mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
			// + role.roleId);
			return;
		}
		// 进入副本战斗（现在不用，改到ClanCopyTask.java）
		if (this.isEnterClanCopy) {
			// synchronized (ClanMgr.getInstance().processLocks) {
			// Clan clan = ClanMgr._instance.getClan(role.roleId);
			// if (clan != null) {
			// // 非第一层和或者非第一轮
			// if (clan.nextSaveNum > 1 || clan.nextClearClanNum > 0) {
			// if (now - clan.resetTime >= clan.timeDelay) {
			// clan.resetTime = System.currentTimeMillis();
			// mkdb.Trace.error("重新设置:" + clan.timeDelay);
			// nextSaveClanCopyMap(role.roleId, clan);
			// }
			// } else {
			// // if (Main.total == 3) {
			// long resetTime = clan.resetTime;
			// long timeDelay = clan.timeDelay;
			// boolean isFirst = clan.isFirst;
			// // 如果是第一次，需要让他停留15秒在第一次层
			// if (resetTime == 0l && !isFirst) {
			// clan.timeDelay = 15000l;
			// clan.isFirst = true;
			// } else {
			// if (now - resetTime >= timeDelay) {
			// clan.resetTime = System.currentTimeMillis();
			// nextSaveClanCopyMap(role.roleId, clan);
			// }
			// }
			// }
			// // }
			// }
			// }
		}
		lastTime = System.currentTimeMillis();
		// if (!this.isEnterClanCopy)
		// // 角色处理
		// roleProcess1();
		// else
		roleProcess();
	}

	// /**
	// * 角色处理
	// */
	// private void roleProcess1() {
	// if (ClanMgr.getInstance().isMaster(role.roleId)) {
	// } else if (ClanMgr.getInstance().isMember(role.roleId)) {
	// } else {
	// // 没有公会的成员的处理
	// int rolenum = LoginRoleMgr.getInstance().getRoles().size();
	// int clannum = ClanMgr.getInstance().getClanNum();
	// if (clannum > (rolenum / clanNumRate)) {
	// // mkdb.Trace.error("**********创建公会数量够了" + role.roleId);
	// // 申请公会
	// applyClan();
	// } else {
	// // 第一个玩家上来后把几率调小点，目的是创建公会
	// double random = ClanMgr.getInstance().getRandom();
	// // mkdb.Trace.error("**********第一次随机：" + role.roleId +
	// // "===random"
	// // + random);
	// if (random == 1.0)
	// random = Math.random();
	// // mkdb.Trace.error("**********第二次随机：" + role.roleId +
	// // "===random"
	// // + random);
	// if (random > applyClanRate) {
	// // 申请公会
	// applyClan();
	// } else {
	// // mkdb.Trace
	// // .error("**********创建公会:role.roleId" + role.roleId);
	// if (role.getLevel() < 50) {
	// role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
	// }
	// if (isEnterClanCopy) {
	// // 以上是临时添加
	// // if (!ClanMgr.getInstance().isHaveClan) {
	// // ClanMgr.getInstance().isHaveClan = true;
	// synchronized (ClanMgr.getInstance().currentClanNumLocks) {
	// if (ClanMgr.getInstance().currentClanNum < clanMaxNum) {
	// // 建立公会
	// role.sendProtocol(new CCreateClan(ClanMgr.getInstance().getRandomName(8), "yt" + role.roleId));
	// ClanMgr.getInstance().currentClanNum++;
	// }
	// }
	// // }
	// } else {
	// synchronized (ClanMgr.getInstance().currentClanNumLocks) {
	// if (ClanMgr.getInstance().currentClanNum < clanMaxNum) {
	// // 建立公会
	// role.sendProtocol(new CCreateClan(ClanMgr.getInstance().getRandomName(8), "yt" + role.roleId));
	// ClanMgr.getInstance().currentClanNum++;
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	/**
	 * 角色处理
	 */
	private void roleProcess() {
		if (ClanMgr.getInstance().isMaster(role.roleId)) {
			// 容错处理
			if (role.getSceneID() == -1) {
				// 进入公会领地
				enterClanMap(true);
			}
			// mkdb.Trace.error("**********队长处理" + "role.roleId" + role.roleId);
			Long memberid = ClanMgr.getInstance().getRandomMem(role.roleId);
			if (memberid == -1)
				return;
			double random = Math.random();
			// mkdb.Trace.error("**********random" + role.roleId + "--random："
			// + random);
			if (isNotLeaveClan && random < 0.4) {
				random = 1.0;
			}
			// 踢人
			if (random < 0.4) {
				// mkdb.Trace.error("**********踢人:role.roleId" + role.roleId
				// + "--memberid" + memberid);
				role.sendProtocol(new CFireMember(memberid, 1));
			} else if (random >= 0.4 && random <= 0.7) {
				// 改变职位
				int position = (int) ((Math.random() * 11) + 1);
				// mkdb.Trace.error("**********改变职位:role.roleId" + role.roleId
				// + "--memberid" + memberid + "--position" + position);
				role.sendProtocol(new CChangePosition(memberid, position));
			} else {
				// mkdb.Trace.error("**********进入邀请:role.roleId" + role.roleId
				// + "--memberid" + memberid);
				Long roleId = ClanMgr.getInstance().getRandomOnlineNotClan();
				if (roleId != -1) {
					// mkdb.Trace.error("**********邀请:role.roleId" + role.roleId
					// + "--被邀请者" + roleId);
					// 公会邀请
					role.sendProtocol(new CClanInvitation(roleId));
				}
			}
		} else if (ClanMgr.getInstance().isMember(role.roleId)) {
			// 容错处理
			if (role.getSceneID() == -1) {
				// 进入公会领地
				enterClanMap(true);
			}
			int value = ClanMgr.getInstance().getRandom(4);
			// 如果不能离开工会
			if (isNotLeaveClan && value == 0) {
				value = 1;
			}
			// 如果请求过了就不能再请求
			if (isRuneRequest && value == 2) {
				double random = Math.random();
				if (random > 0.5) {
					value = 1;
				} else {
					value = 3;
				}
			}
			// // 测试用
			// value = ClanMgr.getInstance().getRandom(2);
			// if (value == 0)
			// value = 2;
			// else
			// value = 3;
			// mkdb.Trace.error("**********主动离开公会:role.roleId" + role.roleId);
			if (value == 0) {
				// 主动离开
				role.sendProtocol(new CLeaveClan());
			} else if (value == 1) {
				// mkdb.Trace.error("请求符文请求信息" + role.roleId);
				// 请求符文请求信息
				role.sendProtocol(new CRequestRuneInfo());
			} else if (value == 2) {
				// mkdb.Trace.error("请求符文" + role.roleId);
				// 请求符文
				role.sendProtocol(new CRuneRequestView());
			}
			// 捐献符文
			else if (value == 3) {
				// mkdb.Trace.error("捐献符文" + role.roleId);
				// 增加活跃度
				role.sendCommand("//addhyd " + 100);
				Long clanId = ClanMgr.getInstance().getClanId(role.roleId);
				// 得到被捐献者和iteamid
				List<?> dataList = ClanMgr.getInstance().getRunerRoleIDAndIteamID(role.school, clanId, role.roleId);
				if (dataList != null && dataList.size() > 0) {
					long otherId = (Long) dataList.get(0);
					int iteamId = (Integer) dataList.get(1);
					// mkdb.Trace.error("捐献符文role.roleId" + role.roleId
					// + "---otherId" + otherId + "---" + iteamId);
					role.sendProtocol(new CRuneGive(otherId, 0, iteamId, 0, 0));
					// 移除缓存的对应数据
					ClanMgr.getInstance().removeRunerRoleIDAndIteamID(otherId, clanId, iteamId);
				}
			}
		} else {
			// 没有公会的成员的处理
			int rolenum = LoginRoleMgr.getInstance().getRoles().size();
			int clannum = ClanMgr.getInstance().getClanNum();
			if (clannum > (rolenum / clanNumRate)) {
				// mkdb.Trace.error("**********创建公会数量够了" + role.roleId);
				// 申请公会
				applyClan();
			} else {
				// 第一个玩家上来后把几率调小点，目的是创建公会
				double random = ClanMgr.getInstance().getRandom();
				// mkdb.Trace.error("**********第一次随机：" + role.roleId +
				// "===random"
				// + random);
				if (random == 1.0)
					random = Math.random();
				// mkdb.Trace.error("**********第二次随机：" + role.roleId +
				// "===random"
				// + random);
				if (random > applyClanRate) {
					// 申请公会
					applyClan();
				} else {
					// mkdb.Trace
					// .error("**********创建公会:role.roleId" + role.roleId);
					if (role.getLevel() < 50) {
						role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
					}
					if (isEnterClanCopy) {
						// 以下临时添加
						if (!ClanMgr.getInstance().isHaveClan) {
							ClanMgr.getInstance().isHaveClan = true;
							// 缓存创建公会数据
							ClanMgr.getInstance().createClan(4097, 4097, "aa");
						}
						// // 以上是临时添加
						// if (!ClanMgr.getInstance().isHaveClan) {
						// // 建立公会
						// role.sendProtocol(new CCreateClan(ClanMgr.getInstance().getRandomName(8), "yt" + role.roleId));
						// ClanMgr.getInstance().isHaveClan = true;
						// }
					} else {
						synchronized (ClanMgr.getInstance().currentClanNumLocks) {
							if (ClanMgr.getInstance().currentClanNum <= clanMaxNum) {
								// 建立公会
								role.sendProtocol(new CCreateClan(ClanMgr.getInstance().getRandomName(8), "yt" + role.roleId));
								ClanMgr.getInstance().currentClanNum++;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		// mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId);
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof SClanInvitation) {
			// 被邀请者同意邀请
			long hostroleid = ((SClanInvitation) p).hostroleid;
			// mkdb.Trace.error("**********同意邀请:role.roleId" + role.roleId
			// + "邀请者：" + hostroleid);
			CAcceptOrRefuseInvitation cr = new CAcceptOrRefuseInvitation(hostroleid, (byte) 1);
			role.sendProtocol(cr);
			isProcessProtocols = true;
		}
		// 申请状态
		if (p instanceof SApplyClanList) {
			// mkdb.Trace.error("*********申請列表:role.roleId" + role.roleId);
			SApplyClanList scl = (SApplyClanList) p;
			java.util.ArrayList<fire.pb.clan.ApplyClan> applyclanlist = scl.applyclanlist;
			if (applyclanlist != null) {
				for (ApplyClan applyClan : applyclanlist) {
					int applystate = applyClan.applystate;
					// 取消申请
					if (applystate == 1) {
						role.sendProtocol(new CCancelApplyClan(applyClan.clankey));
					}
				}
				isProcessProtocols = true;
			}
		}
		// 返回请求符文界面
		if (p instanceof SRuneRequestView) {
			// mkdb.Trace.error("发送请求的符文:" + role.roleId);
			// 请求符文
			role.sendProtocol(new CRuneRequest(ClanMgr.runerequestinfolist));
			isProcessProtocols = true;
		}
		// 返回请求的符文列表
		if (p instanceof SRuneRequest) {
			isRuneRequest = true;
			SRuneRequest sRuneRequest = (SRuneRequest) p;
			if (sRuneRequest.requestnum <= 0) {
				return;
			}
			// mkdb.Trace.error("缓存玩家请求的符文:" + role.roleId);
			// 缓存玩家请求的符文
			ClanMgr.getInstance().cacheClanId2runerequestinfolist(null, role.roleId, sRuneRequest.runerequestinfolist);
			isProcessProtocols = true;
		}
		// 处理创建公会成功和被邀请者入会
		if (p instanceof SOpenClan) {
			SOpenClan sc = (SOpenClan) p;
			long clanid = sc.clanid;
			long masterid = sc.masterid;
			// mkdb.Trace.error("**********进入SOpenClan:role.roleId" +
			// role.roleId);
			// 创建公会成功
			if (role.roleId == masterid) {
				// mkdb.Trace.error("*********进入创建公会成功:role.roleId" +
				// role.roleId);
				if (!ClanMgr.getInstance().isHaveClanByRoleId(masterid)) {
					// mkdb.Trace.error("**********创建公会成功:role.roleId"
					// + role.roleId + "---clanid" + clanid);
					// 设置自动申请加入
					role.sendProtocol(new COpenAutoJoinClan(1, (short) 1));
					// 进入公会领地
					enterClanMap(true);
					isCanMove = true;
					// mkdb.Trace.error("**********进入公会领地:" + role.roleId);
					// 缓存创建公会数据
					ClanMgr.getInstance().createClan(clanid, masterid, sc.clanname);
					isProcessProtocols = true;
				}
			}
			// 邀请或者申请成功
			else {
				// mkdb.Trace.error("*********进入邀请或者申请成功:role.roleId"
				// + role.roleId);
				if (!ClanMgr.getInstance().isHaveClanByRoleId(role.roleId)) {
					// mkdb.Trace.error("**********邀请或者申请成功:role.roleId"
					// + masterid + "---clanid" + clanid);
					// 进入公会领地
					enterClanMap(true);
					isCanMove = true;
					// 一键学习技能，为了捐献符文
					role.sendProtocol(new CUpdateInborn(role.school * 100 + 11, (byte) 1));
					// 设置缓存数据
					ClanMgr.getInstance().addMem(clanid, role.roleId);
					isProcessProtocols = true;
				}
			}
		}
		// 只有监听队长的
		if (ClanMgr.getInstance().isMaster(role.roleId)) {
			// mkdb.Trace.error("**********监听队长:role.roleId" + role.roleId);
			// 剔除成员
			if (p instanceof SFireMember) {
				SFireMember sfm = (SFireMember) p;
				long leaveMemberId = sfm.memberroleid;
				// mkdb.Trace.error("**********踢人成功:role.roleId" +
				// leaveMemberId);
				ClanMgr.getInstance().removeMem(-1l, leaveMemberId);
				isProcessProtocols = true;
			}
			// 改变职务
			if (p instanceof SRefreshPosition) {
				SRefreshPosition srp = (SRefreshPosition) p;
				long roleid = srp.roleid;
				int position = srp.position;
				// mkdb.Trace.error("**********职位改变成功:role.roleId" + srp.roleid
				// + "---position:" + position);
				ClanMgr.getInstance().changePosition(-1l, roleid, position);
				isProcessProtocols = true;
			}
		}
		// 主动离开公会
		if (p instanceof SLeaveClan) {
			// mkdb.Trace.error("**********进入主动离开:role.roleId" + role.roleId);
			SLeaveClan slc = (SLeaveClan) p;
			long leaveMemberId = slc.memberid;
			if (role.roleId == leaveMemberId) {
				// mkdb.Trace
				// .error("**********主动离开成功:role.roleId" + leaveMemberId);
				ClanMgr.getInstance().removeMem(-1l, leaveMemberId);
				isProcessProtocols = true;
			}
		}
	}

	/**
	 * 申请加入公会
	 */
	private boolean applyClan() {
		Long clanId = -1l;
		if (isEnterClanCopy) {
			clanId = ClanMgr.getInstance().getRandomClanByCopy();
		} else {
			clanId = ClanMgr.getInstance().getRandomClan();
		}
		// mkdb.Trace.error("*********进入申请:role.roleId" + role.roleId);
		if (clanId != -1) {
			// mkdb.Trace.error("**********申请加入公会:role.roleId" + role.roleId
			// + "----clanId" + clanId);
			// Map dataMap = ClanMgr._instance.clanId2ClanMembers.get(clanId);
			// if (dataMap != null && dataMap.size() >= 90)
			// return false;
			// 申请入公会
			role.sendProtocol(new CApplyClan(clanId));
			return true;
		}
		return false;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		isEnterCopy = false;
	}

	/*
	 * 处理接受到的消息 (non-Javadoc)
	 * @see robot.task.Task_RoleBase#processProtocol(mkio.Protocol)
	 */
	@Override
	public void processProtocol(Protocol p) {
		if (randomTeamTask != null)
			randomTeamTask.processProtocol(p);
		// 邀请玩家进入公会
		if (p instanceof SClanInvitation || p instanceof SLeaveClan || p instanceof SRefreshPosition || p instanceof SFireMember
				|| p instanceof SOpenClan || p instanceof SApplyClanList || p instanceof SRuneRequestView
				|| p instanceof SRuneRequest || p instanceof SRoleEnterScene) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		if (moveBattle != null)
			moveBattle.processProtocol(p);
	}

	/**
	 * 进入公会领地或者公会副本
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:53:24
	 * @version 1.0
	 */
	public void enterClanMap(boolean isAtonce) {
		if (!this.isEnterClan) {
			return;
		}
		if (this.isEnterClanCopy) {
			enterClanCopyMap(isAtonce);
		} else {
			role.sendProtocol(new CEnterClanMap());
		}
		isCanMove = true;
	}

	/**
	 * 进入公会副本
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:54:48
	 * @version 1.0
	 */
	public void enterClanCopyMap(boolean isAtonce) {
		if (!isEnterCopy) {
			Long getClanId = ClanMgr._instance.getClanId(role.roleId);
			if (isAtonce || getClanId != null) {
				isEnterCopy = true;
				mkdb.Trace.error("*********进入副本:role.roleId" + role.roleId);
				role.sendCommand("//goinst 111");
			}
		}
	}

	/**
	 * 进入进下一层（每层停留15秒，每轮停留90秒）
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:54:48
	 * @version 1.0
	 */
	public void nextSaveClanCopyMap(long roleId, Clan clan) {
		Long getClanId = ClanMgr._instance.getClanId(roleId);
		int nextSaveNum = 1;
		if (clan != null)
			nextSaveNum = clan.nextSaveNum;
		mkdb.Trace.error("nextSaveNum:" + nextSaveNum + "getClanId" + getClanId + "ClanName:"
				+ ClanMgr._instance.getClanName(getClanId) + "---roleId:" + roleId);
		// 进入下一层
		if (nextSaveNum < 10)
			role.sendCommand("//nextsave");
		int lastnum = nextSaveNum + 1;
		ClanMgr._instance.setClanPro(roleId, lastnum, -1, -1);
		// 第十层停留15秒后，清除副本
		if (lastnum == 11) {
			clan.timeDelay = 90000;
			destroyzoneClanCopyMap();
			ClanMgr._instance.reset(roleId);
			mkdb.Executor.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					reGoinst(getClanId);
				}
			}, 70, TimeUnit.SECONDS);
		} else {
			clan.timeDelay = 15000;
		}
		mkdb.Trace.error("重新ClanMgr._instance.nextSaveNum:" + lastnum);
	}

	/**
	 * 重新进入副本，第n+1轮
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月18日 下午8:08:10
	 * @version 1.0
	 */
	public static void reGoinst(long getClanId) {
		Map<Long, LoginRole> roleMap = LoginRoleMgr.getInstance().getRoles();
		for (Map.Entry<Long, LoginRole> entry : roleMap.entrySet()) {
			LoginRole role = entry.getValue();
			Long clanId = ClanMgr._instance.getClanId(role.roleId);
			if (clanId == getClanId) {
				mkdb.Trace.error("*********再次进入副本:role.roleId" + role.roleId);
				role.sendCommand("//goinst 111");
			}
		}
	}

	/**
	 * 清出副本
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:54:48
	 * @version 1.0
	 */
	public void destroyzoneClanCopyMap() {
		role.sendCommand("//destroyzone");
	}
}
