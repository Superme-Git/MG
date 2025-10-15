package fire.pb.gm;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.GsClient;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

public class GM_gotorole extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final String name = args[0];
		final Long roleid = xtable.Rolename2key.select(name);

		mkio.Protocol p = new mkio.Protocol() {
			@Override
			public void process() {
				if (null == roleid) {
					sendToGM("没有找到名为 " + name + " 的玩家");

				} else {
					Role findrole = RoleManager.getInstance().getRoleByID(
							roleid);
					sendToGM("玩家 " + name + " 在地图："
							+ findrole.getScene().getMapID() + "  ; 坐标为： "
							+ findrole.getPos().toGridPos().getX() + "  "
							+ findrole.getPos().toGridPos().getY());
					Transfer.justGoto(getGmroleid(), findrole.getScene()
							.getMapID(), findrole.getPos().toGridPos().getX(),
							findrole.getPos().toGridPos().getY(),
							SRoleEnterScene.QUEST);
				}
			}

			@Override
			public OctetsStream marshal(OctetsStream arg0) {
				return null;
			}

			@Override
			public OctetsStream unmarshal(OctetsStream arg0)
					throws MarshalException {
				return null;
			}

			@Override
			public int getType() {
				return 0;
			}
		};
		GsClient.sendToScene(p);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
