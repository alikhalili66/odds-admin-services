package ir.khalili.products.odds.core.biz.excp;

import ir.khalili.products.odds.core.dao.excp.DAOException;

public class BIZEXCP_Folder extends DAOException {
	/**
	 *
	 */
	private static final long serialVersionUID = -217164595149692618L;

	private int resultCode;
	private String resultMessage;

	public BIZEXCP_Folder(int resultCode, String resultMessage) {
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
	}  }
