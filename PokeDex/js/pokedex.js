window.onload=function(){
    bulba = JSON.parse(bulba);
    populateDex(bulba);

    document.getElementById("pokemonSubmit").onclick=getPokemon;
}
function getPokemon(){
    var id = document.getElementById('pokemonId').value;
    // AJAX - Asynchronous JavaScript and XML

    // Step 1: Create a new XML Http Request
    var xhttp = new XMLHttpRequest();
    // Step 2: Set a callback function to ReadyStateChange
    xhttp.onreadystatechange=parsePokemon;
    // Step 3: Open the request
    xhttp.open("GET", "https://pokeapi.co/api/v2/pokemon/"+id+"/");
    // Step 4: Send the request
    xhttp.send();


    //callback function should be in the same scope as the xhttp object
    function parsePokemon() {
        var pokeImg = document.getElementById("pokemonImg");

        /* Ready States
        0: UNSENT: Open() has not yet been called.
        1: OPENED: Open() has been called.
        2: HEADERS_RECEIVED: send() has been called. Headers of the
            response and the status are now available.
        3: LOADING: Downloading the response. responseText is now available
        4: DONE: Operation is complete
        */
       // We don't want to do anything until a success is achieved.
       if(xhttp.readyState===4 && xhttp.status===200) {
           let response = xhttp.responseText;
           response = JSON.parse(response);
           populateDex(response);
       } else {
           //Either loading or we have failed.
           pokeImg.setAttribute("src", "images/RotatingPokeball.gif");
           pokeImg.setAttribute("alt", "Loading...");
       }
    }
}
function populateDex(pokemon) {
    console.log(bulba);
    var image = document.getElementById("pokemonImg");
    var name=document.getElementById("pokemonName");
    var stats = document.getElementById("stats");

    stats.innerHTML='';
    image.src=pokemon.sprites.front_default;
    image.alt = pokemon.name;
    name.innerHTML=pokemon.name;

    populateStats(stats, pokemon.stats);
}

function populateStats(element, stats) {
    var table = document.createElement("table");

    var tableHeaders = document.createElement("tr");
    var tableData = document.createElement("tr");
    for(let i = stats.length -1; i>=0; i--) {
        let th = document.createElement("th");
        let td = document.createElement('td');

        th.innerHTML=stats[i].stat.name;
        tableHeaders.appendChild(th);
        td.innerHTML=stats[i].base_stat;
        tableData.appendChild(td);
    }
    table.appendChild(tableHeaders);
    table.appendChild(tableData);

    element.appendChild(table);
}