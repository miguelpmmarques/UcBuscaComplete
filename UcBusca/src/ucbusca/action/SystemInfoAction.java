
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import ucbusca.model.SystemInfoModel;

import java.util.Map;

/**
 * Action to show the System Statistics View, add the System info bean to the session
 */
public class SystemInfoAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	/**
	 * @return GET METHOD
	 * @throws Exception
	 */
	@Override
	public String execute() throws Exception {
		if (!session.containsKey("admin") || !(boolean) session.get("admin")){
			return "notAdmin";
		}
		return SUCCESS;
	}
	public SystemInfoModel getSystemInfoModel() {
		if(!session.containsKey("systemSession"))
			this.setSystemInfoModel(new SystemInfoModel(session));
		return (SystemInfoModel) session.get("systemSession");
	}

	public void setSystemInfoModel(SystemInfoModel systemsession) {
		this.session.put("systemSession", systemsession);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Map<String, Object> getSession() {
		return this.session;
	}

}
