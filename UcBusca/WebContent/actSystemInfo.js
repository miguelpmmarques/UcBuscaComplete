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
        //window.location.href = '';
        $('#reloadStuff').fadeOut('fast').load(' #reloadStuff > *').fadeIn("fast");
    }
}

