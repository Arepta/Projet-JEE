function table_init(dataToParse){
    window._table_data = JSON.parse(dataToParse);
    window._table_currentPage = 0;
    window._table_elementPerPage = 25;
    window._table_head = [];

    let head = document.getElementById("table-data-head");

    for(let e=0; e<head.children.length; e++){

        window._table_head.push(head.children[e].getAttribute("name"));
    }
}

function table_setPage(number){
    
    if( number >= (window._table_data.length / window._table_elementPerPage) || number < 0){
        return;
    }

    window._table_currentPage = number;

    let container = document.getElementById("table-data-body");
    let line = "";

    container.innerHTML = "";

 
    
    for(let i = window._table_elementPerPage*number; i < window._table_elementPerPage*(number+1) && i < window._table_data.length; i++){

        line = `<tr onclick='table_setForm(${i+1})'>`;
        for(let attr=0; attr<window._table_head.length; attr++){
            line += `<td>${window._table_data[i][window._table_head[attr]]}</td>`;
        }

        line += "</tr>";
        container.innerHTML += line;
    }
}

function table_setForm(id){
    let inputs = document.getElementsByClassName("table-edit-form-input");

    for(let i=0; i<inputs.length; i++){
        inputs[i].value = window._table_data[id-1][inputs[i].getAttribute("name")];
    }
}

function table_setSelectFieldData(LabelValueDict, fieldName){
    let inputs = document.getElementsByClassName("table-edit-form-input");

    for(let i=0; i<inputs.length; i++){

        if(inputs[i].getAttribute("name").localeCompare(fieldName)){
            for(let label in LabelValueDict){
                inputs[i].innerHTML += `<option value=${LabelValueDict[label]}> ${label} </option>`;
            }
        }
        console.log(window._table_data[id][inputs[i].getAttribute("name")])
        inputs[i].value = window._table_data[id][inputs[i].getAttribute("name")];
    }
}