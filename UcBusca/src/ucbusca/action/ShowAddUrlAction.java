
package ucbusca.action;

import RMISERVER.ServerLibrary;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;


import java.util.Map;
import java.util.Properties;


/**
 * Action that redirects the user to the ADD URL view
 */
public class ShowAddUrlAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;
    private String url;
    private ServerLibrary ucBusca;
    private Properties prop = new Properties();


    /**
     * GET METHOD
     * @return Success or notAdmin in case the user dont have permission to acess the page
     */
    @Override
    public String execute() {
        if (!session.containsKey("admin") || !(boolean) session.get("admin")){
            return "notAdmin";
        }
        return SUCCESS;
    }

    /**
     * @param session Set the Session
     */
    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * @return Returns the session
     */
    public Map<String, Object> getSession() {
        return this.session;
    }

}
