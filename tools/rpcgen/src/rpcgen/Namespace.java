package rpcgen;

import java.util.*;
import java.io.*;

public class Namespace {
	private String name;
	private Map<String, Name> objs = new HashMap<String, Name>();

	private Namespace parent;
	private Map<String, Namespace> childs = new HashMap<String, Namespace>();

	private static Namespace root = new Namespace();

	public static Namespace root() {
		return root;
	}

//	public void clearAll() {
//		for (Namespace child : childs.values())
//			child.clearAll();
//		childs.clear();
//		objs.clear();
//	}

	public Namespace child(String name) {
		Namespace ns = childs.get(name);
		if (null == ns)
			childs.put(name, ns = new Namespace(this, name));
		return ns;
	}

	public Namespace parent() {
		return parent;
	}

	public void compile() {
		for (Name name : objs.values())
			name.compile();
		for (Namespace child : childs.values())
			child.compile();
	}

	private Namespace() {
	}

	private Namespace(Namespace parent, String name) {
		if (name.isEmpty())
			throw new IllegalArgumentException("name MUST not be empty!");

		this.name = name;
		this.parent = parent;
	}

	public void add(Name n) {
		if (null != objs.put(n.getName(), n))
			throw new RuntimeException("Duplicate Name. " + this + "." + n.getName() + "@" + n.getClass().getName());
		n.setSpace(this);
	}

	private StringBuilder join(String sep, StringBuilder fullName) {
		if (null != name) {
			if (fullName.length() > 0)
				fullName.insert(0, sep);
			fullName.insert(0, name);
			if (null != parent)
				parent.join(sep, fullName);
		}
		return fullName;
	}

	public String getName() {
		return name;
	}

	public static Name search(Namespace current, String name) {
		String [] names = name.split("\\.");
		if (names.length > 1) {
			// fullName
			Namespace space = root();
			for (int i = 0; i < names.length - 1; ++i) {
				space = space.childs.get(names[i]);
				if (null == space)
					return null;
			}
			return space.objs.get(names[names.length - 1]);
		}
		if (null != current)
			return current.objs.get(name);
		return null;
	}

	public String join(String sep) {
		return join(sep, new StringBuilder()).toString();
	}

	public void dump() {
//		for (Name name : objs.values())
//			System.out.println(name.fullName());
		for (Namespace child : childs.values())
			child.dump();
	}

	public static void main(String args[]) {
		Namespace root = root();
		root.add(new Naming("NameInRoot"));
		Namespace gnet = root.child("gnet");
		gnet.add(new Naming("NameInGnet"));
		Namespace link = gnet.child("link");
		link.add(new Naming("NameInLink"));
		System.out.println("findObject=" + search(null, "gnet.NameInGnet"));
		root.dump();
		System.out.println(root.path());
		System.out.println(gnet.path());
		System.out.println(link.path());
	}

	public List<Namespace> path() {
		List<Namespace> path = new ArrayList<Namespace>();
		for (Namespace p = this; p != null && p != root; p = p.parent()) {
			path.add(0, p);
		}
		return path;
	}

//	public List<Namespace> pathReversed() {
//		List<Namespace> path = path();
//		java.util.Collections.reverse(path);
//		return path;
//	}

	public File path(File base) {
		for (Namespace nm : path())
			base = new File(base, nm.getName());
		base.mkdirs();
		for (int i = 10; !base.exists(); --i) {
			if (i > 0) {
				System.out.println("rpcgen: mkdirs() failed, and will try again after 1 sec");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				base.mkdirs();
			}
			else
				throw new RuntimeException("can not create dirs: " + base.getName());			
		}
		return base;
	}

	public boolean fparse(File base, String name, LineParser parser) {
		try {
			File file = new File(path(base), name);
			if (!file.exists())
				return false;
			LineNumberReader lnr = new LineNumberReader(
					new InputStreamReader(new FileInputStream(file), Main.inputEncoding));
			String line = null;
			while ( (line = lnr.readLine()) != null)
				parser.parseLine(line);
			lnr.close();
			return true;
		} catch (java.io.IOException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream fopen(File base, String name, boolean overwrite) {
		try {
			File file = new File(path(base), name);
			boolean exists = file.exists();
			if (overwrite || !exists) {
				Main.debug("file " + (exists ? "overwrite" : "new") + " '" + file + "'");
				return new PrintStream(new CachedFileOutputStream(file), false, Main.outputEncoding);
			}
			Main.debug("file skip '" + file + "'");
			return null;
		} catch (java.io.IOException e) {
			throw new RuntimeException(e);
		}
	}

	public PrintStream fopen(String name, boolean overwrite) {
		return fopen(new File("."), name, overwrite);
	}

	public String toString() {
		return join(".");
	}

	/////////////////////////////////////
	// cxx only
	public void begin(PrintStream ps) {
		for (Namespace ns : path())
			ps.println("namespace " + ns.getName() + " { ");
		//ps.println("");
	}

	public void end(PrintStream ps) {
		for (int i = 0; i < path().size(); ++i)
			ps.println("} ");
		//ps.println("");
	}
	
	//lua only
	public void maketable(PrintStream ps) {
		List<Namespace> localpath = path();
		
		for(int i=0; i< localpath.size(); i++)
		{
			String modulename = "luaproto." + localpath.get(0).getName();
			for(int j=1; j<= i; j++)
			{
				modulename += "." + localpath.get(j).getName();
			}
			
			ps.println( modulename + " = " + modulename + " or {}");
		}
	}
}
