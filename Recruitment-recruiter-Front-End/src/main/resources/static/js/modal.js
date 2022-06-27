// Get the modal
var modal = document.getElementById("myModal");
var printNewState = document.getElementById("printNewState");
var setNewState = document.getElementById("newState");
var span = document.getElementsByClassName("close")[0];

function confirmChange(newState) {
    printNewState.innerHTML = newState;
    setNewState.value = newState;
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}