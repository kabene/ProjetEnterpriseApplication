
import {escapeHtml} from "../utils/utils.js"
import {getUserSessionData, setUserSessionData, setUserLocalData} from "../utils/session";
import Navbar from "./Navbar";
import {setLayout}from "../utils/render.js"
import {RedirectUrl} from "./Router";


let pageHTML = `
        <form>
            <div class="row mx-0 mt-5">
                <div class="col-4 px-0 py-4 ml-5 border border-dark m-auto">
                    <div class="form-group">
                        <label for="usernameLogin" class="ml-5">Pseudo</label>
                        <input class="form-control w-75 m-auto" id="usernameLogin" type="text" name="usernameLogin" placeholder="Entrez votre pseudo"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordLogin" class="ml-5">Mot de passe</label>
                        <input class="form-control w-75 m-auto" id="passwordLogin" type="password" name="passwordLogin" placeholder="Entrez votre mot de passe"/>
                    </div>
                    <div class="form-group">
                      <label for="rememberMe" class="ml-5 form-check-label">
                        <input class="form-check-input" id="rememberMe" type="checkbox" name="rememberMe"/> Se souvenir de moi
                      </label>
                    </div>
                    <button class="btn btn-primary w-35 m-5" id="loginButton" type="submit">Se connecter</button>
                </div>
                <div class="col-1" id="authenticationLine"></div>
                <div class="col-1"></div>
                <div class="col-4 px-0 py-4 px-5 mr-5 border border-dark m-auto">
                    <div class="form-group">
                        <label for="usernameRegister" class="ml-5">Pseudo</label>
                        <input class="form-control w-75 m-auto" id="usernameRegister" type="text" name="usernameRegister" placeholder="Entrez votre pseudo"/>
                    </div>
                    <div class="form-group">
                        <label for="nameRegister" class="ml-5">Nom</label>
                        <input class="form-control w-75 m-auto" id="nameRegister" type="text" name="nameRegister" placeholder="Entrez votre nom"/>
                    </div>
                    <div class="form-group">
                        <label for="fornameRegister" class="ml-5">Prenom</label>
                        <input class="form-control w-75 m-auto" id="fornameRegister" type="text" name="fornameRegister" placeholder="Entrez votre prénom"/>
                    </div>
                    <div class="form-group">
                        <label for="emailRegister" class="ml-5">Email</label>
                        <input class="form-control w-75 m-auto" id="emailRegister" type="text" name="emailRegister" placeholder="Entrez votre email"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordRegister" class="ml-5">Mot de passe</label>
                        <input class="form-control w-75 m-auto" id="passwordRegister" type="password" name="passwordRegister" placeholder="Entrez votre mot de passe"/>
                    </div>
                    <p class="mb-2 ml-5">Adresse</p>
                    <div class="form-group">
                        <input class="form-control w-75 m-auto" id="streetRegister" type="text" name="streetRegister" placeholder="Entrez votre rue"/>
                        
                    </div>
                    <div class="form-group">
                        <input class="form-control inputRegister my-2" id="numRegister" type="text" name="numRegister" placeholder="numero"/>
                        <input class="form-control inputRegister my-2" id="boxRegister" type="text" name="boxRegister" placeholder="boite"/>
                        <input class="form-control inputRegister my-2" id="postalRegister" type="text" name="postalRegister" placeholder="code postal"/>
                        <input class="form-control inputRegister my-2" id="communeRegister" type="text" name="communeRegister" placeholder="commune"/>
                        <input class="form-control inputRegister my-2" id="countryRegister" type="text" name="countryRegister" placeholder="pays"/>
                    </div>
                    <select class="selectpicker" id="role">
                        <option selected="selected"  value="customer">Client</option>
                        <option value="antique_dealer">Anticaire</option>
                        <option value="admin">Administrateur </option>  
                    </select>
                    <button class="btn btn-primary w-35 ml-5 mt-4" id="registerButton" type="submit">S'inscrire</button>
                </div>
            </div>
        </form>
    `;

const Authentication = () => {
  let page = document.querySelector("#page");
  page.innerHTML = pageHTML;

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

const onLogin = (e) => {
  e.preventDefault();
  console.log("on login");
  let username = document.querySelector("#usernameLogin").value;
  let password = document.querySelector("#passwordLogin").value;
  let rememberMe = document.querySelector("#rememberMe").checked;
  console.log("remember me: ", rememberMe);
  if (username === "" || password === "") {
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
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  })
  .then((data) => onUserLogin(data, rememberMe))
  .catch((err) => console.log("Erreur de fetch !! :´<\n" + err));
}

const onUserLogin = (data, rememberMe) => {
  console.log("Logged in !!\n", data)
  const user = {...data, isAutenticated: true};
  setUserSessionData(user);
  if(rememberMe === true) {
    setUserLocalData(data.token);
  }
  setLayout();
  RedirectUrl("/");
}

const onSignUp = (e) => {
  e.preventDefault();
  console.log("on sign up");
  let user = {
    username: document.querySelector("#usernameRegister").value,
    lastName: document.querySelector("#nameRegister").value,
    firstName: document.querySelector("#fornameRegister").value,
    email: document.querySelector("#emailRegister").value,
    password: document.querySelector("#passwordRegister").value,
    role:document.querySelector("#role").value,
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
    method: "POST", // *GET, POST, PUT, DELETE, etc.
    body: JSON.stringify(user), // body data type must match "Content-Type" header
    headers: {
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();

  }).then((data) => onUserRegistration(data))
    .catch((err) => console.log("Erreur de fetch !! :\n" + err));
}
const onUserRegistration = (userData) => {
  console.log("onUserRegistration", userData);
  const user = {...userData, isAutenticated: true};
  setUserSessionData(user);
  setLayout();
  RedirectUrl("/");
}

export default Authentication;