package fire.pb.fushi.redpack;

import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.fushi.SRedPackConfig;
import fire.pb.main.ConfigManager;

public class PSendRedPackHisView extends Procedure {
	public static final Logger logger = Logger.getLogger("REDPACK");
	
	private final long roleid;
	private final int modeltype; // 红包类型
	private final String redpackid; // 红包id
	
	

	public PSendRedPackHisView(long roleid,int modeltype,String redpackid) {
		this.roleid = roleid;
		this.modeltype = modeltype;
		this.redpackid = redpackid;
	}

	@Override
	protected boolean process() throws Exception {
		//判断红包类型是否正确
		if(modeltype<RedPackType.TYPE_WORLD||modeltype>RedPackType.TYPE_TEAM){
			return false;
		}
		SRedPackConfig sRedPackConfigdata=RedPackManage.getSRedPackConfigdataByType(modeltype);
		if(sRedPackConfigdata==null){
			return false;
		}
		//解析红包id 1_12289_20_1_1459324586158
		String infos[]=redpackid.split("_");
		if(infos.length<5){
			logger.info("角色id\t"+roleid+"\t红包不存在1,红包id\t"+redpackid+"\t红包类型"+modeltype);
			return false;
		}
		long sendroleid=Long.parseLong(infos[1]);
		xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(sendroleid);
		if(redPackRoleInfoList==null){
			logger.info("角色id\t"+roleid+"\t红包不存在2,红包id\t"+redpackid+"\t红包类型"+modeltype);
			return false;
		}
		xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redpackid);
		if(redpackinfo==null){
			logger.info("角色id\t"+roleid+"\t红包不存在3,红包id\t"+redpackid+"\t红包类型"+modeltype);
			return false;
		}	
		
		SSendRedPackHisView sSendRedPackHisView=new SSendRedPackHisView();
		sSendRedPackHisView.modeltype=modeltype; // 红包类型
		sSendRedPackHisView.redpackid=redpackinfo.getRedpackid(); // 红包Id
		sSendRedPackHisView.redpackdes=redpackinfo.getRedpackdes(); // 红包寄语
		sSendRedPackHisView.redpackallnum=redpackinfo.getRedpackallnum(); // 红包总个数
		sSendRedPackHisView.redpackallmoney=redpackinfo.getRedpackallmoney(); // 红包总金额
		sSendRedPackHisView.time=redpackinfo.getSendtime();
		for(xbean.RedPackRoleHisInfo hisinfo:redpackinfo.getRedpackrolehisinfolist()){
			//过滤没有领取过的红包
			if(hisinfo.getRoleid()<=0){
				continue;
			}
			RedPackRoleHisInfo redPackRoleHisInfo=new RedPackRoleHisInfo();
			redPackRoleHisInfo.roleid = hisinfo.getRoleid();
			xbean.Properties player=xtable.Properties.select(hisinfo.getRoleid());
			redPackRoleHisInfo.rolename = player.getRolename();
			redPackRoleHisInfo.redpackmoney = hisinfo.getRedpackmoney();
			sSendRedPackHisView.redpackrolehisinfolist.add(redPackRoleHisInfo);
		}
		psendWhileCommit(roleid, sSendRedPackHisView);
		
		return true;
	}
	


	
	
	

}
