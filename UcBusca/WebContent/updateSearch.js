if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}

function main() {
    const search = document.getElementById("myForm");
    console.log(search);

    $("form").submit(function(e) {
        e.preventDefault();
        console.log(document.getElementById("searchfield").value);
        $.ajax({
            url: "search.action",
            type: "POST",
            data: "SearchModel.seachWords="+document.getElementById("searchfield").value,
            success: function() {
                $('#searches').load(' #searches > *');
            },
            error : function() {
                alert("Some error occured.");
            }
        });
    });
    /*for (var i = 0; i < admin_user.length; i++) {

        admin_user[i].addEventListener("click", function(e) {
            e.preventDefault();
            addAdmin(this.id.split(" -> ")[0]);

        })

    }*/
}