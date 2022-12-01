package ir.khalili.products.odds.core.enums;

public enum HistoryEnum {

	UPDATE("U"),
	ASSIGN("A"),
	UNASSIGN("N"),
	RESULT("R"),
	DELETE("D");
	
	String symbol;
	
	HistoryEnum(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
