import {findCurrentUser, setTakeoverSessionData} from "../utils/session";
import {removeTimeouts, generateLoadingAnimation, displayErrorMessage} from "../utils/utils";
import {Loader} from "@googlemaps/js-api-loader";
import { RedirectUrl } from "./Router";
import Navbar from "./Navbar";

let page = document.querySelector("#page");
let waitingUsersList;
let confirmedUsersList;
let currentUser;
let timeouts = [];
let userDetail;

let nbrCurrentUserWaiting;
let nbrCurrentUserAccepted;

let filter = {
  role : "",
  info : ""
}

const Users = async (id) => {
  currentUser = findCurrentUser();

  page.innerHTML = generateLoadingAnimation();

  await getUserLists();

  page.innerHTML = generateUsersPage();

  document.querySelectorAll(".toBeClicked").forEach(element => element.addEventListener("click", onRowClick));
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
  document.querySelector("#buttonApplyFilter").addEventListener("click", onApplyFilterClick);
  document.querySelector("#buttonClearFilter").addEventListener("click", onClearFilterClick);

  if (id) {
    displayShortElements();
    await displayUserCardById(id);
  }
}


/********************  Business methods  **********************/

const getUserLists = async () => {

  const waiting = () => new Promise(resolve => {
    console.log("GET users/waiting");
    getWaitingUserList().then((data) => {
      waitingUsersList = data;
      console.log(waitingUsersList);
      resolve();
    });
  });

  const confirmed = () => new Promise(resolve => {
    console.log("GET users/confirmed");
    getConfirmedUsersList().then((data) => {
      confirmedUsersList = data;
      console.log(confirmedUsersList);
      resolve();
    });
  });

  //await Promise.all([waiting(), confirmed()]);
  await waiting();
  await confirmed();
}

/**
 * Called when clicking a row in the body table.
 * Display the short elements if the table is large, else just refresh the user card.
 */
const onRowClick = (e) => {
  if (document.querySelector('#shortTableContainer') == null) {
    displayShortElements();
  }
  onUserClickHandler(e);
}

/**
 * Called when clicking on the buttonReturn.
 * Hide all the short elements, display the large ones and magnify the large.
 */
 const displayLargeTable = () => {
  removeTimeouts(timeouts);
  //hide
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
  //display
  timeouts.push(setTimeout(() => document.querySelectorAll('.notNeeded').forEach(element => element.style.display = "")
                    , 750));
  //magnify
  document.querySelector('#shortTable').id = "largeTable";
  document.querySelector("#buttonsFilterSpan").style.display = "";
  if (document.querySelector('#shortTableContainer') !== null) //can be undefined because of the setTimeout in displayShortElements
    document.querySelector('#shortTableContainer').id = "largeTableContainer";
}

/**
 * Shrink the large table, hide the not needed element in the table and display the user card needed.
 */
const displayShortElements = () => {
  removeTimeouts(timeouts);
  //shrink
  document.querySelector('#largeTable').id = "shortTable";
  document.querySelector("#buttonsFilterSpan").style.display = "block";
  timeouts.push(setTimeout( () => document.querySelector('#largeTableContainer').id = "shortTableContainer"
                    , 750));
  //hide
  document.querySelectorAll('.notNeeded').forEach(element => element.style.display = 'none');
  //display
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "block");
}

/**
 * Called when clicking on one of the rows of the table's body (large or short).
 * Display the card of the user that has been clicked on the table.
 */
const onUserClickHandler = async (e) => {
  //get the tr element
  let element = e.target;
  while (!element.className.includes("toBeClicked"))
    element = element.parentElement;
  let userId = element.attributes["userId"].value;
  await displayUserCardById(userId);
}

/**
 * Displays the card of the corresponding user
 * @param {*} userId the user's id 
 */
const displayUserCardById = async (userId) => {
  let userCardDiv = document.querySelector("#userCardDiv");
  userCardDiv.innerHTML = generateLoadingAnimation();
  //generate the user card
  userDetail = await clientDetail(userId);
  userCardDiv.innerHTML = generateUserCard(userDetail);
  await AddressToGeo(userDetail.address.street + ` ` +  userDetail.address.buildingNumber + `, ` +  userDetail.address.postcode + ` ` + userDetail.address.commune)
                      .catch((err) => console.error(err));
  //add event listener to validation button if the user of the user card is waiting
  if (userDetail.waiting) {
    valueButtonValid = e.target.id;
    document.querySelector("#accept").addEventListener("click",onValidateClick);
    document.querySelector("#refuse").addEventListener("click",onValidateClick);
  } else {
    let takeoverBtn = document.querySelector("#takeoverBtn");
    takeoverBtn.className = "profile-edit-btn";
    takeoverBtn.addEventListener("click", (e) => onTakeoverClick(e));
  }
}

