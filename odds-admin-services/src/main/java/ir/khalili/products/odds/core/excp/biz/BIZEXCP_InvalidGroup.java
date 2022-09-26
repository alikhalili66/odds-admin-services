package ir.khalili.products.odds.core.excp.biz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BIZEXCP_InvalidGroup extends Exception {

	private static final long serialVersionUID = 9141432548554161363L;
	
	static Logger logger = LogManager.getLogger(BIZEXCP_InvalidGroup.class);
	
	private int resultCode;
	private String resultMessage;

	public BIZEXCP_InvalidGroup(int resultCode, String resultMessage) {
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
