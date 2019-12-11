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
    connectToOnlineClients('wss:/' + window.location.host + '/UcBusca/wss');
    socket.onopen    = onOpen;
    socket.onmessage = onMessage;

}

function onMessage(message) {
    console.log(username);
    console.log(message.data);

    if (username=== message.data){
        alert("JUST GOT PROMOTED TO ADMIN, PLEASE RELOAD THE PAGE");
    }
}

function addAdmin(myid) {
    socket.send(myid.split(" -> ")[0]);
    $.ajax({
        type : "POST",
        url : "addAdmin",
        data : "username=" + myid.split(" -> ")[0],
        success : function(data) {
            $('#hotreload').fadeOut('fast').load(' #hotreload > *').fadeIn("fast");
            $('#listUsers').load(' #listUsers > *', function(responseText, textStatus, XMLHttpRequest) {
                const admin_user = document.getElementsByClassName("users");
                for (var i = 0; i < admin_user.length; i++) {
                    console.log("BOTAO CRL")
                    admin_user[i].addEventListener("click", function(e) {
                        e.preventDefault();
                        console.log("CLICOUUUUUU!!")
                        addAdmin(this.id.split(" -> ")[0]);

                    })
                }
            });

        },
        error : function(data) {
            alert("Some error occured.");
        }
    });
}