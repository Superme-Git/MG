package fire.pb.shop.srv.market.jdbc.utils.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fire.pb.shop.srv.market.jdbc.utils.BasicRowProcessor;
import fire.pb.shop.srv.market.jdbc.utils.RowProcessor;

public class ArrayHandler implements ResultSetHandler<List<Object[]>> {

    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    private static final List<Object[]> EMPTY_ARRAY = new ArrayList<>();

    private final RowProcessor convert;

    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    @Override
    public List<Object[]> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
