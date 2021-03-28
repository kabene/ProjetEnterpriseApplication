import {getUserSessionData} from "../utils/session";
import {Loader} from "@googlemaps/js-api-loader";

let page = document.querySelector("#page");
let usersList;
let currentUser;

const Users = async () => {
  currentUser = getUserSessionData();

  page.innerHTML = generateLoadingAnimation();

  usersList = await getUserList();
  
  page.innerHTML = generateUsersPage();

  document.querySelectorAll(".toBeClicked").forEach(element => element.addEventListener("click", displayShortElements));
  document.getElementById("buttonReturn").addEventListener("click", displayLargeTable);
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
}

const displayShortElements = async (e) => {
  //display / hide the needed elements
  let largeTable = document.querySelector('#largeTable');
  if (largeTable !== null)
    largeTable.id = "shortTable";
  document.querySelectorAll('.notNeeded').forEach(element => element.style.display = 'none');
  setTimeout(changeContainerId, 1000);
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "block");
  let userCardDiv = document.getElementById("userCardDiv");
  userCardDiv.innerHTML = generateLoadingAnimation();

  //get the correct element
  let element = e.srcElement;
  while (!element.className.includes("toBeClicked")) {
    element = element.parentElement;
  }
  let attributesTab = element.attributes;

  //generate the user card
  let userDetail = await clientDetail(attributesTab["userId"].value);
  await AddressToGeo(userDetail.address.street + ` ` +  userDetail.address.buildingNumber + `, ` +  userDetail.address.postcode + ` ` + userDetail.address.commune)
                      .catch((err) => console.error(err));
  userCardDiv.innerHTML = generateUserCard(userDetail);
}

const changeContainerId = () => {
  document.querySelector('#largeTableContainer').id = "shortTableContainer";
}

const displayLargeTable = () => {
  console.log(document.querySelectorAll('.notNeeded'));
  document.querySelectorAll('.notNeeded').forEach(element => element.style.display = "");
  document.querySelector('#shortTableContainer').id = "largeTableContainer";
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
  document.querySelector('#shortTable').id = "largeTable";
}

const generateUsersPage = () => {
  return `
        <div id="largeTableContainer">
          <div>
            <!-- @author Milan Raring
            https://freefrontend.com/css-search-boxes/ -->
            <form action="" class="search-bar">
                <input type="search" name="search" pattern=".*\S.*" required>
                <button class="search-btn" type="submit">
                    <span>Search</span>
                </button>
            </form>
            <button type="button" id="buttonReturn" class="shortElement btn btn-dark m-3">Retour à la liste</button>`
            + generateTable() +
          `</div>
          <div class="shortElement" id="userCardDiv"></div>
        </div>`;
}

const generateTable = () => {
  let res = `
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
    </table>`;
  return res;
}

const getAllUsersRows = () => {
  let res = "";
  usersList.users.forEach(user => res += generateRow(user));
  return res;
}

const generateRow = (user) => {
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
  status = "Accepté";
  return `
   <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-md-6">
          <div class="profile-head">
            <h5 id="Name&Firstname">` + userDetail.lastName + ` ` + userDetail.firstName +`</h5>
            <p class="proile-rating">ROLE : 
              <span id="role">
                <p>` + userDetail.role + `</p> 
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
          <input type="submit" class="profile-edit-btn" name="btnAddMore" value="prendre le controle"/>
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
                <label>status</label>
              </div>
              <div class="col-md-6">
                <p id="waiting">` + status + `</p>
              </div>
            </div>

            <div class="col-md-2" style="display: flex"> 
              <input type="submit" class="profile-edit-btn" name="btnAddMore"  id="approuver" value="approuver" style="color: #0062cc; margin:5px"/>
              <input type="submit" class="profile-edit-btn" name="btnAddMore" id="refuser" value="refuser" style="color: red; margin:5px"/>
            </div>
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
</div>
    `;
}

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
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    userDetails = data;
  }).catch((err) => {
    console.error(err);
  });
  return userDetails;
}

const map = (latitude, lngitude) => {
  if (latitude != null && lngitude != null) {
    console.log(latitude, lngitude);
    let map;
    const additionalOptions = {};
    const place = {lat: latitude, lng: lngitude};
    const loader = new Loader({
      apiKey: "AIzaSyCOBWUhB79EsC0kEXXucgtPUgmLHqoJ1u4",
      version: "weekly",
      ...additionalOptions,
    });
    loader.load().then(() => {
      map = new google.maps.Map(document.getElementById("map"), {
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
    const additionalOptions = {};
    let map;
    let  place={lat: 41.726931, lng: -49.948253};
    const loader = new Loader({
      apiKey: "AIzaSyCOBWUhB79EsC0kEXXucgtPUgmLHqoJ1u4",
      version: "weekly",
      ...additionalOptions,
    });
    loader.load().then(() => {
      map = new google.maps.Map(document.getElementById("map"), {
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

const generateLoadingAnimation = () => {
  return `
      <div class="text-center">
          <h2>Loading <div class="spinner-border"></div></h2>
      </div>`
}

const getUserList = async () => {
  let ret = [];
  await fetch("/users/detail", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    ret = data;
  }).catch((err) => {
    console.error(err);
  });
  return ret;
}

const AddressToGeo = async (address) => {

  var platform = new H.service.Platform({
    'apikey': 's4UWDeMzmG-UyPMvL1e41P24GWrJk2AvNahLgtZ4eio'
  });

  var service = platform.getSearchService();

  await service.geocode({
        q: address
      }, (result) => {
        console.log(result.items[0].position)
        map(result.items[0].position.lat, result.items[0].position.lng);
      },
      map(null, null))
}

export default Users;
