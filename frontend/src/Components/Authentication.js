import {escapeHtml, displayErrorMessage} from "../utils/utils.js"
import {
  getUserSessionData,
  setUserSessionData,
  setUserLocalData
} from "../utils/session";
import Navbar from "./Navbar";
import {setLayout} from "../utils/render.js"
import {RedirectUrl} from "./Router";

let loginForm;
let registerForm;
let errorDiv;

let pageHTML = `
<div>
  <div class="col-5 mx-auto">
    <div id="errorDiv" class="d-none"></div>
  </div>
  <div class="row mx-0 mt-5">

    <!-- Login form -->

    <div class="col-0 col-lg-1"></div>

    <div class="col-10 col-lg-4 p-3 mx-auto mx-lg-0 mb-5 mb-lg-0 border border-1">
      <h2 class="text-center">Connexion</h2>
      <form id="loginForm">
        <div class="form-group w-75 mx-auto">
          <label for="usernameLogin" class="mt-2">Pseudo</label>
          <input class="form-control" id="usernameLogin" type="text" name="usernameLogin" placeholder="Entrez votre pseudo" required/>

          <label for="passwordLogin" class="mt-2">Mot de passe</label>
          <input class="form-control" id="passwordLogin" type="password" name="passwordLogin" placeholder="Entrez votre mot de passe" required/>

          <div class="invalid-feedback">Les champs marqués par * sont obligatoires.</div>

          <label for="rememberMe" class="mt-5 ml-3 form-check-label">
            <input class="form-check-input" id="rememberMe" type="checkbox" name="rememberMe"/> Se
            souvenir de moi
          </label>
        </div>
        <button class="btn btn-primary w-35 m-5" id="loginButton" type="submit">Se connecter
        </button>
      </form>
    </div>
  
    <div class="col-0 col-lg-2"></div>

    <!-- Register form -->

    <div class="col-lg-4 col-10 p-3 mx-auto mx-lg-0 border border-1">
      <h2 class="text-center">Inscription</h2>
      <form id="registerForm">
        <div class="form-group p-5 mx-auto">
          <div class="row">
            <div class="col-0 col-xl-1"></div>

            <div class="col-12 col-xl-4">
              <label for="usernameRegister">Pseudo*</label>
              <input class="form-control" id="usernameRegister" type="text" name="usernameRegister" placeholder="Entrez votre pseudo" required/>
              
              <label for="lastnameRegister">Nom*</label>
              <input class="form-control" id="lastnameRegister" type="text" name="lastnameRegister" placeholder="Entrez votre nom" required/>
              
              <label for="firstnameRegister">Prenom*</label>
              <input class="form-control" id="firstnameRegister" type="text" name="firstnameRegister" placeholder="Entrez votre prénom" required/>
              
              <label for="emailRegister">Email*</label>
              <input class="form-control" id="emailRegister" type="text" name="emailRegister" placeholder="Entrez votre email" required/>
              
              <label for="passwordRegister">Mot de passe*</label>
              <input class="form-control" id="passwordRegister" type="password" name="passwordRegister" placeholder="Entrez votre mot de passe" required/>
            </div>

            <div class="col-0 col-xl-2"></div>

            <!-- Adresse -->

            <div class="col-12 col-xl-4 mt-5 mt-xl-0">

              <label for="streetRegister">Rue*</label>
              <input class="form-control" id="streetRegister" type="text" name="streetRegister" placeholder="Entrez votre rue" required/>
              
              <label for="numRegister">Numero*</label>
              <input class="form-control" id="numRegister" type="text" name="numRegister" placeholder="numero" required/>
              
              <label for="boxRegister">Boite</label>
              <input class="form-control" id="boxRegister" type="text" name="boxRegister" placeholder="boite"/>
              
              <label for="postalRegister">Code postal*</label>
              <input class="form-control" id="postalRegister" type="text" name="postalRegister" placeholder="code postal" required/>
              
              <label for="communeRegister">Commune*</label>
              <input class="form-control" id="communeRegister" type="text" name="communeRegister" placeholder="commune" required/>
              
              <label for="countryRegister">Pays*</label>
              <input class="form-control" id="countryRegister" type="text" name="countryRegister" placeholder="pays" required/>
              <div class="invalid-feedback">Les champs marqués par * sont obligatoires.</div>
            </div>

            <div class="col-0 col-xl-1"></div>
          </div>
        </div>
        <select class="selectpicker" id="role">
          <option selected="selected" value="customer">Client</option>
          <option value="antique_dealer">Anticaire</option>
          <option value="admin">Administrateur</option>
        </select>
        <button class="btn btn-primary w-35 ml-5 mt-4" id="registerButton" type="submit">S'inscrire
        </button>
      </form>
    </div>
  </div>
</div>
    `;

