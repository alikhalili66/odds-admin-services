package ir.khalili.products.odds.core.excp.validation;

/**
 * @author A.KH
 */
public class EXCP_RtMgr_Validation extends Exception{

	private static final 	long 						serialVersionUID = 5690299203890181601L;
	
	private int resultCode;
	private String resultMessage;

	public EXCP_RtMgr_Validation(int resultCode, String resultMessage) {
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
