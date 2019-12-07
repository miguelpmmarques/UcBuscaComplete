package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.ServerLibrary;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

public class ClosePopUpAction  extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
