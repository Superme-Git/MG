package fire.pb;

import mkdb.Procedure;

public class PSetLineConfig extends Procedure {

	private final long roleid;
	private final CSetLineConfig setconfig;
	
	public PSetLineConfig(long roleid, CSetLineConfig setconfig) {
		this.roleid = roleid;
		this.setconfig = setconfig;
	}

	@Override
	protected boolean process() throws Exception {
		if (null == setconfig)
			return false;

		final xbean.Properties prop = xtable.Properties.get(roleid);
		if (null == prop)
			return false;
		
		final java.util.Map<Integer, Integer> sysConfig = prop.getLineconfigmap();
		for (Integer key : setconfig.configmap.keySet()) {
			sysConfig.put(key, setconfig.configmap.get(key));
		}
		
		final SSetLineConfig msg = new SSetLineConfig(setconfig.configmap);
		psendWhileCommit(roleid, msg);
		
		return true;
	}
}
