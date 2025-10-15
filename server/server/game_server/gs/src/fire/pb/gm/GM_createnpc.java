package fire.pb.gm;

import fire.pb.main.ConfigManager;
import fire.pb.map.GridPos;
import fire.pb.map.Position;
import fire.pb.map.SceneNpcManager;
import fire.pb.npc.SNpcShare;

public class GM_createnpc extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance()
				.getRoleByID(getGmroleid());

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		Integer npcid = null;

		try {
			npcid = Integer.parseInt(args[0]);
		} catch (java.lang.NumberFormatException e) {
			npcid = getObjectIdByName(args[0]);
		}
		if (null == npcid)
			return false;
		SNpcShare npcshare = ConfigManager.getInstance()
				.getConf(SNpcShare.class).get(npcid);
		if (npcshare == null) {
			sendToGM("NPC " + args[0] + " 配置不存在");
			return false;
		}
		String npcName = npcshare.getName();

		if (args.length > 1) {
			int mapid = Integer.valueOf(args[1]);
			long ownerid = Long.valueOf(args[2]);
			int gposx = Integer.valueOf(args[3]);
			int gposy = Integer.valueOf(args[4]);
			Position pos = new GridPos(gposx, gposy).toPosition();
			SceneNpcManager.createNpcByPos(SceneNpcManager.getInstance()
					.getNextId(), npcid, npcName, mapid, ownerid, pos.getX(),
					pos.getY(), 0, 0);
		} else
			createNPC(npcid, npcName, role);

		return true;
	}

	private void createNPC(int npcid, String npcName, fire.pb.map.Role role) {

		SceneNpcManager.createNpcByGridScale(SceneNpcManager.getInstance()
				.getNextId(), npcid, npcName, role.getScene(), 4, role.getPos()
				.getX(), role.getPos().getY(), 16, 0, 1);
	}

	@Override
	String usage() {
		return "//createnpc [npcId] [mapid] [ownerid] [posx] [posy]";
	}

}
