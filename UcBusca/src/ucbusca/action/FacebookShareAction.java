package ucbusca.action;

import RMISERVER.ServerLibrary;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;
import java.util.Properties;

import com.github.scribejava.core.model.Token;

import uc.sd.apis.FacebookApi2;

public class FacebookShareAction extends ActionSupport {

    private String facebookSession;

    public String getFacebookSession(){
        String apiKey = "3327462673961598";
        String apiSecret = "994cd6e13ace400e63e368cd349a98fd";
        return "https://www.facebook.com/dialog/feed?app_id="+apiKey+"&display=popup&href="+"https%3A%2F%2Flocalhost%3A8443%2F&link=https%3A%2F%2Ffacebook.com/ustudyapp/?eid=ARAXsuh29D9MdjnRn67L78hXg2BphnL7PMIHVq8ijfo-AjokFQe1DDFYSnVxX7EhUNXJhPHQZDF89_pO%2F&redirect_uri=https://localhost:8443/UcBusca/closePopUp.action";

    }

    @Override
    public String execute(){

        facebookSession = getFacebookSession();
        System.out.println("FB_SESSION==="+ facebookSession);
        return SUCCESS;
    }


}
