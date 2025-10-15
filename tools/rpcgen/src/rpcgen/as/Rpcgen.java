package rpcgen.as;

import rpcgen.CachedFileOutputStream;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

import rpcgen.Main;
import rpcgen.Manager;
import rpcgen.Protocol;
import rpcgen.Rpc;
import rpcgen.Service;
import rpcgen.types.Bean;
import java.io.PrintStream;

/**
 * flash as 代码生成
 */
public class Rpcgen {
	private Service service;

	// 用来生成 as 临时变量名字。as的{}不能定义变量生存期，临时变量名字在一个函数内必须唯一。
	private static Map<String, AtomicInteger> tempvarIds = new HashMap<String, AtomicInteger>();

	public static long nextTempVarId(String key) {
		AtomicInteger seed = tempvarIds.get(key);
		if (null == seed)
			tempvarIds.put(key, seed = new AtomicInteger());
		return seed.incrementAndGet();
	}

	/**
	 * 根据给定的key生成指定名字空间的临时变量。
	 * @param key 当key为null时，从默认的空间中分配名字。
	 * @return 临时变量名字
	 */
	public static String tempVarName(String key) {
		return "_" + String.valueOf(nextTempVarId(key)) + (null == key ? "_n_" : "_");
	}

	public Rpcgen(Service service) {
		this.service = service;
	}

	private ProtocolFormatter getFormatter(Protocol p) {
		return p instanceof Rpc ? new RpcFormatter((Rpc)p) : new ProtocolFormatter(p);
	}

	public void make() {
		if (null == Main.asSrcDir)
			Main.asSrcDir = service.getName() + "/src";
		if (null == Main.asBeansDir)
			Main.asBeansDir = service.getName() + "/beans";

		final java.io.File srcDir = new java.io.File(Main.asSrcDir);
		final java.io.File beansDir = new java.io.File(Main.asBeansDir);

		// 生成协议，包含rpc。
		for (Protocol protocol : service.getProtocols()) {
			final ProtocolFormatter pf = this.getFormatter(protocol);
			pf.make(srcDir);
			pf.makeInc(beansDir);
		}

		// always clean all
		for (Bean bean : service.getBeans())
			if (bean.isBean())
				new BeanFormatter(bean).make(beansDir);

		// 删除不需要的文件：协议或者Bean被删除时，删除对应的文件（inc或者bean）。
		CachedFileOutputStream.removeOtherFiles(beansDir);

		for (Manager  m : service.getManagers())
			new ManagerFormatter(m).make(srcDir);

		makeRegisterProtocols(srcDir);
	}

	private void makeRegisterProtocols(java.io.File output) {
		final PrintStream ps = service.getSpace().fopen(output, "registerProtocols.as", true);
		if (null == ps)
			return;

		ps.println("package " + service.getSpace() + " {");
		ps.println("	import com.locojoy.io.ProtocolStub;");
		ps.println("");

		for (Protocol p : service.getProtocols())
		ps.println("	import " + p.fullName() + ";");

		ps.println("");
		ps.println("	public function registerProtocols():void {");

		for (Protocol p : service.getProtocols())
		ps.println("		ProtocolStub.registerProtocol(" + p.getType() + ", " + p.fullName() + ");");

		ps.println("	}");
		ps.println("");
		ps.println("}");

		ps.close();
	}
}
