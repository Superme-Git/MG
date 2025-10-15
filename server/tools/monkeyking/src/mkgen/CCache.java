package mkgen;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CCache {
	private String name;
	private String key;
	private List<CTable> ctables = new ArrayList<CTable>();
	private Set<String> ctableNames = new HashSet<String>();

	//////////////////////////////
	// compiled
	private Mkdb mkdb;
	private Type keytype;

	public String getName() {
		return name;
	}

	public Mkdb getMkdb() {
		return mkdb;
	}

	void compile(Mkdb mkdb) {
		this.mkdb = mkdb;

		for (CTable ctable : ctables)
			ctable.compile(this);

		if (ctables.isEmpty())
			throw new IllegalStateException("ccache reference none table. ccache=" + name);
		this.keytype = ctables.get(0).getTable().getKeyType();
		for (CTable ctable : ctables) {
			if (false == ctable.getTable().getKeyType().getName().equals(this.key))
				throw new IllegalStateException("ccache : different table-keytype. ccache='"
						+ name + "' ctable='" + ctable.getName());
		}
	}


	void make() {
		PrintStream ps = Main.fopen(mkdb.getXcachedir(), Main.toUpper1(name));
		ps.println("package xcache;");
		ps.println("");
		ps.println("// �û�ָ����cache������Ϊ�����������֡�cache�ķ�����ڡ�");
		ps.println("public class " + Main.toUpper1(name) + " {");
		ps.println("	////////////////////////////////////////////////////////////////");
		ps.println("	// define cached valuetype");
		ps.println("	public static class Valuetype {");

		ps.println("		// declare valuetype");
		for (CTable ctable : ctables) {
			ctable.makeDeclareValuetype(ps);
		}

		ps.println("		// define valuetype");
		for (CTable ctable : ctables) {
			ps.println("		" + ctable.getCValuetypeName() + " " + ctable.getName() + ";");
		}
		ps.println("");

		for (CTable ctable : ctables) {
			ctable.makeGetValuetype(ps);
		}
		ps.println("		Valuetype shallowcopy() {");
		ps.println("			Valuetype sc = new Valuetype();");
		for (CTable ctable : ctables) {
			ps.println("			sc." + ctable.getName() + " = this." + ctable.getName() + ";");
		}
		ps.println("			return sc;");
		ps.println("		}");
		ps.println("	}");
		ps.println("");
		ps.println("	///////////////////////////////////////////////////////////////");
		ps.println("	// cache define");
		ps.println("	// ��㹫���Ľӿڣ��õ�����Cache�����ǻ���ġ�����ֻ����get��existInCache(���Ǳ�Ҫ��)��");
		ps.println("	public static mkdb.util.ConcurrentCache<"
							+ keytype.getBoxingName() + ", Valuetype> getCache() {");
		ps.println("		return CacheImpl.instance;");
		ps.println("	}");
		ps.println("");
		ps.println("	public static Valuetype get(" + keytype.getBoxingName() + " key) {");
		ps.println("		return CacheImpl.instance.get(key);");
		ps.println("	}");
		ps.println("");
		ps.println("	public static class CacheImpl extends mkdb.util.ConcurrentCache<"
							+ keytype.getBoxingName() + ", Valuetype> {");
		ps.println("		CacheImpl() {");
		ps.println("			super(\"" + name + "\");");
		ps.println("		}");
		ps.println("");
		ps.println("		// ��ʱ�Ȳ���������Ҫ��ʱ���ڻ�����ӽӿڲ�����������롣");
		ps.println("		// @Override");
		ps.println("		mkdb.Lockey[] realGetLocks(" + keytype.getBoxingName() + " key) {");
		ps.println("			return new mkdb.Lockey[]{");
		for (CTable ctable : ctables) {
			ps.println("					mkdb.Lockeys.get(xtable."
					+ Main.toUpper1(ctable.getName()) + ".getTable(), key),");
		}
		ps.println("			};");
		ps.println("		}");
		ps.println("");
		ps.println("		private static class RealGet extends mkdb.Procedure {");
		ps.println("			private mkdb.Lockey[] locks;");
		ps.println("			private " + keytype.getBoxingName() + " key;");
		ps.println("			private Valuetype value = new Valuetype();");
		ps.println("			");
		ps.println("			@Override");
		ps.println("			protected boolean process() throws Exception {");
		ps.println("				// �ȰѼ�¼���������ϣ������������ܡ�");
		ps.println("				mkdb.Lockeys.lock(locks);");
		ps.println("");
		for (CTable ctable : ctables) {
			ctable.makeRealGet(ps, "				");
		}
		ps.println("				return true;");
		ps.println("			}");
		ps.println("");
		ps.println("			Valuetype get(mkdb.Lockey[] locks, " + keytype.getBoxingName() + " key) {");
		ps.println("				this.locks = locks;");
		ps.println("				this.key = key;");
		ps.println("				if (false == this.call())");
		ps.println("					throw new RuntimeException(\"readGet fail.\", this.getException());");
		ps.println("				return this.value;");
		ps.println("			}");
		ps.println("		}");
		ps.println("");
		ps.println("		@Override");
		ps.println("		protected Valuetype realGet(" + keytype.getBoxingName() + " key) {");
		ps.println("			return new RealGet().get(realGetLocks(key), key);");
		ps.println("		}");
		ps.println("");
		ps.println("		/////////////////////////////////////////////////////////////////////");
		ps.println("		// update helper");
		for (CTable ctable : ctables) {
			ctable.makeUpdateHelper(ps);
		}
		ps.println("		static CacheImpl instance = new CacheImpl();");
		ps.println("	}");
		ps.println("");
		ps.println("	////////////////////////////////////////////////////////////////////////////////");
		ps.println("	// deepcopy helper");
		for (CTable ctable : ctables) {
			ctable.makeDeepcopyHelper(ps);
		}
		ps.println("	/////////////////////////////////////////////////////////////////////////////////");
		ps.println("	// ע�� Listener��");
		ps.println("	static void registryTableListener() {");
		for (CTable ctable : ctables) {
			ctable.makeRegistryTableListener(ps);
		}
		ps.println("	}");
		ps.println("");
		ps.println("	static void unregistryTableListener() {");
		for (CTable ctable : ctables) {
			ctable.makeUnregistryTableListener(ps);
		}
		ps.println("	}");
		ps.println("");
		for (CTable ctable : ctables) {
			ctable.makeTableListener(ps);
		}
		ps.println("}");
		ps.close();
	}

	static void make_ConcurrentCaches_(Mkdb mkdb, List<CCache> ccaches) {
		PrintStream ps = Main.fopen(mkdb.getXcachedir(), "_ConcurrentCaches_");

		ps.println("package xcache;");
		ps.println("");
		ps.println("public class _ConcurrentCaches_ extends mkdb.util.ConcurrentCaches {");
		ps.println("	public _ConcurrentCaches_() {");
		ps.println("	}");
		ps.println("");
		ps.println("	@Override");
		ps.println("	protected void onStart() {");
		for (CCache ccache : ccaches) {
			ps.println("		super.add(" + Main.toUpper1(ccache.getName()) + ".getCache());");
		}
		ps.println("");
		for (CCache ccache : ccaches) {
			ps.println("		" + Main.toUpper1(ccache.getName()) + ".registryTableListener();");
		}
		ps.println("	}");
		ps.println("");
		ps.println("	@Override");
		ps.println("	protected void onStop() {");
		for (CCache ccache : ccaches) {
			ps.println("		" + Main.toUpper1(ccache.getName()) + ".unregistryTableListener();");
		}
		ps.println("	}");
		ps.println("");
		ps.println("}");
		ps.println("");

		ps.close();
	}

	void add(CTable c) {
		if (false == ctableNames.add(c.getName()))
			throw new IllegalArgumentException("duplicate ctable: '" + c.getName() + "'@'" + name + "'");
		ctables.add(c);
	}

	public CCache(Element self) {
		name = self.getAttribute("name").trim().toLowerCase();
		Main.verifyName(name);
		key = self.getAttribute("key");

		NodeList childnodes = self.getChildNodes();
		for (int i = 0; i < childnodes.getLength(); ++i) {
			Node node = childnodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element e = (Element) node;
			String nodename = e.getNodeName();

			if (nodename.equals("ctable"))
				add(new CTable(e));
			else
				throw new RuntimeException("node=" + nodename);
		}
	}
}
