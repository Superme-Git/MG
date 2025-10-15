package fire.pb.item.groceries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xbean.RoleAddPointProperties;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.STimeReturn;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.talk.MessageMgr;



public class TimeReturn extends GroceryItem {

	public TimeReturn(ItemMgr im, int itemid) {
		super(im, itemid);		
	}
	
	public TimeReturn(ItemMgr im, xbean.Item item) {
		super(im, item);
	}
	
	@Override
	protected UseItemHandler getUseItemHandler() {
		final int itemid = getItemId();
		return new UseTimeReturnHandler(itemid);
	}
	
	private class UseTimeReturnHandler implements UseItemHandler{
		private int itemid;
		public UseTimeReturnHandler(int itemid){
			this.itemid = itemid;
		}
		
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			
			xbean.Properties prop = xtable.Properties.get(roleId);
			if (prop == null)
				throw new IllegalArgumentException("错误的roleId:" + roleId);
			
			STimeReturn cnf = fire.pb.main.ConfigManager.getInstance().getConf(STimeReturn.class).get(itemid);
			RoleAddPointProperties addfp = prop.getAddpointfp();
			int curScheme = prop.getScheme();
			
			int agi = 0, cons = 0, endu = 0, iq = 0, str = 0;
			
			int returnCnt = 0;
			
			if (cnf.getAgi() < 0) {
				if (Math.abs(cnf.getAgi()) > addfp.getAgi_save().get(curScheme))
					agi = addfp.getAgi_save().get(curScheme)*(-1);
				else agi = cnf.getAgi();
				
				returnCnt ++;
			}
			else agi = cnf.getAgi();
				
			if (cnf.getCons() < 0) {
				if (Math.abs(cnf.getCons()) > addfp.getCons_save().get(curScheme))
					cons = addfp.getCons_save().get(curScheme)*(-1);
				else cons = cnf.getCons();
				
				returnCnt ++;
			}
			else cons = cnf.getCons();
			
			if (cnf.getEndu() < 0) {
				if (Math.abs(cnf.getEndu()) > addfp.getEndu_save().get(curScheme)) 
					endu = addfp.getEndu_save().get(curScheme)*(-1);
				else endu = cnf.getEndu();
				
				returnCnt ++;
			}
			else endu = cnf.getEndu();
			
			if (cnf.getIq() < 0) {
				if (Math.abs(cnf.getIq()) > addfp.getIq_save().get(curScheme))
					iq = addfp.getIq_save().get(curScheme)*(-1);
				else iq = cnf.getIq();
				
				returnCnt ++;
			}
			else iq = cnf.getIq();
			
			if (cnf.getStr() < 0) {
				if (Math.abs(cnf.getStr()) > addfp.getStr_save().get(curScheme)) 
					str = addfp.getStr_save().get(curScheme)*(-1);
				else str = cnf.getStr();
				
				returnCnt ++;
			}
			else str = cnf.getStr();
			
			final int curpoint = prop.getPoint().get(curScheme);
			final int savepoint = addfp.getAgi_save().get(curScheme) + addfp.getCons_save().get(curScheme) +
					addfp.getEndu_save().get(curScheme) + addfp.getIq_save().get(curScheme) + addfp.getStr_save().get(curScheme);
			final int decpoints = agi + cons + endu + iq + str;
			if ((savepoint + decpoints) < 0) {
				MessageMgr.sendMsgNotify(roleId, 150015, null);
				return Commontext.UseResult.FAIL;
			}
			
			prop.getPoint().put(curScheme, curpoint + Math.abs(decpoints));
			
			xbean.BasicFightProperties bfp = prop.getBfp();
			bfp.setAgi((short) (bfp.getAgi() + agi));
			bfp.setCons((short) (bfp.getCons() + cons));
			bfp.setEndu((short) (bfp.getEndu() + endu));
			bfp.setIq((short) (bfp.getIq() + iq));
			bfp.setStr((short) (bfp.getStr() + str));
			
			addfp.getAgi_save().put(curScheme, addfp.getAgi_save().get(curScheme) + agi);
			addfp.getCons_save().put(curScheme, addfp.getCons_save().get(curScheme) + cons);
			addfp.getEndu_save().put(curScheme, addfp.getEndu_save().get(curScheme) + endu);
			addfp.getIq_save().put(curScheme, addfp.getIq_save().get(curScheme) + iq);
			addfp.getStr_save().put(curScheme, addfp.getStr_save().get(curScheme) + str);		
			
			Map<Integer,Float> changedAttrs = new HashMap<Integer,Float>();
			fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(roleId);
			changedAttrs.putAll(erole.updateAllFinalAttrs());
			
//			if(cons > 0)
//				changedAttrs.putAll(fullHpAndRecoverWound());
//			if(iq > 0)
//				changedAttrs.putAll(fullMp());
			//changedAttrs.put(AttrType.HP, (float)getHp());
			//changedAttrs.put(AttrType.MP, (float)getMp());
			
			changedAttrs.put(AttrType.POINT, (float)prop.getPoint().get(curScheme));	
			
			SRefreshRoleData roledata = new SRefreshRoleData();
			roledata.datas.putAll(changedAttrs);;
			mkdb.Procedure.psendWhileCommit(roleId, roledata);
			
			//给本人发送透明框消息
			if (returnCnt == 5) { //全部重置				
				MessageMgr.sendMsgNotify(roleId, 150019, null);
			}
			else {
				String para1 = null;
				String para2 = null;
				if (str != 0) {para1 = "力量"; para2 = String.valueOf(Math.abs(str));}
				else if (iq != 0) {para1 = "智力"; para2 = String.valueOf(Math.abs(iq));}
				else if (cons != 0) {para1 = "体质"; para2 = String.valueOf(Math.abs(cons));}
				else if (endu != 0) {para1 = "耐力"; para2 = String.valueOf(Math.abs(endu));}
				else if (agi != 0) {para1 = "敏捷"; para2 = String.valueOf(Math.abs(agi));}	
				
				if (para1 != null && para2 != null) {
					List<String> param = new ArrayList<String>();
					param.add(para1);
					param.add(para2);
					MessageMgr.sendMsgNotify(roleId, 150018, param);
				}
			}
			
			//刷新人物加点信息
			final fire.pb.attr.SRefreshPointType refresh = new fire.pb.attr.SRefreshPointType();			
			
			refresh.bfp.agi = (short)bfp.getAgi();
			refresh.bfp.cons = (short)bfp.getCons();
			refresh.bfp.endu = (short)bfp.getEndu();
			refresh.bfp.iq = (short)bfp.getIq();
			refresh.bfp.str = (short)bfp.getStr();			
			
			refresh.bfp.agi_save.putAll(prop.getAddpointfp().getAgi_save());
			refresh.bfp.cons_save.putAll(prop.getAddpointfp().getCons_save());
			refresh.bfp.endu_save.putAll(prop.getAddpointfp().getEndu_save());
			refresh.bfp.iq_save.putAll(prop.getAddpointfp().getIq_save());
			refresh.bfp.str_save.putAll(prop.getAddpointfp().getStr_save());
			
			refresh.point.putAll(prop.getPoint());
			refresh.pointscheme = prop.getScheme();
			refresh.schemechanges = prop.getSchemechanges();
			
			mkdb.Procedure.psendWhileCommit(roleId, refresh);
			
			return Commontext.UseResult.SUCC;
		}
	}
}
