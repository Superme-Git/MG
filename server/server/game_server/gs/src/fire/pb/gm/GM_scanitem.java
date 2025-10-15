package fire.pb.gm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xbean.Properties;
import mkdb.TTable.IWalk;
import fire.pb.item.Depot;
import fire.pb.item.Pack;

public class GM_scanitem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		int itemid = -1;
		if (args[0].matches("\\d+")) {
			itemid = Integer.parseInt(args[0]);
		} else {
			itemid = getObjectIdByName(args[0]);
		}

		final int num = (args.length >= 2) ? Integer.valueOf(args[1]) : 1;
		final int bindflag = (args.length >= 3) ? Integer.valueOf(args[2]) : 0;

		new ScanItem(itemid, num, bindflag).submit();

		return true;
	}

	@Override
	String usage() {
		return "//scanitem [item(id or name)] [num:大于等于数量(default:1)] [all:0,bind:1,unbind:2(defalut:all)] ";
	}

	public static class ScanItem extends mkdb.Procedure {
		int itemid;
		int num;
		int bindflag;

		public ScanItem(int itemid, int num, int bindflag) {
			super();
			this.itemid = itemid;
			this.num = num;
			this.bindflag = bindflag;
		}

		@Override
		protected boolean process() throws Exception {
			final Map<Long, Role> roles = new HashMap<Long, GM_scanitem.Role>();

			mkdb.TTableCache<Long, xbean.Properties> btable = xtable.Properties
					.getCache();
			btable.getTable().browse(new IWalk<Long, xbean.Properties>() {

				@Override
				public boolean onRecord(Long roleId, Properties prop) {
					roles.put(
							roleId,
							new Role(roleId, prop.getUserid(), prop
									.getRolename(), prop.getLevel()));
					return false;
				}

			});
			System.out.println(getClass() + ",Rolenum=" + roles.size());
			final List<Role> selectedroles = new LinkedList<GM_scanitem.Role>();

			for (Role role : roles.values()) {
				Pack bag = new Pack(role.roleId, false);
				Depot depot = new Depot(role.roleId, false);
				role.itemId = itemid;
				role.allnum = bag.getItemNum(itemid, 0)
						+ depot.getItemNum(itemid, 0);
				role.unbindnum = bag.getItemNum(itemid, fire.pb.Item.BIND)
						+ depot.getItemNum(itemid, fire.pb.Item.BIND);
				role.bindnum = role.allnum - role.unbindnum;
				if (bindflag == 0) {
					if (role.allnum >= num)
						selectedroles.add(role);
				} else if (bindflag == 1) {
					if (role.bindnum >= num)
						selectedroles.add(role);
				} else if (bindflag == 2) {
					if (role.unbindnum >= num)
						selectedroles.add(role);
				}
			}
			System.out.println(getClass() + ",SelectedRolenum=" + roles.size());
			printfile(selectedroles);
			return true;
		}

		private void printfile(List<Role> selectedroles) throws IOException {
			FileWriter writer = null;
			try {
				File file = new File("scanitem.log");
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				writer = new FileWriter(file);

				for (Role role : selectedroles) {
					System.out.println(role.toString());
					writer.write(role.toString());
					writer.flush();
				}
			} finally {
				if (writer != null)
					writer.close();
			}
		}

	}

	public static class Role {
		long roleId;
		int userId;
		String rolename;
		int level;

		public Role(long roleId, int userId, String rolename, int level) {
			this.roleId = roleId;
			this.userId = userId;
			this.rolename = rolename;
			this.level = level;
		}

		int itemId;
		int allnum;
		int unbindnum;
		int bindnum;

		@Override
		public String toString() {
			return "userid=" + userId + ":roleId=" + roleId + ":rolename="
					+ rolename + ":level=" + level + ":itemId=" + itemId
					+ ":allnum=" + allnum + ":unbindnum=" + unbindnum
					+ ":bindnum=" + bindnum + "\n";
		}

	}

}
