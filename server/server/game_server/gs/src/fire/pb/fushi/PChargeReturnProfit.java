
package fire.pb.fushi;

import java.util.Map;

import mkdb.Procedure;
import fire.pb.fushi.ChargeReturnProfit;
import fire.pb.fushi.SChargeReturnProfit;
import fire.pb.main.ConfigManager;

/***
 * 计算充值返利最新状态
 * @author changhao
 *
 */
public class PChargeReturnProfit extends Procedure {

	private final long roleid;
	
	PChargeReturnProfit(long roleid)
	{
		this.roleid = roleid;
	}
	
	@Override
	protected boolean process() throws Exception {
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		int userid = prop.getUserid();
		
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		
		SRequestChargeReturnProfit msg = new SRequestChargeReturnProfit();
		
		Map<Integer, SChargeReturnProfit> map =  ConfigManager.getInstance().getConf(SChargeReturnProfit.class);
				
		boolean nodata = false;
		if (ybNums == null)
		{
			nodata = true;
		}
		else 
		{
			xbean.YbNum y = ybNums.getRoleyb().get(roleid);
			if (y == null)
			{
				nodata = true;
			}
			else
			{
				xbean.EChargeReturnProfit returnprofit = xtable.Roleid2chargereturnprofit.get(roleid); //得到返利数据 by changhao
				if (returnprofit == null)
				{
					xbean.EChargeReturnProfit profit = xbean.Pod.newEChargeReturnProfit();
			        for (SChargeReturnProfit e : map.values()) //初始化玩家的返利数据 by changhao
			        {
			        	xbean.ChargeReturnProfit p = xbean.Pod.newChargeReturnProfit();
			        	p.setId(e.getId());
			        	p.setStatus(2);
			        	p.setValue(0);
			        	p.setMaxvalue(e.chargevalue);
			        	
			        	profit.getReturnprofitmap().put(e.getId(), p);
			        }
		
					xtable.Roleid2chargereturnprofit.insert(roleid, profit);
					returnprofit = xtable.Roleid2chargereturnprofit.get(roleid);
				}
				
				int fushi = y.getNopresentnum();
				
				for (xbean.ChargeReturnProfit profit : returnprofit.getReturnprofitmap().values())
				{
					if (profit.getStatus() == 2) //如果还未达标 by changhao
					{						
						if (fushi >= profit.getMaxvalue())
						{
							profit.setStatus(1);  //设置为可领取状态 by changhao
						}
					}
					
		        	ChargeReturnProfit p = new ChargeReturnProfit();
		        	p.id = profit.getId();
		        	p.status = 2;
		        	p.value = fushi;
		        	p.maxvalue = profit.getMaxvalue();
		        	
		        	msg.listchargereturnprofit.add(p);
				}
			}	
		}
		
		if (nodata == true)
		{
	        for (SChargeReturnProfit e : map.values())
	        {
	        	ChargeReturnProfit profit = new ChargeReturnProfit();
	        	profit.id = e.getId();
	        	profit.value = 0;
	        	profit.maxvalue = e.getChargevalue();	        	
	        	profit.status = 2;
	        	msg.listchargereturnprofit.add(profit);
	        }				
		}
		
		Procedure.psendWhileCommit(roleid, msg);
		
		return true;
	}
}
