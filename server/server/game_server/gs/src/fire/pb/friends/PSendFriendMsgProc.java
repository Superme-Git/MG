package fire.pb.friends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.talk.ChatChannel;
import fire.pb.talk.DisplayInfo;
import fire.pb.talk.MessageMgr;

public class PSendFriendMsgProc extends mkdb.Procedure {

	private final long senderRoleId;
	private long receiverRoleId;

	public String content;
	public String checkShiedMsg;
	public ArrayList<DisplayInfo> displayInfo;
	private OffLineMessageManager manager = OffLineMessageManager.getOffLineMsgManager();

	public PSendFriendMsgProc(long senderRoleId, long receiverRoleId,
			String content, String checkshiedmsg,
			ArrayList<DisplayInfo> displayinfo) {
		this.senderRoleId = senderRoleId;
		this.receiverRoleId = receiverRoleId;
		this.content = content;
		this.checkShiedMsg = checkshiedmsg;
		this.displayInfo = displayinfo;
	}

	@Override
	protected boolean process() throws Exception {
		if (senderRoleId == receiverRoleId) {
			MessageMgr.psendMsgNotify(senderRoleId, 140643, null);
			return true;
		}
		List<Long> list = new ArrayList<Long>();
		list.add(senderRoleId);
		list.add(receiverRoleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));


		xbean.FriendGroups groups = xtable.Friends.get(receiverRoleId);
		if (groups == null) {
			groups = xbean.Pod.newFriendGroups();
			xtable.Friends.insert(receiverRoleId, groups);
		}

		boolean isFriend = groups.getFriendmap().get(senderRoleId) != null;
		int refuseStrangerMsg = groups.getRefusestrangermsg();

		// 不接陌生人消息
		if (!isFriend && refuseStrangerMsg > 0) {
			fire.pb.talk.MessageMgr.sendMsgNotify(senderRoleId, 140203, null);
			return false;
		}

		SFriendMessageToRole msg = new SFriendMessageToRole();
		if (manager.isOnline(receiverRoleId)) {

			if (isFriend) {
				PropRole role = new PropRole(senderRoleId, true);

				msg.roleid = senderRoleId;
				msg.content = content;
				msg.rolelevel = (short) role.getLevel();
				for (DisplayInfo info : displayInfo) {
					msg.details.add(ChatChannel.getInstance().getOctets(senderRoleId, info));
				}
				msg.displayinfo = displayInfo;

				// 验证不通过
				if (msg.details == null || msg.details.size() != msg.displayinfo.size()) {
					return false;
				}
				mkdb.Procedure.psendWhileCommit(receiverRoleId, msg);

			} else {
				SStrangerMessageToRole strangerMsg = new SStrangerMessageToRole();
				StrangerMessageBean messageBean = new StrangerMessageBean();
				messageBean.friendinfobean = FriendHelper.toInfoBean(receiverRoleId, senderRoleId);
				messageBean.content = content;
				for (DisplayInfo info : displayInfo) {
					messageBean.details.add(ChatChannel.getInstance().getOctets(senderRoleId, info));
				}
				messageBean.displayinfo = displayInfo;
				// 验证不通过
				if (messageBean.details == null || messageBean.details.size() != messageBean.displayinfo.size()) {
					return false;
				}
				strangerMsg.strangermessage = messageBean;
				mkdb.Procedure.psendWhileCommit(receiverRoleId, strangerMsg);
			}
		} else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = format.format(System.currentTimeMillis());
			xbean.OfflineMsg offlineMsg = xbean.Pod.newOfflineMsg();
			offlineMsg.setContent(content);
			offlineMsg.setRoleid(senderRoleId);
			offlineMsg.setSendtime(currentTime);
			for (DisplayInfo info : displayInfo) {
				offlineMsg.getDetails().add(ChatChannel.getInstance().getOctets(senderRoleId, info).array());
				xbean.ShowInfoBean showInfoBean = xbean.Pod.newShowInfoBean();
				showInfoBean.setCounterid(info.counterid);
				showInfoBean.setRoleid(info.roleid);
				showInfoBean.setShopid(info.shopid);
				showInfoBean.setShowtype(info.displaytype);
				showInfoBean.setThisid(info.uniqid);
				offlineMsg.getShowinfos().add(showInfoBean);
			}
			groups.getOfflinemsglist().add(0, offlineMsg);
			if (groups.getOfflinemsglist().size() > 20) {
				groups.getOfflinemsglist().remove(20);
			}
		}

		// sender 看不到 tips
		mkdb.Procedure.psendWhileCommit(senderRoleId, msg);

		return true;
	}
}
