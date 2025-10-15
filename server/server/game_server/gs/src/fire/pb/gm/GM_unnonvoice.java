package fire.pb.gm;

import fire.pb.util.FireProp;

public class GM_unnonvoice extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			sendToGM("参数错误 ,正确格式:" + usage());
			return false;
		}

		final long dstroleid = Long.parseLong(args[0]);
		final int gmuserid = getGmUserid();
		final xbean.Properties prop = xtable.Properties.select(dstroleid);
		if (prop == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}

		final int dstuserid = prop.getUserid();
		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.unonvoice.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.UserPunish userpunish = xtable.Userpunish.get(dstuserid);
				if (userpunish == null) {
					userpunish = xbean.Pod.newUserPunish();
					xtable.Userpunish.insert(dstuserid, userpunish);
				}
				xbean.PunishRecord record = xbean.Pod.newPunishRecord();
				record.setGmuserid(gmuserid);
				record.setForbidtime(0);
				record.setOptime(System.currentTimeMillis());
				record.setReason("");
				record.setRoleid(dstroleid);
				record.setType(xbean.PunishRecord.TYPE_UNSHUTUP);
				record.setUserid(dstuserid);
				userpunish.getRecords().add(record);

				xtable.Userid2forbidtime.remove(dstuserid);
				return true;
			}
		}.submit();
		StringBuilder content = new StringBuilder("//unnonvoice ");
		content.append(args[0]);
		return true;
	}

	@Override
	String usage() {
		return "//unnonvoice $roleId$";
	}

}
