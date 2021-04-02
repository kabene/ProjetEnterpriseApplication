import {RedirectUrl} from "./Router";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js"
import {getUserSessionData} from "../utils/session.js";
import {displayErrorMessage} from "../utils/utils.js"

let page = document.querySelector("#page");
let mainPage;
let furnitureList;
let furnitureMap = [];
let timeouts = [];
let currentUser;
let pageHTML;

const FurnitureList = async (id) => {
  currentUser = getUserSessionData();

  pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12">${generateLoadingAnimation()}</div>`;
  page.innerHTML = pageHTML;
  mainPage = document.querySelector("#mainPage");

  await findFurnitureList();

  if(!id) {
    pageHTML =  generatePageHtml();

    mainPage.innerHTML = pageHTML;

    document.querySelectorAll(".toBeClicked").forEach(
        element => element.addEventListener("click", displayShortElements));
    document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
  }else {
    loadCard(id);
  }
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
      throw new Error(
        response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    furnitureList = data;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
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
      throw new Error(
        response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    furnitureMap[data.furnitureId] = data;
    generateCard(data);
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

const removeTimeouts = () => {
  timeouts.forEach(timeout => {
    clearTimeout(timeout);
})
}

const generatePageHtml = (largeTable = true) => {
  let tableSize = "large";
  let notNeededClassName = "notNeeded";
  let shortElementClassName = "shortElement d-none";
  if(largeTable === false) {
    tableSize = "short";
    notNeededClassName = "notNeeded d-none";
    shortElementClassName = "shortElement";
  }
  let res = `
  <div id="${tableSize}TableContainer">
    <div>
      <button type="button" id="buttonReturn" class="btn btn-dark m-3 ${shortElementClassName}">Retour à la liste</button>
      <table id="${tableSize}Table" class="table table-hover border border-1">
        <thead class="table-secondary">
          <tr>
            <th></th>
            <th>Description</th>
            <th class="${notNeededClassName}">Type</th>
            <th>État</th>
            <th class="${notNeededClassName}">Vendeur</th>
            <th class="${notNeededClassName}">Acheteur</th>
            <th class="${notNeededClassName}">Prix de vente</th>
            <th class="${notNeededClassName}">Prix spécial</th>
          </tr>
        </thead>
        <tbody>
          ${generateAllRows(notNeededClassName)}
        </tbody>
      </table>
    </div>
    <div class="shortElement ${shortElementClassName}" id="furnitureCardDiv">Hello</div>
  </div>
    
  </div>`;
  return res;
}

const generateAllRows = (notNeededClassName) => {
  let res = "";
  furnitureList.forEach(furniture => {
    if(!furnitureMap[furniture.furnitureId]){
      furnitureMap[furniture.furnitureId] = furniture;
    } else if(furniture !== furnitureMap[furniture.furnitureId]) {
      furniture = furnitureMap[furniture.furnitureId];
    }
    res += generateRow(furniture, notNeededClassName);
    furnitureMap[furniture.furnitureId] = furniture;
  });
  return res;
}

const generateRow = (furniture, notNeededClassName) => {
  let conditionHtml;
  let thumbnailClass;
  if(notNeededClassName === "notNeeded") { //large table
    conditionHtml = generateColoredCondition(furniture);
    thumbnailClass = "w-50"
  }else { //short table
    let infos = generateConditionInfos(furniture.condition);
    conditionHtml = generateDot(infos.classname);
    thumbnailClass = "w-100"
  }
  let res = `
    <tr class="toBeClicked" furnitureId="${furniture.furnitureId}">
      <th><div id="thumbnail" class="w-25 mx-auto">${generateFavouritePhotoImgTag(furniture)}<div></th>
      <th><p>${furniture.description}</p></th>
      <th class="${notNeededClassName}"><p>${furniture.type}</p></th>
      <th class="tableCondition text-center" condition="${furniture.condition}">${conditionHtml}</th>
      <th class="${notNeededClassName}">${generateSellerLink(furniture)}</th>
      <th class="${notNeededClassName}">${generateBuyerLink(furniture)}</th>
      <th class="${notNeededClassName}">${generateSellingPriceTableElement(furniture)}</th>
      <th class="${notNeededClassName}">${generateSpecialPriceTableElement(furniture)}</th>
    </tr>`;
  return res;
}

const generateFavouritePhotoImgTag = (furniture) => {
  let res = "";
  if (furniture.favouritePhoto) {
    res = `<img class="img-fluid" src="${furniture.favouritePhoto.source}" alt="thumbnail photoId=${furniture.favouritePhoto.photoId}"/>`;
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

const generateConditionInfos = (condition) => {
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
    case "refused":
      res.classname = "dark";
      res.condition = "Refusé";
      break;
    case "requested_for_visit":
    case "reserved":
    case "delivered":
    case "collected":
    default:
      res.classname = "";
      res.condition = condition;
  }
  return res;
}

const generateColoredCondition = (furniture) => {
  let infos = generateConditionInfos(furniture.condition);
  return `<p class="text-${infos.classname}">${infos.condition}</p>`;
}

//input: "primary", "secondary", "info", etc...
const generateDot = (colorClassName) => `<span class="badge badge-pill p-1 badge-${colorClassName}"> </span>`;

const generateBadgeCondition = (furniture) => {
  let infos = generateConditionInfos(furniture.condition);
  let res = `<span class="badge badge-pill badge-${infos.classname} text-light">${infos.condition}</span>`;
  return res;
}

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
  document.querySelectorAll("#thumbnail").forEach(
    element => element.className = "w-100 mx-auto");
  document.querySelectorAll(".shortElement").forEach(
      element => element.className = "shortElement");
  let returnBtn = document.querySelector("#buttonReturn");
  returnBtn.className = "btn btn-dark m-3";
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  furnitureCardDiv.innerHTML = generateLoadingAnimation();

  document.querySelectorAll(".toBeClicked").forEach( element => {
    element.className = "toBeClicked";
  });

  let element = e.srcElement;
  while (!element.className.includes("toBeClicked")) {
    element = element.parentElement;
  }
  element.className = "toBeClicked bg-secondary text-light";

  document.querySelectorAll(".tableCondition").forEach(element => {
    let condition = element.attributes["condition"].value;
    let classname = generateConditionInfos(condition).classname;
    element.innerHTML = generateDot(classname);
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
  document.querySelectorAll("#thumbnail").forEach(
    element => element.className = "w-25 mx-auto");
  document.querySelectorAll(".tableCondition").forEach(element => {
    let condition = element.getAttribute("condition");
    let infos = generateConditionInfos(condition);
    element.innerHTML = `<p class="text-${infos.classname}">${infos.condition}</p>`
  })
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
                <p class="proile-rating">ÉTAT : <span id="conditionCardEntry">${generateBadgeCondition(furniture)}</span></p>
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
              ${generateSaleWithdrawalDateCardEntry(furniture)}

              ${generateButtonRow(furniture)}
            </div>       
            <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
              ${generatePhotoList(furniture)}
            </div>
          </div>
        </div>
      </div>
    </form>           
  </div>
  `;
  return res;
}

const generatePhotoList = (furniture) => {
  let photos = "";
  furniture.photos.forEach(photo => {
    photos += `<img class="img-fluid flex-grow-1 p-1" src="${photo.source}" alt="photo of id ${photo.photoId}"/>`;
  });
  let res = `<div class="d-flex flex-lg-fill">${photos}</div>`;
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
  let res ="";
  if(furniture.option) {
    res = generateUserCardEntry("Client intéressé", "optionUserCardEntry", furniture.option.user);
  }
  return res;
}

const generateSellingPriceCardEntry = (furniture) => {
  if(furniture.sellingPrice) {
    return generateCardLabelKeyEntry("Prix de vente", "sellingPriceCardEntry", furniture.sellingPrice + "€");
  }
  return "";
}

const generateSaleWithdrawalDateCardEntry = (furniture) => {
  if(furniture.saleWithdrawalDate) {
    return generateCardLabelKeyEntry("Date de retrait de la vente", "WithdrawalDateCardEntry", furniture.saleWithdrawalDate);
  }
  return "";
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
      res += generateTransitionModal("ToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionModal("ToRestoration", "Indiquer en restoration");
      break;
    case "available_for_sale":
      res += generateTransitionModal("ToSold", "Indiquer vendu");
      res += generateTransitionModal("Withdraw", "Retirer de la vente", "danger", "secondary");
      break;
    case "in_restoration":
      res += generateTransitionModal("ToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionModal("Withdraw", "Retirer de la vente", "danger", "secondary");
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

const generateModalBodyFromTransitionId = (transitionId) => {
  switch(transitionId) {
    case "ToAvailable":
      return generateToAvailableForm();
    case "ToRestoration":
      return "Voulez-vous vraiment indiquer ce meuble comme allant en restoration ?"
    case "ToSold":
      return "Voulez-vous vraiment indiquer ce meuble comme vendu ?"
    case "Withdraw":
      return "Voulez-vous vraiment retirer ce meuble de la vente ? <br/><strong>Cette action est irréversible</strong>"
    default:
      return "not implemented yet";
  }
}

const generateToAvailableForm = () => {
  let res = `
  <form>
    <div class="form-group">
      <label for="sellingPriceInput" class="mr-3">Prix de vente: </label>
      <input type="number" id="sellingPriceInput" class="w-25" name="sellingPriceInput" min="0.01" step="0.01"> €
    </div>
  </form>
  `;
  return res;
}

const generateTransitionModal = (id, label, triggerColorClass="primary", closeColorClass="danger") => {
  let body = generateModalBodyFromTransitionId(id);
  let sendBtn = generateCloseBtn(label, "btn"+id, `btn btn-${triggerColorClass} mx-5 transitionBtn`);
  return generateModalPlusTriggerBtn("modal_"+id, label, `btn btn-${triggerColorClass}`, `<h4>${label}</h4>`, body, `${sendBtn}`, "Fermer", `btn btn-${closeColorClass}`);
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

//condition transition methods

const toAvailable = (e, furniture) => { //TODO
  e.preventDefault();
  let sellingPrice = e.target.parentElement.parentElement.querySelector("#sellingPriceInput").value;
  let bundle = {
    selling_price: sellingPrice,
  };
  fetch("/furniture/available/"+furniture.furnitureId, {
    method: "PATCH",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      console.log("Erreur de fetch !! :´\n" + response);
      throw new Error(
        response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    furnitureMap[data.furnitureId] = data;
    loadCard(data.furnitureId);
  }).catch((err) => {
    displayErrorMessage("errorDiv", err);
  });
}


const toRestoration = (e, furniture) => {//TODO
  e.preventDefault();
  fetch("/furniture/restoration/"+furniture.furnitureId, {
    method: "PATCH",
    headers: {
      "Authorization": currentUser.token,
    }
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
        response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    furnitureMap[data.furnitureId] = data;
    loadCard(data.furnitureId);
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

const withdraw = (e, furniture) => {//TODO
  e.preventDefault();
  fetch("/furniture/withdraw/"+furniture.furnitureId, {
    method: "PATCH",
    headers: {
      "Authorization": currentUser.token,
    }
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
        response.status + " : " + response.statusText
      );
    }
    return response.json();
  }).then((data) => {
    furnitureMap[data.furnitureId] = data;
    loadCard(data.furnitureId);
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

const loadCard = (id) => {
  mainPage.innerHTML = generatePageHtml(false);
  generateCard(furnitureMap[id]);
  document.querySelectorAll(".toBeClicked").forEach(
    element => element.addEventListener("click", displayShortElements));
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
}

export default FurnitureList;