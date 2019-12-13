if (document.readyState === "complete" ||
    (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
} else {
    document.addEventListener("DOMContentLoaded", main);
}

function main(){
    var buttons = document.getElementsByClassName("h_button");
    for (let elem of buttons){
        elem.onclick= function(){
            var words = elem.getAttribute("name").split("-")[0];
            var splitwords= words.trim().split(" ");
            console.log("hshshs===", splitwords);
            let aux="";
            for (let word of splitwords){
                aux = aux+"+"+word;
            }
            window.location.href="search.action?words="+aux;
        }
    }
}