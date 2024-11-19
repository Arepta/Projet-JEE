function table_init(dataToParse){
    window._table_data = JSON.parse(dataToParse);
    window._table_onDisplay = window._table_data;
    window._table_currentPage = 0;
    window._table_elementPerPage = 20;
    window._table_head = [];

    let head = document.getElementById("table-data-head");

    for(let e=0; e<head.children.length; e++){

        window._table_head.push(head.children[e].getAttribute("name"));
    }
}

function table_initFilters(filters, filterValueToLabel){
    window._table_filters = JSON.parse(filters);
    window._table_filterValueToLabel = JSON.parse(filterValueToLabel);
    if(window._table_filterValueToLabel === undefined){
        window._table_filterValueToLabel = {};
    }

    if( window._table_filters ){
        table_setFilter();
    }
    
}

function table_setFilter(){
    let filtersValues = {};
    let buffer;
    let currrentFilter;

    for(let j=0; j<window._table_filters.length; j++){
        filtersValues[window._table_filters[j]] = [];
        document.querySelector('[filter="'+window._table_filters[j]+'"]').innerHTML = `<option value="" selected></option>`;
    }
    

    for(let i=0; i<window._table_data.length; i++){
        for(let j=0; j<window._table_filters.length; j++){
            currrentFilter = window._table_filters[j];
            buffer = window._table_data[ i ] [ currrentFilter ];

            if( buffer != null && !filtersValues[currrentFilter].includes( buffer )){
                filtersValues[currrentFilter].push( buffer )

                if(currrentFilter in window._table_filterValueToLabel && buffer in  window._table_filterValueToLabel[currrentFilter]){
                    document.querySelector('[filter="'+currrentFilter+'"]').innerHTML += `<option value=${ buffer }>${window._table_filterValueToLabel[currrentFilter][buffer]}</option>`;
                }
                else{
                    document.querySelector('[filter="'+currrentFilter+'"]').innerHTML += `<option value=${ buffer }>${buffer}</option>`;
                }

                
            }
        }
    }
    
}

function table_filter(){
    let buffer;
    let currrentFilter;
    let ok = true;
    
    window._table_onDisplay = [];

    for(let i=0; i<window._table_data.length; i++){
        ok = true;

        for(let j=0; j<window._table_filters.length; j++){

            currrentFilter = document.querySelector('[filter="'+window._table_filters[j]+'"]');
            buffer = window._table_data[ i ] [ window._table_filters[j] ];


            if( currrentFilter.value.localeCompare("") !== 0 && currrentFilter.value.localeCompare(buffer) !== 0){
                ok = false;
                break;
            }

        }

        if(ok){
            

            window._table_onDisplay.push(window._table_data[ i ]);
        }
    }
    console.log(window._table_onDisplay);

    table_setPage(0);
}


function table_setPage(number){

    window._table_currentPage = number;

    let container = document.getElementById("table-data-body");
    let line = "";

    container.innerHTML = "";

    if( number >= (window._table_onDisplay.length / window._table_elementPerPage) || number < 0){
        if(window._table_onDisplay.length !== 0){
            return;
        }
    }
    
    for(let i = window._table_elementPerPage*number; i < window._table_elementPerPage*(number+1); i++){

        if(i >= window._table_onDisplay.length){
            line = `<tr onclick='table_clearForm()'>`;
            for(let attr=0; attr<window._table_head.length; attr++){
                line += `<td style="color:transparent;">-</td>`;
            }

            line += "</tr>";
        }
        else{
            line = `<tr onclick='table_setForm(${window._table_onDisplay[i]["id"]})'>`;
            for(let attr=0; attr<window._table_head.length; attr++){
                line += `<td>${window._table_onDisplay[i][window._table_head[attr]]}</td>`;
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
    table_setPage(Math.ceil(window._table_onDisplay.length / window._table_elementPerPage)-1);
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

        inputs[i].value = window._table_data[id][inputs[i].getAttribute("name")];
    }
}
