package ir.khalili.products.odds.core.enums;

public enum ServiceType {

	NON_ATTENDANCE_AUTHENTICATION		(3l, "احراز هویت غیرحضوری"),
	MOBILE_BANK_ACTIVATION				(4l,"فعال سازی موبایل بانک"),
	INTERNET_BANK_ACTIVATION			(5l, "فعال سازی اینترنت بانک"),
	CHANGE_CUSTOMER_MOBILE				(6l, "تغییر مشخصات ارتباطی مشتری-تلفن همراه"),
	CHANGE_CUSTOMER_PHONE				(7l, "تغییر مشخصات ارتباطی مشتری-تلفن ثابت"),
	CHANGE_CUSTOMER_ADDRESS				(8l, "تغییر مشخصات ارتباطی مشتری-آدری پستی"),
	CHANGE_CUSTOMER_EMAIL				(9l, "تغییر مشخصات ارتباطی مشتری-پست الکترونیک"),
	NON_ATTENDANCE_OPENINIG_ACCOUNT		(10l, "افتتاح حساب غیرحضوری");
	
	private Long serviceId;
	private String persianTitle;
	
	ServiceType(Long serviceId, String persianTitle) {
		this.serviceId=serviceId;
		this.persianTitle=persianTitle;
	}

	public String getPersianTitle() {
		return persianTitle;
	}
	
	public Long getServiceId() {
		return serviceId;
	}
}
