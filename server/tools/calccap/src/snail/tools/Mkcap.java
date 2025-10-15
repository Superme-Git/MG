package snail.tools;

import mkdb.util.DatabaseMetaData;
import java.util.*;

public class Mkcap extends Tool {

	@Override
	public void build(Options options) {
		String[][] opts = {
				{ "-mkdb", "mkdb.xml", "mkdb conf", "!" },
				{ "-mkbean", "classpath", "mkbeans classpath.", "!" },
				{ "-o", "name", "order by", "cache%" },
				{ null, "", "names: table,cache,capacity,page", null },
				{ null, "", "names%: table%,cache%,...", null },
				{ null, "", "use empty-name to order by table-name", null },
				{ "-n", "number", "limit number", "0" },
		};
		options.add(opts);
	}

	static class Item {
		long key = 0;
		long value = 0;
		long table = 0;
		long cache = 0;
		long capacity = 0;
		long page = 0;
	}

	static class Percent {
		double key = 0;
		double value = 0;
		double table = 0;
		double cache = 0;
		double capacity = 0;
		double page = 0;
	}

	static class TableSize {
		private final DatabaseMetaData.Table table;

		Item columns = new Item();
		Percent percent = new Percent();

		TableSize(DatabaseMetaData.Table table, mkdb.TableConf tconf, Item total) {
			this.table = table;

			// size
			columns.key = table.getKeyType().sizeof();
			columns.value = table.getValueType().sizeof();
			columns.table = columns.key + columns.value;
			columns.capacity = tconf.getCacheCapacity();
			columns.cache = columns.capacity * columns.table;
			columns.page = tconf.getCacheHigh();

			// total
			total.key += columns.key;
			total.value += columns.value;
			total.table += columns.table;
			total.capacity += columns.capacity;
			total.cache += columns.cache;
			total.page += columns.page;
		}

		void percent(Item total) {
			percent.key = (double)columns.key / total.key;
			percent.value = (double)columns.value / total.value;
			percent.table = (double)(columns.table) / total.table;
			percent.capacity = (double)columns.capacity / total.capacity;
			percent.cache = (double)(columns.cache) / total.cache;
			percent.page = (double)(columns.page) / total.page;
		}

		public static void formatHeaders(List<String> cols) {
			cols.add("name");
			cols.add("table");
			cols.add("table%");
			cols.add("cache");
			cols.add("cache%");
			cols.add("capacity");
			cols.add("capacity%");
			cols.add("page");
			cols.add("page%");
		}

		public void formatColumns(List<String> cols) {
			cols.add(table.getNameEx());
			cols.add(String.valueOf(columns.table));
			cols.add(String.format("%.4f", percent.table));
			cols.add(String.valueOf(columns.cache));
			cols.add(String.format("%.4f", percent.cache));
			cols.add(String.valueOf(columns.capacity));
			cols.add(String.format("%.4f", percent.capacity));
			cols.add(String.valueOf(columns.page));
			cols.add(String.format("%.4f", percent.page));
		}
	}

	static class Comp implements Comparator<TableSize> {
		private final String name;
		private final boolean percent;
		private Class<?> pclass = Percent.class;
		private Class<?> iclass = Item.class;

		public Comp(String name) {
			this.percent = name.endsWith("%");
			this.name = this.percent ? name.substring(0, name.length() - 1) : name;
		}

		@Override
		public int compare(TableSize o1, TableSize o2) {
			try {
				if (name.isEmpty())
					return o1.table.getNameEx().compareTo(o2.table.getNameEx());

				if (percent) {
					double v1 = (Double)pclass.getDeclaredField(name).get(o1.percent);
					double v2 = (Double)pclass.getDeclaredField(name).get(o2.percent);
					return Long.signum((long)(v2 * 10000 - v1 * 10000));
				}
				long v1 = (Long)iclass.getDeclaredField(name).get(o1.columns);
				long v2 = (Long)iclass.getDeclaredField(name).get(o2.columns);
				return Long.signum(v2 - v1);
			} catch (Exception x) {
				throw new RuntimeException(x);
			}
		}
		@Override
		public boolean equals(Object obj) {
			return this == obj;
		}
	}
	@Override
	public void run(Options options) throws Exception {
		Classpath.addFile(options.getOption("-mkbean").getValue());
		mkdb.MkdbConf mkconf = new mkdb.MkdbConf(options.getOption("-mkdb").getValue());

		// compute size and sum total
		TableSize [] tables = new TableSize[DatabaseMetaData.getInstance().getTables().size()];
		Item total = new Item();
		{
			int i = 0;
			for (DatabaseMetaData.Table table : DatabaseMetaData.getInstance().getTables())
				tables[i++] = new TableSize(table, mkconf.getTableConf(table.getName()), total);
			for (TableSize table : tables)
				table.percent(total);
	
			Arrays.sort(tables, new Comp(options.getOption("-o").getValue()));
		}

		// format header
		List<String> headers = new ArrayList<String>();
		TableSize.formatHeaders(headers);
		List<Integer> length = new ArrayList<Integer>();
		for (String header : headers)
			length.add(header.length());

		// format and record max-column-length
		List<List<String>> rows = new ArrayList<List<String>>();
		rows.add(headers);
		for (TableSize table : tables) {
			List<String> cols = new ArrayList<String>();
			table.formatColumns(cols);
			for (int i = 0; i < cols.size(); ++i) {
				if (cols.get(i).length() > length.get(i))
					length.set(i, cols.get(i).length());
			}
			rows.add(cols);
		}

		// limit number
		int number = Integer.valueOf(options.getOption("-n").getValue());
		if (number <= 0)
			number = Integer.MAX_VALUE;
		else
			number ++; // ++ count for header

		// print tables
		for (int n = 0; n < rows.size() && n < number; ++n) {
			List<String> cols = rows.get(n);
			for (int i = 0; i < cols.size(); ++i) {
				String col = cols.get(i);
				System.out.print(col);
				System.out.print(Tool.spaceN(length.get(i) + 4, col));
			}
			System.out.println();
		}
		if (number < rows.size())
			System.out.println("...");

		// print total
		System.out.println("-------------------------------------------------------------------");
		System.out.println("TOTAL");
		System.out.println(String.format(
				"	cache: %dM page: %dM table<key, value>: %dK<%d, %dK>"
				, total.cache / 1024 / 1024, total.page * 4 / 1024
				, total.table / 1024, total.key, total.value / 1024));
		System.out.println("	TABLE-COUNT: " + rows.size());
		System.out.println("-------------------------------------------------------------------");
	}
}
