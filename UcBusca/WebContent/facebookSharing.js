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
    $.ajax({
        url: "fbShare",
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