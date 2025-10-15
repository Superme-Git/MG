package fire.pb.fushi.redpack;
import java.util.Arrays;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddFuShi;
import fire.pb.game.MoneyType;
import fire.pb.talk.MessageMgr;

/**
 * 处理红包过期逻辑
 * @author Administrator
 *
 */
public class RedPackTask  extends TimerTask{
	public static final Logger logger = Logger.getLogger("REDPACK");
	@Override
	public void run() {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.RedPackInfoViewList redPackInfoList=xtable.Redpackinfoviewlisttab.select(1);
				if(redPackInfoList==null){
					return false;
				}
				for(xbean.RedPackBaseInfo redPackBaseInfo:redPackInfoList.getRedpackinfoviewlist().values()){
					if(redPackBaseInfo==null){
						continue;
					}
					//过滤掉时间没到的红包
					if(System.currentTimeMillis()-redPackBaseInfo.getSendtime()<=RedPackManage.getValidRedPackTime()){
						continue;
					}
					xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.select(redPackBaseInfo.getRoleid());
					if(redPackRoleInfoList==null){
						continue;
					}
					xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
					if(redpackinfo==null){
						continue;
					}
					//过滤掉已经领取完成的红包
					if(redpackinfo.getRedpackstate()==RedPackState.STATE_NONE){
						continue;
					}
					//过滤掉数量已经没有的红包
					if(redpackinfo.getRedpackreceivednum()>=redpackinfo.getRedpackallnum()){
						continue;
					}
					//过滤掉时间没到的红包
					if(System.currentTimeMillis()-redpackinfo.getSendtime()<=RedPackManage.getValidRedPackTime()){
						continue;
					}
					//退回红包给玩家
					int money=redpackinfo.getRedpackallmoney()-redpackinfo.getRedpackreceiveallmoney();
					if(money<=0){
						continue;
					}
					if(redpackinfo.getBackflag()==1){
						continue;
					}
					mkdb.Procedure proc=new mkdb.Procedure(){
						@Override
						protected boolean process() throws Exception {
							xbean.RedPackRoleInfoList redPackRoleInfoList=xtable.Redpackroleinfolisttab.get(redPackBaseInfo.getRoleid());
							if(redPackRoleInfoList==null){
								return false;
							}
							xbean.RedPackInfo redpackinfo=redPackRoleInfoList.getRedpackinfolist().get(redPackBaseInfo.getRedpackid());
							if(redpackinfo==null){
								return false;
							}
							//过滤掉已经领取完成的红包
							if(redpackinfo.getRedpackstate()==RedPackState.STATE_NONE){
								return false;
							}
							//过滤掉数量已经没有的红包
							if(redpackinfo.getRedpackreceivednum()>=redpackinfo.getRedpackallnum()){
								return false;
							}
							//过滤掉时间没到的红包
							if(System.currentTimeMillis()-redpackinfo.getSendtime()<=RedPackManage.getValidRedPackTime()){
								return false;
							}
							//退回红包给玩家
							int money=redpackinfo.getRedpackallmoney()-redpackinfo.getRedpackreceiveallmoney();
							if(money<=0){
								return false;
							}
							Integer userid =xtable.Properties.selectUserid(redpackinfo.getSendroleid());
							long roleid=redpackinfo.getSendroleid();
							if(userid==null){
								return false;
							}
							if(redpackinfo.getBackflag()==1){
								return false;
							}
							redpackinfo.setBackflag(1);//设置退钱标志
//							if(redpackinfo.getModeltype()==RedPackType.TYPE_TEAM){
//								new PAddFuShi(userid, roleid,money,FushiConst.CASH_FUSHI, YYLoggerTuJingEnum.tujing_Value_redpack).call();
//								logger.info("角色id"+"\t"+roleid+"退回没有领取的红包,红包类型\t"+redpackinfo.getModeltype()+"红包金额\t"+money+"\t红包id"+redpackinfo.getRedpackid());
//								MessageMgr.psendSystemMessageToRole(roleid, 172007, Arrays.asList(RedPackManage.getTypeStringByType(redpackinfo.getModeltype()),money+""));
//							}else{
								//添加金币
								fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
								bag.addSysGold(money, "退回没有领取的红包", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
								logger.info("角色id"+"\t"+roleid+"退回没有领取的红包,红包类型\t"+redpackinfo.getModeltype()+"红包金额\t"+money+"\t红包id"+redpackinfo.getRedpackid());
								MessageMgr.psendSystemMessageToRole(roleid, 172008, Arrays.asList(RedPackManage.getTypeStringByType(redpackinfo.getModeltype()),money+""));
								
								{
									if(redpackinfo.getModeltype()==RedPackType.TYPE_WORLD)
										bag.addSysCurrency((long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_RETURN_WORLD_BONUS))
												, MoneyType.MoneyType_EreditPoint, "红包过期退回",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
									else if(redpackinfo.getModeltype()==RedPackType.TYPE_CLAN)
										bag.addSysCurrency((long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_RETURN_CLAN_BONUS))
												, MoneyType.MoneyType_EreditPoint, "红包过期退回",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
									else if(redpackinfo.getModeltype()==RedPackType.TYPE_TEAM)
										bag.addSysCurrency((long)(money*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_RETURN_TEAM_BONUS))
												, MoneyType.MoneyType_EreditPoint, "红包过期退回",fire.log.enums.YYLoggerTuJingEnum.tujing_Value_redpack, 0);
								}
//							}
							return true;
						}
					};
					mkdb.Procedure.pexecuteWhileCommit(proc);
				}
				return true;
			};
		}.submit();
	}
	
	
}