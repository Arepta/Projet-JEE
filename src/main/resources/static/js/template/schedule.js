function schedule_getStartOfWeek(date = new Date()) {
    // Get the current day of the week (0 - Sunday, 1 - Monday, etc.)
    const day = date.getDay()-1;
    
    // Set the date to the previous Sunday (or today if it's Sunday)
    const startOfWeek = new Date(date);
    startOfWeek.setDate(date.getDate() - day);
    
    // Set the time to midnight (00:00:00) to get the start of the day
    startOfWeek.setHours(0, 0, 0, 0);
    return startOfWeek;
}

function schedule_init(dataToParse, links, linksData, NGValuesToLabel){
    window._schedule_data = JSON.parse(dataToParse);
    window._schedule_onDisplay = window._schedule_data;
    window._schedule_currentWeek = schedule_getStartOfWeek();
    console.log(window._schedule_currentWeek);  

    window._schedule_links = JSON.parse(links);
    window._schedule_linksData = JSON.parse(linksData);
    window._schedule_NGValuesToLabel = JSON.parse(NGValuesToLabel);

    schedule_changeWeek(0);
}

function schedule_onChangeSelectValue(trigger){
    let from = trigger.getAttribute('name');
    let to = "";
    let selectTo = null;
    let values = null;
    let valueOk = false;

    if(!window._schedule_links[from]){
        return;
    }

    to = window._schedule_links[from];
    selectTo = document.querySelector('select[name="'+to+'"]');
    
    values = window._schedule_linksData[from+"-"+to][trigger.value];
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

function schedule_setUpFilter(){
    let forClass = document.getElementById("schedule-data-filter-class");
    let forteacher = document.getElementById("schedule-data-filter-teacher");
    if(document.getElementById("schedule-data-filter-manager").value === "0"){
        forClass.hidden = false;
        forteacher.hidden = true;
        forClass.onchange();
    }
    else{
        forClass.hidden = true;
        forteacher.hidden = false;
        forteacher.onchange();
    }
}

function schedule_filter(trigger){
    let buffer;
    let filter = trigger.getAttribute('filter');
    
    window._schedule_onDisplay = [];

    for(let i=0; i<window._schedule_data.length; i++){

        buffer = window._schedule_data[ i ] [ filter ];

        if( trigger.value.localeCompare("") !== 0 && trigger.value.localeCompare(buffer) !== 0){
            continue;
        }

        window._schedule_onDisplay.push(window._schedule_data[i]);
    }

    schedule_setPage();
}

function schedule_setPage(){
    
    let container = document.getElementById("schedule-data-body");
    let line = "";
    let startBuffer = null;
    let endBuffer = null;
    let dateLimit =  new Date(window._schedule_currentWeek);
    dateLimit.setDate(dateLimit.getDate() + 7);
    let dataBuffer = null;
    
    container.innerHTML = "";

    for(let i = 0; i < window._schedule_onDisplay.length; i++){

        dataBuffer = window._schedule_onDisplay[i]

        startBuffer = new Date(dataBuffer["start"]);
        endBuffer = new Date(dataBuffer["end"]);
        
        if(startBuffer < window._schedule_currentWeek ){
            continue;
        }

        if(startBuffer >= dateLimit){
            break;
        }

        line = `<div class="schedule-data-course"  
                style="grid-column: ${startBuffer.getDate()-window._schedule_currentWeek.getDate()+1}; grid-row: ${ startBuffer.getHours()-8+1}/${ endBuffer.getHours()-8+1};
                    margin-top: ${startBuffer.getMinutes()}px;
                    margin-bottom: ${endBuffer.getMinutes()}px;
                    " 
                onclick='table_setForm(${window._schedule_onDisplay[i]["id"]})'
            >
            <p>${startBuffer.getHours()}h${startBuffer.getMinutes() < 10 ? '0'+startBuffer.getMinutes() : startBuffer.getMinutes()} - ${endBuffer.getHours()}h${endBuffer.getMinutes() < 10 ? '0'+endBuffer.getMinutes() : endBuffer.getMinutes()} ${window._schedule_NGValuesToLabel['room'][dataBuffer['room']]}</p>
            <p>${window._schedule_NGValuesToLabel['class'][dataBuffer['classes']]} </p>
            <p> ${window._schedule_NGValuesToLabel['course'][dataBuffer['course']]}</p>
            <p>${window._schedule_NGValuesToLabel['teacher'][dataBuffer['teacher']]}</p>
        </div>`;
        
        container.innerHTML += line;
    }

    for(let i = 1; i <= 7; i++){
        for(let j = 1; j <= 12; j++){
            container.innerHTML += `<div class="schedule-data-course-void" style="grid-column: ${i}; grid-row: ${j};"> </div>`;
        }
    }

}

function schedule_changeWeek(howMany){

    window._schedule_currentWeek.setDate(window._schedule_currentWeek.getDate() + 7*howMany);
    let dateBuffer = new Date(window._schedule_currentWeek);
    console.log(dateBuffer);
    let container = document.getElementsByClassName("schedule-data-day");
    for(let i=0; i<container.length; i++){

        dateBuffer.setDate(dateBuffer.getDate() + 1);
        console.log(dateBuffer);
        container[i].children[1].innerHTML = `${dateBuffer.getDate()}/${dateBuffer.getMonth() + 1}`;
    }

    schedule_setPage()
}