const Authentication = () => {
  let page = document.querySelector("#page");
  page.innerHTML = pageHTML;
  loginForm = document.querySelector("#loginForm");
  registerForm = document.querySelector("#registerForm");
  errorDiv = document.querySelector("#errorDiv");

  let loginButton = document.querySelector("#loginButton");
  loginButton.addEventListener("click", onLogin);

  let registerButton = document.querySelector("#registerButton");
  registerButton.addEventListener("click", onSignUp);

  const user = getUserSessionData();
  if (user) {
    Navbar();
    RedirectUrl("/");
  }
}

const validateLogin = (username, password) => {
  if (username === "" || password === "") {
    return false;
  }
  return true;
}

const validateRegister = () => {
  let res = true;
  if(document.querySelector("#usernameRegister").value === "") res = false;
  else if(document.querySelector("#lastnameRegister").value === "") res = false;
  else if(document.querySelector("#firstnameRegister").value === "") res = false;
  else if(document.querySelector("#emailRegister").value === "") res = false;
  else if(document.querySelector("#passwordRegister").value === "") res = false;
  else if(document.querySelector("#role").value === "") res = false;
  else if(document.querySelector("#streetRegister").value === "") res = false;
  else if(document.querySelector("#numRegister").value === "") res = false;
  else if(document.querySelector("#postalRegister").value === "") res = false;
  else if(document.querySelector("#communeRegister").value === "") res = false;
  else if(document.querySelector("#countryRegister").value === "") res = false;
  return res;
}

const onLogin = (e) => {
  e.preventDefault();
  console.log("on login");

  registerForm.className = "";
  loginForm.className = "was-validated";
  errorDiv.className = "d-none";

  let username = document.querySelector("#usernameLogin").value;
  let password = document.querySelector("#passwordLogin").value;
  let rememberMe = document.querySelector("#rememberMe").checked;
  console.log("remember me: ", rememberMe);
  if (validateLogin(username, password) === false) {
    return;
  }
  let user = {
    username: username,
    password: password,
    rememberMe: rememberMe,
  }
  fetch("/users/login", {
    method: "POST",
    body: JSON.stringify(user),
    headers: {
      "Content-Type": "application/json",
    }
  })
  .then((response) => {
    if (!response.ok) {
      if (response.status === 403) {
        throw new Error("Pseudo ou mot de passe incorrect");
      } else {
        throw new Error(
            response.status + " : " + response.statusText);
      }
    }
    return response.json();
  })
  .then((data) => onUserLogin(data, rememberMe))
  .catch((err) => {
    console.log("Erreur de fetch !! :\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

const onUserLogin = (data, rememberMe) => {
  console.log("Logged in !!\n", data)
  const user = {...data, isAutenticated: true};
  setUserSessionData(user);
  if (rememberMe === true) {
    setUserLocalData(data.token);
  }
  setLayout();
  RedirectUrl("/");
}

const onSignUp = (e) => {
  e.preventDefault();
  console.log("on sign up");

  loginForm.className = "";
  registerForm.className = "was-validated";
  errorDiv.className = "d-none";

  if(!validateRegister()) 
    return;

  let user = {
    username: document.querySelector("#usernameRegister").value,
    lastName: document.querySelector("#lastnameRegister").value,
    firstName: document.querySelector("#firstnameRegister").value,
    email: document.querySelector("#emailRegister").value,
    password: document.querySelector("#passwordRegister").value,
    role: document.querySelector("#role").value,
    address: {
      street: document.querySelector("#streetRegister").value,
      buildingNumber: document.querySelector("#numRegister").value,
      unitNumber: document.querySelector("#boxRegister").value,
      postcode: document.querySelector("#postalRegister").value,
      commune: document.querySelector("#communeRegister").value,
      country: document.querySelector("#countryRegister").value,
    }
  }

  fetch("/users/register", {
    method: "POST",
    body: JSON.stringify(user),
    headers: {
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Error code : " + response.status + " : " + response.statusText)}
    return response.json();

  }).then((data) => {
    if (user.role =="customer") {
      onUserRegistration(data);
    } else {
      displayErrorMessage("errorDiv",new Error("Inscription en attente: vous devez être validé avant de pouvoir vous connecter"));
    }
  })
  .catch((err) => {
    console.log("Erreur de fetch !! :\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}
const onUserRegistration = (userData) => {
  const user = {...userData, isAutenticated: true};
  setUserSessionData(user);
  setLayout();
  RedirectUrl("/")
}

export default Authentication;