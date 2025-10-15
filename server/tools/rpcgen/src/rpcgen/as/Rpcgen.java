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

		// ����Э�飬����rpc��
		for (Protocol protocol : service.getProtocols()) {
			final ProtocolFormatter pf = this.getFormatter(protocol);
			pf.make(srcDir);
			pf.makeInc(beansDir);
		}

		// always clean all
		for (Bean bean : service.getBeans())
			if (bean.isBean())
				new BeanFormatter(bean).make(beansDir);

		// ɾ������Ҫ���ļ���Э�����Bean��ɾ��ʱ��ɾ����Ӧ���ļ���inc����bean����
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
