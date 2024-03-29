if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}
function main() {


    yandexTranslation();

    $("form").submit(function(e) {
        console.log(document.getElementById("searchfield").value);
        /*$.ajax({
            url: "search.action",
            type: "POST",
            data: "SearchModel.seachWords="+document.getElementById("searchfield").value,
            success: function() {
                window.location.href="";
                /*$('#searches').load(' #searches > *');
                $('#searchanswer').load(' #searchanswer > *');
                $('#description').load(' #description > *');
                $('#linkurl').load(' #linkurl > *');
                $('#language').load(' #language > *');
                yandexTranslation();
            },
            error : function() {
                alert("Some error occured.");
            }
        });*/
    });
}
function yandexTranslation() {
    const descriptions = document.getElementsByClassName("description");
    const titles = document.getElementsByClassName("title");
    const language = document.getElementsByClassName("language");

    for (let l = 0; l < descriptions.length; l++) {
        const i = l;
        let language_origin = '';
        const text = descriptions[i].innerHTML.trim();
        const title = titles[i].innerHTML;
        console.log(text);
        if (text === "NO DESCRIPTION AVAILABE") {
            console.log("NAO TRADUZIU");
            language[i].innerHTML ="[Impossible To Traslate]";
        }else{
            console.log("TRADUZIU");
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
                            console.log(data.firstChild.textContent);
                            language[i].innerHTML ="[Original Website Language: "+language_origin.toUpperCase()+"]";
                            descriptions[i].innerHTML = data.firstChild.textContent;
                        },
                        error : function() {
                            language[i].innerHTML ="[Impossible To Traslate]";
                        }
                    });
                    $.ajax({
                        url: "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20191206T010200Z.d1b35297d5cc4f1e.7b554c751eab0701225c480a7b5c93da8b6b7b75&text="+title+"&lang="+language_origin+"-pt",
                        type: "GET",
                        success: function(data) {
                            titles[i].innerHTML = data.firstChild.textContent;
                        },
                        error : function() {
                            console.log("Can't translate title")
                        }
                    });
                },
                error : function() {
                    language[i].innerHTML ="[Impossible To Traslate]";
                }
            });
        }





    }
}