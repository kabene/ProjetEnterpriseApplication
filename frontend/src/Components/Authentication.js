let page = document.querySelector("#page");

//mettre dans deux pages différentes ? -> impossible de rendre la page seponsive avec deux formulaires

const Authentication = () => {
    page.innerHTML = `
        <form>
            <div class="row mx-0 mt-5">
                <div class="col-4 px-0 py-4 ml-5 border border-dark m-auto">
                    <div class="form-group">
                        <label for="username_login" class="ml-5">Pseudo</label>
                        <input class="form-control w-75 m-auto" id="username_login" type="text" name="username_login" placeholder="Entrez votre pseudo"/>
                    </div>
                    <div class="form-group">
                        <label for="password_login" class="ml-5">Mot de passe</label>
                        <input class="form-control w-75 m-auto" id="password_login" type="password" name="password_login" placeholder="Entrez votre mot de passe"/>
                    </div>
                    <button class="btn btn-primary w-35 m-5" id="button_login" type="submit">Se connecter</button>
                </div>
                <div class="col-1" id="authentication-line"></div>
                <div class="col-1"></div>
                <div class="col-4 px-0 py-4 px-5 mr-5 border border-dark m-auto">
                    <div class="form-group">
                        <label for="username_signup" class="ml-5">Pseudo</label>
                        <input class="form-control w-75 m-auto" id="username_signup" type="text" name="username_signup" placeholder="Entrez votre pseudo"/>
                    </div>
                    <div class="form-group">
                        <label for="name_signup" class="ml-5">Nom</label>
                        <input class="form-control w-75 m-auto" id="name_signup" type="text" name="name_signup" placeholder="Entrez votre nom"/>
                    </div>
                    <div class="form-group">
                        <label for="forname_signup" class="ml-5">Prenom</label>
                        <input class="form-control w-75 m-auto" id="forname_signup" type="text" name="forname_signup" placeholder="Entrez votre prénom"/>
                    </div>
                    <div class="form-group">
                        <label for="email_signup" class="ml-5">Email</label>
                        <input class="form-control w-75 m-auto" id="email_signup" type="text" name="email_signup" placeholder="Entrez votre email"/>
                    </div>
                    <div class="form-group">
                        <label for="password_signup" class="ml-5">Mot de passe</label>
                        <input class="form-control w-75 m-auto" id="password_signup" type="password" name="password_signup" placeholder="Entrez votre mot de passe"/>
                    </div>
                    <p class="mb-2 ml-5">Adresse</p>
                    <div class="form-group">
                        <input class="form-control w-75 m-auto" id="street_signup" type="text" name="street_signup" placeholder="Entrez votre rue"/>
                        
                    </div>
                    <div class="form-group">
                        <input class="form-control input-signup my-2" id="num_signup" type="text" name="num_signup" placeholder="numero"/>
                        <input class="form-control input-signup my-2" id="boite_signup" type="text" name="boite_signup" placeholder="boite"/>
                        <input class="form-control input-signup my-2" id="postal_signup" type="text" name="postal_signup" placeholder="code postal"/>
                        <input class="form-control input-signup my-2" id="commune_signup" type="text" name="commune_signup" placeholder="commune"/>
                        <input class="form-control input-signup my-2" id="municipality_signup" type="text" name="municipality_signup" placeholder="pays"/>
                    </div>
                    <select class="selectpicker" id="selectpicker_role">
                        <option>Client</option>
                        <option>Anticaire</option>
                        <option>Administrateur</option>  
                    </select>
                    <button class="btn btn-primary w-35 ml-5 mt-4" id="button_signup" type="submit">S'inscrire</button>
                </div>
            </div>
        </form>
    
    `;
}

export default Authentication;