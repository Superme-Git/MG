package cn.com.zc.db.walker;

import xbean.ClanInfo;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.Dbx;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;
import cn.com.zc.db.util.FileManager;

import com.goldhuman.Common.Marshal.OctetsStream;

public class FactionDetailWalker extends Walker {

	public FactionDetailWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		Long factionKey = (Long)key;
//		xbean.FactionInfo2 factionInfo = (xbean.FactionInfo2)value;
		
		try {
//			doQuickPhoto(factionKey, factionInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private void doQuickPhoto(Long factionKey, ClanInfo factionInfo) throws Exception {
/*		Long master = factionInfo.getFactionmaster();
		int level =factionInfo.getFactionlevel();
		int buildLevel = factionInfo.getBuildlevel();
		String facname = factionInfo.getFactionname();
		Long factionCreater = factionInfo.getCreator();
		
		StringBuffer sb = new StringBuffer();
		String separator = ",";
		sb.append(factionKey + separator);
		sb.append(master + separator);
		sb.append(level + separator);
		sb.append(buildLevel + separator);
		sb.append(facname + separator);
		sb.append(factionCreater);*/
	
		//FileManager.getInstance().setQuickPhotoRecord(Constant.FACTION_DETAIL, sb.toString());
	}

}
