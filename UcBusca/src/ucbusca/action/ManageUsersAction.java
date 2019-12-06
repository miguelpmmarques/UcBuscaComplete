
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import ucbusca.model.ManageUsersModel;

import java.util.Map;

public class ManageUsersAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	public ManageUsersModel getManageUsersModel() {
		if(!session.containsKey("manageusersSession"))
			this.setManageUsersModel(new ManageUsersModel(session));
		return (ManageUsersModel) session.get("manageusersSession");
	}

	public void setManageUsersModel(ManageUsersModel manageuserssession) {
		this.session.put("manageusersSession", manageuserssession);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
