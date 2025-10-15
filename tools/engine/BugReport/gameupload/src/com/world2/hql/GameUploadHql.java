package com.world2.hql;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;
import com.world2.dao.*;
import com.world2.utils.LogUtil;
public class GameUploadHql {
	private static ExceptionsDAO dao = new ExceptionsDAO();
	
	public static boolean  insertNewException(Exceptions etable){
		try{
			Transaction tx = dao.getSession().beginTransaction();
			dao.save(etable);
			tx.commit();
			LogUtil.logger.info("Insert new exception:ecode="+etable.getExceptionCode()+",eaddress="+etable.getExceptionAddress()+"," +
					"edriver="+etable.getDriver()+",graphic="+etable.getGraphic()+",osversion="+etable.getOsVersion()+",sinfo="+etable.getStackInfo()
					+",upload="+etable.getUpload()+",descritp="+etable.getDescript());
		}catch(Exception e){
			e.printStackTrace();
			LogUtil.logger.error(e.toString());
			return false;
		}finally{
			HibernateSessionFactory.closeSession();
		}
		return true;
	}
	
	public static boolean containEtable(Exceptions etable){
		try{
			String hql = "from Exceptions as model where model.exceptionCode=? "+
						 " and model.exceptionAddress=? and model.appversion=?";
			Query query = dao.getSession().createQuery(hql);
			query.setString(0,etable.getExceptionCode());
			query.setString(1,etable.getExceptionAddress());
			query.setString(2,etable.getAppversion());
			List<Exceptions> list = query.list();
			if(list != null && list.size()>0)
				return true;
			return false;
		}catch(Exception e){
			e.printStackTrace();
			//LogUtil.logger.error(e.getMessage());
			LogUtil.logger.error(e.toString());
			return true;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static int getMaxID(){
		try{
			String hql = "select max(model.id) from Exceptions as model";
			Object query = dao.getSession().createQuery(hql).uniqueResult();
			if(null!= query)
				return (Integer)(query);
			return -1;
		}catch(Exception e){
			e.printStackTrace();
			LogUtil.logger.error(e.toString());
			return -2;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static int getTotalItem(){
		try{
			String hql = "select count(*) from Exceptions as model";
			Object query = dao.getSession().createQuery(hql).uniqueResult();
			if(null != query)
				return ((Number)query).intValue();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			LogUtil.logger.error(e.toString());
			return 0;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	public static List<Exceptions> getException(int start,int pagesize){
		try{
			String hql = "from Exceptions";
			Query query = dao.getSession().createQuery(hql);
			query.setFirstResult(start);
			query.setMaxResults(pagesize);
			return query.list();
		}catch(Exception e){
			e.printStackTrace();
			// LogUtil.logger.error(e.getMessage());
			LogUtil.logger.error(e.toString());
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
	
	
	
	public static Exceptions deleteException(int id){
		try{
			Exceptions ex = dao.findById(id);
			String hql = "delete from Exceptions as model where model.id="+id;
			Transaction tx = dao.getSession().beginTransaction();
			Query query = dao.getSession().createQuery(hql);
			query.executeUpdate();
			tx.commit();
			LogUtil.logger.info("delete success"+hql);
			return ex;
		}catch(Exception e){
			e.printStackTrace();
			//LogUtil.logger.error(e.getMessage());
			LogUtil.logger.error(e.toString());
			return null;
		}
	}
	
	public static List<Exceptions> getException(int start,int pagesize,String search){
		try{
			String hql = new StringBuilder().append("from Exceptions as m where m.exceptionCode like '%").append(search)
						.append("%' or ").append(" m.exceptionAddress like '%").append(search).append("%' or ")
						.append(" m.osVersion like '%").append(search).append("%' or ").append(" m.stackInfo like '%")
						.append(search).append("%' or ").append(" m.graphic like '%").append(search).append("%' or ")
						.append(" m.driver like '%").append(search).append("%' or ").append(" m.upload like '%")
						.append(search).append("%' or m.descript like '%").append(search).append("%'").toString();
			Query query = dao.getSession().createQuery(hql);
			query.setFirstResult(start);
			query.setMaxResults(pagesize);
			return query.list();
		}catch(Exception e){
			e.printStackTrace();
			LogUtil.logger.error(e.getMessage());
			LogUtil.logger.error(e.toString());
			return null;
		}finally{
			HibernateSessionFactory.closeSession();
		}
	}
}
