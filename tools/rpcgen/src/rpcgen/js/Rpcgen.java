package rpcgen.js;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

import rpcgen.Main;
import rpcgen.Protocol;
import rpcgen.Rpc;
import rpcgen.Service;
import rpcgen.types.Bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

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

	public void make() throws FileNotFoundException, UnsupportedEncodingException {
		
		String beanfn = service.getName() + ".beans.js";
		PrintStream ps = new PrintStream( new File(beanfn), Main.outputEncoding );
		
		ps.println("(function(io){");
		ps.println("");
		
		// 导出名字空间
		ps.println("io.define(\"" + service.getSpace() + ".registerProtocols\", registerProtocols);");
		ps.println("");

		for (Protocol protocol : service.getProtocols()) {
			ps.println("io.define(\"" + protocol.fullName() + "\", " + protocol.getName() + ");");
		}
		ps.println("");

		for (Bean bean : service.getBeans()) {
			if (bean.isBean())
				ps.println("io.define(\"" + bean.fullName() + "\", " + bean.getName() + ");");
		}
		ps.println("");
		
		// 注册协议
		makeRegisterProtocols(ps);
		ps.println("");
		
		// 生成协议
		for (Protocol protocol : service.getProtocols()) {
			if (protocol instanceof Rpc){
				//throw new RuntimeException("rpc unimplemented");
				System.out.println(protocol.fullName() + " rpc unimplemented");
			}else
				new ProtocolFormatter(protocol).make(ps);
		}

		for (Bean bean : service.getBeans())
			if (bean.isBean())
				new BeanFormatter(bean).make(ps);
		
		ps.println("})(this.io);");
		ps.close();
	}

	private void makeRegisterProtocols(PrintStream ps) {

		ps.println( "function registerProtocols(){");

		for (Protocol p : service.getProtocols())
		ps.println("	io.Socket.registerProtocol(" +  p.fullName() + ");");

		ps.println("};");

	}
}
