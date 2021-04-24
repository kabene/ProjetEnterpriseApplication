import {RedirectUrl} from "./Router";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js"
import {getUserSessionData} from "../utils/session.js";
import {displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, findFavImgSrc, generateLoadingAnimation} from "../utils/utils.js"
import notFoundPhoto from "../img/notFoundPhoto.png";

let page = document.querySelector("#page");
let mainPage;
let furnitureList;
let furnitureMap = [];
let timeouts = [];
let currentUser;
let pageHTML;
let images = importAllFurnitureImg();
let openTab = "infos";

const FurnitureList = async (id) => {
  currentUser = getUserSessionData();

  pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12 px-0">${generateLoadingAnimation()}</div>`;
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
  if(largeTable === false) {
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
  let statusHtml;
  let thumbnailClass = "mx-auto";
  if(!notNeededClassName.includes("d-none")) { //large table
    statusHtml = generateColoredStatus(furniture);
    thumbnailClass += " w-50"
  }else { //short table
    let infos = generateStatusInfos(furniture.status);
    statusHtml = generateDot(infos.classname);
    thumbnailClass += " w-100"
  }
  let res = `
    <tr class="toBeClicked" furnitureId="${furniture.furnitureId}">
      <th><div id="thumbnail" class="${thumbnailClass}">${generateFavouritePhotoImgTag(furniture)}<div></th>
      <th class="align-middle"><p>${furniture.description}</p></th>
      <th class="${notNeededClassName}"><p>${furniture.type}</p></th>
      <th class="tableStatus text-center align-middle" status="${furniture.status}">${statusHtml}</th>
      <th class="${notNeededClassName}"><p>${generateSellerLink(furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateBuyerLink(furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateSellingPriceTableElement(furniture)}</p></th>
      <th class="${notNeededClassName}"><p>${generateSpecialPriceTableElement(furniture)}</p></th>
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
  return `<img class="img-fluid" src="${findFavImgSrc(furniture, images)}" alt="thumbnail id:${furniture.favouritePhoto.photoId}" furnitureId="${furniture.furnitureId}" id="list-fav-photo" original_fav_id="${furniture.favouritePhoto.photoId}"/>`;
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
  return `<img class="img-fluid" src="${findFavImgSrc(furniture, images)}" alt="thumbnail id:${furniture.favouritePhoto.photoId}" furnitureId="${furniture.furnitureId}" id="card-fav-photo" original_fav_id="${furniture.favouritePhoto.photoId}"/>`;
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
const generateDot = (colorClassName) =>{
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

  document.querySelectorAll(".tableStatus").forEach(element => {
    let status = element.attributes["status"].value;
    let classname = generateStatusInfos(status).classname;
    element.innerHTML = generateDot(classname);
  });
  let id = element.attributes["furnitureId"].value;
  let furniture = furnitureMap[id];
  if(!furniture) await reloadPage();
  if(furniture) {
    generateCard(furniture);
    document.querySelectorAll(".userLink").forEach((link) => link.addEventListener("click", onUserLinkClicked))
  }else {
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
  openTab = "infos";
  document.querySelector('#shortTableContainer').id = "largeTableContainer";
  timeouts.push(setTimeout(displayLargeElements, 750));
  document.querySelectorAll(".shortElement").forEach(element => element.className = "shortElement d-none");
  document.querySelector('#shortTable').id = "largeTable";
  document.querySelectorAll(".toBeClicked").forEach(element => element.className = "toBeClicked");
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
  document.querySelectorAll('.notNeeded').forEach(element => element.className = "notNeeded align-middle");
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
                <h5 id="descriptionCardEntry">${furniture.description}</h5>
                <p class="proile-rating">ÉTAT : <span id="statusCardEntry">${generateBadgeStatus(furniture)}</span></p>
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
              ${generateBuyerCardEntry(furniture)}
              ${generateOptionCardEntry(furniture)}
              ${generateSaleWithdrawalDateCardEntry(furniture)}

              ${generateButtonRow(furniture)}
            </div>       
            <div class="tab-pane fade ${photoTab.tabClassname}" id="profile" role="tabpanel" aria-labelledby="profile-tab">
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
    let favRadioName = `radioFav${photo.photoId}`;
    let visibleCheckName = `checkboxVisible${photo.photoId}`;
    let homePageCheckName = `checkboxHomepage${photo.photoId}`;
    
    let favChecked = ``;
    if(photo.photoId === furniture.favouritePhoto.photoId) {
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
          <img class="img-fluid" src="${findFurnitureImgSrcFromFilename(photo.source, images)}" alt="photo id:${photo.photoId}"/>
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
  let res = `
  <form>
    <input id="originalFav${furniture.furnitureId}" type="hidden" class="originalFav" value="${furniture.favouritePhoto.photoId}">
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
  let cardFavImg = document.querySelector("#card-fav-photo");
  let originalFavId = cardFavImg.getAttribute("original_fav_id");
  let furnitureId = cardFavImg.getAttribute("furnitureid");
  //fav
  let selectedFavId = findSelectedFav();
  if(originalFavId != selectedFavId) {
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
  switch(furniture.status) {
    case "ACCEPTED":
      res += generateTransitionModal("ToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionModal("ToRestoration", "Indiquer en restauration");
      break;
    case "AVAILABLE_FOR_SALE":
      res += generateTransitionModal("ToSold", "Indiquer vendu");
      res += generateTransitionModal("Withdraw", "Retirer de la vente", "danger", "secondary");
      break;
    case "IN_RESTORATION":
      res += generateTransitionModal("ToAvailable", "Indiquer disponible à la vente");
      res += generateTransitionModal("Withdraw", "Retirer de la vente", "danger", "secondary");
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
  switch(transitionId) {
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

//status transition methods

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

const loadCard = (furnitureId) => {
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
}

export default FurnitureList;