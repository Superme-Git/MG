package fire.pb.fushi.redpack;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import mkdb.Procedure;

public class PSendRedPackRoleRecordView extends Procedure {
	private final long roleid;
	private final int modeltype;
	private final String redpackid; // 红包id

	public PSendRedPackRoleRecordView(long roleid,int modeltype,String redpackid) {
		this.roleid = roleid;
		this.modeltype = modeltype;
		this.redpackid = redpackid;
	}
	
	
	public static final int MAX_NUM=100;//每次请求发送的最大数量
	

	@Override
	protected boolean process() throws Exception {
		if(modeltype==1){
			sendRedPackRoleRecord();
		}else{
			//收到
			receiveRedPackRoleRecord();
		}
		return true;
	}
	
	/**
	 * 玩家发送红发的历史记录
	 */
	public void sendRedPackRoleRecord(){
		List<xbean.RedPackInfo> templist=new ArrayList< xbean.RedPackInfo>();
		xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(roleid);
		if(redPackRoleInfoList==null){
			return ;
		}
		SSendRedPackRoleRecordView sSendRedPackRoleRecordView=new SSendRedPackRoleRecordView();
		sSendRedPackRoleRecordView.modeltype=modeltype; // 1发出 0收到
		//发出
		for(xbean.RedPackInfo redpackinfo:redPackRoleInfoList.getRedpackinfolist().values()){
			templist.add(redpackinfo);
		}
		if(templist.size()<=0){
			return;
		}
		
		//根据时间排序
		Collections.sort(templist,new RedPackTimeComparator());
		
		xbean.RoleRedPackRecord roleRedPackRecord=xtable.Roleredpackrecordtab.select(roleid);
		if(roleRedPackRecord!=null){
			sSendRedPackRoleRecordView.redpackallnum=(int) roleRedPackRecord.getSendnum(); // 红包总个数
			sSendRedPackRoleRecordView.redpackallmoney=roleRedPackRecord.getSendgold(); // 红包总金额
			sSendRedPackRoleRecordView.redpackallfushi=roleRedPackRecord.getSendfushi(); // 红包总符石
		}
		if(redpackid.equals("0")){
			int min=Math.min(MAX_NUM, templist.size());
			for(int i=0;i<min;i++){
				xbean.RedPackInfo xbeaninfo=templist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				RedPackRoleRecord RedPackRoleRecord=createRedPackRoleRecord(xbeaninfo);
				sSendRedPackRoleRecordView.redpackrolerecord.add(RedPackRoleRecord);
			}
			sSendRedPackRoleRecordView.firstpageflag=0; // 0发出 1收到
		}else{
			//如果客户端发送过来的redpackid不是0，需要查询到这个值，发送后面的数据
			int num=0;
			boolean flag=false;
			for(int i=0;i<templist.size();i++){
				xbean.RedPackInfo xbeaninfo=templist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				if(flag==false&&xbeaninfo.getRedpackid().equals(redpackid)){
					flag=true;
				}
				if(xbeaninfo.getRedpackid().equals(redpackid)){
					continue;
				}
				
				if(flag==false){
					continue;
				}
				num++;
				RedPackRoleRecord RedPackRoleRecord=createRedPackRoleRecord(xbeaninfo);
				sSendRedPackRoleRecordView.redpackrolerecord.add(RedPackRoleRecord);
				if(num==MAX_NUM){
					break;
				}
			}
			sSendRedPackRoleRecordView.firstpageflag=1; // 0发出 1收到
		}
		psendWhileCommit(roleid, sSendRedPackRoleRecordView);
	}
	
	
	/**
	 * 玩家收到红包记录
	 */
	public void receiveRedPackRoleRecord(){
		//记录领取的红包信息
		xbean.RoleReceiveRedPackRecordList roleReceiveRedPackRecordList=xtable.Rolereceiveredpackrecordtab.select(roleid);
		if(roleReceiveRedPackRecordList==null){
			return;
		}
		List<xbean.RoleReceiveRedPackRecord> templist=new ArrayList<xbean.RoleReceiveRedPackRecord>();
		for(xbean.RoleReceiveRedPackRecord roleReceiveRedPackRecord:roleReceiveRedPackRecordList.getRolereceiveredpacklist().values()){
			templist.add(roleReceiveRedPackRecord);
		}
		if(templist.size()<=0){
			return;
		}
		SSendRedPackRoleRecordView sSendRedPackRoleRecordView=new SSendRedPackRoleRecordView();
		sSendRedPackRoleRecordView.modeltype=modeltype; // 0发出 1收到
		
		//根据时间排序
		Collections.sort(templist,new RedPackReceiveTimeComparator());
		
		
		xbean.RoleRedPackRecord roleRedPackRecord=xtable.Roleredpackrecordtab.select(roleid);
		if(roleRedPackRecord!=null){
			sSendRedPackRoleRecordView.redpackallnum=(int) roleRedPackRecord.getReceivenum(); // 红包总个数
			sSendRedPackRoleRecordView.redpackallmoney=roleRedPackRecord.getReceivegold(); // 红包总金额
			sSendRedPackRoleRecordView.redpackallfushi=roleRedPackRecord.getReceivefushi(); // 红包总符石
		}
		
		if(redpackid.endsWith("0")){
			int min=Math.min(MAX_NUM, templist.size());
			for(int i=0;i<min;i++){
				xbean.RoleReceiveRedPackRecord xbeaninfo=templist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				RedPackRoleRecord RedPackRoleRecord=createRoleReceiveRedPackRecord(xbeaninfo);
				sSendRedPackRoleRecordView.redpackrolerecord.add(RedPackRoleRecord);
			}
			sSendRedPackRoleRecordView.firstpageflag=0;
		}else{
			//如果客户端发送过来的redpackid不是0，需要查询到这个值，发送后面的数据
			int num=0;
			boolean flag=false;
			for(int i=0;i<templist.size();i++){
				xbean.RoleReceiveRedPackRecord xbeaninfo=templist.get(i);
				if(xbeaninfo==null){
					continue;
				}
				if(flag==false&&xbeaninfo.getRedpackid().equals(redpackid)){
					flag=true;
				}
				//去掉自己
				if(xbeaninfo.getRedpackid().equals(redpackid)){
					continue;
				}
				
				if(flag==false){
					continue;
				}
				num++;
				RedPackRoleRecord RedPackRoleRecord=createRoleReceiveRedPackRecord(xbeaninfo);
				sSendRedPackRoleRecordView.redpackrolerecord.add(RedPackRoleRecord);
				if(num==MAX_NUM){
					break;
				}
			}
			sSendRedPackRoleRecordView.firstpageflag=1; // 0发出 1收到
		}
		
		
		psendWhileCommit(roleid, sSendRedPackRoleRecordView);
	}
	
	

