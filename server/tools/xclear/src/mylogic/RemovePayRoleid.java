package mylogic;

import java.util.Map.Entry;
import java.util.Set;

/**
 * ��ɾ���б����Ƴ����ֵ�����
 * @author Administrator
 *
 */
public class RemovePayRoleid {
	
	public static void removePlayRole(Set<Long> roleIds,int type){
		//�Ƿ�Ӧ�ñ�������   0����������    1Ӧ�ñ�������
		if(type==0){
			xtable.Fushinum.getTable().browse(
					new mkdb.TTable.IWalk<Integer, xbean.YbNums>() {
						@Override
						public boolean onRecord(Integer userid, xbean.YbNums value) {
							if(value==null){
								return true;
							}
							for(Entry<Long, xbean.YbNum> entry:value.getRoleybAsData().entrySet()){
								Long roleid=entry.getKey();
								xbean.YbNum ybnum=entry.getValue();
								if(ybnum.getNopresentnum()>0&&roleid!=null){
									boolean flag=roleIds.remove(roleid);
									if(flag){
										System.out.println("ɾ����ֵ�������\t"+roleid);
									}
								}
							}
							return true;
						}
					});
		}else{
			xtable.Yybfushi.getTable().browse(
					new mkdb.TTable.IWalk<Integer, xbean.YybFushiNums>() {
						@Override
						public boolean onRecord(Integer userid, xbean.YybFushiNums value) {
							if(value==null){
								return true;
							}
							for(Entry<Long, xbean.YybFushiNum> entry:value.getRolefushiAsData().entrySet()){
								Long roleid=entry.getKey();
								xbean.YybFushiNum ybnum=entry.getValue();
								if(ybnum.getSaveamt()>0&&roleid!=null){
									boolean flag=roleIds.remove(roleid);
									if(flag){
										System.out.println("ɾ����ֵ�������\t"+roleid);
									}
								}
							}
							return true;
						}
					});
		}
	}

}
