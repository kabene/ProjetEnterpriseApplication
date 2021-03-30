import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session";

let page = document.querySelector("#page");
let furnitureList;
let furnitureMap = [];
let timeouts = [];
let currentUser;
let pageHTML;

const FurnitureList = async () => {
  currentUser = getUserSessionData();

  pageHTML = generateLoadingAnimation();
  page.innerHTML = pageHTML;

  await findFurnitureList();

  pageHTML =  generatePageHtml();

  page.innerHTML = pageHTML;

  document.querySelectorAll(".toBeClicked").forEach(
      element => element.addEventListener("click", displayShortElements));
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
}

const findFurnitureList = async () => {
  return fetch("/furniture/detail", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw error();
    }
    return response.json();
  }).then((data) => {
    furnitureList = data;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
  });
}

const findOneFurniture = async (id) => {
  return fetch(`/furniture/detail/${id}`, {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw error();
    }
    return response.json();
  }).then((data) => {
    furnitureMap[data.furnitureId] = data;
    generateCard(data);
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
  });
}

const removeTimeouts = () => {
  timeouts.forEach(timeout => {
    clearTimeout(timeout);
})
}

const generatePageHtml = () => {
  let res = `
  <div id="largeTableContainer">
    <div>
      <button type="button" id="buttonReturn" class="btn btn-dark m-3 d-none">Retour à la liste</button>
      <table id="largeTable" class="table table-hover">
        <thead class="table-secondary">
          <tr>
            <th></th>
            <th>Description</th>
            <th class="notNeeded">Type</th>
            <th>État</th>
            <th class="notNeeded">Vendeur</th>
            <th class="notNeeded">Acheteur</th>
            <th class="notNeeded">Prix de vente</th>
            <th class="notNeeded">Prix spécial</th>
          </tr>
        </thead>
        <tbody>
          ${generateAllRows()}
        </tbody>
      </table>
    </div>
    <div class="shortElement d-none" id="furnitureCardDiv">Hello</div>
  </div>
    
  </div>`;
  return res;
}

const generateAllRows = () => {
  let res = "";
  furnitureList.forEach(furniture => {
    res += generateRow(furniture);
  });
  return res;
}

const generateRow = (furniture) => {
  let res = `
    <tr class="toBeClicked" furnitureId="${furniture.furnitureId}">
      <th>${generateFavouritePhotoImgTag(furniture)}</th>
      <th><p>${furniture.description}</p></th>
      <th class="notNeeded"><p>${furniture.type}</p></th>
      <th class="tableState" condition="${furniture.condition}">${generateColoredState(furniture)}</th>
      <th class="notNeeded">${generateSellerLink(furniture)}</th>
      <th class="notNeeded">${generateBuyerLink(furniture)}</th>
      <th class="notNeeded">${generateSellingPriceTableElement(furniture)}</th>
      <th class="notNeeded">${generateSpecialPriceTableElement(furniture)}</th>
    </tr>`;
  return res;
}

const generateFavouritePhotoImgTag = (furniture) => {
  let res = "";
  if (furniture.favouritePhoto) {
    res = `<img src="${furniture.favouritePhoto.source}" alt="thumbnail photoId=${furniture.favouritePhoto.photoId}"/>`;
  } else {
    // TODO: default img if no favourite
  }
  return res;
}

const generateSellerLink = (furniture) => {
  let res = "";
  if (furniture.seller) {
    res = generateUserLink(furniture.seller);
  }
  return res;
}

const generateBuyerLink = (furniture) => {
  let res = "";
  if (furniture.buyer) {
    res = generateUserLink(furniture.buyer);
  }
  return res;
}

const generateUserLink = (user) => {
  return `<a href="#" userId="${user.userId}" class="userLink">${user.username}</a>`;
}

const generateSellingPriceTableElement = (furniture) => {
  let res = "";
  if (furniture.sellingPrice) {
    res = `<p>${furniture.sellingPrice}€</p>`;
  }
  return res;
}

const generateSpecialPriceTableElement = (furniture) => {
  let res = "";
  if (furniture.specialSalePrice) {
    res = `<p>${furniture.specialSalePrice}</p>`;
  }
  return res;
}

