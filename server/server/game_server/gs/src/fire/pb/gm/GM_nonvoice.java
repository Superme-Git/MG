package fire.pb.gm;

import fire.pb.util.FireProp;
import mkdb.Transaction;

import com.locojoy.base.Octets;

public class GM_nonvoice extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 3) {
			sendToGM("参数错误 ,正确格式:" + usage());
			return false;
		}
		final long dstroleid = Long.parseLong(args[0]);
		final xbean.Properties prop = xtable.Properties.select(dstroleid);
		if (prop == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}

		final int forbid_time = Integer.parseInt(args[1]);
		if (forbid_time <= 0) {
			sendToGM(usage());
			return false;
		}
		final int dstuserid = prop.getUserid();
		final int gmuserid = getGmUserid();
		final String reason = args[2];
		final boolean aushutup = (args.length <= 3) ? true : (args[3]
				.equals("1"));
		final Octets reasonoctets = fire.pb.talk.MessageMgr
				.convertString2LogOctets(args[2]);
		if (aushutup) {
			gnet.GMShutup send = new gnet.GMShutup(getGmUserid(),
					getGmLocalsid(), dstuserid, forbid_time, reasonoctets);
			gnet.DeliveryManager.getInstance().send(send);
		} else {
			mkdb.Procedure p = new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					final long now = System.currentTimeMillis();
					xtable.Userid2forbidtime.remove(dstuserid);
					xbean.forbidinfo forbid = xbean.Pod.newforbidinfo();
					forbid.setForbidtime(forbid_time * 1000L + now);
					xtable.Userid2forbidtime.add(dstuserid, forbid);
					return true;
				}
			};
			if (Transaction.current() == null)
				p.submit();
			else
				mkdb.Procedure.pexecuteWhileCommit(p);

		}
		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.nonvoice.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);

		mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.UserPunish userpunish = xtable.Userpunish.get(dstuserid);
				if (userpunish == null) {
					userpunish = xbean.Pod.newUserPunish();
					xtable.Userpunish.insert(dstuserid, userpunish);
				}
				xbean.PunishRecord record = xbean.Pod.newPunishRecord();
				record.setGmuserid(gmuserid);
				record.setForbidtime(forbid_time * 1000);
				record.setOptime(System.currentTimeMillis());
				record.setReason(reason);
				record.setRoleid(dstroleid);
				record.setType(xbean.PunishRecord.TYPE_SHUTUP);
				record.setUserid(dstuserid);
				userpunish.getRecords().add(record);
				return true;
			}
		};
		if (Transaction.current() == null)
			p.submit();
		else
			mkdb.Procedure.pexecuteWhileCommit(p);
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return "//nonvoice  $roleId$ $seconds(>0)$ $reason$ $1:全服;0:单服";
	}

}