/**
 * Called when click on accepter or refuser button
 * Accept or refuse the user's request, display a small notification and update the card and the table
 */
const onValidateClick = async (e) => {
  let validationButton = document.querySelector("#validationButton");
  let validationButtonHTML = validationButton.outerHTML;
  validationButton.innerHTML = generateLoadingAnimation();
  let snackbar = document.querySelector("#snackbar");

  let ret = await validation(e);

  if (ret === null) {
    //put back the validation buttons
    validationButton.innerHTML = validationButtonHTML;
    snackbar.innerText = "Erreur, l'opération a echoué";
  } else {
    //remove the validation button and update the user card and the table
    validationButton.innerHTML = "";
    snackbar.innerText = "L'opération a réussie";

    removeFromArray(ret.id, waitingUsersList);
    confirmedUsersList.push(ret);

    document.querySelector("#waiting").innerText = "Considéré";
    document.querySelector("#role").innerText = ret.role;

    let tr = document.querySelector("[userId='" + ret.id +"']");
    tr.lastElementChild.innerText = ret.role;
    let trParent = tr.parentNode;
    trParent.removeChild(tr);
    trParent.appendChild(tr);

    document.querySelector("#msgTableWaiting").innerText = waitingUsersList.length + " inscription(s) en attente";
    document.querySelector("#msgTableConfirmed").innerText = confirmedUsersList.length + " client(s) inscrit(s)";
  }

  //display toast for 5 seconds
  snackbar.className = "show";
  setTimeout(() => snackbar.className = snackbar.className.replace("show", "")
    , 5000);
}

const removeFromArray = (id, array) => {
  for (let i = 0; i < array.length; i++) {
    if (array[i].id === id) {
      array.splice(i, 1);
      break;
    }
  }
}

const onApplyFilterClick = () => {
  filter.info = document.querySelector("#userSearchBar").value;
  filter.role = document.querySelector("#filterRole").value;
  document.querySelector("#tableDiv").outerHTML = generateTable();

  document.querySelectorAll(".toBeClicked").forEach(element => element.addEventListener("click", onRowClick));
  if (document.querySelector(".shortElement").style.display === "block") {
    document.querySelectorAll('.notNeeded').forEach(element => element.style.display = 'none');
    document.querySelector('#largeTable').id = "shortTable";
  }

}

const onClearFilterClick = () => {
  filter.info = "";
  filter.role = "";  
  document.querySelector("#tableDiv").outerHTML = generateTable();
  document.querySelector("option[value='']").selected = "true";
  document.querySelector("#userSearchBar").value = "";
  
  document.querySelectorAll(".toBeClicked").forEach(element => element.addEventListener("click", onRowClick));
  if (document.querySelector(".shortElement").style.display === "block") {
    document.querySelectorAll('.notNeeded').forEach(element => element.style.display = 'none');
    document.querySelector('#largeTable').id = "shortTable";
  }
}

/********************  HTML generation  **********************/

const generateUsersPage = () => {
  return `
        <div class="col-5 mx-auto">
          <div id="errorDiv" class="d-none"></div>
        </div>
        <div id="largeTableContainer">
          <div>
            <input type="search" name="search" class="m-3" id="userSearchBar" placeholder="Rechercher par nom, prenom, code postal ou ville">
            <select id="filterRole">
              <option value="">Rechercher par rôle</option>
              <option value="customer">client</option>
              <option value="antique_dealer">antiquaire</option>
              <option value="admin">admin</option>
            </select>
            <span id='buttonsFilterSpan'>   
              <button type="submit" id="buttonClearFilter" class="btn btn-secondary m-3">Retirer les filtres</button>
              <button type="submit" id="buttonApplyFilter" class="btn btn-secondary m-3">Appliquer les filtres</button>
            </span>
            <button type="button" id="buttonReturn" class="shortElement btn btn-dark m-3">Retour à la liste</button>
            ` + generateTable() + `
          </div>
          <div class="shortElement" id="userCardDiv"></div>
        </div>
        <div id="snackbar"></div>`;
}

