if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}
let username;
let socket;
function main() {
    connectToOnlineClients('wss:/' + window.location.host + '/UcBusca/wss');

}
function connectToOnlineClients(host) {
    if ('WebSocket' in window)
        socket = new WebSocket(host);
    else if ('MozWebSocket' in window)
        socket = new MozWebSocket(host);
    else {
        alert('Get a real browser which supports WebSocket.');
        return;
    }
    username = document.getElementById("username").innerHTML;
    socket.onopen    = onOpen;
    socket.onmessage = onMessage;
}

function onMessage(message) {
    console.log("REAL TIME ---> "+message.data);

    if (username=== message.data){
        alert("JUST GOT PROMOTED TO ADMIN IF YOU CHANGE PAGE YOU WI'LL HAVE THE ADMIN'S NAVBAR");
        $.ajax({
            type : "POST",
            url : "notifyAdmin",

            success : function(data) {
                console.log("ADMIN ADDED");
            },
            error : function(data) {
                alert("Some error occured.");
            }
        });
    }
}


function onOpen(event) {
    console.log('Connected to ' + window.location.host + '.');
}
