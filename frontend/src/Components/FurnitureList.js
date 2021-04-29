import notFoundPhoto from "../img/notFoundPhoto.png";
import {RedirectUrl} from "./Router";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js"
import {getUserSessionData} from "../utils/session.js";
import {
  displayErrorMessage,
  generateLoadingAnimation,
} from "../utils/utils.js"
let page = document.querySelector("#page");
let mainPage;
let furnitureList;
let typeList;
let furnitureMap = [];
let timeouts = [];
let currentUser;
let isDisplayingLargeTable; //state of display (true = large list, false = furniture card)
let currentFurnitureId; //to read only if largeTable === false
let openTab = "infos";
const emptyFilter = {
  username: "",
  price: "-1",
  status: "",
}
let activeFilters = {... emptyFilter};

const FurnitureList = async (id) => {
  currentUser = getUserSessionData();

  let pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12 px-0">${generateLoadingAnimation()}</div>`;
  page.innerHTML = pageHTML;
  mainPage = document.querySelector("#mainPage");
  await findFurnitureList();
  await findTypeList();

  if (!id) {
    generateLargeTablePage();
  } else {
    loadCard(id);
  }
  displayNoResultMsg();
}

const generateLargeTablePage = () => {
  isDisplayingLargeTable = true;
  let pageHTML = generatePageHtml();
  mainPage.innerHTML = pageHTML;

  placeFilterForm();
  document.querySelectorAll(".toBeClicked").forEach(
    element => element.addEventListener("click", displayShortElements));
  document.querySelector("#buttonReturn").addEventListener("click",displayLargeTable);
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
 * Fetches all furniture types and updates typeList
 */
const findTypeList = async () => {
  try {
    let response = await fetch("/furnitureTypes/", {
      method: "GET",
    });
    if(!response.ok) {
      throw new Error(response.status + " : " + response.statusText);
    }
    let data = await response.json();
    typeList = data;
  } catch (err) {
    displayErrorMessage("errorDiv", err);
  }
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
  <div class="container-fluid px-5 py-3 border border-top-0 border-right-0 border-left-0">
    <h3>Filtrer les meubles:</h3>
    <form class="form-inline">
      <div class="form-group mx-3">
        <input type="text" class="form-control" id="username-filter" placeholder="Filtrer par pseudo"/>
      </div>
      <div class="form-group mx-3">
        <select class="form-control" id="price-filter">
          <option value="-1">Filtrer les prix</option>
          <option value="1">1-10€</option>
          <option value="10">11-100€</option>
          <option value="100">101-500€</option>
          <option value="500">501-1000€</option>
          <option value="1000">+1000€</option>
        </select>
      </div> 
      <div class="form-group mx-3">
        <select class="form-control" id="status-filter">
          <option value="">Filtrer les états</option>
          <option value="REQUESTED_FOR_VISIT">En demande de visite</option>
          <option value="ACCEPTED">Accepté</option>
          <option value="IN_RESTORATION">En restauration</option>
          <option value="AVAILABLE_FOR_SALE">Disponible à la vente</option>
          <option value="UNDER_OPTION">Sous option</option>
          <option value="SOLD">Vendu</option>
          <option value="WITHDRAWN">Retiré de la vente</option>
          <option value="REFUSED">Refusé</option>
        </select>
      </div>
      <button type="submit" id="apply-filters-btn" class="btn btn-primary mx-2">Appliquer</button>
      <button type="submit" id="clear-filters-btn" class="btn btn-secondary mx-2">Retirer les filtres</button>
    </form>
  </div>
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
        <tbody id="furniture-list-body">${generateAllRows(notNeededClassName)}</tbody>
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
    if(respectsAllActiveFilters(furniture)){
      res += generateRow(furniture, notNeededClassName);
    }
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

/**
 * used in list
 * @param {*} furniture
 * @returns html
 */
const generateFavouritePhotoImgTag = (furniture) => {
  if(!furniture.favouritePhoto) {
    return `<img class="img-fluid" src="${notFoundPhoto}" alt="not found photo" furnitureId="${furniture.furnitureId}" id="favPhoto"/>`
  }
  return `<img class="img-fluid" src="${furniture.favouritePhoto.source}" alt="thumbnail id:${furniture.favouritePhoto.photoId}" furnitureId="${furniture.furnitureId}" id="list-fav-photo" original_fav_id="${furniture.favouritePhoto.photoId}"/>`;
}

/**
 * used in card
 * @param {*} furniture
 * @returns html
 */
const generateCardFavouritePhotoImgTag = (furniture) => {
  if(!furniture.favouritePhoto) {
    return `<img class="img-fluid" src="${notFoundPhoto}" alt="not found photo" furnitureId="${furniture.furnitureId}" id="favPhoto"/>`
  }
  return `<img class="img-fluid" src="${furniture.favouritePhoto.source}" alt="thumbnail id:${furniture.favouritePhoto.photoId}" furnitureId="${furniture.furnitureId}" id="card-fav-photo" original_fav_id="${furniture.favouritePhoto.photoId}"/>`;
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
    res = `<p>${furniture.specialSalePrice}€</p>`;
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
    currentFurnitureId = id;
    generateCard(furniture);
    document.querySelectorAll(".userLink").forEach(
        (link) => link.addEventListener("click", onUserLinkClicked));
      isDisplayingLargeTable = false;
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
  isDisplayingLargeTable = true;
  openTab = "infos";
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
  document.querySelectorAll(".favRadio").forEach((element) => {
    element.addEventListener("click", onFavRadioSelected);
  });
  document.querySelectorAll(".visibleCheckbox").forEach((element) => {
    element.addEventListener("click", onVisibleCheckClicked);
  });
  document.querySelector("#saveBtnPhoto").addEventListener("click", onSaveModifPhotos);
  document.querySelector("#home-tab").addEventListener("click", () => {
    openTab = "infos";
  });
  document.querySelector("#profile-tab").addEventListener("click", () => {
    openTab = "photos";
  });
  document.querySelectorAll(".input-furniture-info").forEach((input) => {
    input.addEventListener("change", updateSaveInfoBtn);
  })
  addImage(furniture);
}

const updateSaveInfoBtn = () => {
  let saveInfoBtn = document.querySelector("#save-info-btn");
  if(verifyDifferentInfo()) {
    saveInfoBtn.disabled = false;
  } else {
    saveInfoBtn.disabled = true;
  }
}

/**
 * verifies if the save info btn should be disabled or not
 * @returns true -> enabled / false -> disabled
 */
const verifyDifferentInfo = () => {
  let inputDescription = document.querySelector("#input-description");
  let selectType = document.querySelector("#select-type");
  let inputSellingPrice = document.querySelector("#input-selling-price");

  let originalDescriptionInput = document.querySelector("#original-description");
  let originalTypeInput = document.querySelector("#original-type-id");
  let originalSellingPrice = document.querySelector("#original-selling-price");

  let bundle = {};
  //description
  let newDesc = inputDescription.value;
  let oldDesc = originalDescriptionInput.value;
  if(newDesc !== oldDesc) {
    if(newDesc !== ""){
      return true;
    }
  }
  //type id
  let newTypeId = selectType.value;
  let oldTypeId = originalTypeInput.value;
  if(newTypeId !== oldTypeId) {
    return true;
  }
  //selling price
  if(inputSellingPrice) {
    let newSellingPrice = inputSellingPrice.value;
    let oldSellingPrice = originalSellingPrice.value;
    if(newSellingPrice !== oldSellingPrice) {
      return true;
    }
  }
  return false;
}

const changeContainerId = () => {
  document.querySelector('#largeTableContainer').id = "shortTableContainer";
}

const generateCardHTML = (furniture) => {
  const openTabObject = {
    ariaSelected: "true",
    tabClassname: "show active",
    aClassname: "active",
  }

  const closedTabObject = {
    ariaSelected: "false",
    tabClassname: "",
    aClassname: "",
  }

  let infoTab = openTabObject;
  let photoTab = closedTabObject;
  if(openTab === "photos"){
    infoTab = closedTabObject;
    photoTab = openTabObject;
  }
  let res = `
  <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-12">
          <div class="profile-head">
            <div class="row">
              <div class="col-md-6">
                <p>${generateCardFavouritePhotoImgTag(furniture)}</p>
              </div>
              <div class="col-md-6 text-left">
                <h5 id="descriptionCardEntry">${generateFurnitureDescriptionCardEntry(furniture)}</h5>
                <p class="proile-rating">ÉTAT : <span id="statusCardEntry">${generateBadgeStatus(
      furniture)}</span></p>
              </div>
            </div>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
              <li class="nav-item">
                <a class="nav-link ${infoTab.aClassname}" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="${infoTab.ariaSelected}">Information</a>
              </li>
              <li class="nav-item">
                <a class="nav-link ${photoTab.aClassname}" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="${photoTab.ariaSelected}">Photos</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
  
      <div class="row">
        <div class="col-md-8">
          <div class="tab-content profile-tab" id="myTabContent">
            <div class="tab-pane fade ${infoTab.tabClassname}" id="home" role="tabpanel" aria-labelledby="home-tab">
              ${generateTypeCardEntry(furniture)}
              ${generateBuyingPriceCardEntry(furniture)}
              ${generateBuyingDateCardEntry(furniture)}
              ${generateSellerCardEntry(furniture)}
              ${generateSellingPriceCardEntry(furniture)}
              ${generateSpecialSalePriceCardEntry(furniture)}
              ${generateBuyerCardEntry(furniture)}
              ${generateOptionCardEntry(furniture)}
              ${generateSaleWithdrawalDateCardEntry(furniture)}
              ${generateSaveInfoBtn()}
              ${generateButtonRow(furniture)}
            </div>       
            <div class="tab-pane fade ${photoTab.tabClassname}" id="profile" role="tabpanel" aria-labelledby="profile-tab">
              ${addImgForm()}
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

const addImgForm = () => {
  let res = `<div className="row text-left">
    <div className="col-md-6">
      <label> Ajouter une photo:</label>
    </div>
    <div className="col-md-6">
      <input type='file' id='addImg'>
      <button class="btn btn-primary" id="sendImg"> Ajouter</button>
    </div>  
    <hr/>
  </div>`;
  return res;
}

/**
 * load the image and transform it to base64  then  listen the click on accept to send the image
 * 
 * @param {*} furniture 
 */
const addImage = (furniture) => {
  document.getElementById("addImg").addEventListener("change", () => {
    var FR = new FileReader();
    let bas;
    var file = document.querySelector('input[type=file]').files[0];
      if (typeof (FR) != "undefined") {
        FR.addEventListener("load", function (e) {
          bas = e.target.result;
          document.getElementById("sendImg").addEventListener("click",(e) => {
            e.preventDefault();
            fetching(bas, furniture.furnitureId);
          });
        });
        if (file) {
          FR.readAsDataURL(file);
        }
        //addImgForm();
      }else{
        throw new Error('le navigateur ne supporte pas FileReader');
      }
    addImgForm();
  });
}

const fetching = async (base64, furnitureId) => {
  let toSend = {
    source: base64,
    furnitureId: furnitureId
  }
  let pers = getUserSessionData();
  let response = await fetch("/photos/", {
    method: "POST",
    body: JSON.stringify(toSend),
    headers: {
      Authorization: pers.token,
      "Content-Type": "application/json",
    }
  });
  if(!response.ok) {
    displayErrorMessage("errorDiv", new Error("Erreur de fetch :<"));
    return;
  }
  let data = await response.json();
  furnitureMap[furnitureId].photos.push(data);
  loadCard(furnitureId);
}

const generatePhotoList = (furniture) => {
  let photos = "";
  furniture.photos.forEach(photo => {
    let favRadioName = `radioFav${photo.photoId}`;
    let visibleCheckName = `checkboxVisible${photo.photoId}`;
    let homePageCheckName = `checkboxHomepage${photo.photoId}`;

    let favChecked = ``;
    if(furniture.favouritePhoto && photo.photoId === furniture.favouritePhoto.photoId) {
      favChecked = `checked`;
    }

    let visibleCheckedOriginaly = false;
    let homePageCheckedOriginaly = false;
    let visibileChecked = ``;
    let homePageChecked = ``;
    if(photo.isVisible) {
      visibileChecked = `checked`;
      visibleCheckedOriginaly = true;

      if(photo.onHomePage && photo.isVisible) {
        homePageChecked = `checked`;
        homePageCheckedOriginaly = true;
      }
    }else {
      homePageChecked = `disabled`;
    }

    photos += `
    <div class="p-1 w-50 container photo-list-container" photoId=${photo.photoId}>
      <div class="row px-0">
        <div class="col-6">
          <img class="img-fluid" src="${photo.source}" alt="photo id:${photo.photoId}"/>
        </div>
        <div class="text-left col-6">
          <label class="form-check-label" for="${favRadioName}">
            <input id="${favRadioName}" type="radio" class="form-check-input favRadio" name="${favRadioName}" photoId="${photo.photoId}" furnitureid="${photo.furnitureId}" ${favChecked}>
            Photo favorite
          </label>
          <br/>
          <label class="form-check-label" for="${visibleCheckName}">
            <input id="${visibleCheckName}" type="checkbox" class="form-check-input visibleCheckbox" name="${visibleCheckName}" photoId=${photo.photoId} checked_originaly="${visibleCheckedOriginaly}" ${visibileChecked}>
            Visible
          </label>
          <br/>
          <label class="form-check-label" for="${homePageCheckName}">
            <input id="${homePageCheckName}" type="checkbox" class="form-check-input homepageCheckbox" name="${homePageCheckName}" photoId=${photo.photoId} checked_originaly="${homePageCheckedOriginaly}" ${homePageChecked}>
            Affiché sur la page d'accueil
          </label>
        </div>
      </div>
    </div>`;
  });
  let pId
  if(!furniture.favouritePhoto){
    pId = "notFound";
  }else {
    pId = furniture.favouritePhoto.photoId;
  }
  let res = `
  <form>
    <input id="originalFav" type="hidden" photoId="${pId}" furnitureId="${furniture.furnitureId}"/>
    <div class="form-check d-flex flex-lg-fill flex-row">
      ${photos}
    </div>
    <button id="saveBtnPhoto" class="btn btn-primary my-5 float-right">Enregistrer les modifications</button>
  </form>`;
  return res;
}

const onFavRadioSelected = (e) => {
  unselectAllFavRadioExcept(e.target.id);
}

const unselectAllFavRadioExcept = (radioId) => {
  document.querySelectorAll(".favRadio").forEach((element) => {
    if(element.id !== radioId){
      element.checked = false;
    }
  })
}

const onVisibleCheckClicked = (e) => {
  let photoId = e.target.getAttribute("photoid");
  let homepageCheckbox = document.querySelector(`#checkboxHomepage${photoId}`);

  if(!e.target.checked) {
    homepageCheckbox.checked = false;
    homepageCheckbox.disabled = true;
    e.target.checked = false;
  }else {
    homepageCheckbox.disabled = false;
  }
}

const onSaveModifPhotos = async (e) => {
  e.preventDefault();
  let originalFav = document.querySelector("#originalFav");
  let originalFavPhotoId = originalFav.getAttribute("photoid");
  let furnitureId = originalFav.getAttribute("furnitureid");
  //fav
  let selectedFavId = findSelectedFav();
  if(originalFavPhotoId != selectedFavId) {
    let newFurniture = await patchNewFav(furnitureId, selectedFavId);
    furnitureMap[furnitureId] = newFurniture;
  }
  //display flags
  let modifiedPhotoArray = findAllPhotosForFlagUpdate();
  await patchDisplayFlagsAllPhotos(modifiedPhotoArray);
  //refresh card
  loadCard(furnitureId);
}

/**
 * Finds the selected favourite photo (between all radio buttons)
 * @returns photoId
 */
const findSelectedFav = () => {
  let res = null;
  document.querySelectorAll(".favRadio").forEach((radioBtn) => {
    if(radioBtn.checked) {
      res = radioBtn.getAttribute("photoid");
    }
  });
  return res;
}

/**
 * Finds all photos having at least one modified display flag
 *
 * @returns {Array} list of object
 * {
 *   photoId,
 *   isVisible,
 *   isOnHomePage,
 * },
 */
const findAllPhotosForFlagUpdate = () => {
  let arrayFound = new Array();
  document.querySelectorAll(".photo-list-container").forEach((container) => {
    let visibleCheckbox = container.querySelector(".visibleCheckbox");
    let homepageCheckbox = container.querySelector(".homepageCheckbox");
    let photoId = container.getAttribute("photoid");
    let isModified = false;

    let visibleChecked = "false";
    let homepageChecked = "false";
    if(visibleCheckbox.checked) {
      visibleChecked = "true";
    }
    if(!homepageCheckbox.disabled && homepageCheckbox.checked) {
      homepageChecked = "true";
    }

    if(visibleCheckbox.getAttribute("checked_originaly") != visibleChecked) {
      isModified = true;
    } else if(homepageCheckbox.getAttribute("checked_originaly") != homepageChecked) {
      isModified = true;
    }

    if(isModified) {
      let bundle = {
        photoId: photoId,
        isVisible: visibleCheckbox.checked,
        isOnHomePage: homepageCheckbox.checked,
      }
      arrayFound.push(bundle);
    }
  });
  return arrayFound;
}

/**
 * [Fetch] PATCH new favourite photo
 *
 * @param {int} furnitureId
 * @param {int} favPhotoId
 * @returns {*} new photo obj.
 */
const patchNewFav = async (furnitureId, favPhotoId) => {
  let bundle =  {
    photoId: favPhotoId,
  };
  let response = await fetch("/furniture/favouritePhoto/"+furnitureId, {
    method: "PATCH",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  });
  if(!response.ok) {
    displayErrorMessage("errorDiv", new Error(
      response.status + " : " + response.statusText
    ));
    return;
  }
  let data = await response.json();
  return data;
}

/**
 * [Fetch] PATCH new display flags for one photo
 *
 * @param {
 *  photoId,
 *  isVisible,
 *  isOnHomePage,
 * } bundle
 * @return new photo (fetch response)
 */
const patchDisplayFlags = async (bundle) => {
  let addr = "/photos/displayFlags/"+bundle.photoId;
  let response = await fetch(addr, {
    method: "PATCH",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  });
  if(!response.ok) {
    displayErrorMessage("errorDiv", new Error(
      response.status + " : " + response.statusText
    ));
    return;
  }
  let data = await response.json();
  return data;
}

/**
 * Performs all necesary fetches for photo display flags and updates furnitureMap accordingly.
 *
 * @param {Array} array result of findAllPhotosForFlagUpdate()
 */
const patchDisplayFlagsAllPhotos = async (array) => {
  for(const obj of array) {
    let newPhoto = await patchDisplayFlags(obj);
    //update furnitureMap
    if(newPhoto){
      let furnitureId = newPhoto.furnitureId;
      let photoIndex = findPhotoIndexById(furnitureMap[furnitureId].photos, newPhoto.photoId);
      if(photoIndex != -1){
        furnitureMap[furnitureId].photos[photoIndex] = newPhoto;
      }
    }
  }
}

/**
 * Finds the index of the first occurence of a photo in an array by id.
 *
 * @param {Array} photoArray
 * @param {int} photoId
 * @returns {int} index of photoId in photoArray or
 */
const findPhotoIndexById = (photoArray, photoId) => {
  return photoArray.findIndex((photo) => photo.photoId === photoId);
}

const generateCardLabelKeyEntry = (label, id, value) => {
  let p = `<p id="${id}">${value}</p>`;
  return generateCardLabelKeyEntryHtml(label, p);
}

const generateCardLabelKeyEntryHtml = (label, value) => {
  let res = `
  <div class="row text-left my-2">
    <div class="col-md-6">
      <label>${label}</label>
    </div>
    <div class="col-md-6">
      ${value}
    </div>
  </div>
  `;
  return res;
}

const generateFurnitureDescriptionCardEntry = (furniture) => {
  let input = `<textarea rows="3" id="input-description" class="input-furniture-info form-control w-100">${furniture.description}</textarea>
  <input type="hidden" id="original-description" value="${furniture.description}"/>`
  return input;
}

const generateTypeCardEntry = (furniture) => {
  let select = `
  <select id="select-type" class="input-furniture-info form-control">
    ${generateAllTypeOptions(furniture)}
  </select>
  <input type="hidden" id="original-type-id" value="${furniture.typeId}"/>`;
  return generateCardLabelKeyEntryHtml("Type", select);
}

const generateAllTypeOptions = (furniture) => {
  let res = "";
  for(const typeIndex in typeList) {
    let typeObject = typeList[typeIndex];
    let opt;
    if(furniture.typeId === typeObject.typeId) {
      opt = `<option value="${typeObject.typeId}" selected>${typeObject.typeName}</option>`;
    }else {
      opt = `<option value="${typeObject.typeId}">${typeObject.typeName}</option>`;
    }
    res += opt;
  }
  return res;
}

const generateBuyingPriceCardEntry = (furniture) => {
  let res = "";
  if(furniture.purchasePrice !== undefined) {
    res = generateCardLabelKeyEntry("Prix d'achat",
    "purchase-price-card-entry",
    `${furniture.purchasePrice}€`);
  }
  return res;
}

const generateBuyingDateCardEntry = (furniture) => {
  let res = "";
  if(furniture.customerWithdrawalDate !== undefined) {
    res = generateCardLabelKeyEntry("Date de retrait chez le vendeur",
    "purchase-price-card-entry",
    furniture.customerWithdrawalDate);
  }
  return res;
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
    if(furniture.status === "AVAILABLE_FOR_SALE"){
      let input = `
      <div class="input-group">
        <input type="number" min="0.01" step="0.01" class="form-control input-furniture-info w-lg-25 w-50" id="input-selling-price" value="${furniture.sellingPrice}"/>
        <div class="input-group-append">
          <span class="input-group-text">€</span>
        </div>
        <input type="hidden" id="original-selling-price" value="${furniture.sellingPrice}"/>
      </div>`;
      return generateCardLabelKeyEntryHtml("Prix de vente",
        input);
    }else {
      return generateCardLabelKeyEntry("Prix de vente", "sellingPriceCardEntry",
        furniture.sellingPrice + "€");
    }
  }
  return "";
}

const generateSpecialSalePriceCardEntry = (furniture) => {
  if (furniture.specialSalePrice) {
    return generateCardLabelKeyEntry("Prix de vente spécial", "specialSalePriceCardEntry",
        furniture.specialSalePrice + "€");
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

const generateSaveInfoBtn = () => {
  return `
  <div class="d-flex flex-row-reverse">
  <button type="submit" id="save-info-btn" class="btn btn-primary" disabled>Enregistrer les modifications</button>
  </div>`;
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
      res += generateTransitionModal("ToSold", "Indiquer vendu");
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
      return generateToSoldForm();
    case "Withdraw":
      return "Voulez-vous vraiment retirer ce meuble de la vente ? <br/><strong>Cette action est irréversible</strong>"
    default:
      return "not implemented yet";
  }
}

const generateToAvailableForm = () => {
  let res = `
    <div class="form-group">
      <label for="sellingPriceInput" class="mr-3">Prix de vente: </label>
      <input type="number" id="sellingPriceInput" class="w-25 mx-3 form-control" name="sellingPriceInput" min="0.01" step="0.01"/> €
    </div>
  `;
  return `<div class="form-inline">${res}</div>`;
}

const generateToSoldForm = () => {
  let furnitureId = currentFurnitureId;
  let furniture = furnitureMap[furnitureId];
  let status = furniture.status;
  let res = "";
  switch(status) {
    case "AVAILABLE_FOR_SALE":
      res = `
        <div class="form-group">
          <label for="buyerUsernameInput">Pseudo de l'acheteur: 
            <input type="text" id="buyerUsernameInput" class="w-25 mx-3 my-1 form-control" name="buyerUsernameInput"/>
          </label>
        </div>
        <div class="form-group">
          <label for="specialSalePriceInput">Prix spécial: 
            <input type="number" id="specialSalePriceInput" class="w-25 mx-3 my-1 form-control" name="specialSalePriceInput" min="0.01" step="0.01"/> €
          </label>
        </div>`;
      break;
    case "UNDER_OPTION":
      res = `
        <div class="form-group">
          Confirmer que le meuble a été vendu au client intéressé ?
        </div>`;
      break;
    default:
  }
  return `<div class="form-inline">${res}</div>`;
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
  });
  document.querySelector("#save-info-btn").addEventListener("click", onSaveInfoBtnClicked)
}

