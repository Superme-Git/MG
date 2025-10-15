package mylogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import mkdb.Trace;

public class UserIdsDel {
	public static final int DEL_ROLE_LEVEL=30;
	public static final long DEL_ROLE_TIME=60 * 60*24*30;//ע��30����������ˣ���������������
	public static final int MAX_ROLE_NUM=1;//����ɫ����
	
	public static void remove(int type,Set<Integer> userIds,Set<Long> roleIds){
		try {
			//type�Ƿ�Ӧ�ñ�������   0����������    1Ӧ�ñ�������
			mkdb.Procedure proc = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					long curtime=System.currentTimeMillis();
					int allrolenum=0;//�ܽ�ɫ����
					int paynum=0;//��ֵ�����������
					int sexdelnum=0;//����6����ɫɾ�����������
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
							//ɾ��30�����°���30�������(���Ҹ����û�г�ֵ��¼),30��û�е�¼�����  getOfflinetime������û�а��ɵ����
							if(prop.getLevel()<=DEL_ROLE_LEVEL&&(curtime-prop.getOfflinetime())/1000>DEL_ROLE_TIME&&prop.getClankey()<=0){
								//�����������Ƿ��ֵ��
								if(type==0){
									xbean.YbNums value=xtable.Fushinum.select(userId);
									if(value!=null){
										xbean.YbNum ybnum=value.getRoleyb().get(roleid);
										if(ybnum!=null&&(ybnum.getNopresentnum()>0||ybnum.getNum()>0||ybnum.getSysnum()>0)){
											System.out.println("���˳�ֵ�������\t"+roleid);
											paynum++;
											continue;
										}
									}
								}else{
									xbean.YybFushiNums value=xtable.Yybfushi.select(userId);
									if(value!=null){
										xbean.YybFushiNum yybFushiNum = value.getRolefushi().get(roleid);
										if(yybFushiNum!=null&&(yybFushiNum.getSaveamt()>0||yybFushiNum.getGenbalance()>0||yybFushiNum.getBalance()>0)){
											System.out.println("�˺�id\t"+userId+"\t���˳�ֵ�������\t"+roleid+"\t��ɫ����\t"+prop.getRolename());
											paynum++;
											continue;
										}
									}
								}
								//��Ҫɾ�������
								//��user�����Ƴ�ɾ���Ľ�ɫid
								roleIds.add(roleid);//��ӵ�ɾ���Ľ�ɫ�б�
								user.getIdlist().remove(roleid);
								Trace.info("�˺�id\t"+userId+"\tɾ����ɫ��ɾ����ɫ"+roleid+"\t��ɫ����\t"+prop.getRolename());
							}
						}
						//�Ϸ����ɫ����1����Ҫ��������
						if(user.getIdlist().size()>1){
							//�ȱ����ȼ���ߵ�6����ɫ;�����ͬ�ȼ�,������ǰ�������Ľ�ɫ
							List<Rolebean> rolebeanlist=new ArrayList<Rolebean>();
							for(Long id:user.getIdlist()){
								xbean.Properties propertie=xtable.Properties.select(id);
								if(propertie!=null){
									int serverid=(int) (id%4096);
									rolebeanlist.add(new Rolebean(id, propertie.getLevel(), serverid, propertie.getCreatetime(),propertie.getRolename()));
								}
							}
							//����ɾ������ĺ�
							Collections.sort(rolebeanlist,new RoleBaseComparator());
							//��������ֵ������䵽�û��б�
							user.getIdlist().clear();
							for(Rolebean bean:rolebeanlist){
								user.getIdlist().add(bean.getRoleid());
							}
//							if(rolebeanlist.size()>MAX_ROLE_NUM){
//								for(int i=MAX_ROLE_NUM;i<rolebeanlist.size();i++){
//									long v=rolebeanlist.get(i).getRoleid();
////									System.out.println("�˺�id\t"+userId+"\t��ǰ�˺��³���6����ɫ��ɾ������Ľ�ɫ"+v);
//									Trace.info("�˺�id\t"+userId+"\t��ǰ�˺��³���6����ɫ��ɾ������Ľ�ɫ"+v+"\t��ɫ����"+rolebeanlist.get(i).getName());
//									sexdelnum++;
//									roleIds.add(v);//��ӵ�ɾ���Ľ�ɫ�б�
//									user.getIdlist().remove(v);
//								}
//							}
						}
					}
					
					Trace.info("�ܽ�ɫ����\t"+allrolenum+"\t��ֵ�����������\t"+paynum+"\t��ǰ�˺��³���6����ɫ��ɾ������Ľ�ɫ����"+sexdelnum);
					Trace.info("���ɾ����ɫ�ܽ�ɫ����\t"+roleIds.size());
					
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
