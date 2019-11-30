
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import ucbusca.model.SearchModel;

public class SearchAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() throws Exception {
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		return SUCCESS;
	}

	public SearchModel getSearchModel() {
		if(!session.containsKey("searchSession"))
			this.setSearchModel(new SearchModel());
		return (SearchModel) session.get("searchSession");
	}

	public void setSearchModel(SearchModel searchsession) {
		this.session.put("searchSession", searchsession);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
