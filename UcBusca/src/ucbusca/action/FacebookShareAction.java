package ucbusca.action;

import RMISERVER.ServerLibrary;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;
import java.util.Properties;

import com.github.scribejava.core.model.Token;

import uc.sd.apis.FacebookApi2;

public class FacebookShareAction extends ActionSupport {
    private String words;
    private String facebookSession;

    public String getFacebookSession(){
        String apiKey = "3327462673961598";
        String apiSecret = "994cd6e13ace400e63e368cd349a98fd";
        this.words = getWords();
        String new_words = this.words.replace(" ", "+");
        String link = "https://raul.deus:8443/UcBusca/search.action?words="+ new_words;
        return "https://www.facebook.com/dialog/feed?app_id="+apiKey+"&display=popup&href="+"https%3A%2F%2Fraul.deus%3A8443%2F&link="+link+"&redirect_uri=https://raul.deus:8443/UcBusca/closePopUp.action";
    }

    @Override
    public String execute(){

        facebookSession = getFacebookSession();
        System.out.println("FB_SESSION==="+ facebookSession);
        return SUCCESS;
    }

    public String getWords(){
        return this.words;
    }
    public void setWords(String words){
        this.words = words;
    }


}
