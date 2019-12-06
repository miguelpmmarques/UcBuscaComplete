if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}
function main() {


    yandexTranslation();

    $("form").submit(function(e) {
        e.preventDefault();
        console.log(document.getElementById("searchfield").value);
        $.ajax({
            url: "search.action",
            type: "POST",
            data: "SearchModel.seachWords="+document.getElementById("searchfield").value,
            success: function() {
                //window.location.href="";



                $('#searches').load(' #searches > *');
                $('#searchanswer').load(' #searchanswer > *');
                $('#description').load(' #description > *');
                $('#linkurl').load(' #linkurl > *');
                $('#language').load(' #language > *');
                yandexTranslation();




            },
            error : function() {
                alert("Some error occured.");
            }
        });
    });
}
function yandexTranslation() {
    const descriptions = document.getElementsByClassName("description");
    const language = document.getElementsByClassName("language");

    for (var l = 0; l < descriptions.length; l++) {
        const i = l;
        var language_origin = '';
        var text = descriptions[i].innerHTML;


        $.ajax({
            url: "https://translate.yandex.net/api/v1.5/tr/detect?key=trnsl.1.1.20191206T010200Z.d1b35297d5cc4f1e.7b554c751eab0701225c480a7b5c93da8b6b7b75&text="+text,
            type: "GET",
            success: function(data) {
                language_origin = data.firstChild.attributes.lang.value;
                console.log(language_origin);
                console.log(text);
                $.ajax({
                    url: "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20191206T010200Z.d1b35297d5cc4f1e.7b554c751eab0701225c480a7b5c93da8b6b7b75&text="+text+"&lang="+language_origin+"-pt",
                    type: "GET",
                    success: function(data) {
                        console.log(data);
                        language[i].innerHTML ="[Original Website Language: "+language_origin.toUpperCase()+"]";
                        descriptions[i].innerHTML = data.firstChild.textContent;
                    },
                    error : function() {
                        language[i].innerHTML ="[Impossible To Traslate]";
                    }
                });
            },
            error : function() {
                language[i].innerHTML ="[Impossible To Traslate]";
            }
        });

    }
}