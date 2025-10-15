package fire.pb.shop.srv.market.jdbc.utils.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fire.pb.shop.srv.market.jdbc.utils.RowProcessor;

public class ArrayListHandler extends AbstractListHandler<List<Object[]>> {

    private final RowProcessor convert;

    public ArrayListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    public ArrayListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    @Override
    protected List<Object[]> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}
