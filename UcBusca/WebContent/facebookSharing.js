if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}

function main(){
    var share_signin = document.getElementById("share_facebook");

    share_signin.addEventListener("click", (e)=>{
        request_Url(e);
    })

}

function receive_Message(e, newWindow)
{
    newWindow.close();
}
function pop_it_up(url,windowName) {
    newwindow=window.open(url,windowName,'height=200,width=150');
    if (window.focus) {newwindow.focus()}
    window.addEventListener("message", function(e){
        receive_Message(e, newwindow);
    });
    return false;
}

function request_Url(e){
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
            pop_it_up(data.facebookSession, "Facebook Login")
        },
        failure: (data)=>{
            console.log("FAILURE===", data);
        },
    });
}