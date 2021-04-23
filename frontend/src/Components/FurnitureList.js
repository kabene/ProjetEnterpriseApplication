import {RedirectUrl} from "./Router";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js"
import {getUserSessionData} from "../utils/session.js";
import {
  displayErrorMessage,
  importAllFurnitureImg,
  findFurnitureImgSrcFromFilename,
  findFavImgSrc,
  generateLoadingAnimation
} from "../utils/utils.js"

let page = document.querySelector("#page");
let mainPage;
let furnitureList;
let furnitureMap = [];
let timeouts = [];
let currentUser;
let pageHTML;
let images = importAllFurnitureImg();

const FurnitureList = async (id) => {
  currentUser = getUserSessionData();

  pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12 px-0">${generateLoadingAnimation()}</div>`;
  page.innerHTML = pageHTML;
  mainPage = document.querySelector("#mainPage");
  await findFurnitureList();

  if (!id) {
    pageHTML = generatePageHtml();

    mainPage.innerHTML = pageHTML;

    document.querySelectorAll(".toBeClicked").forEach(
        element => element.addEventListener("click", displayShortElements));
    document.querySelector("#buttonReturn").addEventListener("click",
        displayLargeTable);
  } else {
    loadCard(id);
  }
}

/**
 * Loads the furnitureList & furnitureMap from backend
 */
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
    furnitureList.forEach(furniture => {
      furnitureMap[furniture.furnitureId] = furniture;
    });
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

/**
 * Reloads the page and re-fetch furniture information.
 * Displays loading animation while awaiting the fetch.
 */
const reloadPage = async () => {
  mainPage.innerHTML = generateLoadingAnimation();
  await findFurnitureList();
  mainPage = generatePageHtml();
}

const removeTimeouts = () => {
  timeouts.forEach(timeout => {
    clearTimeout(timeout);
  })
}