	public RedPackRoleRecord createRedPackRoleRecord(xbean.RedPackInfo xbeaninfo){
		RedPackRoleRecord redPackRoleRecord=new RedPackRoleRecord();
		redPackRoleRecord.modeltype=xbeaninfo.getModeltype();
		redPackRoleRecord.redpackid = xbeaninfo.getRedpackid();
		redPackRoleRecord.roleid = xbeaninfo.getSendroleid();
		xbean.Properties prop=xtable.Properties.select(xbeaninfo.getSendroleid());
		redPackRoleRecord.school=prop.getSchool(); // 职业
		redPackRoleRecord.rolename=prop.getRolename();
		redPackRoleRecord.shape=prop.getShape(); // 外形
		redPackRoleRecord.redpackmoney=xbeaninfo.getRedpackallmoney(); // 红包金额
		redPackRoleRecord.time=xbeaninfo.getSendtime(); // 时间
		return redPackRoleRecord;
		
	}
	
	public RedPackRoleRecord createRoleReceiveRedPackRecord(xbean.RoleReceiveRedPackRecord xbeaninfo){
		RedPackRoleRecord redPackRoleRecord=new RedPackRoleRecord();
		redPackRoleRecord.modeltype=xbeaninfo.getModeltype();
		redPackRoleRecord.redpackid = xbeaninfo.getRedpackid();
		redPackRoleRecord.roleid = xbeaninfo.getSendroleid();
		xbean.Properties prop=xtable.Properties.select(xbeaninfo.getSendroleid());
		redPackRoleRecord.school=prop.getSchool(); // 职业
		redPackRoleRecord.rolename=prop.getRolename();
		redPackRoleRecord.shape=prop.getShape(); // 外形
		//判断是否是队伍
//		if(xbeaninfo.getModeltype()==RedPackType.TYPE_TEAM){
//			redPackRoleRecord.redpackmoney=(int) xbeaninfo.getReceivefushi(); // 红包金额
//		}else{
			redPackRoleRecord.redpackmoney=(int) xbeaninfo.getReceivegold(); // 红包金额
//		}
		
		redPackRoleRecord.time=xbeaninfo.getReceivetime(); // 时间
		return redPackRoleRecord;
		
	}

}
