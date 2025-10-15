package mylogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import mkdb.Trace;

public class UserIdsDel {
	public static final int DEL_ROLE_LEVEL=30;
	public static final long DEL_ROLE_TIME=60 * 60*24*30;//注意30天毫秒就溢出了，所以用秒来处理
	public static final int MAX_ROLE_NUM=1;//最大角色数量
	
	public static void remove(int type,Set<Integer> userIds,Set<Long> roleIds){
		try {
			//type是否应用宝服务器   0正常服务器    1应用宝服务器
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					long curtime=System.currentTimeMillis();
					int allrolenum=0;//总角色数量
					int paynum=0;//充值过的玩家数量
					int sexdelnum=0;//超过6个角色删除的玩家数量
					if (userIds.isEmpty()) {
						return true;
					}
					lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userIds));
					for (int userId : userIds) {
						xbean.User user = xtable.User.get(userId);
						if(user == null){
							continue;
						}
						List<Long> userlists=new ArrayList<Long>();
						userlists.addAll(user.getIdlist());
						for(Long roleid:userlists){
							if(roleid==null){
								continue;
							}
							xbean.Properties prop=xtable.Properties.select(roleid);
							if(prop==null){
								continue;
							}
							allrolenum++;
							//删除30级以下包括30级的玩家(并且改玩家没有充值记录),30天没有登录的玩家  getOfflinetime，并且没有帮派的玩家
							if(prop.getLevel()<=DEL_ROLE_LEVEL&&(curtime-prop.getOfflinetime())/1000>DEL_ROLE_TIME&&prop.getClankey()<=0){
								//过滤这个玩家是否充值了
								if(type==0){
									xbean.YbNums value=xtable.Fushinum.select(userId);
									if(value!=null){
										xbean.YbNum ybnum=value.getRoleyb().get(roleid);
										if(ybnum!=null&&(ybnum.getNopresentnum()>0||ybnum.getNum()>0||ybnum.getSysnum()>0)){
											System.out.println("过滤充值过的玩家\t"+roleid);
											paynum++;
											continue;
										}
									}
								}else{
									xbean.YybFushiNums value=xtable.Yybfushi.select(userId);
									if(value!=null){
										xbean.YybFushiNum yybFushiNum = value.getRolefushi().get(roleid);
										if(yybFushiNum!=null&&(yybFushiNum.getSaveamt()>0||yybFushiNum.getGenbalance()>0||yybFushiNum.getBalance()>0)){
											System.out.println("账号id\t"+userId+"\t过滤充值过的玩家\t"+roleid+"\t角色名称\t"+prop.getRolename());
											paynum++;
											continue;
										}
									}
								}
								//需要删除的玩家
								//从user表里移除删除的角色id
								roleIds.add(roleid);//添加到删除的角色列表
								user.getIdlist().remove(roleid);
								Trace.info("账号id\t"+userId+"\t删除角色，删除角色"+roleid+"\t角色名称\t"+prop.getRolename());
							}
						}
						//合服后角色大于1就需要重新排序
						if(user.getIdlist().size()>1){
							//先保留等级最高的6个角色;如出现同等级,保留靠前服务器的角色
							List<Rolebean> rolebeanlist=new ArrayList<Rolebean>();
							for(Long id:user.getIdlist()){
								xbean.Properties propertie=xtable.Properties.select(id);
								if(propertie!=null){
									int serverid=(int) (id%4096);
									rolebeanlist.add(new Rolebean(id, propertie.getLevel(), serverid, propertie.getCreatetime(),propertie.getRolename()));
								}
							}
							//排序，删除多余的号
							Collections.sort(rolebeanlist,new RoleBaseComparator());
							//把排序后的值重新填充到用户列表
							user.getIdlist().clear();
							for(Rolebean bean:rolebeanlist){
								user.getIdlist().add(bean.getRoleid());
							}
//							if(rolebeanlist.size()>MAX_ROLE_NUM){
//								for(int i=MAX_ROLE_NUM;i<rolebeanlist.size();i++){
//									long v=rolebeanlist.get(i).getRoleid();
////									System.out.println("账号id\t"+userId+"\t当前账号下超过6个角色，删除多余的角色"+v);
//									Trace.info("账号id\t"+userId+"\t当前账号下超过6个角色，删除多余的角色"+v+"\t角色名称"+rolebeanlist.get(i).getName());
//									sexdelnum++;
//									roleIds.add(v);//添加到删除的角色列表
//									user.getIdlist().remove(v);
//								}
//							}
						}
					}
					
					Trace.info("总角色数量\t"+allrolenum+"\t充值过的玩家数量\t"+paynum+"\t当前账号下超过6个角色，删除多余的角色数量"+sexdelnum);
					Trace.info("最后删除角色总角色数量\t"+roleIds.size());
					
					return true;
				}
			};
			proc.submit().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
