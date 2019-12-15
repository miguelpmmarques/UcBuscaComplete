package RMISERVER;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String username="";
    private String password="";
    private String usernameFb="";
    private String passwordFb="";
    private ClientLibrary client;
    private boolean isAdmin = false;
    private boolean notify = false;
    private ArrayList<String> userHistory = new ArrayList<>();
    public User(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, ClientLibrary client) {
        this.username = username;
        this.password = password;
        this.client = (ClientLibrary)client;
    }
    public User(String usernameFb, String passwordFb, boolean isFaceebook,  ClientLibrary client) {
        if (isFaceebook){
            this.usernameFb = usernameFb;
            this.passwordFb = passwordFb;
            this.client = (ClientLibrary)client;
        }
    }

    public User(String usernameFb, String passwordFb, boolean isFaceebook) {
        if (isFaceebook){
            this.usernameFb = usernameFb;
            this.passwordFb = passwordFb;
        }
    }



    public ArrayList<String> getHistory(){
        return this.userHistory;
    }
    public String getUsername(){
        if(!this.username.equals("")){
            return this.username;
        }else{
            return this.usernameFb;
        }
    }
    public String getPassword(){
        if(!this.password.equals("")){
            return this.password;
        }else{
            return this.passwordFb;
        }
    }
    public ClientLibrary getClient(){
        return this.client;
    }
    public void setThis(ClientLibrary client){
        this.client = client;
    }
    public void addSearchToHistory(String words){
        userHistory.add(words);
    }
    public ArrayList<String> getSearchToHistory(){
        return userHistory;
    }
    public Boolean getIsAdmin(){
        return this.isAdmin;
    }
    public Boolean getNotify() { return this.notify; }
    public void setIsAdmin() { this.isAdmin = true;}
    public void setNotify(Boolean decision) { this.notify = decision;}

    public String getUsernameFb() {
        return usernameFb;
    }

    public void setUsernameFb(String usernameFb) {
        this.usernameFb = usernameFb;
    }

    public String getPasswordFb() {
        return passwordFb;
    }

    public void setPasswordFb(String passwordFb) {
        this.passwordFb = passwordFb;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", usernameFb='" + usernameFb + '\'' +
                ", passwordFb='" + passwordFb + '\'' +
                ", isAdmin=" + isAdmin +
                ", notify=" + notify +
                '}';
    }
}