const findTransitionMethod = (btnId, furniture) => {
  switch (btnId) {
    case "btnToAvailable":
      return (e) => toAvailable(e, furniture);
    case "btnToRestoration":
      return (e) => toRestoration(e, furniture);
    case "btnWithdraw":
      return (e) => withdraw(e, furniture);
    case "btnToSold":
        return (e) => toSold(e, furniture);
    default:
      return (e) => {
        e.preventDefault();
        console.log("unrecognized button id: " + btnId); //'do nothing' method
      };
  };
}

const onSaveInfoBtnClicked = async (e) => {
  e.preventDefault();
  let inputDescription = document.querySelector("#input-description");
  let selectType = document.querySelector("#select-type");
  let inputSellingPrice = document.querySelector("#input-selling-price");

  let originalDescriptionInput = document.querySelector("#original-description");
  let originalTypeInput = document.querySelector("#original-type-id");
  let originalSellingPrice = document.querySelector("#original-selling-price");

  let bundle = {};
  //description
  let newDesc = inputDescription.value;
  let oldDesc = originalDescriptionInput.value;
  if(newDesc !== oldDesc) {
    if(newDesc !== ""){
      bundle = {
        ...bundle, 
        description: newDesc,
      }
    }
  }
  //type id
  let newTypeId = selectType.value;
  let oldTypeId = originalTypeInput.value;
  if(newTypeId !== oldTypeId) {
    bundle = {
      ...bundle, 
      typeId: newTypeId,
    }
  }
  //selling price
  if(inputSellingPrice) {
    let newSellingPrice = inputSellingPrice.value;
    let oldSellingPrice = originalSellingPrice.value;
    if(newSellingPrice !== oldSellingPrice) {
      bundle = {
        ...bundle, 
        sellingPrice: newSellingPrice,
      }
    }
  }
  if(bundle !== {}) {
    try {
      let response = await fetch("furniture/infos/"+currentFurnitureId, {
        method: "PATCH",
        body: JSON.stringify(bundle),
        headers: {
          "Authorization": currentUser.token,
          "Content-Type": "application/json",
        },
      });
      if(!response.ok) {
        throw new Error(response.status + " : " + response.statusText);
      }
      let data = await response.json();
      furnitureMap[data.furnitureId] = data;
      loadCard(data.furnitureId);
    }catch(err) {
      displayErrorMessage("errorDiv", err);
    }
  }
}

