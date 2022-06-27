function showOptions() {
    var acc = document.getElementById("optionAccepted");
    var rej = document.getElementById("optionRejected");
    if (acc.style.display === "inline-block") {
        acc.style.display = "none";
    } else {
        acc.style.display = "inline-block";
    }

    if (rej.style.display === "inline-block") {
        rej.style.display = "none";
    } else {
        rej.style.display = "inline-block";
    }
}