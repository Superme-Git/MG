
package fire.pb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import fire.pb.main.ConfigManager;
import mkdb.Procedure;


public class PForbidUserByMacOrIDFA extends Procedure {

	public static List<String> macs = new ArrayList<String>();
	public static List<String> idfas = new ArrayList<String>();
	
	static{
		Properties prop = ConfigManager.getInstance().getPropConf("forbid");
		if (prop!=null){	
			String macsString = prop.getProperty("macs");
			if (macsString!=null){
				String[] macsArray = macsString.split(",");
				macs.addAll(Arrays.asList(macsArray));
			}
			String idfaString = prop.getProperty("idfas");
			if (idfaString!=null){
				String[] idfaArray = idfaString.split(",");
				idfas.addAll(Arrays.asList(idfaArray));
			}
		}
	}
	public static void setMacs(List<String> macsList) {
		
		macs = macsList;
	}
	public static void setIdfas(List<String> idfaList) {

		idfas = idfaList;
	}
	
	public long roleid;
	public String roleMac;
	public String roleIdfa;
	

	public PForbidUserByMacOrIDFA(long roleid, String mac,String idfa) {

		super();
		this.roleid = roleid;
		this.roleMac = mac;
		this.roleIdfa = idfa;
	}
	@Override
	protected boolean process() throws Exception {

		if (macs.size()>0&&!roleMac.isEmpty()){
			if (macs.contains(roleMac)){
				forbid();
			}
		}
		if (idfas.size()>0&&!roleIdfa.isEmpty()){
			if (idfas.contains(roleIdfa))
				forbid();
		}
		return true;
	}
	
	public  void forbid() {
		gnet.link.Onlines.getInstance().kick(roleid, fire.pb.KickErrConst.ERR_GACD_KICKOUT);	
	}
	
	
}

