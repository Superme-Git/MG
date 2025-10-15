package fire.pb.gm;

/**
 * 进入公会副本下一层
 */

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.faction.FactionInstZone;
import mkdb.Procedure;

public class GM_nextsave extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId;
		if (args.length == 0)
			roleId = getGmroleid();
		else if (args.length == 1)
			roleId = Long.valueOf(args[0]);
		else
			return false;
		Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				InstanceZone.logger.error("工会副本进入下一层:" + roleId);
				nextsave();
				return true;
			}

			void nextsave() {
				/*xbean.ClanInfo InfoclanCreate = ClanUtils.getClanInfoById(roleId, true);
				if (null == InfoclanCreate) {
					InstanceZone.logger.error("没有公会副本 1:" + roleId);
					return;
				}
				
				// 公会副本是否已经存在
				xbean.ClanInstances clanCreateInst = xtable.Claninstances.select(InfoclanCreate.getKey());
				if (clanCreateInst == null) {
					InstanceZone.logger.error("没有公会副本 2:" + roleId);
					return;
				}
				
				final Long instanceKey = clanCreateInst.getInstkey().get(111);
				if (instanceKey == null) {
					InstanceZone.logger.error("没有公会副本 3:" + roleId);
					return;
				}
				
				InstanceZone oldzone = InstanceZoneFactory.getInstanceZone(instanceKey, false);
				if (null == oldzone){
					InstanceZone.logger.error("没有公会副本 4:" + roleId);
					return;
				}
				
				int saveId = oldzone.getCurActiveSave();
				InstanceZone.logger.error("当前层数:" + saveId + ",roleid:" + roleId);
				Module.getInstance().clearDynamicNpc(oldzone.getLandkey());

				if (((FactionInstZone) oldzone).triggerSaveCompleted(saveId))
					((FactionInstZone) oldzone).gmSaveCompleted(saveId);

				sendToGM("角色 " + roleId + " 的公会副本 " + oldzone.getInstZoneId()
						+ "进入下一层");*/
				
				InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(getGmroleid(), false);
				int saveId = instzone.getCurActiveSave();
				Module.getInstance().clearDynamicNpc(instzone.getLandkey());

				if (((FactionInstZone) instzone).triggerSaveCompleted(saveId))
					((FactionInstZone) instzone).gmSaveCompleted(saveId);

				sendToGM("角色 " + roleId + " 的公会副本 " + instzone.getInstZoneId()
						+ "进入下一层");
			}
		};
		procedure.submit();

		return true;
	}

	@Override
	String usage() {
		return "//destroyzone [roleid可选，不填默认为摧毁自己和自己队伍的所有副本] 注意：摧毁队伍副本时，此人必须为队长";
	}

}
