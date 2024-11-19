function table_init(dataToParse){
    window._table_data = JSON.parse(dataToParse);
    window._table_currentPage = 0;
    window._table_elementPerPage = 20;
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

 
    
    for(let i = window._table_elementPerPage*number; i < window._table_elementPerPage*(number+1); i++){

        if(i >= window._table_data.length){
            line = `<tr onclick='table_clearForm()'>`;
            for(let attr=0; attr<window._table_head.length; attr++){
                line += `<td style="color:transparent;">-</td>`;
            }

            line += "</tr>";
        }
        else{
            line = `<tr onclick='table_setForm(${window._table_data[i]["id"]})'>`;
            for(let attr=0; attr<window._table_head.length; attr++){
                line += `<td>${window._table_data[i][window._table_head[attr]]}</td>`;
            }

            line += "</tr>";
        }

        
        container.innerHTML += line;
    }

    document.getElementById("table-data-action-page").innerHTML = window._table_currentPage;
}

function table_nextPage(){
    table_setPage(window._table_currentPage+1);
}

function table_previousPage(){
    table_setPage(window._table_currentPage-1);
}

function table_firstPage(){
    table_setPage(0);
}

function table_lastPage(){
    table_setPage(Math.ceil(window._table_data.length / window._table_elementPerPage)-1);
}

function table_setForm(id){
    table_setFormMode(false);
    let inputs = document.getElementsByClassName("table-form-input");
    let data = null;


    for(let i=0; i<inputs.length; i++){
        if(window._table_data[i]["id"] === id){
            data = window._table_data[i];
            break;
        }
    }

    for(let i=0; i<inputs.length; i++){
        inputs[i].value = data[inputs[i].getAttribute("name")];
        if(inputs[i].onchange){
            inputs[i].onchange();
        }
    }
}

function table_clearForm(){
    let inputs = document.getElementsByClassName("table-form-input");

    for(let i=0; i<inputs.length; i++){
        
        if(inputs[i].tagName.localeCompare('SELECT') === 0){
            
            try{
                inputs[i].value = inputs[i].children[0].value;
            }
            catch(e){
                continue;
            }
        }
        else{
            
            inputs[i].value = ""

        }
    }
}

function table_setFormMode(isCreate){
    document.getElementById('table-create-form-actions').style = !isCreate ? "display:none;" : "";
    document.getElementById('table-edit-form-actions').style = isCreate ? "display:none;" : "";
    document.getElementById('table-form-method').value = isCreate ? 'POST' : 'PUT';
    document.getElementById('table-viewer-content-title').innerHTML = isCreate ? 'Nouveau' : 'Modifier';
    if(isCreate) table_clearForm();

}

function table_setSelectFieldData(LabelValueDict, fieldName){
    let inputs = document.getElementsByClassName("table-form-input");

    for(let i=0; i<inputs.length; i++){

        if(inputs[i].getAttribute("name").localeCompare(fieldName) === 0){
            for(let label in LabelValueDict){
                inputs[i].innerHTML += `<option value=${LabelValueDict[label]}> ${label} </option>`;
            }
        }
        console.log(window._table_data[id][inputs[i].getAttribute("name")])
        inputs[i].value = window._table_data[id][inputs[i].getAttribute("name")];
    }
}
