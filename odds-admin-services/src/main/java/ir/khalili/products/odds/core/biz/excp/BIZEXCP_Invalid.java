package ir.khalili.products.odds.core.biz.excp;

import ir.khalili.products.odds.core.dao.excp.DAOException;

public class BIZEXCP_Invalid extends DAOException {

    private static final long serialVersionUID = 9141432548554161363L;

    public BIZEXCP_Invalid() {
        super();
    }

    public BIZEXCP_Invalid(Throwable e, String daoMessage) {
        super(e, daoMessage);
    }

    public BIZEXCP_Invalid(String resMessage) {
        super(resMessage);
    }

}
