package ir.khalili.products.odds.core.excp.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DAOEXCP_AuthenticationValidation extends Exception {

	private static final long serialVersionUID = 7977855686030871170L;

	static Logger logger = LogManager.getLogger(DAOEXCP_AuthenticationValidation.class);
	
	private int resultCode;
	private String resultMessage;

	public DAOEXCP_AuthenticationValidation(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@Override
	public Throwable fillInStackTrace() {
		return null;
	}
}
