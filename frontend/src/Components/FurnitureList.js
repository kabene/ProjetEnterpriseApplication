import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session";

let page = document.querySelector("#page");
let furnitureList;
let currentFurniture;
let currentUser;
let pageHTML;

const FurnitureList = async () => {
  currentUser = getUserSessionData();

  pageHTML = generateLoadingAnimation();
  page.innerHTML = pageHTML;

  await findFurnitureList();

  pageHTML = `<div class="mx-5"><h1>Liste des meubles:</h1>`;
  pageHTML += generateTableView();

  page.innerHTML = pageHTML;

  document.querySelectorAll(".toBeClicked").forEach(
      element => element.addEventListener("click", displayShortElements));

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
  }).then((data) => generateCard(data)).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
  });
}

const generateTableView = () => {
  let res = `
    <table id="largeTable" class="table table-bordered table-hover">
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
        <div class="shortElement d-none" id="furnitureCardDiv">Hello</div>
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
      <th>${generateFavouritePhotoTableElement(furniture)}</th>
      <th><p>${furniture.description}</p></th>
      <th class="notNeeded"><p>${furniture.type}</p></th>
      <th class="tableState" condition="${furniture.condition}">${generateColoredState(furniture.condition)}</th>
      <th class="notNeeded">${generateSellerTableElement(furniture)}</th>
      <th class="notNeeded">${generateBuyerTableElement(furniture)}</th>
      <th class="notNeeded">${generateSellingPriceTableElement(furniture)}</th>
      <th class="notNeeded">${generateSpecialPriceTableElement(furniture)}</th>
    </tr>`;
  return res;
}

const generateFavouritePhotoTableElement = (furniture) => {
  let res = "";
  if (furniture.favouritePhoto) {
    res = `<img src="${furniture.favouritePhoto.source}" alt="thumbnail photoId=${furniture.favouritePhoto.photoId}"/>`;
  } else {
    // TODO: default img if no favourite
  }
  return res;
}

const generateSellerTableElement = (furniture) => {
  let res = "";
  if (furniture.seller) {
    res = `<a href="#" id="${furniture.seller.userId}" class="userLink">${furniture.seller.username}</a>`;
  }
  return res;
}

const generateBuyerTableElement = (furniture) => {
  let res = "";
  if (furniture.buyer) {
    res = `<a href="#" id="${furniture.buyer.userId}" class="userLink">${furniture.buyer.username}</a>`;
  }
  return res;
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
  let res, classname, condition;
  let infos = generateStateInfos(furniture);
  res = `<p class="text-${infos.classname}">${infos.condition}</p>`;
  return res;
}

//input: "primary", "secondary", "info", etc...
const generateDotState = (colorClassName) => `<span class="badge badge-pill badge-${colorClassName}">‏‏‎ ‎</span>`;

const generateLoadingAnimation = () => `<div class="text-center"><h2>Loading <div class="spinner-border"></div></h2></div>`;

const displayShortElements = (e) => {

  //hide large table
  let largeTable = document.querySelector('#largeTable');
  if (largeTable !== null) {
    largeTable.id = "shortTable";
  }
  document.querySelectorAll(".notNeeded").forEach(
      element => element.className = "notNeeded d-none");
  document.querySelectorAll(".shortElement").forEach(
      element => element.className = "shortElement");
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  furnitureCardDiv.innerHTML = generateLoadingAnimation();

  let element = e.srcElement;
  while (!element.className.includes("toBeClicked")) {
    element = element.parentElement;
  }

  document.querySelectorAll(".tableState").forEach(element => {
    let condition = element.attributes["condition"].value;
    let classname = generateStateInfos(condition).classname;
    element.innerHTML = generateDotState(classname);
  });
  let id = element.attributes["furnitureId"].value;
  findOneFurniture(id);
}

const generateCard = (furniture) => {
  console.log("generateCard id=" + furniture.furnitureId)
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  let cardHTML = generateCardHTML(furniture);
  furnitureCardDiv.innerHTML = cardHTML;

}

const generateCardHTML = (furniture) => {
  let res = `
  <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-md-6">
          <div class="profile-head">
            <h5 id="cardDescription">${furniture.description}</h5>
            <p class="proile-rating">STATE : 
              <span id="cardState">${generateColoredState(furniture)}</span>
            </p>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
              <li class="nav-item">
                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">tab link 1</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">tab link 2</a>
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
  
              ${generateCardEntry("label1", "id1", "value1")}
              ${generateCardEntry("label2", "id2", "value2")}
              ${generateCardEntry("label3", "id3", "value3")}

            </div>         
            <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">

              ${generateCardEntry("label tab2", "id1", "value tab 2")}

            </div>
          </div>
        </div>
      </div>
    </form>           
  </div>
  `;
  return res;
}

const generateCardEntry = (label, id, value) => {
  let res = `
  <div class="row">
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

export default FurnitureList;