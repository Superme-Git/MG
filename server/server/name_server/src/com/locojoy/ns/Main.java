package com.locojoy.ns;

import java.util.Iterator;
import java.util.Date;

public class Main {
	static void usage() {
		System.out.println("Usage: java -jar ns.jar [options]");
		System.out.println("    -Mkdb Mkdb.xml");
		System.out.println("    -trace debug|info|warn|error|fatal");
		System.out.println("    -groups          dump all groups info.");
		System.out.println("    -freeid          dump freeid if it is id-group. use with -groups");
		System.exit(0);
	}

	private static String ARGS(String args[], int i) {
		if (i < args.length) return args[i];
		usage();
		return null; // make compiler happy
	}

	public static void main(String args[]) throws Exception {
		String trace = null;
		String Mkdbxml = null;
		boolean groups = false;
		boolean freeid = false;

		for (int i = 0; i < args.length; ++i) {
			if      (args[i].equals("-trace"))  trace = ARGS(args, ++i);
			else if (args[i].equals("-Mkdb"))    Mkdbxml = ARGS(args, ++i);
			else if (args[i].equals("-groups")) groups = true;
			else if (args[i].equals("-freeid")) freeid = true;
			else if (null == Mkdbxml)
				Mkdbxml = args[i]; // 兼容
			else usage();
		}

		if (null == Mkdbxml)
			usage();

		mkdb.Mkdb db = mkdb.Mkdb.getInstance();
		mkdb.MkdbConf MkdbConf = new mkdb.MkdbConf(Mkdbxml);
		db.setConf(MkdbConf);
		if (null != trace) // set trace if present
			mkdb.Trace.set(mkdb.Trace.valueOf(trace.toUpperCase()));

		AllocateId.initialize(MkdbConf);

		if (groups) {
			// dump group info.
			db.start();
			dumpGroups(freeid);

		} else {
			// start uniqname service
			db.start();
			db.startNetwork();
			while (true) {
				Thread.sleep(1000000);
			}
		}
	}

	static class DumpAllocId extends mkdb.Procedure {
		private final mkdb.TTable<Long, xbean.IdState> ids;

		DumpAllocId(mkdb.TTable<Long, xbean.IdState> ids) {
			this.ids = ids;
		}

		@Override
		protected boolean process() throws Exception {
			synchronized(ids) { // 表锁。 see AllocateId ReleaseId
				xbean.IdState seed = ids.get(AllocateId.NEXT_SEED_KEY);
				System.out.println("    seed: next=" + seed.getNextid()
						+ ", (localid=" + seed.getLocalid()
						+ ", time=" + mkdb.Trace.dateFormat.format(new Date(seed.getTime()))
						+ ", peer=" + seed.getPeerip()
						+ ")");

				int nLine = 0;
				for (Iterator<Long> it = new AllocateId.FreeList(ids).iterator(); it.hasNext(); ) {
					if (nLine == 0)
						System.out.print("    free: ");
					System.out.print(it.next());
					System.out.print(" ");
					++nLine;
					if (nLine == 8) {
						nLine = 0;
						System.out.println();
					}
				}
				if (nLine > 0)
					System.out.println();
			}
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private static void dumpGroups(boolean freeid) {
		for (mkdb.Table table : mkdb.Mkdb.getInstance().getTables().getTables()) {
			if (table instanceof mkdb.TTable) {
				mkdb.TTable<Object, Object> ttable = (mkdb.TTable<Object, Object>)table;
				// 创建一个值的实例，用来判断是那种类型的名字空间。 
				Object valueInstanceForCheckGroup = ttable.newValue();
				if (valueInstanceForCheckGroup instanceof xbean.IdState) {
					System.out.println(ttable.getName() + "<Id>");
					if (freeid) {
						new DumpAllocId((mkdb.TTable<Long, xbean.IdState>)table).call();
					}

				} else if (valueInstanceForCheckGroup instanceof xbean.NameState) {
					System.out.println(ttable.getName() + "<Name>");

				} else {
					System.out.println(ttable.getName() + "<Unknown>");
				}
			}
			else
				System.out.println(table.getName() + "<Error>");
		}
	}
}
