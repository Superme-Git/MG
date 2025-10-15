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
 * flash as ��������
 */
public class Rpcgen {
	private Service service;

	// �������� as ��ʱ�������֡�as��{}���ܶ�����������ڣ���ʱ����������һ�������ڱ���Ψһ��
	private static Map<String, AtomicInteger> tempvarIds = new HashMap<String, AtomicInteger>();

	public static long nextTempVarId(String key) {
		AtomicInteger seed = tempvarIds.get(key);
		if (null == seed)
			tempvarIds.put(key, seed = new AtomicInteger());
		return seed.incrementAndGet();
	}

	/**
	 * ���ݸ�����key����ָ�����ֿռ����ʱ������
	 * @param key ��keyΪnullʱ����Ĭ�ϵĿռ��з������֡�
	 * @return ��ʱ��������
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
		
		// �������ֿռ�
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
		
		// ע��Э��
		makeRegisterProtocols(ps);
		ps.println("");
		
		// ����Э��
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
