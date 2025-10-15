package fire.pb.pet;


public class PetChat {

	/**
	 * 向战斗中所有战斗单元发送宠物闲话
	 */
	public static void randomPetChatAndSend(final xbean.BattleInfo battle,
			int index, boolean self) {
		int randomChat = -1;
		if (self) {
			randomChat = fire.pb.util.Misc.getRandomBetween(
					PetChatLimit.Self20percentBegin,
					PetChatLimit.Self20percentEnd);
		} else {
			randomChat = fire.pb.util.Misc.getRandomBetween(
					PetChatLimit.Master20percentBegin,
					PetChatLimit.Master20percentEnd);
		}

		final SPetGossip send = new SPetGossip(index, randomChat);
		gnet.link.Onlines.getInstance().send(battle.getRoleids().keySet(), send);
		if (Module.logger.isDebugEnabled()) {
			Module.logger.debug("Battleindex:" + index + " GossipId:" + randomChat);
		}
	}
}
