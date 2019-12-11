if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}
function main() {
    let host = 'wss:/' + window.location.host + '/UcBusca/wss';
    console.log("ESTA AQUI FDS")
    if ('WebSocket' in window)
        socket = new WebSocket(host);
    else if ('MozWebSocket' in window)
        socket = new MozWebSocket(host);
    else {
        alert('Get a real browser which supports WebSocket.');
        return;
    }
    console.log("WELELELELEL");
    socket.onopen    = onOpenSystem;
    socket.onmessage = onMessageSystem;
    console.log(1)
}
function onOpenSystem(event) {
    console.log('Connected to ' + window.location.host + '.');
}

function onMessageSystem(message) {
    console.log("----------->"+message.data);

    if ("CHANGED"=== message.data){
        console.log("BATEU CARALHOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        window.location.href = '';
        $('#reloadStuff').load('reloadStuff');
        /*
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
        });*/
    }
}

