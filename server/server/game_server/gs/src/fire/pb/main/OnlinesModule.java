package fire.pb.main;

import gnet.link.Onlines;
import gnet.link.Onlines.Handle;
import mkdb.Procedure;

import javax.xml.parsers.DocumentBuilderFactory;

import fire.pb.RoleLinkBrokenHanle;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;


public class OnlinesModule implements ModuleInterface {
	private static Logger logger = Logger.getLogger("SYSTEM");

	@Override
	public void exit() {
		logger.info("正在关闭网络");
		mkio.Engine.getInstance().close();
		logger.info("网络已关闭");
	}

	@Override
	public void init() throws Exception {
		try {
			final Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse("gs.xio.xml");
			mkio.Engine.getInstance().register(
					new mkio.MkioConf(doc.getDocumentElement()));
		} catch (final ClassNotFoundException ex) {
			logger.error("mkio文件与jar包不匹配,请重新执行rpcgen.jar", ex);
			throw ex;
		}
		final gnet.link.Onlines onlines = Onlines.getInstance();
		Procedure.setOlines(onlines);
		final Handle brok = new RoleLinkBrokenHanle();
		onlines.setHandle(brok);
	}
	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}

}
