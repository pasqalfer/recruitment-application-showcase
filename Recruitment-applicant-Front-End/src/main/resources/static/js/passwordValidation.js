const password = document.getElementById("password"),
    confirmPassword = document.getElementById("passwordVerify"),
    passwordMatch = document.getElementById("passwordMatch"),
    form = document.getElementById("formWithPassword");

password.addEventListener('input', function (e){
    passwordMatch.style.color = "white";
    if (e.target.value !== confirmPassword.value && confirmPassword.value !== "") {
        passwordMatch.innerHTML = "Password doesn't match";
    } else
        passwordMatch.innerHTML = "";
});

confirmPassword.addEventListener('input', function (e){
    passwordMatch.style.color = "white";
    if (e.target.value !== password.value && password.value !== "") {
        passwordMatch.innerHTML = "Password doesn't match";
    } else
        passwordMatch.innerHTML = "";
});

form.addEventListener('submit', function(e){
    if(passwordMatch.innerHTML === "Password doesn't match"){
        e.preventDefault();
        passwordMatch.style.color = "red";
    }
});