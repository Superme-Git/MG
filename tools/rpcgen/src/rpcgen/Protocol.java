package rpcgen;

import org.w3c.dom.Element;

import rpcgen.types.Bean;
import java.util.*;
import rpcgen.types.Type;

public class Protocol extends Naming {
	public final static int RESERVE_TYPE = 32;
	public final static short LEVEL0 = 0;

	public static Set<Protocol> compile(Namespace current, Set<String> ps) {
		Set<Protocol> protocols = new HashSet<Protocol>();
		for (String p : ps)
			protocols.add(compile(current, p));
		return protocols;
	}

	private static Protocol compile(Namespace current, String name) {
		Name n = Namespace.search(current, name);
		if (n == null || !(n instanceof Protocol))
			throw new RuntimeException("unkown Protocol: name=" + name);
		return (Protocol)n;
	}

	private void checkReserveType(short typeHigh, short typeLow) {
		// compatible
		if (typeHigh == 0) {
			if (15 == typeLow) return; // AU:UserLogin
			if (10 == typeLow) return; // AU:KickoutUser
		}
		// reserve for share
		if (typeHigh == 0 && this.typeLow <= RESERVE_TYPE)
			throw new RuntimeException(fullName() + ", [type -le " + RESERVE_TYPE + "] is reserved" );
		// reserve sign
		if (typeHigh < 0 || typeLow < 0)
			throw new RuntimeException(fullName() + ", [typeHigh < 0 , typeLow < 0] is reserved" );
	}

	// define
	private short typeHigh;
	private short typeLow;
	private int   maxsize;
	private String prior;
	private String waitingProtocol;
	private int tolua;

	private Bean implementInc;

	protected Protocol(Namespace space, Element self, short typeHigh, boolean isRpc) {
		super(self.getAttribute("name"));
		space.add(this);

		this.typeHigh = typeHigh;
		this.typeLow = Short.parseShort(self.getAttribute("type"));
		checkReserveType(this.typeHigh, this.typeLow);
		Main.globalProtocolTypes.checkAdd(getType());

		this.maxsize = Integer.parseInt(self.getAttribute("maxsize"));
		if (this.maxsize < 0 || this.maxsize > 2*1024*1024) // xxx
			throw new RuntimeException(this.fullName() + ": maxsize out of range");
		this.prior = self.getAttribute("prior");
		String waitingstr = self.getAttribute("waiting");
		if(waitingstr == null || waitingstr.equals(""))
			this.waitingProtocol = "";
		else
			this.waitingProtocol = self.getAttribute("waiting");
		
		this.tolua = 0;
		if (self.getAttribute("tolua") != "")
			this.tolua = Integer.parseInt(self.getAttribute("tolua"));
	}

	public Protocol(Namespace space, Element self, short typeHigh) {
		this(space, self, typeHigh, false);
		implementInc = new Bean(super.getName()) {
			@Override public String getInclude() { return null; }
			@Override public boolean isBean() { return false; }
		};
		implementInc.setSpace(space);
		String argument = self.getAttribute("argument");
		if (!argument.isEmpty())
			implementInc.add(new rpcgen.types.Variable("argument", argument, null, null));
		implementInc.parse(self);
	}

	public int getType() {
		return typeHigh << 16 | typeLow;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public short getTypeHigh() {
		return typeHigh;
	}
	
	public int getTolua(){
		return tolua;
	}

	@Override
	public void compile() {
		implementInc.compile();
	}

	public String getInclude() {
		return "#include " + Main.quote(getSpace().join("/") + "/" + getName() + ".hpp");
	}
	
	public String getpkg() {
		return "$pfile " + Main.quote("ProtoDef/" + getSpace().join("/") + "/" + getName() + ".pkg");
	}
	
	public String getlua() {
		return "require(" + Main.quote("protodef." + getSpace().join(".").toLowerCase() 
				+ "." + getName().toLowerCase() + "") + ")";
	}

	public void dependBeans(Set<Bean> beans) {
		Set<Type> deps = new HashSet<Type>();
		implementInc.depends(deps);
		for (Type type : deps)
			if (type instanceof Bean)
				beans.add((Bean)type);
	}

	public void makeCoder(Element coder, int deep) {
		Element protocol = coder.getOwnerDocument().createElement(getClass().getSimpleName());
		protocol.setAttribute("class", fullName());
		protocol.setAttribute("maxSize", String.valueOf(maxsize));
		Main.appendChild(coder, protocol, deep);
	}

	public String getPrior() {
		return prior;
	}
	
	public String getWaitingProtocol() {
		return waitingProtocol;
	}

	/**
	 * 协议结构由一个匿名Bean类实例解析。这个方法返回这个匿名类的实例。
	 * @return
	 */
	public Bean getImplementBean() {
		return implementInc;
	}
}
