package fire.pb.shop.srv.market.jdbc.utils.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<T>();
        while (rs.next()) {
            rows.add(this.handleRow(rs));
        }
        return rows;
    }

    protected abstract T handleRow(ResultSet rs) throws SQLException;
}