const generateStateInfos = (condition) => {
  let res = {
    classname: "",
    condition: "",
  }

  switch (condition) {
    case "available_for_sale":
      res.classname = "success";
      res.condition = "Disponible à la vente";
      break;
    case "accepted":
      res.classname = "info";
      res.condition = "Accepté";
      break;
    case "in_restoration":
      res.classname = "warning";
      res.condition = "En restoration";
      break;
    case "under_option":
      res.classname = "danger";
      res.condition = "Sous option";
      break;
    case "sold":
      res.classname = "danger";
      res.condition = "Vendu";
      break;
    case "withdrawn":
      res.classname = "dark";
      res.condition = "Retiré de la vente";
      break;
    case "requested_for_visit":
    case "refused":
    case "reserved":
    case "delivered":
    case "collected":
    default:
      res.classname = "";
      res.condition = condition;
  }
  return res;
}

const generateColoredState = (furniture) => {
  let infos = generateStateInfos(furniture.condition);
  return `<p class="text-${infos.classname}">${infos.condition}</p>`;
}

//input: "primary", "secondary", "info", etc...
const generateDotState = (colorClassName) => `<span class="badge badge-pill badge-${colorClassName}">‏‏‎ ‎</span>`;

const generateLoadingAnimation = () => `<div class="text-center"><h2>Loading <div class="spinner-border"></div></h2></div>`;

const displayShortElements = (e) => {
  removeTimeouts();
  //hide large table
  let largeTable = document.querySelector('#largeTable');
  if (largeTable !== null) 
    largeTable.id = "shortTable";
  if (document.querySelector('#largeTableContainer') !== null)
    timeouts.push(setTimeout(changeContainerId, 1000));
  document.querySelectorAll(".notNeeded").forEach(
      element => element.className = "notNeeded d-none");
  document.querySelectorAll(".shortElement").forEach(
      element => element.className = "shortElement");
  let returnBtn = document.querySelector("#buttonReturn");
  returnBtn.className = "btn btn-dark m-3";
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  furnitureCardDiv.innerHTML = generateLoadingAnimation();

  let element = e.srcElement;
  while (!element.className.includes("toBeClicked")) {
    element = element.parentElement;
  }
  element.className = "toBeClicked table-dark text-dark";

  document.querySelectorAll(".tableState").forEach(element => {
    let condition = element.attributes["condition"].value;
    let classname = generateStateInfos(condition).classname;
    element.innerHTML = generateDotState(classname);
  });
  let id = element.attributes["furnitureId"].value;
  if(!furnitureMap[id]) {
    findOneFurniture(id);
  }else {
    console.log("found furniture in map");
    generateCard(furnitureMap[id]);
  }
}

const displayLargeTable = () => {
  document.querySelector('#shortTableContainer').id = "largeTableContainer";
  timeouts.push(setTimeout(displayLargeElements, 750));
  document.querySelectorAll(".shortElement").forEach(element => element.className = "shortElement d-none");
  document.querySelector('#shortTable').id = "largeTable";
  document.querySelectorAll(".toBeClicked").forEach(element => element.className = "toBeClicked");
  document.querySelector("#buttonReturn").className = "btn btn-dark m-3 d-none";
}

const displayLargeElements = () => {
  document.querySelectorAll('.notNeeded').forEach(element => element.className = "notNeeded");
}

const generateCard = (furniture) => {
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  let cardHTML = generateCardHTML(furniture);
  furnitureCardDiv.innerHTML = cardHTML;
  addTransitionBtnListeners(furniture);
}

const changeContainerId = () => {
  document.querySelector('#largeTableContainer').id = "shortTableContainer";
}

const generateCardHTML = (furniture) => {
  let res = `
  <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-12">
          <div class="profile-head">
            <div class="row">
              <div class="col-md-6">
                <p>${generateFavouritePhotoImgTag(furniture)}</p>
              </div>
              <div class="col-md-6 text-left">
                <h5 id="descriptionCardEntry">${furniture.description}</h5>
                <p class="proile-rating">ÉTAT : <span id="stateCardEntry">${generateColoredState(furniture)}</span></p>
              </div>
            </div>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
              <li class="nav-item">
                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Information</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Photos</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
  
      <div class="row">
        <div class="col-md-8">
          <div class="tab-content profile-tab" id="myTabContent">
            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
              ${generateTypeCardEntry(furniture)}
              ${generateBuyingPriceCardEntry(furniture)}
              ${generateBuyingDateCardEntry(furniture)}
              ${generateSellerCardEntry(furniture)}
              ${generateSellingPriceCardEntry(furniture)}
              ${generateBuyerCardEntry(furniture)}
              ${generateOptionCardEntry(furniture)}

              ${generateButtonRow(furniture)}
            </div>       
            <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
              ${generateCardLabelKeyEntry("label tab2", "id1", "value tab 2")}
            </div>
          </div>
        </div>
      </div>
    </form>           
  </div>
  `;
  return res;
}

