
package ucbusca.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import ucbusca.model.SearchModel;

/**
 * Action to show the SearchREsults View, add the results bean to the session
 */
public class SearchAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	private String words;

	/**
	 * @return GET METHOD
	 * @throws Exception
	 */
	@Override
	public String execute() {
		this.words = getWords();
		return SUCCESS;
	}

	public SearchModel getSearchModel() {

		this.setSearchModel(new SearchModel(session, this.words));

		return (SearchModel) session.get("searchSession");
	}

	public String getWords(){
		System.out.println("WORDS---------------------------------->"+this.words);
		return this.words;
	}
	public void setWords(String words){
		System.out.println("SET WORDS---------------------------------->"+words);
		this.words = words;
	}

	public void setSearchModel(SearchModel searchsession) {
		this.session.put("searchSession", searchsession);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		System.out.println("SESSIONE ===" + session.get("searchSession"));
		this.session = session;
	}
}
