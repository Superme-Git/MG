package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mkdb.Trace;
import mylogic.BrowseClans;
import mylogic.BrowseUser;
import mylogic.ClanEventDel;
import mylogic.RemoveAllRoleTable;
import mylogic.UserIdsDel;



public class XdbMain {
	private static final String PROPERTY_FILE = "server.txt";
	private static HashMap<String,String> properties = new HashMap<String,String>();
	
	public static void main(String args[]) {
		loadProperties(PROPERTY_FILE);
		int type=Integer.parseInt(properties.get("servertype"));
		
		//�Ϸ�ɾ������Ҫע�⣬Ӧ�ñ��ĳ�ֵ�ǵ���һ�ű����ĺϷ���Ҫ���⴦���Ƿ��ֵ�����
		Set<Long> roleIds = new HashSet<Long>();
		Set<Long> clanIds = new HashSet<Long>();
		Set<Integer> userIds = new HashSet<Integer>();
		
		
		System.out.println("��ʼ����ʱ��\t"+new Date());
		Trace.info("��ʼ����ʱ��\t"+new Date());
		String xdbConfName = "gsx.mkdb.xml";
		mkdb.MkdbConf conf = new mkdb.MkdbConf(xdbConfName);
		mkdb.Mkdb.getInstance().setConf(conf);
		mkdb.Mkdb.getInstance().start();
		try {
//			//���д������ݵ��߼�
//			ClearBagMoney.clearBagGoldMoney();
			
//			//1.ɾѡ�ȼ���ʱ�䲻���ϵ����
//			SelectDelRoleid.selectDelRole(roleIds);
//			int num1=roleIds.size();
//			//2.��ɾ���б����Ƴ���ֵ�����
//			RemovePayRoleid.removePlayRole(roleIds,type);
//			int num2=roleIds.size();
//			System.out.println("����ɾ���ȼ����������\t"+num1);
//			System.out.println("ȥ����ֵ��Һ󣬷���ɾ���ȼ����������\t"+num2+"\t��ֵ\t"+(num1-num2));
			
			//1.����user��ɾ������6����ɫ�����
			BrowseUser.browseUsersids(userIds);
			UserIdsDel.remove(type,userIds,roleIds);
			
			//�������еĹ�����Ϣ�����һ�¹����¼�
			BrowseClans.browseClansids(clanIds);
			ClanEventDel.clearClanEvent(clanIds);
			
			//ע����Ҫɾ��user����Ľ�ɫ�б��е�id
			//����ɾ����������������ı�
			RemoveAllRoleTable.removeAllRoleTable(roleIds);
			
			
			
		} catch (Exception e) {
			System.out.println("Exception = " + e);
		}
		finally {
			mkdb.Mkdb.getInstance().stop();
			System.out.println("Mkdb stop.");
			System.out.println("����ʱ��\t"+new Date());
		}
	}
	
	public static boolean loadProperties(String file){
		try{
			File f = new File(file);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String message = null;

			while((message = br.readLine())!=null){
				if(message.length() > 0){
					if(message.charAt(0) != ';' ||
							message.charAt(0) != '#' ||
							message.charAt(0) != '!'){
						String[] chars = message.split("=");
						if(chars.length >= 2){
							chars[1] = chars[1].replace("\\n", "\n");
							properties.put(chars[0],chars[1]);
						}
					}
				}
			}
			return true;
		}catch(Exception e){
			System.out.printf("\t" + "��ȡ�����ļ�" + file + "ʧ��",e);
		}
		return false;
	}
	
	
	
	
}
