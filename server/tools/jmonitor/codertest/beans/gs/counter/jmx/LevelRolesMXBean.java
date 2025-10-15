package gs.counter.jmx;

public interface LevelRolesMXBean {

	java.util.Map<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> getLevelRoles();

}

