package com.world2.dao;

/**
 * AbstractExceptions entity provides the base persistence definition of the
 * Exceptions entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public abstract class AbstractExceptions implements java.io.Serializable {

	// Fields

	private Integer id;
	private String exceptionCode;
	private String exceptionAddress;
	private String stackInfo;
	private String osVersion;
	private String graphic;
	private String driver;
	private String upload;
	private String relativePath;
	private String descript;
	private String register;
	private String appversion;
	private String computerName;
	private String userName;
	private java.util.Date uploadTime;
	private String phoneNumber;
	private String qqOrMsn;
	private String email;
	
	// Constructors

	/** default constructor */
	public AbstractExceptions() {
	}

	/** minimal constructor */
	public AbstractExceptions(String exceptionCode, String exceptionAddress) {
		this.exceptionCode = exceptionCode;
		this.exceptionAddress = exceptionAddress;
	}

	/** full constructor */
	public AbstractExceptions(String exceptionCode, String exceptionAddress,
			String stackInfo, String osVersion, String graphic, String driver,
			String upload, String relativePath, String descript,
			String register, String appversion, String computerName,
			String userName, java.util.Date uploadTime,
			String phoneNumber,	String qqOrMsn,	String email) {
		this.exceptionCode = exceptionCode;
		this.exceptionAddress = exceptionAddress;
		this.stackInfo = stackInfo;
		this.osVersion = osVersion;
		this.graphic = graphic;
		this.driver = driver;
		this.upload = upload;
		this.relativePath = relativePath;
		this.descript = descript;
		this.register = register;
		this.appversion = appversion;
		this.computerName = computerName;
		this.userName = userName;
		this.uploadTime = uploadTime;
		this.phoneNumber = phoneNumber;
		this.qqOrMsn = qqOrMsn;
		this.email = email;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExceptionCode() {
		return this.exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionAddress() {
		return this.exceptionAddress;
	}

	public void setExceptionAddress(String exceptionAddress) {
		this.exceptionAddress = exceptionAddress;
	}

	public String getStackInfo() {
		return this.stackInfo;
	}

	public void setStackInfo(String stackInfo) {
		this.stackInfo = stackInfo;
	}

	public String getOsVersion() {
		return this.osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getGraphic() {
		return this.graphic;
	}

	public void setGraphic(String graphic) {
		this.graphic = graphic;
	}

	public String getDriver() {
		return this.driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUpload() {
		return this.upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getDescript() {
		return this.descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getRegister() {
		return this.register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getAppversion() {
		return this.appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getComputerName() {
		return this.computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public java.util.Date getUploadTime()
	{
		return this.uploadTime;
	}
	
	public void setUploadTime(java.util.Date uploadTime)
	{
		this.uploadTime = uploadTime;
	}
	
	public String getPhoneNumber()
	{
		return this.phoneNumber;
	}
	
	public void setPhoneNumber( String phoneNumber ) 
	{
		this.phoneNumber = phoneNumber;
	}

	public String getQqOrMsn()
	{
		return this.qqOrMsn;
	}
	
	public void setQqOrMsn( String qqOrMsn) 
	{
		this.qqOrMsn = qqOrMsn;
	}

	public String getEmail()
	{
		return this.email;
	}
	
	public void setEmail( String email ) 
	{
		this.email = email;
	}
	
}