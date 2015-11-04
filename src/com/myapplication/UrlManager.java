package com.myapplication;


import org.apache.log4j.Logger;

public class UrlManager {

	private static Logger log = Logger.getLogger(Logger.class.getName());

	private String voucherRedirect;
	private String voucherElseRedirect;
	private String homePage;
	private String homePageIndex;
	private String loginContext;
	private String loginToRegister;
	private String loginToLogout;
	private String webApiContext;
	private String webApiContextUrl;
	

	
	public UrlManager(String voucherredirect, String voucherelseredirect, String hp, String hpi, String logincontext, String logintoregister, String logintologout, String webapicontext, String webapicontexturl){
		
		this.setVoucherRedirect(voucherredirect);
		this.setVoucherElseRedirect(voucherelseredirect);
		this.setHomePage(hp);
		this.setHomePageIndex(hpi);
		this.setLoginContext(logincontext);
		this.setLoginToRegister(logintoregister);
		this.setLoginToLogout(logintologout);
		this.setWebApiContext(webapicontext);
		this.setWebApiContextUrl(webapicontexturl);
		
		log.info("Urls are successfully loaded");
		
	}



	/**
	 * @return the voucherElseRedirect
	 */
	public String getVoucherElseRedirect() {
		return voucherElseRedirect;
	}



	/**
	 * @param voucherElseRedirect the voucherElseRedirect to set
	 */
	public void setVoucherElseRedirect(String voucherElseRedirect) {
		this.voucherElseRedirect = voucherElseRedirect;
	}



	/**
	 * @return the homePage
	 */
	public String getHomePage() {
		return homePage;
	}



	/**
	 * @param homePage the homePage to set
	 */
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}



	/**
	 * @return the homePageIndex
	 */
	public String getHomePageIndex() {
		return homePageIndex;
	}



	/**
	 * @param homePageIndex the homePageIndex to set
	 */
	public void setHomePageIndex(String homePageIndex) {
		this.homePageIndex = homePageIndex;
	}



	/**
	 * @return the loginContext
	 */
	public String getLoginContext() {
		return loginContext;
	}



	/**
	 * @param loginContext the loginContext to set
	 */
	public void setLoginContext(String loginContext) {
		this.loginContext = loginContext;
	}



	/**
	 * @return the loginToRegister
	 */
	public String getLoginToRegister() {
		return loginToRegister;
	}



	/**
	 * @param loginToRegister the loginToRegister to set
	 */
	public void setLoginToRegister(String loginToRegister) {
		this.loginToRegister = loginToRegister;
	}



	/**
	 * @return the loginToLogout
	 */
	public String getLoginToLogout() {
		return loginToLogout;
	}



	/**
	 * @param loginToLogout the loginToLogout to set
	 */
	public void setLoginToLogout(String loginToLogout) {
		this.loginToLogout = loginToLogout;
	}



	/**
	 * @return the webApiContext
	 */
	public String getWebApiContext() {
		return webApiContext;
	}



	/**
	 * @param webApiContext the webApiContext to set
	 */
	public void setWebApiContext(String webApiContext) {
		this.webApiContext = webApiContext;
	}



	/**
	 * @return the webApiContextUrl
	 */
	public String getWebApiContextUrl() {
		return webApiContextUrl;
	}



	/**
	 * @param webApiContextUrl the webApiContextUrl to set
	 */
	public void setWebApiContextUrl(String webApiContextUrl) {
		this.webApiContextUrl = webApiContextUrl;
	}




	/**
	 * @return the voucherRedirect
	 */
	public String getVoucherRedirect() {
		return voucherRedirect;
	}




	/**
	 * @param voucherRedirect the voucherRedirect to set
	 */
	public void setVoucherRedirect(String voucherRedirect) {
		this.voucherRedirect = voucherRedirect;
	}
	
}
