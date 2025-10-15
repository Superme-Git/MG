package fire.pb.fushi.spotcheck;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

public class Module implements ModuleInterface {

	public static final Logger logger = Logger.getLogger("CARD");

	@Override
	public void exit() {
		
	}
	
	@Override
	public void init() throws Exception {
		initSpotCheckList();
	}
	
	
	public void initSpotCheckList() {
		List<xbean.RoleTradingRecord> list=new LinkedList<xbean.RoleTradingRecord>();
		xtable.Roletradingrecordstab.getTable().browse(
			new mkdb.TTable.IWalk<String, xbean.RoleTradingRecord>(){
				@Override
				public boolean onRecord(String k, xbean.RoleTradingRecord v) {
					list.add(v.toData());
					return true;
				}
			}
		);
		mkdb.Procedure proc = new mkdb.Procedure(){
			@Override
			protected boolean process(){
				xbean.TradingMap tradingmap=xtable.Tradingidtab.get(1);//最大锁,锁住全部交易
				if(tradingmap==null){
					tradingmap=xbean.Pod.newTradingMap();
					xtable.Tradingidtab.insert(1, tradingmap);
				}
				for(xbean.RoleTradingRecord record:list){
					SpotCheckManage.init(tradingmap, record);
				}
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
	}
	
	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
}
