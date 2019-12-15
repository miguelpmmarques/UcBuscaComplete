package ucbusca.action;


import com.opensymphony.xwork2.ActionSupport;



import com.github.scribejava.core.builder.ServiceBuilder;

import com.github.scribejava.core.model.Token;

import com.github.scribejava.core.oauth.OAuthService;

import uc.sd.apis.FacebookApi2;

public class AssociateFacebookAction extends ActionSupport {
    private static final long serialVersionUID = 5590830L;
    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;
    private String facebookSession;

    public String getFacebookSession(){
        String apiKey = "3327462673961598";
        String apiSecret = "994cd6e13ace400e63e368cd349a98fd";

        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("https://raul.deus:8443/UcBusca/associateFacebookAuth.action") // Do not change this.//
                .scope("public_profile")
                .build();

        // Obtain the Authorization URL
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        return authorizationUrl;
    }

    @Override
    public String execute() {

        facebookSession = getFacebookSession();
        System.out.println("FB_SESSION==="+ facebookSession);
        return SUCCESS;

    }

}

