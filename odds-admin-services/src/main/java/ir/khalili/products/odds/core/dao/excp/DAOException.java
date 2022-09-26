package ir.khalili.products.odds.core.dao.excp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAOException extends Exception {

	private static final long serialVersionUID = 7977855686030871170L;

	static Logger logger = LogManager.getLogger(DAOException.class);
	
	private int resultCode;
	private String resultMessage;

	public DAOException() {
		this(null);
	}

	public DAOException(Throwable e, String daoMessage) {
		super(daoMessage, e);
		logger.error(daoMessage+" GOT EXCEPTION:",e.getMessage());
	}
	
	public DAOException(String resultMessage) {
		this(0, resultMessage);
	}

	public DAOException(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	public int getresultCode() {
		return resultCode;
	}

	public void setresultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getresultMessage() {
		return resultMessage;
	}

	public void setresultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	@Override
	public Throwable fillInStackTrace() {
		return null;
	}
}
