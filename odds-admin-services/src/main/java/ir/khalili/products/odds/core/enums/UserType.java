package ir.khalili.products.odds.core.enums;

public enum UserType {

	agent			("A"),
	superAdmin		("S"),
	customer		("C"),
	operator		("O");
	
	private String type;
	
	UserType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