const generatePageHtml = (largeTable = true) => {
  let tableSize = "large";
  let notNeededClassName = "notNeeded align-middle";
  let shortElementClassName = "shortElement d-none";
  if (largeTable === false) {
    tableSize = "short";
    notNeededClassName = "notNeeded d-none";
    shortElementClassName = "shortElement";
  }
  let res = `
  <div id="${tableSize}TableContainer" class="px-0">
    <div>
      <button type="button" id="buttonReturn" class="btn btn-dark m-3 ${shortElementClassName}">Retour à la liste</button>
      <table id="${tableSize}Table" class="table table-hover border border-1 text-center">
        <thead class="table-secondary">
          <tr class="">
            <th class="w-25"></th>
            <th class="align-middle">Description</th>
            <th class="${notNeededClassName}">Type</th>
            <th class="align-middle">État</th>
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
    if (!furnitureMap[furniture.furnitureId]) {
      furnitureMap[furniture.furnitureId] = furniture;
    } else if (furniture !== furnitureMap[furniture.furnitureId]) {
      furniture = furnitureMap[furniture.furnitureId];
    }
    res += generateRow(furniture, notNeededClassName);
    furnitureMap[furniture.furnitureId] = furniture;
  });
  return res;
}

const generateRow = (furniture, notNeededClassName) => {
  let statusHtml;
  let thumbnailClass = "mx-auto";
  if (!notNeededClassName.includes("d-none")) { //large table
    statusHtml = generateColoredStatus(furniture);
    thumbnailClass += " w-50"
  } else { //short table
    let infos = generateStatusInfos(furniture.status);
    statusHtml = generateDot(infos.classname);
    thumbnailClass += " w-100"
  }
  let res = `
    <tr class="toBeClicked" furnitureId="${furniture.furnitureId}">
      <th><div id="thumbnail" class="${thumbnailClass}">${generateFavouritePhotoImgTag(
      furniture)}<div></th>
      <th class="align-middle"><p>${furniture.description}</p></th>
      <th class="${notNeededClassName}"><p>${furniture.type}</p></th>
      <th class="tableStatus text-center align-middle" status="${furniture.status}">${statusHtml}</th>
      <th class="${notNeededClassName}"><p>${generateSellerLink(furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateBuyerLink(furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateSellingPriceTableElement(
      furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateSpecialPriceTableElement(
      furniture)}</p></th>
    </tr>`;
  return res;
}

const generateFavouritePhotoImgTag = (furniture) => {
  return `<img class="img-fluid" src="${findFavImgSrc(furniture,
      images)}" alt="thumbnail id:${furniture.favouritePhoto.photoId}"/>`;
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
  return `<a href="#" userId="${user.id}" class="userLink">${user.username}</a>`;
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

const generateStatusInfos = (status) => {
  let res = {
    classname: "",
    status: "",
  }

  switch (status) {
    case "AVAILABLE_FOR_SALE":
      res.classname = "success";
      res.status = "Disponible à la vente";
      break;
    case "ACCEPTED":
      res.classname = "info";
      res.status = "Accepté";
      break;
    case "IN_RESTORATION":
      res.classname = "warning";
      res.status = "En restauration";
      break;
    case "UNDER_OPTION":
      res.classname = "danger";
      res.status = "Sous option";
      break;
    case "SOLD":
      res.classname = "danger";
      res.status = "Vendu";
      break;
    case "WITHDRAWN":
      res.classname = "dark";
      res.status = "Retiré de la vente";
      break;
    case "REFUSED":
      res.classname = "dark";
      res.status = "Refusé";
      break;
    case "REQUESTED_FOR_VISIT":
    case "RESERVED":
    case "DELIVERED":
    case "COLLECTED":
    default:
      res.classname = "";
      res.status = status;
  }
  return res;
}

const generateColoredStatus = (furniture) => {
  let infos = generateStatusInfos(furniture.status);
  return `<p class="text-${infos.classname}">${infos.status}</p>`;
}

//input: "primary", "secondary", "info", etc...
const generateDot = (colorClassName) => {
  return `<span class="badge badge-pill p-1 badge-${colorClassName}"> </span>`;
}

const generateBadgeStatus = (furniture) => {
  let infos = generateStatusInfos(furniture.status);
  let res = `<span class="badge badge-pill badge-${infos.classname} text-light">${infos.status}</span>`;
  return res;
}

const displayShortElements = async (e) => {
  removeTimeouts();
  //hide large table
  let largeTable = document.querySelector('#largeTable');
  if (largeTable !== null) {
    largeTable.id = "shortTable";
  }
  if (document.querySelector('#largeTableContainer') !== null) {
    timeouts.push(setTimeout(changeContainerId, 1000));
  }
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

  document.querySelectorAll(".toBeClicked").forEach(element => {
    element.className = "toBeClicked";
  });

  let element = e.srcElement;
  while (!element.className.includes("toBeClicked")) {
    element = element.parentElement;
  }
  element.className = "toBeClicked bg-secondary text-light";

  document.querySelectorAll(".tableStatus").forEach(element => {
    let status = element.attributes["status"].value;
    let classname = generateStatusInfos(status).classname;
    element.innerHTML = generateDot(classname);
  });
  let id = element.attributes["furnitureId"].value;
  let furniture = furnitureMap[id];
  if (!furniture) {
    await reloadPage();
  }
  if (furniture) {
    generateCard(furniture);
    document.querySelectorAll(".userLink").forEach(
        (link) => link.addEventListener("click", onUserLinkClicked))
  } else {
    displayErrorMessage("errorDiv", new Error("Meuble introuvable :'<"));
  }
}

const onUserLinkClicked = (e) => {
  e.preventDefault();
  let link = e.target;
  let userId = link.getAttribute("userid");
  console.log(`Linking to user card (id: ${userId})`);
  RedirectUrl("/users", userId);
}

const displayLargeTable = () => {
  document.querySelector('#shortTableContainer').id = "largeTableContainer";
  timeouts.push(setTimeout(displayLargeElements, 750));
  document.querySelectorAll(".shortElement").forEach(
      element => element.className = "shortElement d-none");
  document.querySelector('#shortTable').id = "largeTable";
  document.querySelectorAll(".toBeClicked").forEach(
      element => element.className = "toBeClicked");
  document.querySelector("#buttonReturn").className = "btn btn-dark m-3 d-none";
  document.querySelectorAll("#thumbnail").forEach(
      element => element.className = "w-50 mx-auto");
  document.querySelectorAll(".tableStatus").forEach(element => {
    let status = element.getAttribute("status");
    let infos = generateStatusInfos(status);
    element.innerHTML = `<p class="text-${infos.classname}">${infos.status}</p>`
  })
}

const displayLargeElements = () => {
  document.querySelectorAll('.notNeeded').forEach(
      element => element.className = "notNeeded align-middle");
}

const generateCard = (furniture) => {
  let furnitureCardDiv = document.querySelector("#furnitureCardDiv");
  let cardHTML = generateCardHTML(furniture);
  furnitureCardDiv.innerHTML = cardHTML;
  addTransitionBtnListeners(furniture);
  addImage(furniture);

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
                <p class="proile-rating">ÉTAT : <span id="statusCardEntry">${generateBadgeStatus(
      furniture)}</span></p>
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
              ${addImgForm(furniture)}
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

const addImgForm=(furniture)=>{
  let res=`<div className="row text-left">
    <div className="col-md-6">
      <label> Ajouter une photo:</label>
    </div>
    <div className="col-md-6">
      <input type='file' id='addImg'>
        <button className="btn btn-primary" id="addImg"></button>
    </div>
  </div>`;
  return res;
}

const addImage=(furniture)=>{
  document.getElementById("addImg").addEventListener("change",()=>{
    var FR= new FileReader();
    var base64;
    var file = document.querySelector('input[type=file]').files[0];
    FR.addEventListener("load", function(e) {
      base64= e.target.result ;
    });
    if(file) {
      FR.readAsDataURL(file);
    }
  });

}


const generatePhotoList = (furniture) => {
  let photos = "";
  furniture.photos.forEach(photo => {
    photos += `<img class="img-fluid flex-grow-1 p-1" src="${findFurnitureImgSrcFromFilename(
        photo.source, images)}" alt="photo id:${photo.photoId}"/>`;
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
      <p id="${id}">${generateUserLink(
      user)} (${user.firstName} ${user.lastName})</p>
    </div>
  </div>`;
  return res;
}

const generateSellerCardEntry = (furniture) => {
  let res = "";
  if (furniture.seller) {
    res = generateUserCardEntry("Vendeur", "sellerCardEntry", furniture.seller);
  }
  return res;
}

const generateBuyerCardEntry = (furniture) => {
  let res = "";
  if (furniture.buyer) {
    res = generateUserCardEntry("Acheteur", "buyerCardEntry", furniture.buyer);
  }
  return res;
}

const generateOptionCardEntry = (furniture) => {
  let res = "";
  if (furniture.option) {
    res = generateUserCardEntry("Client intéressé", "optionUserCardEntry",
        furniture.option.user);
  }
  return res;
}

const generateSellingPriceCardEntry = (furniture) => {
  if (furniture.sellingPrice) {
    return generateCardLabelKeyEntry("Prix de vente", "sellingPriceCardEntry",
        furniture.sellingPrice + "€");
  }
  return "";
}

const generateSaleWithdrawalDateCardEntry = (furniture) => {
  if (furniture.saleWithdrawalDate) {
    return generateCardLabelKeyEntry("Date de retrait de la vente",
        "WithdrawalDateCardEntry", furniture.saleWithdrawalDate);
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
  switch (furniture.status) {
    case "ACCEPTED":
      res += generateTransitionModal("ToAvailable",
          "Indiquer disponible à la vente");
      res += generateTransitionModal("ToRestoration",
          "Indiquer en restauration");
      break;
    case "AVAILABLE_FOR_SALE":
      res += generateTransitionModal("ToSold", "Indiquer vendu");
      res += generateTransitionModal("Withdraw", "Retirer de la vente",
          "danger", "secondary");
      break;
    case "IN_RESTORATION":
      res += generateTransitionModal("ToAvailable",
          "Indiquer disponible à la vente");
      res += generateTransitionModal("Withdraw", "Retirer de la vente",
          "danger", "secondary");
      break;
    case "UNDER_OPTION":
    case "SOLD":
    case "WITHDRAWN":
    case "REQUESTED_FOR_VISIT":
    case "REFUSED":
    case "RESERVED":
    case "DELIVERED":
    case "COLLECTED":
    default:
  }
  return res;
}

const generateModalBodyFromTransitionId = (transitionId) => {
  switch (transitionId) {
    case "ToAvailable":
      return generateToAvailableForm();
    case "ToRestoration":
      return "Voulez-vous vraiment indiquer ce meuble comme allant en restauration ?"
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

const generateTransitionModal = (id, label, triggerColorClass = "primary",
    closeColorClass = "danger") => {
  let body = generateModalBodyFromTransitionId(id);
  let sendBtn = generateCloseBtn(label, "btn" + id,
      `btn btn-${triggerColorClass} mx-5 transitionBtn`);
  return generateModalPlusTriggerBtn("modal_" + id, label,
      `btn btn-${triggerColorClass}`, `<h4>${label}</h4>`, body, `${sendBtn}`,
      "Fermer", `btn btn-${closeColorClass}`);
}

const addTransitionBtnListeners = (furniture) => {
  document.querySelectorAll(".transitionBtn").forEach(element => {
    element.addEventListener("click",
        findTransitionMethod(element.id, furniture));
  })
}

const findTransitionMethod = (btnId, furniture) => {
  switch (btnId) {
    case "btnToAvailable":
      return (e) => toAvailable(e, furniture);
    case "btnToRestoration":
      return (e) => toRestoration(e, furniture);
    case "btnWithdraw":
      return (e) => withdraw(e, furniture);
    default:
      return (e) => {
        e.preventDefault();
        console.log("unrecognized button id: " + btnId); //'do nothing' method
      };
  }
  ;
}

//status transition methods


const toAvailable = (e, furniture) => { //TODO
  e.preventDefault();
  let sellingPrice = e.target.parentElement.parentElement.querySelector(
      "#sellingPriceInput").value;
  let bundle = {
    selling_price: sellingPrice,
  };
  fetch("/furniture/available/" + furniture.furnitureId, {
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
  fetch("/furniture/restoration/" + furniture.furnitureId, {
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
  fetch("/furniture/withdraw/" + furniture.furnitureId, {
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
  document.querySelector("#buttonReturn").addEventListener("click",
      displayLargeTable);
}

const sendImg= (e,base64)=>{
  e.preventDefault();
  fetch()

}





export default FurnitureList;