const generateTable = () => {
  let res = `
  <div id="tableDiv">
    <table id="largeTable" class="table table-bordered table-hover">
        <thead class="table-secondary">
            <tr>
              <th>Nom</th>
              <th>Prénom</th>
              <th class="notNeeded">Pseudo</th>
              <th class="notNeeded">Email</th>
              <th class="notNeeded">Nombre de meubles achetés</th>
              <th class="notNeeded">Nombre de meubles vendus</th>
              <th class="notNeeded">Rôle</th>
            </tr>
        </thead>
        <tbody>`
          + getAllUsersRows() + `
        </tbody>
    </table>
  </div>`;
  return res;
}

/**
 * generate all the rows needed and 2 messages informing the number of user waiting or accepted.
 * @returns an html element containing all the rows (filtered or not) and the messages informing the number of user waiting and accepted.
 */
const getAllUsersRows = () => {
  nbrCurrentUserWaiting = 0;
  nbrCurrentUserAccepted = 0;

  let rows = ``;
  waitingUsersList.forEach(user => {
    let row = generateRow(user);
    if (row !== ``) {
      nbrCurrentUserWaiting++;
      rows += row;
    }
  });
  let res = "<tr><th colspan = '7' class='msgTable' id='msgTableWaiting'>" + nbrCurrentUserWaiting+ " inscription(s) en attente</th></tr>" + rows;

  rows = ``;
  confirmedUsersList.forEach(user => {
    let row = generateRow(user);
    if (row !== ``) {
      nbrCurrentUserAccepted++;
      rows += row;
    }
  });
  res += "<tr><th colspan = '7' class='msgTable' id='msgTableConfirmed'>" + nbrCurrentUserAccepted + " client(s) inscrit(s)</th></tr>" + rows;

  return res;
}

/**
 * generate a tr element containing the information of a user.
 * if the filters doesn't correspond to the user info then returning a empty string.
 * @param {*} user the user containg the information to put in a tr element.
 * @returns a tr element containing the information of the user if they correspond to the filters.
 */
const generateRow = (user) => {
  //filter
  if (filter.role !== '') {
    if (filter.role !== user.role)
      return ``;
  }
  if (filter.info !== '') {
    let info = filter.info.toLowerCase();
    let postcode = "" + user.address.postcode;
    if (!user.firstName.toLowerCase().includes(info) && !user.lastName.toLowerCase().includes(info) 
        && !(user.address.commune.toLowerCase() === info) && !(postcode === info))
      return ``;
  }

  return ` 
    <tr class="toBeClicked" userId="` + user.id + `">
        <th><p>` + user.lastName + `</p></th>
        <th><p>` + user.firstName + `</p></th>
        <th class="notNeeded"><p>` + user.username + `</p></th>
        <th class="notNeeded"><p>` + user.email + `</p></th>
        <th class="notNeeded"><p>` + user.purchasedFurnitureNbr + `</p></th>
        <th class="notNeeded"><p>` + user.soldFurnitureNbr + `</p></th>
        <th class="notNeeded"><p>` + user.role + `</p></th>
   </tr>`;
}

const generateUserCard = (userDetail) => {
  let status;
  if (userDetail.waiting)
    status = 'En attente';
  else 
  status = "Considéré";
 let page= `
   <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-md-6">
          <div class="profile-head">
            <h5 id="Name&Firstname">` + userDetail.lastName + ` ` + userDetail.firstName +`</h5>
            <p class="profile-rating">ROLE : 
              <span id="role">
               ` + userDetail.role + ` 
              </span>
            </p>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
              <li class="nav-item">
                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">informations personnelles</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">adresses et plan</a>
              </li>
            </ul>
          </div>
        </div>
        <div class="col-md-2">
          <input type="submit" id="takeoverBtn" class="d-none" user-id="${userDetail.id}" name="btnAddMore" value="prendre le controle"/>
        </div>
      </div>

      <div class="row">
        <div class="col-md-8">
          <div class="tab-content profile-tab" id="myTabContent">
            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">

              <div class="row">
                <div class="col-md-6">
                  <label>username</label>
                </div>
                <div class="col-md-6">
                  <p id="username">` + userDetail.username + `</p>
                </div>
              </div>

            <div class="row">
              <div class="col-md-6">
                <label>Nom et prénom:</label>
              </div>
              <div class="col-md-6">
                <p id="Name&Firstname">` + userDetail.lastName + ` ` + userDetail.firstName +`</p>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <label>Email</label>
              </div>
              <div class="col-md-6">
                <p id="email">` + userDetail.email + `</p>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <label>nombre d'achat</label>
              </div>
              <div class="col-md-6">
                <p id="purchased_furniture_nbr">` + userDetail.purchasedFurnitureNbr + `</p>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <label>nombre de ventes</label>
              </div>
              <div class="col-md-6">
                <p id="sold_furniture_nbr">` + userDetail.soldFurnitureNbr + `</p>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <label>statut</label>
              </div>
              <div class="col-md-6">
                <p id="waiting">` + status + `</p>
              </div>
            </div>

            <div class="col-md-2" id="validationButton" style="display: flex"> `;
      if(userDetail.waiting) {
        page += `<input type="button" class="profile-edit-btn" id="accept" value="accepter" style="color: #0062cc; margin:5px"/>
                 <input type="button" class="profile-edit-btn" id="refuse" value="refuser" style="color: red; margin:5px"/>
                 `;
      }
       page += `</div>
          </div>         
          <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
            <div class="row">
              <div class="col-md-6">
                <label> Adresse </label>
              </div>
              <div class="col-md-6">
                <p>` + userDetail.address.street + ` ` +  userDetail.address.buildingNumber + `,` +  userDetail.address.postcode + ` ` + userDetail.address.commune + ` ` + userDetail.address.country + `</p>
              </div>
            </div>
            <div class="row">
              <div class="col-md-12">
                <label>Map</label><br/>
                <div id="map" ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>           
</div>`;
  return page;
}


