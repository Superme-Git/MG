package com.locojoy.jmonitor.webapp.res;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.locojoy.jmonitor.Monitor;
import com.locojoy.jmonitor.util.StringHelper;
import com.locojoy.jmonitor.webapp.Mime;

@Path("tabledata")
public class TableData {

	static private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	static private final long MS_PER_DAY = 1000 * 60 * 60 * 24;
	
	@GET
	public Response data(@QueryParam("table") String table, 
			@QueryParam("dayline") boolean dayline, 
			@DefaultValue("-1") @QueryParam("zid") int zid,
			@DefaultValue("0") @QueryParam("timelow") Long timelow, 
			@DefaultValue("0") @QueryParam("timeup") Long timeup) {

		try {
			com.locojoy.jmonitor.Manager.TableData td = Monitor.getManager().queryBeanRaw(table, zid, timelow, timeup, dayline);
			
			StringBuilder sb = new StringBuilder();
			sb.append(StringHelper.join(td.head, ",")).append("\n");
			for(List<Long> r : td.data )
				sb.append(StringHelper.join(r, ",")).append("\n");
			
			StringBuilder fnsb = new StringBuilder();
			fnsb.append(table);
			fnsb.append("_").append( zid == -1 ? "ALLZONE" : zid );
			if (dayline)
				fnsb.append("_DAY");
			if (timelow != 0){
				 long tm = timelow < 0 ? System.currentTimeMillis() + timelow * MS_PER_DAY : timelow;
				 fnsb.append("_").append(sdf.format(new Date(tm)));
			}
			fnsb.append(".csv");
			
			return Response.ok()
					.type(Mime.type("csv"))
					.header("Content-Disposition", "attachment; filename=" + fnsb.toString())
					.entity(sb.toString())
					.build();

		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
		}
	}
}
