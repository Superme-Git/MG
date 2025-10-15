package gs.counter.jmx;

public interface MoneyIOStateMXBean {

	java.util.Map<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount> getMoneyIOState();

}

