function table_init(dataToParse, links, linksData, NGValuesToLabel){
    window._table_data = JSON.parse(dataToParse);
    window._table_onDisplay = window._table_data;
    window._table_currentPage = 0;
    window._table_elementPerPage = 20;
    window._table_head = [];
    window._table_links = JSON.parse(links);
    window._table_linksData = JSON.parse(linksData);
    window._table_NGValuesToLabel = JSON.parse(NGValuesToLabel);


    let head = document.getElementById("table-data-head");

    for(let e=0; e<head.children.length; e++){

        window._table_head.push(head.children[e].getAttribute("name"));
    }

    let filters = document.querySelectorAll('[filter]');
    let name;
    let currrentSelect;

    for(let j=0; j<filters.length; j++){

        name = filters[j].getAttribute('filter');
        currrentSelect = document.querySelector('select[name="'+name+'"]');

        if( !currrentSelect ){
            continue;
        }

        for(let i=0; i<currrentSelect.children.length; i++){
            if(currrentSelect.children[i].value.localeCompare("") === 0) continue;
            filters[j].innerHTML += `<option value=${currrentSelect.children[i].value}>${currrentSelect.children[i].innerHTML}</option>`;
        }

    }
}

function table_onChangeSelectValue(trigger){
    let from = trigger.getAttribute('name');
    let to = "";
    let selectTo = null;
    let values = null;
    let valueOk = false;

    if(!window._table_links[from]){
        return;
    }

    to = window._table_links[from];
    selectTo = document.querySelector('select[name="'+to+'"]');
    
    values = window._table_linksData[from+"-"+to][trigger.value];
    let bvl;
    for(let i=1; i<selectTo.children.length; i++){
    
        bvl = isNaN(selectTo.children[i].value) ? selectTo.children[i].value : parseInt(selectTo.children[i].value);
        
        if(values === undefined || values.includes(bvl)){
            selectTo.children[i].hidden = false;
            valueOk = selectTo.value === selectTo.children[i].value || valueOk;
            console.log(selectTo.value, valueOk, (isNaN(selectTo.children[i].value) ? selectTo.children[i].value : parseInt(selectTo.children[i].value)))
        }
        else{
            selectTo.children[i].hidden = true;



        }
    }
    selectTo.value = valueOk ? selectTo.value : selectTo.children[0].value;
    if(selectTo.onchange){
        selectTo.onchange();
    }
}

function table_filter(){
    let buffer;
    let currrentFilter;
    let ok = true;
    let filters = document.querySelectorAll('[filter]');
    
    window._table_onDisplay = [];

    for(let i=0; i<window._table_data.length; i++){
        ok = true;

        for(let j=0; j<filters.length; j++){

            currrentFilter = filters[j];
            buffer = window._table_data[ i ] [ currrentFilter.getAttribute('filter') ];


            if( currrentFilter.value.localeCompare("") !== 0 && currrentFilter.value.localeCompare(buffer) !== 0){
                ok = false;
                break;
            }

        }

        if(ok){
            window._table_onDisplay.push(window._table_data[ i ]);
        }
    }

    table_setPage(0);
}


function table_setPage(number){

    window._table_currentPage = number;
    
    let container = document.getElementById("table-data-body");
    let line = "";

   
    if( number >= (window._table_onDisplay.length / window._table_elementPerPage) || number < 0){
        if(window._table_onDisplay.length !== 0){
            return;
        }
    }
    
    container.innerHTML = "";

    for(let i = window._table_elementPerPage*number; i < window._table_elementPerPage*(number+1); i++){

        if(i >= window._table_onDisplay.length){
            line = `<tr onclick='table_clearForm()'>`;
            for(let attr=0; attr<window._table_head.length; attr++){
                line += `<td style="color:transparent;">-</td>`;
            }

            line += "</tr>";
        }
        else{
            if(isNaN(window._table_onDisplay[i]["id"])){
                line = `<tr onclick='table_setForm("${window._table_onDisplay[i]["id"]}")'>`;
            }
            else{
                line = `<tr onclick='table_setForm(${window._table_onDisplay[i]["id"]})'>`;
            }
            for(let attr=0; attr<window._table_head.length; attr++){

                if(window._table_head[attr] in window._table_NGValuesToLabel && window._table_onDisplay[i][window._table_head[attr]] != null){
                    line += `<td>${window._table_NGValuesToLabel[window._table_head[attr]][window._table_onDisplay[i][window._table_head[attr]]]}</td>`;
                }
                else{
                    line += `<td>${window._table_onDisplay[i][window._table_head[attr]]}</td>`;
                }
            }

            line += "</tr>";
        }

        
        container.innerHTML += line;
    }

    document.getElementById("table-data-action-page").innerHTML = window._table_currentPage;
    document.getElementById("table-content-number").innerHTML = "("+window._table_onDisplay.length+") Donnée(s)";
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
    table_setPage(Math.ceil(window._table_onDisplay.length / window._table_elementPerPage)-1);
}

function table_setForm(id){
    table_setFormMode(false);
    let inputs = document.getElementsByClassName("table-form-input");
    let data = null;


    for(let i=0; i<window._table_data.length; i++){
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

function table_setFormMode(isCreate, clear=true){
    document.getElementById('table-create-form-actions').style = !isCreate ? "display:none;" : "";
    document.getElementById('table-edit-form-actions').style = isCreate ? "display:none;" : "";
    document.getElementById('table-form-method').value = isCreate ? 'POST' : 'PUT';
    document.getElementById('table-viewer-content-title').innerHTML = isCreate ? 'Nouveau' : 'Modifier';
    if(clear) table_clearForm();

}

function table_setSelectFieldData(LabelValueDict, fieldName){
    let inputs = document.getElementsByClassName("table-form-input");

    for(let i=0; i<inputs.length; i++){

        if(inputs[i].getAttribute("name").localeCompare(fieldName) === 0){
            for(let label in LabelValueDict){
                inputs[i].innerHTML += `<option value=${LabelValueDict[label]}> ${label} </option>`;
            }
        }

        inputs[i].value = window._table_data[id][inputs[i].getAttribute("name")];
    }
}
