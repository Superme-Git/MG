package fire.pb.shop.srv.market.pojo;

public class NormalDaoBean extends DaoBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5665735210981982009L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NormalDaoBean [id=");
		builder.append(id);
		builder.append(", firstno=");
		builder.append(firstno);
		builder.append(", twono=");
		builder.append(twono);
		builder.append(", threeno=");
		builder.append(threeno);
		builder.append(", uniquid=");
		builder.append(uniquid);
		builder.append(", key=");
		builder.append(key);
		builder.append(", itemid=");
		builder.append(itemid);
		builder.append(", extid=");
		builder.append(extid);
		builder.append(", name=");
		builder.append(name);
		builder.append(", roleid=");
		builder.append(roleid);
		builder.append(", showtime=");
		builder.append(showtime);
		builder.append(", expiretime=");
		builder.append(expiretime);
		builder.append(", level=");
		builder.append(level);
		builder.append(", number=");
		builder.append(number);
		builder.append(", price=");
		builder.append(price);
		builder.append(", attentionnumber=");
		builder.append(attentionnumber);
		builder.append("]");
		return builder.toString();
	}

}
