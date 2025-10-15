package fire.pb.gm;

public class GM_grade extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleid = getGmroleid();
		int totalScore = fire.pb.item.Module.getInstance().getEquipTotalScore(
				roleid);
		fire.pb.talk.MessageMgr.sendMsgToPop(roleid, "小哥， 你的装备总评分是 ："
				+ totalScore + " 分！！");
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
