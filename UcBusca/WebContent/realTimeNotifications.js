if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}
let username;
function main() {

    var websocket = null;
    connectToOnlineClients('ws://' + window.location.host + '/UcBusca/ws');
}
function connectToOnlineClients(host) { // connect to the host websocket
    if ('WebSocket' in window)
        websocket = new WebSocket(host);
    else if ('MozWebSocket' in window)
        websocket = new MozWebSocket(host);
    else {
        alert('Get a real browser which supports WebSocket.');
        return;
    }
    username = document.getElementById("username").innerHTML;
    console.log("USER---- "+username)
    websocket.onopen    = onOpen;
    //websocket.onclose   = onClose;
    websocket.onmessage = onMessage;
}

function onMessage(message) {
    console.log(username);
    console.log(message.data)

    if (username=== message.data){
        alert("JUST GOT PROMOTED TO ADMIN, PLEASE RELOAD THE PAGE")
    }
}


function onOpen(event) {
    console.log('Connected to ' + window.location.host + '.');
}
