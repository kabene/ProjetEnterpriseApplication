import logo from "../img/logoAE_v2.png";
import {findCurrentUser} from "../utils/session.js";
import {escapeHtml} from "../utils/utils.js";


const Navbar = () => {
  document.querySelector("#navbar").innerHTML = generateNavBar();
}


/********************  HTML generation  **********************/

/**
 * generate the whole navbar.
 * @returns the whole navbar in a <nav> tag.
 */
const generateNavBar = () => {
  return `
  <nav class="navbar navbar-expand-md navbar-light py-0"> 
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <ul class="navbar-nav"> 
      <li class="nav-item mt-1 mb-1">
        <img id="logoNavbar" src="`+ logo + `" alt="logo" href="#" data-uri="/"/>
        <span id="storeName" href="#" data-uri="/">LI VI SATCHO</span>
      </li>
    </ul>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav ml-auto">` + generateNavBarButtons(findCurrentUser()) +  `</ul> 
    </div>
  </nav>`;
}

/**
 * generate the buttons in the navbar depending on if the user is connected or not, if he's an admin or if he's a user 'takeover'.
 * @param {*} user the user that needs the navbar.
 * @returns all the needed buttons in a <li> tag.
 */
const generateNavBarButtons = (user) => {
  if (!user) {
    return `
    <li class="nav-item">
      <button type="button" class="btn btn-primary navbarButton" id="seeFurnitureButton" href="#" data-uri="/seeFurniture">Voir les meubles</button>
      <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/authentication">S'identifier</button>
    </li>`;
  }
  let userPrintable = escapeHtml(user.user.username);
  if (user.isAdmin) {
    return `
    <li class="nav-item">
      <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visits">Demandes de visite</button>
      <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/furnitureList">Gestion des meubles</button>
      <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/users">Gestion des utilisateurs</button>
      <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/seeFurniture">Voir les meubles</button>
      <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/logout">Deconnexion</button>
    </li>
    <li class="my-auto font-weight-bold">` + userPrintable + `</li>`;
  }
  if (user.isTakeover) {
    return `
    <li class="nav-item">
      <button type="button" class="btn btn-secondary navbarButton p-0" href="#" data-uri="/myVisits">Mes demandes de visite</button>
      <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visitRequest">Demander une visite</button>
      <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/seeFurniture">Voir les meubles</button>
      <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/releaseTakeover">Abandonner le contrôle</button>
    </li>
    <li class="my-auto font-weight-bold">` + userPrintable + ` <span class="badge badge-warning">C</span></li>`;
  }
  return `
  <li class="nav-item">
    <button type="button" class="btn btn-secondary navbarButton p-0" href="#" data-uri="/myVisits">Mes demandes de visite</button>
    <button type="button" class="btn btn-secondary navbarButton" href="#" data-uri="/visitRequest">Demander une visite</button>
    <button type="button" class="btn btn-primary navbarButton" href="#" data-uri="/seeFurniture">Voir les meubles</button>
    <button type="button" class="btn btn-secondary navbarButton pl-2" href="#" data-uri="/logout">Deconnexion</button>
  </li>
  <li class="my-auto font-weight-bold">` + userPrintable + `</li>`;
}


export default Navbar;