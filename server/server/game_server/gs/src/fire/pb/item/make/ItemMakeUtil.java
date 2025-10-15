package fire.pb.item.make;

import java.util.HashMap;
import java.util.Map;

import fire.log.Logger;
import fire.pb.item.SEquipAddattributelib;
import fire.pb.item.SEquipAddattributerandomlib;
import fire.pb.item.SEquipIteminfo;
import fire.pb.item.SEquipMakeInfo;
import fire.pb.main.ConfigManager;

public class ItemMakeUtil {
	
	static public final Logger logger = Logger.getLogger("ITEM");
	
	public static final Map<Integer, SEquipIteminfo> EQUIPITEMINFO_CFGS = 
			ConfigManager.getInstance().getConf(SEquipIteminfo.class);
	
	
	public static final Map<Integer, SEquipAddattributelib> EQUIPADDATTR_CFGS = 
			ConfigManager.getInstance().getConf(SEquipAddattributelib.class);

	
	public static final Map<Integer, SEquipAddattributerandomlib> EQUIPADDRANDOM_CFGS = 
			ConfigManager.getInstance().getConf(SEquipAddattributerandomlib.class);

	
	public static final Map<Integer, SEquipMakeInfo> EQUIPMAKEINFO_CFGS = 
			fire.pb.main.ConfigManager.getInstance().getConf(SEquipMakeInfo.class);

	
	
	
	public static Map<Integer, ZhuangBeiShuXing> effectConfigs = new HashMap<Integer, ZhuangBeiShuXing>();
	public static Map<Integer, FujiaShuXingXinXi> fujiaShuXingConfigs = new HashMap<Integer, FujiaShuXingXinXi>();
	
	public static void Init() throws Exception {
		
		effectConfigs.clear();		
		for (SEquipIteminfo sequipIteminfo : EQUIPITEMINFO_CFGS.values()) 
		{
			try 
			{
				ShuXing erandom = null;				
				ZhuangBeiShuXing effectRmd = new ZhuangBeiShuXing();  //行
				
				int n = 0;
				int m = 0;
				
				if (sequipIteminfo.shuxing1name != null) {								
					erandom = new ShuXing(sequipIteminfo.shuxing1name);
					n = 0;
					for (int i = 0; i < sequipIteminfo.shuxing1bodongduanmin.size(); i ++)
					{					
						BoDongDuan bodongduan = new BoDongDuan(n, sequipIteminfo.shuxing1bodongquanzhong.get(i),
								sequipIteminfo.shuxing1bodongduanmin.get(i), sequipIteminfo.shuxing1bodongduanmax.get(i));
						
						erandom.PutBoDongDuan(n, bodongduan);
						n ++;
					}				
					effectRmd.PutERandom(m, erandom);
					m ++;
				}
				
				if (sequipIteminfo.shuxing2name != null) {
					erandom = new ShuXing(sequipIteminfo.shuxing2name);
					n = 0;
					for (int i = 0; i < sequipIteminfo.shuxing2bodongduanmin.size(); i ++)
					{					
						BoDongDuan bodongduan = new BoDongDuan(n, sequipIteminfo.shuxing2bodongquanzhong.get(i),
								sequipIteminfo.shuxing2bodongduanmin.get(i), sequipIteminfo.shuxing2bodongduanmax.get(i));					
						
						erandom.PutBoDongDuan(n, bodongduan);
						n ++;
					}
					effectRmd.PutERandom(m, erandom);
					m ++;
				}

				if (sequipIteminfo.shuxing3name != null) {
					erandom = new ShuXing(sequipIteminfo.shuxing3name);
					n = 0;
					for (int i = 0; i < sequipIteminfo.shuxing3bodongduanmin.size(); i ++)
					{					
						BoDongDuan bodongduan = new BoDongDuan(n, sequipIteminfo.shuxing3bodongquanzhong.get(i),
								sequipIteminfo.shuxing3bodongduanmin.get(i), sequipIteminfo.shuxing3bodongduanmax.get(i));	
						
						erandom.PutBoDongDuan(n, bodongduan);
						n ++;
					}					
					effectRmd.PutERandom(m, erandom);
					m ++;
				}						
				
				effectConfigs.put(sequipIteminfo.id, effectRmd);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(new StringBuffer().append("ERROR: Init SEquipIteminfo config fail.").append(e.toString()));
			}
		}
		
		//处理附加属性
		fujiaShuXingConfigs.clear();
		for (SEquipAddattributerandomlib sequiaddr : EQUIPADDRANDOM_CFGS.values()) {
			try 
			{
				FujiaShuXingXinXi fujia = new FujiaShuXingXinXi(); //附加属性
				for (int i = 0; i < sequiaddr.getAddattributer().size(); i ++) {
					int attrid = sequiaddr.getAddattributer().get(i);
					if (attrid > 0) {
						fujia.addtableId.add(attrid);
						fujia.addquanzhong.add(sequiaddr.getAddattributerquanzhong().get(i));
						
						SEquipAddattributelib addattr = EQUIPADDATTR_CFGS.get(attrid);
						if (addattr == null)
							continue;
						
						if (addattr.getAttributename() != null) {
							fujia.addname.add(addattr.getAttributename());
						}
					}
				}
				
				fujiaShuXingConfigs.put(sequiaddr.getId(), fujia);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				logger.error(new StringBuffer().append("ERROR: Init SEquipAddattributerandomlib config fail.").append(e.toString()));
			}
		}
	}
}
