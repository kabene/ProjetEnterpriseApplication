import logo from "../img/logoAE_v2.png";
import {getUserSessionData} from "../utils/session.js";

let navBar = document.querySelector("#navbar");

const Navbar = () => {
  let navbarHtml;
  let user = getUserSessionData();
  navbarHtml = `

        <nav class="navbar navbar-expand-md navbar-light py-0"> 
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <ul class="navbar-nav"> 
                <li class="nav-item mt-1 mb-1"><img id="logoNavbar" src="`
      + logo + `" alt="logo" href="#" data-uri="/"/><span id="storeName" href="#" data-uri="/">LI VI SATCHO</span></li>
            </ul>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto"> 
        `;

  if(!user){
    navbarHtml += `
    <li class="nav-item"><button type="button" class="btn btn-primary navbarButton" id="seeFurnitureButton" href="#" data-uri="/furnitures">Voir les meubles</button>
    <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/authentication">S'identifier</button></li>
    `;
  }else {
    navbarHtml += `
                  <li class="nav-item"><button type="button" class="btn btn-secondary navbarButton p-0" href="#" data-uri="/visits">Mes demandes de visite</button>
                  <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visitRequest">Demander une visite</button>
                  <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/furnitures">Voir les meubles</button>
                  <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/logout">Deconnexion</button></li>
                  <li class="my-auto font-weight-bold">utilisateurs.pseudo</li>
                  `;
  }
  

  //if client
  /*
  navbarHtml += `
                  <li class="nav-item"><button type="button" class="btn btn-secondary navbarButton p-0" href="#" data-uri="/visits">Mes demandes de visite</button>
                  <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visitRequest">Demander une visite</button>
                  <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/furnitures">Voir les meubles</button>
                  <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/logout">Deconnexion</button></li>
                  <li class="my-auto font-weight-bold">utilisateurs.pseudo</li>
  `;
  */
  //if admin
  /*navbarHtml += `
                  <li class="nav-item"><button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/furnitureList">Rechercher un meuble</button>
                  <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visits">Demandes de visite</button>
                  <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/customers">Gestion des clients</button>
                  <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/furnitures">Voir les meubles</button>
                  <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/logout">Deconnexion</button></li>
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