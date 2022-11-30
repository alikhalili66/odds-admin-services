package ir.khalili.products.odds.core.enums;

public enum HistoryEnum {

	UPDATE("U"),
	DELETE("D");
	
	String symbol;
	
	HistoryEnum(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
