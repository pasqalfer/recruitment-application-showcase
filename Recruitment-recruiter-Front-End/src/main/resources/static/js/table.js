let state = {options:{page:0, size:10,competence:'all'}}
//const host = "http://localhost:8000"
const host = "https://recruiter-app-recruiter.herokuapp.com"

function observable(state,onStateChange){
    const p = new Proxy(state, {
        set: (target, prop, value, receiver) => {
            target[prop] = value;
            onStateChange(target)
        },

        get : (target, p, receiver) => {
            return target[p]
        }
    });

    return p;
}

let param = (name,val,separator = '') => `${name}=${val}${separator}`

function loadTable(tbl,{size=10, page, competence}){
    state.options.page = page
    state.options.competence = competence
    document.getElementById("page").value = page;

    let url = host + "/components/table?" + param("size",size,"&") + param("page",page,"&") + param("competence",competence)

    $(tbl).load(encodeURI(url))
}

function setName(name) {
    table.options = {...table.options, name:name}
}

function setComp(comp){

    table.options = {...table.options, competence:comp}
}

function setSize(size){
    table.options = {...table.options, size:size}
}

function setDateTo(dateTo){
    table.options = {...table.options, dateTo:dateTo}
}

function setDateFrom(dateFrom){
    table.options = {...table.options, dateFrom:dateFrom}
}

function setPage(page){
    table.options = {...table.options, page:page}
}

function prevPage(){
    const page = state.options.page
    if(page > 0){
        table.options = {...table.options, page: parseInt(page) - 1}
    }
}

function nextPage(){
    const page = state.options.page
    if(page >= 0){
        table.options = {...table.options, page: parseInt(page) + 1}
    }
}

const table = observable({}, (t) => loadTable("#table-container", t.options))

table.options = state.options