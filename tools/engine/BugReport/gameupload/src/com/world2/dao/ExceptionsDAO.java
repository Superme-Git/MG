package com.world2.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

/**
 * A data access object (DAO) providing persistence and search support for
 * Exceptions entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.world2.dao.Exceptions
 * @author MyEclipse Persistence Tools
 */

public class ExceptionsDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(ExceptionsDAO.class);
	// property constants
	public static final String EXCEPTION_CODE = "exceptionCode";
	public static final String EXCEPTION_ADDRESS = "exceptionAddress";
	public static final String STACK_INFO = "stackInfo";
	public static final String OS_VERSION = "osVersion";
	public static final String GRAPHIC = "graphic";
	public static final String DRIVER = "driver";
	public static final String UPLOAD = "upload";
	public static final String RELATIVE_PATH = "relativePath";
	public static final String DESCRIPT = "descript";
	public static final String REGISTER = "register";
	public static final String APPVERSION = "appversion";
	public static final String COMPUTER_NAME = "computerName";
	public static final String USER_NAME = "userName";

	public void save(Exceptions transientInstance) {
		log.debug("saving Exceptions instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Exceptions persistentInstance) {
		log.debug("deleting Exceptions instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Exceptions findById(java.lang.Integer id) {
		log.debug("getting Exceptions instance with id: " + id);
		try {
			Exceptions instance = (Exceptions) getSession().get(
					"com.world2.dao.Exceptions", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Exceptions instance) {
		log.debug("finding Exceptions instance by example");
		try {
			List results = getSession().createCriteria(
					"com.world2.dao.Exceptions").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Exceptions instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Exceptions as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExceptionCode(Object exceptionCode) {
		return findByProperty(EXCEPTION_CODE, exceptionCode);
	}

	public List findByExceptionAddress(Object exceptionAddress) {
		return findByProperty(EXCEPTION_ADDRESS, exceptionAddress);
	}

	public List findByStackInfo(Object stackInfo) {
		return findByProperty(STACK_INFO, stackInfo);
	}

	public List findByOsVersion(Object osVersion) {
		return findByProperty(OS_VERSION, osVersion);
	}

	public List findByGraphic(Object graphic) {
		return findByProperty(GRAPHIC, graphic);
	}

	public List findByDriver(Object driver) {
		return findByProperty(DRIVER, driver);
	}

	public List findByUpload(Object upload) {
		return findByProperty(UPLOAD, upload);
	}

	public List findByRelativePath(Object relativePath) {
		return findByProperty(RELATIVE_PATH, relativePath);
	}

	public List findByDescript(Object descript) {
		return findByProperty(DESCRIPT, descript);
	}

	public List findByRegister(Object register) {
		return findByProperty(REGISTER, register);
	}

	public List findByAppversion(Object appversion) {
		return findByProperty(APPVERSION, appversion);
	}

	public List findByComputerName(Object computerName) {
		return findByProperty(COMPUTER_NAME, computerName);
	}

	public List findByUserName(Object userName) {
		return findByProperty(USER_NAME, userName);
	}

	public List findAll() {
		log.debug("finding all Exceptions instances");
		try {
			String queryString = "from Exceptions";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Exceptions merge(Exceptions detachedInstance) {
		log.debug("merging Exceptions instance");
		try {
			Exceptions result = (Exceptions) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Exceptions instance) {
		log.debug("attaching dirty Exceptions instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Exceptions instance) {
		log.debug("attaching clean Exceptions instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}