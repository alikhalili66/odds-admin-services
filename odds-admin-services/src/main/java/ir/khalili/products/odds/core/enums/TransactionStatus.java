package ir.khalili.products.odds.core.enums;

public enum TransactionStatus {

	pending("P"),
	reject("R"),
	confirm("C");
	
	private String status;
	
	TransactionStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
