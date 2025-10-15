package test;

public class XdbMain {
	public static void main(String args[]) {
		String xdbConfName = "../testdata/xdb.xml";
		xdb.XdbConf conf = new xdb.XdbConf(xdbConfName);
		xdb.Xdb.getInstance().setConf(conf);
		xdb.Xdb.getInstance().start();
		try {
			//System.out.println(xtable.Betgames.nextKey());
		} finally {
			xdb.Xdb.getInstance().stop();
		}
	}
}