//status transition methods

const toAvailable = (e, furniture) => {
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

const toRestoration = (e, furniture) => {
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

const withdraw = (e, furniture) => {
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

const toSold = async (e, furniture) => {
  e.preventDefault();
  let specialSalePrice = "";
  let buyerUsername;
  let bundle;
  if(furniture.status === "AVAILABLE_FOR_SALE"){
    specialSalePrice = e.target.parentElement.parentElement.querySelector("#specialSalePriceInput").value;
    buyerUsername = e.target.parentElement.parentElement.querySelector("#buyerUsernameInput").value;
  }else if(furniture.status === "UNDER_OPTION"){
    buyerUsername = furniture.option.user.username;
  }
  if(specialSalePrice !== "") {
    bundle = {
      buyerUsername: buyerUsername,
      specialSalePrice: specialSalePrice,
    }
  }else {
    bundle = {
      buyerUsername: buyerUsername,
    }
  }
  fetch("/furniture/sold/" + furniture.furnitureId, {
    method: "PATCH",
    body: JSON.stringify(bundle),
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
    loadCard(data.furnitureId);
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });

}

const loadCard = (furnitureId) => {
  isDisplayingLargeTable = false;
  currentFurnitureId = furnitureId;
  mainPage.innerHTML = generatePageHtml(false);
  generateCard(furnitureMap[furnitureId]);
  document.querySelectorAll(".toBeClicked").forEach(
    (element) => {
      let elementFurnId = element.getAttribute("furnitureid");
      if(elementFurnId == furnitureId) {
        element.className = "toBeClicked bg-secondary text-light";
      }
      element.addEventListener("click", displayShortElements)
    });
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
  placeFilterForm();
}

/**
 * Verifies if a piece of furniture respects all active filters.
 *
 * @param {*} furniture : full furniture object
 * @returns {boolean} true if furniture respects all active filters.
 */
const respectsAllActiveFilters = (furniture) => {
  let res = true;
  res = res && respectsUserFilter(furniture);
  res = res && respectsPriceFilters(furniture);
  res = res && respectsStatusFilter(furniture);
  return res;
}

/**
 * Verifies if a piece of furniture respects the active username filter.
 * (filter is contained inside of the seller/buyer's username)
 *
 * @param {*} furniture full furniture object
 * @returns {boolean} true if furniture respects active username filter.
 */
const respectsUserFilter = (furniture) => {
  if (activeFilters.username === "") {
    return true; //inactive filter -> TRUE
  }
  if(!furniture.seller && !furniture.buyer) return false; // active filter + no user -> FALSE

  if(furniture.seller !== undefined) {
    if(furniture.seller.username.includes(activeFilters.username)) {
      return true;
    }
  }
  if(!furniture.buyer){
    return false;
  }else {
    if(furniture.buyer.username.includes(activeFilters.username)) {
      return true;
    }
  }
  return false;
}

/**
 * Verifies if a piece of furniture respects the active price filters.
 *
 * @param {*} furniture
 * @returns {boolean} true if furniture respects active price filters.
 */
const respectsPriceFilters = (furniture) => {
  if(activeFilters.price === "-1") return true; //inactive filters
  let minPrice = -1;
  let maxPrice = -1;
  switch(activeFilters.price) {
    case "1":
      minPrice = 1;
      maxPrice = 10;
      break;
    case "10":
      minPrice = 11;
      maxPrice = 100;
      break;
    case "100":
      minPrice = 101;
      maxPrice = 500;
      break;
    case "500":
      minPrice = 501;
      maxPrice = 1000;
      break;
    case "1000":
      minPrice = 1001;
      maxPrice = -1;
      break;
    default:
  }
  //active filters
  if(!furniture.sellingPrice) return false; //no price

  if(minPrice !== -1 && furniture.sellingPrice < minPrice) return false; //under minPrice
  if(maxPrice !== -1 && furniture.sellingPrice > maxPrice) return false; //above maxPrice

  return true;
}

/**
 * Verifies if a piece of furniture respects the active status filter.
 *
 * @param {*} furniture full furniture object
 * @returns {boolean} true if furniture respects active status filter.
 */
const respectsStatusFilter = (furniture) => {
  if(activeFilters.status === "") return true; //inactive filter
  return furniture.status === activeFilters.status;
}

/**
 * remove all applied filter, then refresh display
 */
const clearFilters = (e) => {
  e.preventDefault();
  activeFilters = {... emptyFilter};
  refreshDisplay();
}

const applyFilters = (e) => {
  e.preventDefault();

  let usernameInput = document.querySelector("#username-filter");
  let priceInput = document.querySelector("#price-filter");
  let statusInput = document.querySelector("#status-filter");

  activeFilters.username = usernameInput.value;
  activeFilters.price = priceInput.value;
  activeFilters.status = statusInput.value;

  refreshDisplay();
}

/**
 * Sets the filter form to the current active filter values,
 * and adds its necessary eventListeners
 */
const placeFilterForm = () => {

  let usernameInput = document.querySelector("#username-filter");
  let priceInput = document.querySelector("#price-filter");
  let statusInput = document.querySelector("#status-filter");

  usernameInput.value = activeFilters.username;
  priceInput.value = activeFilters.price;
  statusInput.value = activeFilters.status;

  let applyFiltersBtn = document.querySelector("#apply-filters-btn");
  let clearFiltersBtn = document.querySelector("#clear-filters-btn");

  applyFiltersBtn.addEventListener("click", applyFilters);
  clearFiltersBtn.addEventListener("click", clearFilters);
}

/**
 * Refresh the current display
 */
const refreshDisplay = () => {
  if(isDisplayingLargeTable === false && typeof(currentFurnitureId) !== undefined){
    let furnitureId = currentFurnitureId;
    loadCard(furnitureId);
  }else {
    generateLargeTablePage();
  }
  placeFilterForm();
  displayNoResultMsg();
}

/**
 * Displays a message in the table if it is empty
 */
const displayNoResultMsg = () => {
  let tbody = document.querySelector("#furniture-list-body");
  const noResultHTML = "";
  if (tbody.innerHTML === noResultHTML){
    tbody.innerHTML = `<th colspan="8"><p>Aucun résultat</p></th>`;
  }
}

export default FurnitureList;