const listLength = document.getElementById("listLength").textContent;
const clearButton = document.getElementById("clearFilter");
clearButton.style.display = "none";

function showClearFilterBtn() {
    clearButton.style.display = "";
}

function filterName(elementId) {
    let filter, i;
    filter = document.getElementById(elementId).value.toLowerCase();
    for (i = 0; i < listLength; i++) {
        filterNameRow(i, filter)
    }
}
function filterNameRow(index, filter) {
    let appRow, td, name;
    appRow = document.getElementById("applicationRow" + index);
    td = appRow.getElementsByTagName("td");
    name = td[0].getElementsByTagName("a")[0].textContent;
    if (name.toLowerCase().indexOf(filter) > -1) {
        appRow.style.display = "";
    } else {
        appRow.style.display = "none";
    }
}

function filterDateFrom(elementId) {
    let filter, i;
    filter = document.getElementById(elementId).value;
    console.log(filter);
    console.log(listLength);
    for (i = 0; i < listLength; i++) {
        filterDateFromRow(i, filter)
    }
}
function filterDateFromRow(index, filter) {
    let appRow, td, dateFrom, i;
    let flag = false;
    appRow = document.getElementById("applicationRow" + index);
    td = appRow.getElementsByTagName("td");
    const availabilityPeriods = td[3].getElementsByClassName("availability").length;
    for (i = 0; i < availabilityPeriods; i++) {
        dateFrom = td[3].getElementsByClassName("availability")[i].textContent;

        if (new Date(dateFrom) >= new Date(filter)) {
            flag = true;
        }
    }

    if (flag) {
        appRow.style.display = "";
    } else {
        appRow.style.display = "none";
    }
}

function filterDateTo(elementId) {
    let filter, i;
    filter = document.getElementById(elementId).value;

    for (i = 0; i < listLength; i++) {
        filterDateToRow(i, filter)
    }
}
function filterDateToRow(index, filter) {
    let appRow, td, dateTo, i;
    let flag = false;
    appRow = document.getElementById("applicationRow" + index);
    td = appRow.getElementsByTagName("td");
    const availabilityPeriods = td[4].getElementsByClassName("availability").length;
    for (i = 0; i < availabilityPeriods; i++) {
        dateTo = td[3].getElementsByClassName("availability")[i].textContent;
        if (new Date(dateTo) <= new Date(filter)) {
            flag = true;
        }
    }

    if (flag) {
        appRow.style.display = "";
    } else {
        appRow.style.display = "none";
    }
}


function clearFilter() {
    let i, appRow;
    document.getElementById("dateTo").value = "";
    document.getElementById("dateFrom").value = "";
    document.getElementById("name").value = "";
    document.getElementById("competence").value = "all";
    for (i = 0; i < listLength; i++) {
        appRow = document.getElementById("applicationRow" + i);
        appRow.style.display = "";
    }
    clearButton.style.display = "none";
}