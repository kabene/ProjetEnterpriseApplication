import logo from "../img/logoAE_v2.png";

let navBar = document.querySelector("#navbar");

const Navbar = () => {
    let navbarHtml;
    navbarHtml = `
        <nav class="navbar navbar-expand-md navbar-light py-0"> 
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <ul class="navbar-nav"> 
                <li class="nav-item mt-1 mb-1"><img id="logo-navbar" src="` + logo + `" alt="logo" href="#" data-uri="/"/><span id="store-name" href="#" data-uri="/">LI VI SATCHO</span></li>
            </ul>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto"> 
        `;
                
    //if quidam
    /*navbarHtml += `
                    <li class="nav-item"><button type="button" class="btn btn-primary navbar-button" id="voirMeubleButton" href="#" data-uri="/meubles">Voir les meubles</button>
                    <button type="button" class="btn btn-primary navbar-button" href="#" data-uri="/auth">S'identifier</button></li>
    `;
    */
    //if client
    navbarHtml += `                 
                    <li class="nav-item"><button type="button" class="btn btn-secondary navbar-button p-0" href="#" data-uri="/visites">Mes demandes de visite</button>
                    <button type="button" class="btn btn-secondary navbar-button" href="#" data-uri="/demanderVisite">Demander une visite</button>
                    <button type="button" class="btn btn-primary navbar-button" href="#" data-uri="/meubles">Voir les meubles</button>
                    <button type="button" class="btn btn-secondary navbar-button pl-2" href="#" data-uri="/logout">Deconnexion</button></li>
                    <li class="my-auto font-weight-bold">utilisateurs.pseudo</li>
    `;
    
    //if admin
    /*navbarHtml += `                 
                    <li class="nav-item"><button type="button" class="btn btn-secondary navbar-button" href="#" data-uri="/listeMeubles">Rechercher un meuble</button>
                    <button type="button" class="btn btn-secondary navbar-button" href="#" data-uri="/visites">Demandes de visite</button>
                    <button type="button" class="btn btn-secondary navbar-button" href="#" data-uri="/clients">Gestion des clients</button>
                    <button type="button" class="btn btn-primary navbar-button" href="#" data-uri="/meubles">Voir les meubles</button>
                    <button type="button" class="btn btn-secondary navbar-button pl-2" href="#" data-uri="/logout">Deconnexion</button></li>
                    <li class="my-auto font-weight-bold">utilisateurs.pseudo</li>
    `;
     */            
    navbarHtml += `
                </ul> 
            </div>
        </nav>
    `;
  navBar.innerHTML = navbarHtml;
}

export default Navbar;