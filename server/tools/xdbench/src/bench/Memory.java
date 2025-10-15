package bench;

public class Memory {

	static class AddMuch extends xdb.Procedure {
		private int count;
		private boolean commit;

		AddMuch(int count, boolean commit) {
			this.count = count;
			this.commit = commit;
		}

		@Override
		protected boolean process() throws Exception {
			byte [] stub = new byte[64];
			for (int i = 0; i < stub.length; ++i)
				stub[i] = (byte)i;
			xdb.Trace.info("stub length=" + java.util.Arrays.toString(stub).length());
			xdb.Trace.info("AddMuch start " + count);
			for (int i = 0; i < count; ++i) {
				xbean.Bag bag = xbean.Pod.newBag();
				for (int j = 0; j < 200; ++j) {
					xbean.BagItem bi = xbean.Pod.newBagItem();
					bag.getItems().put(j, bi);
					bi.getItem().setExtra(java.util.Arrays.toString(stub));
				}
				xtable.Bag.add(i, bag);
			}
			for (int i = 0; i < count; ++i) {
				xtable.Basic.add(i, xbean.Pod.newBasic());
			}
			for (int i = 0; i < count; ++i) {
				xbean.Bag bag = xbean.Pod.newBag();
				for (int j = 0; j < 200; ++j) {
					xbean.BagItem bi = xbean.Pod.newBagItem();
					bag.getItems().put(j, bi);
					bi.getItem().setExtra(java.util.Arrays.toString(stub));
				}
				xtable.Depot.add(i, bag);
			}
			for (int i = 0; i < count; ++i) {
				xbean.Bag bag = xbean.Pod.newBag();
				for (int j = 0; j < 20; ++j) {
					xbean.BagItem bi = xbean.Pod.newBagItem();
					bag.getItems().put(j, bi);
					bi.getItem().setExtra(java.util.Arrays.toString(stub));
				}
				xtable.Equip.add(i, bag);
			}
			for (int i = 0; i < count; ++i) {
				xtable.Family.add(Long.valueOf(i), xbean.Pod.newFamily());
			}
			for (int i = 0; i < count; ++i) {
				xbean.Friends friends = xbean.Pod.newFriends();
				for (int j = 0; j < 200; ++j) {
					friends.getFriendmap().put(j, xbean.Pod.newFriendInfo());
					friends.getForbiddenmap().put(j, java.util.Arrays.toString(stub));
				}
				xtable.Friends.add(i, friends);
			}
			for (int i = 0; i < count; ++i) {
				xbean.Bag bag = xbean.Pod.newBag();
				for (int j = 0; j < 100; ++j) {
					xbean.BagItem bi = xbean.Pod.newBagItem();
					bag.getItems().put(j, bi);
					bi.getItem().setExtra(java.util.Arrays.toString(stub));
				}
				xtable.Pets.add(i, bag);
			}
			for (int i = 0; i < count; ++i) {
				xtable.Produce.add(i, xbean.Pod.newProduce());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Properties.add(i, xbean.Pod.newProperties());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Skills.add(i, xbean.Pod.newSkills());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Spouse.add(i, xbean.Pod.newSpouse());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Swornbrother.add(i, xbean.Pod.newSwornBrother());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Task.add(i, xbean.Pod.newPlayerTaskData());
			}
			for (int i = 0; i < count; ++i) {
				xtable.Uniqname.add(String.valueOf(i), count);
			}
			for (int i = 0; i < count; ++i) {
				xtable.User.add(i, xbean.Pod.newIdlist());
			}
			xdb.Trace.fatal("AddMuch end [press enter to continue ...]");
			System.console().readLine();
			return commit;
		}
	}

	public static void main(String args[]) throws Exception {
		int count = Integer.parseInt(args[0]);
		boolean commit = true;

		xdb.XdbConf xdbConf = new xdb.XdbConf("../xdbench/xdb.xml");
		xdb.Xdb.getInstance().setConf(xdbConf);
		xdb.Xdb.getInstance().start();

		if (commit) xdbConf.setAutoMarshal(false);
		new AddMuch(count, commit).submit().get();
		if (commit) xdbConf.setAutoMarshal(true);
		xdb.Trace.fatal("After AddMuch commit=" + commit + " [press enter to exit ...]");
		System.console().readLine();
	}
}
