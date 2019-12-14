
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import ucbusca.model.HistoryModel;

import java.util.Map;

/**
 * Action to show the History View, add the history bean to the session
 */
public class HistoryAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	/**
	 * @return GET METHOD
	 * @throws Exception
	 */
	@Override
	public String execute() {
		return SUCCESS;
	}

	public HistoryModel getHistoryModel() {
		if(!session.containsKey("historySession"))
			this.setHistoryModel(new HistoryModel(session));
		return (HistoryModel) session.get("historySession");
	}

	public void setHistoryModel(HistoryModel historysession) {
		this.session.put("historySession", historysession);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
