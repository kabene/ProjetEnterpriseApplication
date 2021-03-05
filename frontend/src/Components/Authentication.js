import {escapeHtml} from "../utils/utils.js"

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
                    <button class="btn btn-primary w-35 m-5" id="loginButton" type="submit">Se connecter</button>
                </div>
                <div class="col-1" id="authenticationLine"></div>
                <div class="col-1"></div>
                <div class="col-4 px-0 py-4 px-5 mr-5 border border-dark m-auto">
                    <div class="form-group">
                        <label for="usernameSignup" class="ml-5">Pseudo</label>
                        <input class="form-control w-75 m-auto" id="usernameSignup" type="text" name="usernameSignup" placeholder="Entrez votre pseudo"/>
                    </div>
                    <div class="form-group">
                        <label for="nameSignup" class="ml-5">Nom</label>
                        <input class="form-control w-75 m-auto" id="nameSignup" type="text" name="nameSignup" placeholder="Entrez votre nom"/>
                    </div>
                    <div class="form-group">
                        <label for="fornameSignup" class="ml-5">Prenom</label>
                        <input class="form-control w-75 m-auto" id="fornameSignup" type="text" name="fornameSignup" placeholder="Entrez votre prénom"/>
                    </div>
                    <div class="form-group">
                        <label for="emailSignup" class="ml-5">Email</label>
                        <input class="form-control w-75 m-auto" id="emailSignup" type="text" name="emailSignup" placeholder="Entrez votre email"/>
                    </div>
                    <div class="form-group">
                        <label for="passwordSignup" class="ml-5">Mot de passe</label>
                        <input class="form-control w-75 m-auto" id="passwordSignup" type="password" name="passwordSignup" placeholder="Entrez votre mot de passe"/>
                    </div>
                    <p class="mb-2 ml-5">Adresse</p>
                    <div class="form-group">
                        <input class="form-control w-75 m-auto" id="streetSignup" type="text" name="streetSignup" placeholder="Entrez votre rue"/>
                        
                    </div>
                    <div class="form-group">
                        <input class="form-control inputSignup my-2" id="numSignup" type="text" name="numSignup" placeholder="numero"/>
                        <input class="form-control inputSignup my-2" id="boiteSignup" type="text" name="boiteSignup" placeholder="boite"/>
                        <input class="form-control inputSignup my-2" id="postalSignup" type="text" name="postalSignup" placeholder="code postal"/>
                        <input class="form-control inputSignup my-2" id="communeSignup" type="text" name="communeSignup" placeholder="commune"/>
                        <input class="form-control inputSignup my-2" id="municipalitySignup" type="text" name="municipalitySignup" placeholder="pays"/>
                    </div>
                    <select class="selectpicker">
                        <option>Client</option>
                        <option>Anticaire</option>
                        <option>Administrateur</option>  
                    </select>
                    <button class="btn btn-primary w-35 ml-5 mt-4" id="signupButton" type="submit">S'inscrire</button>
                </div>
            </div>
        </form>
    `;

const Authentication = () => {
    let page = document.querySelector("#page");
    page.innerHTML = pageHTML;

    let loginButton = document.querySelector("#loginButton");
    loginButton.addEventListener("click", onLogin);

    let signupButton = document.querySelector("#signupButton");
    signupButton.addEventListener("click", onSignUp);
}

const onLogin = (e) => {
    e.preventDefault();
    console.log("on login");
    let username = escapeHtml(document.querySelector("#usernameLogin").value);
    let password = escapeHtml(document.querySelector("#passwordLogin").value);
    if (username === "" || password === "")
        return ;
    let user = {
        username: username,
        password: password
    }
    fetch("/api/users/login", {
        method: "POST",
        body: JSON.stringify(user),
        headers: {
            "Content-Type": "application/json",
        }
    })
    .then((response) => {
      if (!response.ok)
        throw new Error( "Error code : " + response.status + " : " + response.statusText);
      return response.json();
    })
    .then((data) => console.log("Logged in !!\n" + data))
    .catch((err) => console.log("Erreur de fetch !! :´<\n" + err));
}

const onSignUp = (e) => {
    e.preventDefault();
    console.log("on sign up");
}

export default Authentication;