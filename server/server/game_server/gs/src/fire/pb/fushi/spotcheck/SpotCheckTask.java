package fire.pb.fushi.spotcheck;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * 处理点卡交易过期逻辑
 * @author Administrator
 *
 */
public class SpotCheckTask  extends TimerTask{
	public static final Logger logger = Logger.getLogger("CARD");
	@Override
	public void run() {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.TradingMap tradingMap = xtable.Tradingidtab.select(1);// 最大锁,锁住全部交易
				if (tradingMap == null) {
					return false;
				}
				List<xbean.TradingPrice> buylist=tradingMap.getBuylist();
				for(xbean.TradingPrice value:buylist){
					if(value==null){
						continue;
					}
					if(value.getNum()<=0){
						continue;
					}
					if(System.currentTimeMillis()-value.getCreatetime()<=SpotCheckManage.getliveTime()){
						continue;
					}
					mkdb.Procedure.pexecuteWhileCommit(new PSysCancelTrading(value.getRoleid(),value.getTradingid()));
				}
				List<xbean.TradingPrice> selllist=tradingMap.getSelllist();
				for(xbean.TradingPrice value:selllist){
					if(value==null){
						continue;
					}
					if(value.getNum()<=0){
						continue;
					}
					if(System.currentTimeMillis()-value.getCreatetime()<=SpotCheckManage.getliveTime()){
						continue;
					}
					mkdb.Procedure.pexecuteWhileCommit(new PSysCancelTrading(value.getRoleid(),value.getTradingid()));
				}
				return true;
			};
		}.submit();
	}
	
	
}