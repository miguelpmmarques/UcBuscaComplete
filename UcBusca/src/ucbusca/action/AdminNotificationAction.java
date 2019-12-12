
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;


public class AdminNotificationAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() {

		String username = (String) session.remove("username");
		session.remove("loggedin");
		session.remove("admin");

		session.put("username", username);
		session.put("loggedin", true);
		session.put("admin", true);
		return SUCCESS;
	}


	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map<String, Object> getSession() {
		return this.session;
	}
}
