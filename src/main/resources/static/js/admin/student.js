function initSelect(JSON_level, JSON_class){
    window.level = JSON.parse(JSON_level);
    window.studentclass = JSON.parse(JSON_class);

    let levelsSelect = document.getElementsByName('level')[0];
    levelsSelect.innerHTML = "";
    levelsSelect.setAttribute("onchange", "onSelectLevel(this);");
    levelsSelect.setAttribute("type", "number");

    let studentclassSelect = document.getElementsByName('studentclass')[0];
    studentclassSelect.innerHTML = "";
    studentclassSelect.setAttribute("onchange", "onSelectStudentclass(this);");
    levelsSelect.setAttribute("type", "number");

    levelsSelect.innerHTML += `<option value=0>Aucune</option>`;
    for(let i=0; i<window.level.length; i++){
        levelsSelect.innerHTML += `<option value=${window.level[i]["id"]}>${window.level[i]["name"]}</option>`;
    }

    levelsSelect.value = levelsSelect.children[0].value;
    onSelectLevel(levelsSelect);
}

function onSelectLevel(select){
    let studentclassSelect = document.getElementsByName('studentclass')[0];
    studentclassSelect.innerHTML = "";

    for(let i=0; i<window.studentclass.length; i++){
        if(window.studentclass[i]["program"] === parseInt(select.value)){
            studentclassSelect.innerHTML += `<option value=${window.studentclass[i]["id"]}>${window.studentclass[i]["title"]}</option>`;
        }
    }
}

function onSelectStudentclass(select){
    let levelsSelect = document.getElementsByName('level')[0];

    for(let i=0; i<window.studentclass.length; i++){
        if(window.studentclass[i]["id"] === parseInt(select.value)){
            levelsSelect.value = window.studentclass[i]["program"];
            return;
        }
    }
}