const generateCardLabelKeyEntry = (label, id, value) => {
  let res = `
  <div class="row text-left">
    <div class="col-md-6">
      <label>${label}</label>
    </div>
    <div class="col-md-6">
      <p id="${id}">${value}</p>
    </div>
  </div>
  `;
  return res;
}

const generateTypeCardEntry = (furniture) => {
  return generateCardLabelKeyEntry("Type", "typeCardEntry", furniture.type);
}

const generateBuyingPriceCardEntry = (furniture) => {
  return "";//TODO
}

const generateBuyingDateCardEntry = (furniture) => {
  return "";//TODO
}

const generateUserCardEntry = (label, id, user) => {
  let res = `
  <div class="row text-left">
    <div class="col-md-6">
      <label class="mr-3">${label}</label>
    </div>
    <div class="col-md-6">
      <p id="${id}">${generateUserLink(user)} (${user.firstName} ${user.lastName})</p>
    </div>
  </div>`;
  return res;
}

const generateSellerCardEntry = (furniture) => {
  let res ="";
  if(furniture.seller) {
    res = generateUserCardEntry("Vendeur", "sellerCardEntry", furniture.seller);
  }
  return res;
}

const generateBuyerCardEntry = (furniture) => {
  let res ="";
  if(furniture.buyer) {
    res = generateUserCardEntry("Acheteur", "buyerCardEntry", furniture.buyer);
  }
  return res;
}

const generateOptionCardEntry = (furniture) => {
  return "";//TODO: infos client qui a demandé l'option
}

const generateSellingPriceCardEntry = (furniture) => {
  return generateCardLabelKeyEntry("Prix de vente", "sellingPriceCardEntry", furniture.sellingPrice + "€");
}

const generateButtonRow = (furniture) => {
  let res = `
  <div class="row d-flex mt-5">
    ${generateAllTransitionBtns(furniture)}
  </div>
  `;
  return res;
}

const generateAllTransitionBtns = (furniture) => {
  let res = "";
  switch(furniture.condition) {
    case "accepted":
      res += generateTransitionBtn("btnToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionBtn("btnToRestoration", "Indiquer en restoration");
      break;
    case "available_for_sale":
      res += generateTransitionBtn("btnToSold", "Indiquer vendu");
      res += generateTransitionBtn("btnWithdraw", "Retirer de la vente", "danger");
      break;
    case "in_restoration":
      res += generateTransitionBtn("btnToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionBtn("btnWithdraw", "Retirer de la vente", "danger");
      break;
    case "under_option":
    case "sold":
    case "withdrawn":
    case "requested_for_visit":
    case "refused":
    case "reserved":
    case "delivered":
    case "collected":
    default:
  }
  return res;
}

const generateTransitionBtn = (id, label, colorClass="primary") => {
  return `<button id=${id} class="transitionBtn btn btn-${colorClass} mr-1">${label}</button>`
}

const addTransitionBtnListeners = (furniture) => {
  document.querySelectorAll(".transitionBtn").forEach(element => {
    element.addEventListener("click", findTransitionMethod(element.id, furniture));
  })
}

const findTransitionMethod = (btnId, furniture) => {
  switch(btnId) {
    case "btnToAvailable":
      return (e) => toAvailable(e, furniture);
    case "btnToRestoration":
      return (e) => toRestoration(e, furniture);
    case "btnWithdraw":
      return (e) => withdraw(e, furniture);
    default:
      return (e) => {
        e.preventDefault();
        console.log("unrecognized button id: " + btnId) ; //'do nothing' method
      };
  };
}

const toAvailable = (e, furniture) => { //TODO
  e.preventDefault();
  console.log("toAvailable");
}

const toRestoration = (e, furniture) => {//TODO
  e.preventDefault();
  console.log("toRestoration");
}

const withdraw = (e, furniture) => {//TODO
  e.preventDefault();
  console.log("withdraw");
}

export default FurnitureList;