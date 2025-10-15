package mylogic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class RemoveAllRoleTable {

	public static void removeAllRoleTable(Set<Long> roleIds){
		try {
			Set<mkdb.Procedure> procss = new HashSet<mkdb.Procedure>();
			
			if (roleIds.isEmpty()) {
				return;
			}
			for (long roleId : roleIds) {
				mkdb.Procedure player = new mkdb.Procedure() {
					@Override
					protected boolean process() {
						String name=xtable.Properties.selectRolename(roleId);
						if(name!=null){
							xtable.Rolename2key.remove(name);//rolename2key	��ɫ���ƶ�Ӧ��keyΪ��ɫ���ƣ�������������
						}
						AllItemEquipDel.remove(roleId);//bag����     //depot�ֿ�    //equipװ������  //temp	��ʱ����   //questbag ���񱳰�     //quality�ָ���Ʒ���� //equipsװ����Ϣ��
						
						xtable.Properties.remove(roleId);//properties	��ɫ�������Ա�
						xtable.Helpcount.remove(roleId);//helpcount	Ԯ��ͳ��
						xtable.Roleupdatetimetab.remove(roleId);//roleupdatetimetab	��ɫ����ʱ���
						
						UniquepetsDel.remove(roleId);//petdepot	����ֿ�  //pet	����    //uniquepets	�����Ψһ��Ϣ������Ψһid��roleid��ӳ���ϵ��
						
						xtable.Rolepetsellcount.remove(roleId);//rolepetsellcount	����ÿ���������
						xtable.Multiexp.remove(roleId);//multiexp	�����ɫ�౶ʱ���״̬
						
						xtable.Buffrolestodisk.remove(roleId);//buffrolestodisk	������Effect
						xtable.Debugflag.remove(roleId);//debugflag	ս�����Ա�
						xtable.Skillroles.remove(roleId);//skillroles	��ɫ����
						xtable.Roleid2liveskill.remove(roleId);//roleid2liveskill	ÿ����ɫ���������Ϣ
						xtable.Roleid2particleskill.remove(roleId);//roleid2particleskill	�������ܵȼ�
						xtable.Role2impexam.remove(roleId);//role2impexam	�ǻ����������¼
//						xtable.Winnerscore.remove(roleId);//winnerscore	�ھ�������¼�ĸ��˳ɼ���
						xtable.Vipinfo.remove(roleId);//vipinfo	vip��Ϣ
						
						xtable.Friendgivenum.remove(roleId);//friendgivenum	����ÿ�����͵���������
						xtable.Rolespaces.remove(roleId);//rolespaces	���˿ռ� 
						xtable.Festival.remove(roleId);//festival	��������
						xtable.Offlinemsg.remove(roleId);//offlinemsg	������Ϣ��
						xtable.Buymedicitemnum.remove(roleId);//buymedicitemnum	ҩ�����߹�������
						xtable.Runeinfotab.remove(roleId);//runeinfotab	������Ĵ洢��Ϣ��
						xtable.Roleidclan.remove(roleId);//roleidclan	��ɫid�͹���id��Ӧ��
						xtable.Roleclanpoint.remove(roleId);//roleclanpoint	���ṱ�ױ�
						xtable.Roleapplyclantab.remove(roleId);//roleapplyclantab	��ɫ���빫��
						xtable.Commitedmission.remove(roleId);//commitedmission	�ύ�����
						xtable.Commitedscenariomission.remove(roleId);//commitedscenariomission	�ύ��֧�������
						xtable.Acceptedscenariomission.remove(roleId);//acceptedscenariomission	֧������
						xtable.Majorscenariomission.remove(roleId);//majorscenariomission	��������
						xtable.Trackedmission.remove(roleId);//trackedmission	��������
						xtable.Anyemaxituan.remove(roleId);//anyemaxituan	��ҹ��Ϸ��
						xtable.Rolecircletask.remove(roleId);//rolecircletask	��ɫѭ������
						xtable.Rolerenxingdata.remove(roleId);//rolerenxingdata	��ɫ���Ա�
						xtable.Rolecircletaskcomplete.remove(roleId);//rolecircletaskcomplete	��ɫѭ���������
						xtable.Professionleadervotechallenge.remove(roleId);//professionleadervotechallenge	��ҵ�ͶƱ����ս��Ϣ
						xtable.Roleid2professionleadertickets.remove(roleId);//roleid2professionleadertickets	��ѡ�˵ĵ�Ʊ���
						xtable.Professionleaderfightdata.remove(roleId);//professionleaderfightdata	��ѡ�˵ĵ�ս������
						xtable.Roletasks.remove(roleId);//roletasks	��ɫ������Ϣ
						xtable.Roleposes.remove(roleId);//roleposes	��ҽ���þ�֮ǰ������,���þ���Ҫ�ص�ԭ��
						xtable.Timingreward.remove(roleId);//timingreward	��ʱ�������
						xtable.Roleinstancetask.remove(roleId);//roleinstancetask	��ɫ��������
						xtable.Activeroletable.remove(roleId);//activeroletable	��Ծ��ɫ
						xtable.Beginnertip.remove(roleId);//beginnertip	��������
						xtable.Rolequitstatistics.remove(roleId);//rolequitstatistics	��ɫͳ�Ʊ�
						xtable.Courses.remove(roleId);//courses	���̱�
						xtable.Activitycalendar.remove(roleId);//activitycalendar	�����
						xtable.Rolesplayactive.remove(roleId);//rolesplayactive	��ɫÿ�ջ
						xtable.Rolesplayactiveweek.remove(roleId);//rolesplayactiveweek	��ɫÿ�ܻ
						xtable.Role2instancezone.remove(roleId);//role2instancezone	��ɫ��ǰ���ĸ������淨��
						xtable.Bingfengroles.remove(roleId);//bingfengroles	 ������������
						xtable.Triggerroles.remove(roleId);//triggerroles	��ɫ����
						xtable.Pvp1roles.remove(roleId);//pvp1roles	����pvp��¼
						xtable.Pvp3roles.remove(roleId);//pvp3roles	pvp��¼
						xtable.Pvp5roles.remove(roleId);//pvp5roles	pvp��¼
						xtable.Modnameitemroles.remove(roleId);//modnameitemroles	��¼��ɫ�������������Ϣ
						xtable.Rolerollcard.remove(roleId);//rolerollcard	�������Ϣ
						xtable.Rolesafelock.remove(roleId);//rolesafelock	��Ұ�ȫ����
						xtable.Rolemuldayloginreward.remove(roleId);//rolemuldayloginreward	���յ�¼
						xtable.Roletimernpcinfos.remove(roleId);//roletimernpcinfos	��ɫ��ʱ�npc��Ϣ
						xtable.Roleeventnpcinfos.remove(roleId);//roleeventnpcinfos	��ʱˢ�ֻ�����Ϣ
						xtable.Role2weibonotify.remove(roleId);//role2weibonotify	΢���������
						xtable.Rolepingbirolesinfos.remove(roleId);//rolepingbirolesinfos	��ɫ������(���ε����)�б���Ϣ
						xtable.Regtbl.remove(roleId);//regtbl	ǩ��
						xtable.Roledoublecharge.remove(roleId);//roledoublecharge	��ֵ�����౶
						xtable.Roleid2chargereturnprofit.remove(roleId);//roleid2chargereturnprofit	��ֵ������
						xtable.Role2npcawards.remove(roleId);//role2npcawards	NPC����������
						xtable.Huobancolumns.remove(roleId);//huobancolumns	��ս���
						xtable.Huobanzhenrongs.remove(roleId);//huobanzhenrongs	�������
						xtable.Rolebuygoodslimits.remove(roleId);//rolebuygoodslimits	��ɫ������߼�¼
						xtable.Rolesalegoodslimits.remove(roleId);//rolesalegoodslimits	��ɫ���۵��߼�¼
						xtable.Market.remove(roleId);//market	��̯����������
						xtable.Marketpet.remove(roleId);//marketpet	��̯����������
						xtable.Rolehook.remove(roleId);//rolehook	�һ�
						xtable.Roleuseitemcount.remove(roleId);//roleuseitemcount	��ɫ����ʹ�ü�¼
						xtable.Rolekaibaoxiang.remove(roleId);//rolekaibaoxiang	��ɫ�������
						xtable.Rolebestow.remove(roleId);//rolebestow	ÿ������Ŀ�������
						xtable.Ldvideoroleroseinfolisttab.remove(roleId);//ldvideoroleroseinfolisttab	���޼�¼
						xtable.Redpackroleinfolisttab.remove(roleId);//redpackroleinfolisttab	���
						xtable.Srredpacknumlisttab.remove(roleId);//srredpacknumlisttab	���
						xtable.Roleredpackrecordtab.remove(roleId);//roleredpackrecordtab	���
						xtable.Rolereceiveredpackrecordtab.remove(roleId);//rolereceiveredpackrecordtab	���
						xtable.Roletradinghisrecordlisttab.remove(roleId);//roletradinghisrecordlisttab	�㿨����
						xtable.Roletradinginfolisttab.remove(roleId);//roletradinginfolisttab	�㿨����
						xtable.Accusationinfos.remove(roleId);//accusationinfos	�ٱ����
						xtable.Roleyaoqianshutables.remove(roleId);//roleyaoqianshutables	ҡǮ��
						xtable.Roleid2monthcard.remove(roleId);//roleid2monthcard	�¿�
						xtable.Rolekaigonghuibaoxiang.remove(roleId);//rolekaigonghuibaoxiang	���ᱦ��
						
						xtable.Roleid2worldredpacksendtotal.remove(roleId);//roleid2worldredpacksendtotal	�����������ܺ�
						xtable.Roleid2weekgiverecegift.remove(roleId);//roleid2weekgiverecegift	ÿ�ܵ������ʻ�����
						xtable.Subscriptions.remove(roleId);//subscriptions	��Ҷ�������
						xtable.Markettradelogtab.remove(roleId);//markettradelogtab	������־��
						
						FriendsDel.remove(procss,roleId);//friends	���ѱ�
						
						return true;
					}
				};
				player.submit().get();
			}
			
			for(mkdb.Procedure pp:procss){
				if(pp!=null){
					pp.submit().get();
				}
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
