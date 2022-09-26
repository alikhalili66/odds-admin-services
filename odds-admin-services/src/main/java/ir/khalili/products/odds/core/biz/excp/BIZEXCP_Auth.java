package ir.khalili.products.odds.core.biz.excp;

import ir.khalili.products.odds.core.dao.excp.DAOException;

public class BIZEXCP_Auth extends DAOException {

    private static final long serialVersionUID = 9141432548554161363L;

    public BIZEXCP_Auth() {
        super();
    }

    public BIZEXCP_Auth(Throwable e, String daoMessage) {
        super(e, daoMessage);
    }

    public BIZEXCP_Auth(String resMessage) {
        super(resMessage);
    }

    public BIZEXCP_Auth(int resCode, String resMessage) {
        super(resCode, resMessage);
    }
    
}