/********************  Backend fetch  **********************/

const clientDetail = async (id) => {
  let userDetails;
  await fetch(`/users/detail/${id}`, {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error( "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    userDetails = data;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
  });
  return userDetails;
}

const getWaitingUserList = async () => {
  let ret = [];
  await fetch("/users/detail/waiting", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error( "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    ret = data.users;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
  });
  return ret;
}

const getConfirmedUsersList = async () => {
  let ret = [];
  await fetch("/users/detail/confirmed", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    ret = data.users;
  }).catch((err) => {
    console.error(err);
  });
  return ret;
}


const validation = async (e) => {
  let value = e.target.id;
  let val;
  if (value === "refuse") {
    val = {value: false,}
  } else if (value=="accept") {
    val = {value: true,}
  }
  let ret = [];
  await fetch(`/users/validate/${userDetail.id}`, {
    method: "PATCH",
    body:JSON.stringify(val),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error("Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    ret = data;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
    return;
  });
  return ret;
}

const onTakeoverClick = async (e) => {
  e.preventDefault();
  let btn = e.target;
  let userId = btn.getAttribute("user-id");
  let response = await fetch(`/users/takeover/${userId}`,{
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
    },
  });
  if(!response.ok) {
    let err = new Error( "Error code : " + response.status + " : " + response.statusText);
    displayErrorMessage("errorDiv", err);
    return;
  }
  let data = await response.json();

  let bundle = {
    ...data,
    isAdmin: false,
    isTakeover: true,
  }
  setTakeoverSessionData(bundle);
  Navbar();
  RedirectUrl("/");
}


/********************  MAP API  **********************/

const AddressToGeo = async (address) => {

  var platform = new H.service.Platform({
    'apikey': 'lork4RxfbCvij9rrt-YAdXwViqsyXrHaxgdhP5cfRJs'
  });

  var service = platform.getSearchService();

  await service.geocode({
        q: address
      },  (result) => {
       if(result.items[0] != null)
         map(result.items[0].position.lat, result.items[0].position.lng);
      },
     await map(null, null));
}


const map = async (latitude, lngitude) => {
  if (latitude != null && lngitude != null) {
    let map;
    const additionalOptions = {};
    const place = {lat: latitude, lng: lngitude};
    const loader = new Loader({
      apiKey: "AIzaSyCOBWUhB79EsC0kEXXucgtPUgmLHqoJ1u4",
      version: "weekly",
      ...additionalOptions,
    });
    loader.load().then(() => {
      map = new google.maps.Map(document.querySelector("#map"), {
        center: place,
        zoom: 13,
      });
      new google.maps.Marker({
        position: place,
        map: map,
      })
    });
  } else {
    console.log("adresse not found");
    let map;
    const additionalOptions = {};
    const place = {lat: 41.726931, lng: -49.948253};
    const loader = new Loader({
      apiKey: "AIzaSyCOBWUhB79EsC0kEXXucgtPUgmLHqoJ1u4",
      version: "weekly",
      ...additionalOptions,
    });
    loader.load().then(() => {
      map = new google.maps.Map(document.querySelector("#map"), {
        center: place,
        zoom: 13,
      });
      new google.maps.Marker({
        position: place,
        map: map,
      })
    });
  }
}

export default Users;