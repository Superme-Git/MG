package fire.pb.instancezone;

import java.util.Map;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.act.Act;
import fire.pb.instancezone.act.BattleAct;
import fire.pb.instancezone.act.CompleteSubsaveAct;
import fire.pb.instancezone.act.GotoAct;
import fire.pb.instancezone.act.MessageAct;
import fire.pb.instancezone.act.QuestionAct;
import fire.pb.instancezone.act.SubmitItemAct;
import fire.pb.instancezone.act.TaskAct;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.talk.MessageMgr;
import mkdb.Transaction;

public abstract class InstanceZoneFactory {
	
	public static InstanceZone getInstanceZone(long landkey, boolean readonly) {
		if (landkey <= 0)
			return null;
		xbean.InstanceZone xzone = null;
		if (!readonly) {
			if (Transaction.current() == null)
				return null;
			xzone = xtable.Instancezones.get(landkey);
		} else
			xzone = xtable.Instancezones.select(landkey);
		if (xzone == null)
			return null;

		InstanceZoneConfig zoneconfig = Module.getInstance()
				.getInstanceZoneConfigs().get(xzone.getInstzoneid());
		if (zoneconfig == null)
			return null;

		Class<? extends InstanceZone> c = Module.getInstance().getId2Class()
				.get(zoneconfig.instZoneId);
		try {
			InstanceZone instzone = c.getConstructor(long.class,
					xbean.InstanceZone.class, InstanceZoneConfig.class,
					boolean.class).newInstance(landkey, xzone, zoneconfig,
					readonly);
			return instzone;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static InstanceZone getRoleCurInstanceZone(long roleId,
			boolean readonly) {
		Long landkey = xtable.Role2instancezone.select(roleId);
		if (landkey == null)
			return null;

		return getInstanceZone(landkey, readonly);
	}

	public static InstanceZone getInstanceZoneByFactionId(final long factionId,
			final int instid) {
		long factionkey = ClanUtils.getClanInstById(factionId, instid);
		if (factionkey <= 0L)
			return null;
		return getInstanceZone(factionkey, false);
	}

	public static InstanceZone getInstanceZoneByFactionRoleId(
			final long roleid, final int instid) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (null == clanInfo)
			return null;

		return getInstanceZoneByFactionId(clanInfo.getKey(), instid);
	}
	
	/**
	 * 根据公会配表创建副本地图对象
	 * @param landid 配表的id
	 * @param roleId
	 * @param ownerId  公会key
	 * @param isGM
	 * @return
	 */
	public static InstanceZone createInstanceZoneByRole(final int landid,
			final long roleId, final long ownerId, boolean isGM) {
		if (Transaction.current() == null) {
			InstanceZone.logger.error("角色[" + roleId + "]在非事物中创建副本[" + landid + "],失败！");
			return null;
		}

		InstanceZoneConfig zoneconfig = Module.getInstance()
				.getInstanceZoneConfigs().get(landid);
		if (zoneconfig == null) {
			InstanceZone.logger.error("角色[" + roleId + "]未找到副本[" + landid + "]相关配置,失败！");
			return null;
		}
		if (!validCreate(roleId, zoneconfig) && !isGM) {
			InstanceZone.logger.error("角色[" + roleId + "]副本[" + landid
					+ "]创建验证失败！");
			return null;
		}
		// 创建公会副本进度存储数据结构
		Map<Integer, Integer> initstates = zoneconfig.getInitInstanceSaves();
		if (initstates == null)
			return null;

		InstanceZone instzone = createInstanceZone(landid, ownerId,	initstates);
		if (instzone == null)
			return null;
		if (instzone instanceof FactionInstZone) {
			if (!ClanUtils.setClanInstByRoleId(roleId, instzone.getLandkey(), landid)) {
				return null;
			} else {
				final long nowtime = System.currentTimeMillis();
				((FactionInstZone) instzone).getClanInstData()
						.getInstreset().put(landid, nowtime);
				fire.pb.instancezone.Module.getInstance().setInstInit(instzone.getLandkey());
			}
		}
		return instzone;
	}

	/**
	 * 创建公会副本
	 * @param landid  配置表id
	 * @param ownerid 公会key
	 * @param initstates 副本进度保存数据结构
	 * @return
	 */
	private static InstanceZone createInstanceZone(final int landid,
			final long ownerid, Map<Integer, Integer> initstates) {
		if (Transaction.current() == null)
			return null;

		InstanceZoneConfig zoneconfig = Module.getInstance()
				.getInstanceZoneConfigs().get(landid);
		if (zoneconfig == null)
			return null;

		xbean.InstanceZone xzone = xbean.Pod.newInstanceZone();
		xzone.setInstzoneid(landid);
		xzone.setOwnerid(ownerid);
		xzone.setZonestate(xbean.InstanceZone.STATE_ACTIVE);// 开启状态
		xzone.setStarttime(zoneconfig.getEnterTime());// 进入时间
		Long landkey = xtable.Instancezones.insert(xzone);//表插入副本实例
		if (landkey == null)
			return null;

		try {
			// 据配置反射创建并初始化
			Class<? extends InstanceZone> c = Module.getInstance()
					.getId2Class().get(landid);
			InstanceZone instzone = c.getConstructor(long.class,
					xbean.InstanceZone.class, InstanceZoneConfig.class,
					boolean.class).newInstance(landkey, xzone, zoneconfig,
					false);
			instzone.init(initstates);
			return instzone;
		} catch (Exception e) {
			Module.logger.error("Reflect new InstanceZone error : ", e);
			return null;
		}
	}

	public static Act createAction(ActionConfig actcfg,
			Map<String, Object> params) {
		if (actcfg == null)
			return null;
		Act act = null;
		int actId = actcfg.servActId;
		try {
			switch (actcfg.type) {
			case ActionConfig.TYPE_MESSAGE:
				act = new MessageAct(actId, params);
				return act;
			case ActionConfig.TYPE_COMPLETE_SUBSAVE:
				act = new CompleteSubsaveAct(actId, params);
				return act;
			case ActionConfig.TYPE_TASK:
				act = new TaskAct(actId, params);
				return act;
			case ActionConfig.TYPE_ANSWER_QUESTION:
				act = new QuestionAct(actId, params);
				return act;
			case ActionConfig.TYPE_BATTLE:
				act = new BattleAct(actId, params);
				return act;
			case ActionConfig.TYPE_SUBMIT_ITEM:
				act = new SubmitItemAct(actId, params);
				return act;
			case ActionConfig.TYPE_GOTO:
				act = new GotoAct(actId, params);
				return act;
			}
		} catch (Exception e) {
			Module.logger.error("create act error，actid = " + actId, e);
		}
		return act;
	}

	static boolean validCreate(long roleId, InstanceZoneConfig zoneconfig) {
		// 首先验证创建者级别
		int leaderlevel = xtable.Properties.selectLevel(roleId);
		if (leaderlevel < zoneconfig.minlevel) {
			MessageMgr.psendMsgNotify(roleId, 142951, MessageMgr.getStringList(
					zoneconfig.minlevel, zoneconfig.name));
			InstanceZone.logger.error("角色[" + roleId + "]未达到副本["
					+ zoneconfig.instZoneId + "]最低等级,创建验证失败！");
			return false;
		}
		if (leaderlevel > zoneconfig.maxlevel) {
			MessageMgr.psendMsgNotify(roleId, 142983, MessageMgr.getStringList(
					zoneconfig.maxlevel, zoneconfig.name));
			InstanceZone.logger.error("角色[" + roleId + "]已超过副本["
					+ zoneconfig.instZoneId + "]最高等级,创建验证失败！");
			return false;
		}
		long now = System.currentTimeMillis();
		if (!zoneconfig.inEnterTime(now)) {
			MessageMgr.psendMsgNotify(roleId, 160364, null);
			InstanceZone.logger.error("角色[" + roleId + "]不在副本["
					+ zoneconfig.instZoneId + "]进入时间,创建验证失败！");
			return false;
		}

		InstanceZone oldinstzone = getInstanceZoneByFactionRoleId(roleId, zoneconfig.instZoneId);
		if (oldinstzone != null) {
			if (!oldinstzone.canDestroy(false)) {
				oldinstzone.destroy(true, false);
			}
		}

		return true;
	}
}
