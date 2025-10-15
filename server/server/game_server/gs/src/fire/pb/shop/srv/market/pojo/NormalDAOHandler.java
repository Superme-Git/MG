package fire.pb.shop.srv.market.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.srv.market.jdbc.utils.handler.ResultSetHandler;

public class NormalDAOHandler implements ResultSetHandler<List<NormalDaoBean>> {

	@Override
	public List<NormalDaoBean> handle(ResultSet rs) throws SQLException {
		List<NormalDaoBean> results = new ArrayList<>();
		
		while(rs.next()) {
			NormalDaoBean bean = new NormalDaoBean();
			bean.setFirstno(rs.getInt("firstno"));
			bean.setTwono(rs.getInt("twono"));
			bean.setThreeno(rs.getInt("threeno"));
			bean.setId(rs.getLong("id"));
			bean.setRoleid(rs.getLong("roleid"));
			bean.setItemid(rs.getInt("itemid"));
			bean.setNumber(rs.getInt("number"));
			bean.setKey(rs.getInt("key"));
			bean.setPrice(rs.getInt("price"));
			bean.setLevel(rs.getInt("level"));
			bean.setShowtime(rs.getLong("showtime"));
			bean.setExpiretime(rs.getLong("expiretime"));
			bean.setUniquid(rs.getLong("uniqueid"));
			bean.setName(rs.getString("name"));
			bean.setAttentionnumber(rs.getInt("attention"));
			results.add(bean);
		}
		
		return results;
	}

}
