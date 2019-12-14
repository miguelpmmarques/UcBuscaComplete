if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}

function main(){
    var facebook_signin = document.getElementById("signin_facebook");

    facebook_signin.addEventListener("click", (e)=>{
        requestUrl(e);
    })

}

function receiveMessage(e, newWindow)
{
    newWindow.close();
}
function popitup(url,windowName) {
    newwindow=window.open(url,windowName,'height=200,width=150');
    if (window.focus) {newwindow.focus()}
    window.addEventListener("message", function(e){
        receiveMessage(e, newwindow);
    });
    return false;
}

function requestUrl(e){
    var urlParams = new URLSearchParams(window.location.search);
    let words="";
    if (urlParams.has("words")){
        words=urlParams.get("words");
    }
    console.log("words===", words);
    var aux = "";
    var split_words= words.split(" ");
    for (elem of split_words){
        console.log("elem===", elem);
        if (aux !==""){
            aux = aux + "+" +elem;
        }
        else{
            aux = aux+elem;
        }
    }
    console.log("aux==", aux);
    console.log("URL==", "fbShare.action?words="+aux );
    $.ajax({
        url: "fbShare.action?words="+aux,
        data: {},
        type: "POST",
        contentType: "application/json",
        success: function (data){
            console.log("SUCCESS==", data);
            popitup(data.facebookSession, "Facebook Login")
        },
        failure: (data)=>{
            console.log("FAILURE===", data);
        },
    });
}