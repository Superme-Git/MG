package fire.pb.gm;

import fire.pb.map.GridPos;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.scene.BlockInfo;
import fire.pb.util.MapUtil;

public class GM_block extends GMCommand {

	@Override
	boolean exec(String[] args) {
		Role role = RoleManager.getInstance().getRoleByID(getGmroleid());
		if (null == role)
			return false;
		short blocktype = BlockInfo.WALK_BLOCK;

		if (args.length >= 1) {
			int b = Integer.valueOf(args[0]);
			if (b >= 16) {
				sendToGM(usage());
				return false;
			}
			for (int i = 1; i < b; i++) {
				blocktype <<= 1;
			}
		}

		GridPos gpos = role.getPos().toGridPos();
		if (args.length >= 3) {
			gpos.setX(Integer.valueOf(args[1]));
			gpos.setY(Integer.valueOf(args[2]));
		}

		String msg = "位置" + gpos;

		BlockInfo blockinfo = MapUtil.getBlockInfo(role.getMapId());
		boolean isblock = blockinfo.checkBlock(gpos.getX(), gpos.getY(),
				blocktype);
		msg += isblock ? "是0x" + Integer.toHexString(blocktype) + "阻挡点"
				: "不是0x" + Integer.toHexString(blocktype) + "阻挡点";
		sendToGM(msg);
		return true;
	}

	@Override
	String usage() {
		return "//block [blocktype: 1-15(没有默认为1)] [GridPos.X] [GridPos.Y] （坐标没有默认为现在坐标）";
	}

}
