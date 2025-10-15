package com.world2.dao;

/**
 * Exceptions entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Exceptions extends AbstractExceptions implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -6815999457133535224L;

	/** default constructor */
	public Exceptions() {
	}

	/** minimal constructor */
	public Exceptions(String exceptionCode, String exceptionAddress) {
		super(exceptionCode, exceptionAddress);
	}

	/** full constructor */
	public Exceptions(String exceptionCode, String exceptionAddress,
			String stackInfo, String osVersion, String graphic, String driver,
			String upload, String relativePath, String descript,
			String register, String appversion, String computerName,
			String userName, java.util.Date uploadTime,
			String phoneNumber,	String qqOrMsn,	String email) {
		super(exceptionCode, exceptionAddress, stackInfo, osVersion, graphic,
				driver, upload, relativePath, descript,register,appversion,
				computerName,userName, uploadTime, phoneNumber, qqOrMsn, email);
	}

}
