const accountInfoDiv = document.getElementById("account-info"),
    personalInfoDiv = document.getElementById("personal-info");

document.getElementById("next").addEventListener("click", function next(event) {
    if (required()) {
        accountInfoDiv.style.display = "block";
        personalInfoDiv.style.display = "none";
        event.preventDefault();
    }
});

function required() {
    let firstname = document.getElementById("firstname").value;
    let lastname = document.getElementById("lastname").value;
    let ssn = document.getElementById("ssn").value;

    if (firstname === "" && lastname === "" && ssn === "") {
        alert("Please fill in the form");
        return false;
    } else if (firstname === "") {
        alert("Please fill in first name");
        return false;
    } else if (lastname === "") {
        alert("Please fill in last name");
        return false;
    } else if (ssn === "") {
        alert("Please fill in social security number");
        return false;
    } else {
        //alert("Continuing..");
        return true;
    }
}
