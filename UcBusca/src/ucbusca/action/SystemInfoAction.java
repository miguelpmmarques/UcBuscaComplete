
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import ucbusca.model.SystemInfoModel;

import java.util.Map;

public class SystemInfoAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() throws Exception {
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
}
