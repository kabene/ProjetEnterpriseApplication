import {baseUrl, displayErrorMessage, gdpr} from "../utils/utils.js"
import {getUserSessionData, setUserSessionData, setUserLocalData,} from "../utils/session";
import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {fetchMe, getSignal} from "../utils/utils.js";

let page = document.querySelector("#page");

let loginForm;
let registerForm;
let errorDiv;


const Authentication = () => {
  if (getUserSessionData()) {
    Navbar();
    RedirectUrl("/");
  }

  page.innerHTML = generatePage();

  loginForm = document.querySelector("#loginForm");
  registerForm = document.querySelector("#registerForm");
  errorDiv = document.querySelector("#errorDiv");
  
  document.querySelector("#loginButton").addEventListener("click", onLogin);
  document.querySelector("#registerButton").addEventListener("click", onSignUp);

  const user = getUserSessionData();

  gdpr(page);
  
  if (user) {
    Navbar();
    RedirectUrl("/");
  }
}


/********************  Business methods  **********************/


/**
 * try log the user in.
 * @param {*} e the event.
 */
const onLogin = async (e) => {
  e.preventDefault();

  registerForm.className = "";
  loginForm.className = "was-validated";
  errorDiv.className = "d-none";

  let user = getUserFromLoginForm();
  
  if (user.username === "" || user.password === "")
    return;

  let data = await login(user);

  onUserLogin(data);
}


/**
 * create a user from the information in the login form. 
 * @returns a user based on the login form.
 */
const getUserFromLoginForm = () => {
  let user = {
    username: document.querySelector("#usernameLogin").value,
    password: document.querySelector("#passwordLogin").value,
    rememberMe: document.querySelector("#rememberMe").checked,
  }
  return user;
}


/**
 * set the session for the user, and redirect him.
 * @param {*} data the data of the user.
 * @param {*} rememberMe, if the user wants to stay connected or not.
 */
const onUserLogin = async (data, rememberMe) => {
  if (!data) return;
  let user = await fetchMe(data.token);
  const bundle = {...data, isAutenticated: true, isAdmin: user.role === "admin"};
  setUserSessionData(bundle);
  if (rememberMe)
    setUserLocalData(data.token);
  Navbar();
  RedirectUrl("/");
}


/**
 * try to signup the user.
 * @param {*} e the event
 */
const onSignUp = async (e) => {
  e.preventDefault();

  loginForm.className = "";
  registerForm.className = "was-validated";
  errorDiv.className = "d-none";

  if(!validateRegister()) 
    return;

  let user = getUserFromSignUpForm();

  let data = await signUp(user);

  if (user.role =="customer")
    onUserRegistration(data);
  else
    displayErrorMessage("errorDiv",new Error("Inscription en attente: vous devez être validé avant de pouvoir vous connecter"));
}


/**
 * check if all the form in signUp form is valid.
 * @returns true if the user is valide else false.
 */
const validateRegister = () => {
  if(document.querySelector("#usernameRegister").value === "") return false;
  if(document.querySelector("#lastnameRegister").value === "") return false;
  if(document.querySelector("#firstnameRegister").value === "") return false;
  if(document.querySelector("#emailRegister").value === "") return false;
  if(document.querySelector("#passwordRegister").value === "") return false;
  if(document.querySelector("#role").value === "") return false;
  if(document.querySelector("#streetRegister").value === "") return false;
  if(document.querySelector("#numRegister").value === "") return false;
  if(document.querySelector("#postalRegister").value === "") return false;
  if(document.querySelector("#communeRegister").value === "") return false;
  if(document.querySelector("#countryRegister").value === "") return false;
  return true;
}


/**
 * create a user from the information in the signUp form. 
 * @returns a user based on the signUp form.
 */
 const getUserFromSignUpForm = () => {
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
  return user;
}


/**
 * redirect the user and set his session.
 * @param {*} userData the data of the user.
 */
const onUserRegistration = (userData) => {
  if (!userData) return;
  const user = {...userData, isAutenticated: true};
  setUserSessionData(user);
  Navbar();
  RedirectUrl("/");
}


/********************  HTML generation  **********************/

const generatePage = () => {
  return `
  <div>
    <div class="col-5 mx-auto">
      <div id="errorDiv" class="d-none"></div>
    </div>
    <div class="row mx-0 mt-5">
  
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
          <button class="btn btn-primary w-35 ml-5 mt-4" id="registerButton" type="submit">S'inscrire</button>
        </form>
      </div>
    </div>
  </div>
      `;
}


/********************  Backend fetch  **********************/

/**
 * ask the backend to log the user in.
 * @param {*} user the user to login
 * @returns the user in json form.
 */
const login = async (user) => {
  let signal = getSignal();

  let response = await fetch(baseUrl+"/users/login", {
    signal,
    method: "POST",
    body: JSON.stringify(user),
    headers: {
      "Content-Type": "application/json",
    }
  });
  if (!response.ok) {
    let err;
    if (response.status === 403) 
      err = ("Pseudo ou mot de passe incorrect");
    else
      err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", new Error(err));
    return;
  }
  return response.json();
}

/**
 * ask the backend to sign the user up.
 * @param {*} user the user to sign up.
 * @returns the user in json form
 */
const signUp = async (user) => {
  let signal = getSignal();

  let response = await fetch(baseUrl+"/users/register", {
    signal,
    method: "POST",
    body: JSON.stringify(user),
    headers: {
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err); 
    displayErrorMessage("errorDiv", new Error(err));
    return;
  }
  return response.json();
}

export default Authentication;