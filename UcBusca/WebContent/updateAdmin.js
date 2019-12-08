if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}

function main() {
    const admin_user = document.getElementsByClassName("users");
    for (var i = 0; i < admin_user.length; i++) {
        admin_user[i].addEventListener("click", function(e) {
            e.preventDefault();
            addAdmin(this.id.split(" -> ")[0]);

        })
    }
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
    console.log("LOGOU AO WS "+websocket);
}

function addAdmin(myid) {
    websocket.send(myid.split(" -> ")[0]);
    $.ajax({
        type : "POST",
        url : "addAdmin",
        data : "username=" + myid.split(" -> ")[0],
        success : function(data) {
            $('#hotreload').fadeOut('fast').load(' #hotreload > *').fadeIn("fast");
        },
        error : function(data) {
            alert("Some error occured.");
        }
    });
}