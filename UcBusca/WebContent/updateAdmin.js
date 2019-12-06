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
}
function addAdmin(myid) {
    console.log(myid.split(" -> ")[0]);
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