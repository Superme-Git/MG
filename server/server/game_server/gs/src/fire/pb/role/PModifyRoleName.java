package fire.pb.role;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import mkdb.Procedure;
import mkdb.util.UniqName;
import fire.log.YYLogger;
import fire.msp.role.GRoleChangeShape;
import fire.msp.role.GRoleModifyName;
import fire.pb.CCreateRole;
import fire.pb.PropRole;
import fire.pb.SModifyRoleName;
import fire.pb.item.Pack;
import fire.pb.map.SceneManager;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.PRankInsertPet;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.ranklist.proc.RankListManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.CheckName;

public class PModifyRoleName extends Procedure {
	private long roleId;
	private String newName;
	private int itemkey;
	private boolean isGM;

	public PModifyRoleName(long roleid, String newName, int itemkey, boolean isGM) {
		super();
		this.roleId = roleid;
		this.newName = newName;
		this.itemkey = itemkey;
		this.isGM = isGM;
	}

	@Override
	protected boolean process() throws Exception {
		if (roleId < 0)
			return false;

		// 检查用用户名合法性
		if (!validRoleName(newName, roleId)) {
			return false; // 用户名不合法
		}

		xbean.Properties prop = xtable.Properties.get(roleId);
		if (prop == null)
			return false;
		String oldname = prop.getRolename();

		if (!isGM) {
			int use = new Pack(roleId, false).removeItemWithKey(itemkey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gaiming, 0,
					"使用改名消耗");
			if (use != 1)
				return false;
		}

		// 若是改回原来的已用过的名字，则不需要验证唯一名了
		if (!prop.getUsedname().contains(newName)) {
			// 检查角色名是否已经用过
			String lowerCaseName = newName.toLowerCase();
			if (!UniqName.allocate("role", lowerCaseName)) {
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 144664, null);
				return false;// 角色名已重复，需要通知
			}
		}

		xbean.ModifyNameRole modRole = xtable.Modnameitemroles.get(roleId);
		if (modRole == null) {
			modRole = xbean.Pod.newModifyNameRole();
			xtable.Modnameitemroles.insert(roleId, modRole);
		}

		modRole.setModcount(modRole.getModcount() + 1);
		modRole.setLastbuytime(System.currentTimeMillis());

		prop.setRolename(newName);
		// 添加不重复的曾用名,可能玩家反复改回以前的名字,进行防重处理
		if (!prop.getUsedname().contains(oldname)) {
			prop.getUsedname().add(oldname);
		}

		xtable.Rolename2key.add(newName, roleId); // 可能失败，因为newName可能是之前用过的名字，想改回去
		fire.pb.talk.MessageMgr.pbroadcastMsgNotify(170025, 0, Arrays.asList(oldname, newName));
		
		xbean.FriendGroups groups = xtable.Friends.get(roleId);
		if (groups != null && groups.getFriendmap().size() != 0) {
			MessageMgr.psendSystemMessageToRoles(groups.getFriendmap().keySet(), 170025, Arrays.asList(oldname, newName));
		}
		
		// 更新排行榜
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleId));
		if (prop.getLevel() >= 30) {
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
				@Override
				public boolean process() {
					xbean.RoleLevelListRecord record = xbean.Pod.newRoleLevelListRecord();
					record.setTime(System.currentTimeMillis());

					xbean.MarshalRoleLevelRecord marshRecord = record.getMarshaldata();
					marshRecord.setLevel(prop.getLevel());
					marshRecord.setRoleid(roleId);
					PropRole pRole = new PropRole(roleId, true);
					marshRecord.setRolename(pRole.getName());
					marshRecord.setSchool(pRole.getSchool());

					xbean.RoleLevelRankList list = xtable.Rolelevellist.get(1);
					if (null == list) {
						list = xbean.Pod.newRoleLevelRankList();
						xtable.Rolelevellist.insert(1, list);
					}

					RankListManager.getInstance().tryInsertRecord(RankType.LEVEL_RANK, list.getRecords(), record);
					return true;
				}
			});
		}

		// 更新宠物排行榜
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, false);
		for (Pet pet : petColumn.getPets()) {
			mkdb.Procedure.pexecuteWhileCommit(new PRankInsertPet(pet.getUniqueId(), true));
		}
		
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRankInsertRoleChangeName(roleId));

		GRoleModifyName send = new GRoleModifyName();
		send.roleid = roleId;
		send.newname = newName;
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		if (team != null){
			send.teammembers.addAll(team.getAllMemberIds());
		}
		fire.pb.GsClient.pSendWhileCommit(send);

		// 通知角色改名事件
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.ModifyRoleNameEvent(roleId, oldname, newName));
		
		// 运营日志
		fire.log.YYLogger.rolNameLog(prop.getUserid(), roleId, 1);
		return true;
	}

	// 检查用用户名合法性
	public static boolean validRoleName(String name, long roleId) {
		int nameLen = CheckName.nameLen();

		int length = 0;
		try {
			length = (new String(name.getBytes("gb2312"), "ISO-8859-1")).length();
		} catch (UnsupportedEncodingException e) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 145627, null);
			return false;
		}

		if (length > nameLen) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 145627, null);
			return false;
		} else if (length < CCreateRole.NAMELEN_MIN) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 145627, null);
			return false;
		}
		int resultCode = fire.pb.util.CheckName.checkValid(name);
		if (resultCode != CheckName.WORD_LEGAL) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 144663, null);
			return false;
		}
		return true;
	}
}
