import notFoundPhoto from "../img/notFoundPhoto.png";

import {findCurrentUser} from "../utils/session";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js";
import {displayErrorMessage, generateLoadingAnimation, displayImgs, gdpr} from "../utils/utils.js"

let page = document.querySelector("#page");

let currentUser;

let mapFurniture;
let mapFurnitureType;
let mapOption;

const errorDiv = `<div class="col-5 mx-auto">  <div id="errorDiv" class="d-none"></div>  </div>`;

let filter = {
  description: "",
  type: ""
};

const Furniture = async () => {
  page.innerHTML =  errorDiv + generateLoadingAnimation();
  currentUser = findCurrentUser();

  let furnitureList = await getFurnitureList();
  mapFurniture = new Map(furnitureList.map(furniture => [furniture.furnitureId, furniture]));

  let furnitureTypeList = await getFurnitureTypeList();
  mapFurnitureType = new Map(furnitureTypeList.map(furnitureType => [furnitureType.typeId, furnitureType]));

  let myOptionList = await getMyOptionList();
  if (myOptionList)
    mapOption = new Map(myOptionList.map(option => [option.furnitureId, option]));

  page.innerHTML = errorDiv + generateTable();
  getFavs().then(() => getPhotos());
  gdpr(page);
  addAllEventListeners();
}

/********************  Business methods  **********************/


const refresh = (data, status) => {
  mapOption.set(data.furnitureId, data);
  let furnitureId = data.furnitureId;

  mapFurniture.get(parseInt(furnitureId)).option = data;
  mapFurniture.get(parseInt(furnitureId)).status = status;

  page.innerHTML = errorDiv + generateTable();
  addAllEventListeners();
}


/**
 * Called when clicking on the apply filter button.
 * Apply all filters chosen on the furniture and render them.
 */
const onClickApplyFilter = (e) => {
  e.preventDefault();
  filter.description = document.querySelector("#furnitureDescriptionFilter").value;
  filter.type = document.querySelector("#furnitureTypeFilter").value;
  page.innerHTML = errorDiv + generateTable();
  addAllEventListeners();
  document.querySelector("[value='" + filter.type + "']").setAttribute('selected', 'true');
}


/**
 * Called when clicking on the cancel filter button.
 * Clear all filters chosen on the furniture and render all the furniture if there already was filters.
 */
const onClickClearFilter = (e) => {
  e.preventDefault();
  if (filter.description === "" && filter.type === "")
    return;
  filter.description = "";
  filter.type = "";
  page.innerHTML = errorDiv + generateTable();
  addAllEventListeners();
}


/**
 * Add all the event listeners required in the document.
 */
const addAllEventListeners = () => {
  document.querySelectorAll(".btnCreateOption").forEach(element => element.addEventListener("click", addOption ));
  document.querySelectorAll(".cancelOptButton").forEach(element => element.addEventListener("click", cancelOption));
  document.querySelector("#apply-filters-btn").addEventListener("click", onClickApplyFilter);
  document.querySelector("#clear-filters-btn").addEventListener("click", onClickClearFilter);
}


/**
 * check if a furniture respect the current filter.
 * @param {*} furniture the user to check if he is correct following the filter criterias.
 * @returns true if the furniture is correct, else false.
 */
 const isFurnitureRespectingFilter = (furniture) => {
  if (filter.description !== "") {
    if (!furniture.description.toLowerCase().includes(filter.description.toLowerCase()))
      return false;
  }
  if (filter.type !== "") {
    if (filter.type !== furniture.type)
      return false;
  }
  return true;
 }
 

/**
 * Updates the modal of a specific piece of furniture.
 *
 * @param {Array} tabPhotoToRender new array of photos to display
 * @param {*} furniture piece of furniture
 */
const updateModal = (tabPhotoToRender, furniture) => {
  let query = `.modal-content[furniture-id='${furniture.furnitureId}']`;
  let div = document.querySelector(query);
  if (div)
    div.innerHTML = getHTMLEntireCarousel(tabPhotoToRender, furniture);
}

/**
 * Updates furnitureList with new favouritePhoto
 * (furniture.favouritePhoto)
 * @param {*} furniture
 * @param {*} photo
 */
const updateCacheFav = (furniture, photo) => {
  furniture.favouritePhoto = photo;
}

/**
 * Updates furniture list with new array of photos
 * (furniture.photos)
 * @param {*} furniture
 * @param {*} photoArray
 */
const updateCacheAllPhotos = (furniture, photoArray) => {
  furniture.photos = photoArray;
}

/********************  HTML generation  **********************/


const generateTable = () => {
  return `
    <div id="loadArea" class="d-block">${generateLoadingAnimation()}</div>
    <div class="wrapperFurniturePage">
        <div></div>
        <div>
          ` + generateFilterHTML() + `
        </div>
        <div></div>
        <div></div>
        <div class="contentFurniturePage">` + generateAllItemsAndModals() + `</div>
        <div></div>
    </div>`;
}


const generateFilterHTML = () => {
  return `
  <form class="form-inline">
    <div class="form-group m-3">
      <input type="text" class="form-control" id="furnitureDescriptionFilter" placeholder="Rechercher un meuble" value="` + filter.description + `"/>
    </div>
    <div class="form-group m-3">` + generateSelectTypeTag() + `</div>
     <button type="submit" id="apply-filters-btn" class="btn btn-primary m-3">Appliquer</button>
     <button type="submit" id="clear-filters-btn" class="btn btn-secondary m-3">Retirer les filtres</button>
  </form>`;
}


const generateSelectTypeTag = () => {
  let ret = `<select class="form-control" id="furnitureTypeFilter"> <option value="">Rechercher un type de meuble</option>`;
  mapFurnitureType.forEach(type => ret += generateOptionTypeTag(type));
  ret += `</select>`;
  return ret;
}


const generateOptionTypeTag = (type) => {
  return `<option value="` + type.typeName + `">` + type.typeName + `</option>`;
}


const generateAllItemsAndModals = () => {
  let res = "";
  mapFurniture.forEach(furniture => res += generateItemAndModal(furniture));
  return res;
}


const generateItemAndModal = (furniture) => {
  if (!isFurnitureRespectingFilter(furniture))
    return "";

  let src = "none";
  if (furniture.favouritePhoto)
    src = furniture.favouritePhoto.source;
  else if (furniture.noFavFound)
    src = notFoundPhoto;
  
  let item = `
        <div>
          ` + getTag(furniture) + `
          <img class="imageFurniturePage img-furniture" src="` + src + `" alt="thumbnail"  photo-id="` + furniture.favouritePhotoId + `" furniture-id="` + furniture.furnitureId + `" data-toggle="modal" data-target="#modal__`+ furniture.furnitureId + `"/>
          <p class="text-center">` + furniture.description + `</p>`
          + getOptionButton(furniture) +
        `</div>`; //TODO: fixer la taille des images
  let modal = `
        <div class="modal fade" id="modal__` + furniture.furnitureId + `">
          <div class="modal-dialog modal-xl">
            <div class="modal-content" furniture-id="` + furniture.furnitureId + `">
            ` + getHTMLEntireCarousel(furniture.photos, furniture) + `
            </div>
          </div>                     
        </div>`;

  return item + modal;
}


/**
 * Create a carousel html for a piece of furniture
 * @param {*} tabPhotoToRender table of all the photos to render in the carousel
 * @param {*} furniture the furniture
 * @returns 
 */
const getHTMLEntireCarousel = (tabPhotoToRender, furniture) => {
  if(tabPhotoToRender.length === 0)
    return generateLoadingAnimation();
  return `
  <div class="row mx-auto pt-5">
    <div id="carousel-${furniture.furnitureId}" class="carousel slide" data-ride="carousel">
      <ol class="carousel-indicators bg-secondary">` + getHTMLCarouselIndicators(tabPhotoToRender.length, furniture) + `</ol>
      <div class="carousel-inner">
      ` + getHTMLCarouselPhotos(tabPhotoToRender) + `
      </div>
      <a class="carousel-control-prev" href="#carousel-${furniture.furnitureId}" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon bg-dark" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
        </a>
      <a class="carousel-control-next" href="#carousel-${furniture.furnitureId}" role="button" data-slide="next">
        <span class="carousel-control-next-icon bg-dark" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
    </div>
  </div>`;
}


/**
 * create an html element containing all the carouselIndicators (small bars below the carousel).
 * @param {*} nbrPhoto the number of photo in the carousel.
 * @returns a html element containing all the carouselIndicators needed for the carousel.
 */
const getHTMLCarouselIndicators = (nbrPhoto, furniture) => {
	let ret = `<li data-target="#carousel-${furniture.furnitureId}" data-slide-to="0" class="active"></li>`;
	for (let i = 1; i < nbrPhoto; i++)
		ret += `<li data-target="#carousel-${furniture.furnitureId}" data-slide-to="` + i + `"></li>`;
	return ret;
}


/**
 * create an html element containing all the photo to render in the carousel.
 * @param {*} tabPhotoToRender the array containing all the photo that has to be in the carousel.
 * @returns an html element containing all the photo to render in the carousel.
 */
const getHTMLCarouselPhotos = (tabPhotoToRender) => {
  let ret = "";
  if (tabPhotoToRender.length === 0) {
    ret = `
    <div class="carousel-item active text-center">
      <img class="w-75 my-3" src="` + notFoundPhoto + `" alt="Photo introuvable">
    </div>
    `;
  }
  tabPhotoToRender.forEach(photo => {
    if (ret === "") {
      ret += `
        <div class="carousel-item active text-center">
          <img class="w-75 my-3" src="` + photo.source + `" alt="Photo meuble onError="this.src='` + notFoundPhoto + `'">
        </div>`;
    } else {
      ret += `
        <div class="carousel-item text-center">
          <img class="w-75 my-3" src="` + photo.source + `" alt="Photo meuble onError="this.src='` + notFoundPhoto + `'">
        </div>`;
    }
  });
  return ret;
}


const getTag = (furniture) => {
  let ret = "";
  if (furniture.status == "SOLD")
    ret = `<span class="badge badgeSold">VENDU !</span>`;
  else if (furniture.status == "UNDER_OPTION") {
    let option = furniture.option;
    if (!option.isCanceled) {
      let optionDate = new Date(option.dateOption);
      let today = new Date();
      let optionDelay = Math.floor((optionDate.getTime() / 86400000) + option.duration - (today.getTime() / 86400000))+1;
      let plural = "";
      if (optionDelay > 1)
        plural += "s";
      ret = `<span class="badge badgeUnderOption">Sous option durant ` + optionDelay + ` jour` + plural + `</span>`;
    }
  }
  return ret;
}


const getOptionButton = (furniture) => {
  let isOnMyOption = false;

  if (currentUser) {
    if (mapOption.has(furniture.furnitureId))
    isOnMyOption = true;
  }
  
  if (furniture.status === "AVAILABLE_FOR_SALE" && currentUser) {
    let sendBtn = generateCloseBtn("Confirmer", "btn" + furniture.furnitureId, "btnCreateOption btn btn-primary mx-5");
    return generateModalPlusTriggerBtn("modal_" + furniture.furnitureId, "Mettre une option", "btn btn-primary", "<h4>Mettre une option</h4>", generateOptionForm(), sendBtn, "Annuler", "btn btn-danger");
  } else if (isOnMyOption)
    return `<button type="button" id="cbtn` + furniture.furnitureId + `" class="btn btn-danger cancelOptButton">Annuler l'option</button>`;
  else
    return "";
}


const generateOptionForm = () => {
  let res = `
  <form>
    <div class="form-group">
      <label for="durationInput" class="mr-3">Duree: </label>
      <input type="number" id="durationInput" class="w-25" name="durationInput" min="1" step="1" max="5"> jours
    </div>
  </form>
  `;
  return res;
}


/********************  Backend fetch  **********************/


const getFurnitureList = async () => {
  let response = await fetch("/furniture/", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch !! :´<\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  return response.json();
}


/**
 * fetch all favourite photos for main list thumbnails
 */
 const getFavs = async () => {
  for(let [furnitureId, furniture] of mapFurniture)
    await fetchFav(furniture);
  
  let loadArea = document.querySelector("#loadArea");
  loadArea.className = "d-none";
}

const fetchFav = async (furniture) => {
  if (!furniture.favouritePhoto) {
    
    let response = await fetch("/photos/favourite/" + furniture.furnitureId, {
      method: "GET",
    });
    if (response.ok) {
      let fav = await response.json();
      displayImgs([fav]);
      updateCacheFav(furniture, fav);
    }
  }
}

const getPhotos = async () => {
  for(let [furnitureId, furniture] of mapFurniture)
    await fetchAllPhotos(furniture);
}

const fetchAllPhotos = async (furniture) => {
  if (!furniture.photos || furniture.photos.length === 0) {
    
    let response = await fetch("/photos/byFurniture/" + furniture.furnitureId, {
      method: "GET",
    });
    if (response.ok) {
      let photoArray = await response.json();
      updateModal(photoArray, furniture);
      updateCacheAllPhotos(furniture, photoArray);
    }
  }
}



const getFurnitureTypeList = async () => {
  let response = await fetch("/furnitureTypes/", {
    method: "GET",
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  return response.json();
}


const getMyOptionList = async () => {
  if (!currentUser) 
    return;
  let response = await fetch("/options/me", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  return response.json();
}


const cancelOption = async (e) => {
  e.preventDefault();
  let furnitureId = e.target.id.substring(4);
  let optionId;
  
  let option = mapOption.get(parseInt(furnitureId));
  if (!option.isCanceled)
    optionId = option.optionId;

  let response = await fetch("/options/cancel/" + optionId, {
    method: "PATCH",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  let jsonResponse = await response.json();
  refresh(jsonResponse, "AVAILABLE_FOR_SALE");
}


const addOption = async (e) => {
  e.preventDefault();

  let bundle = {
    furnitureId: e.target.id.substring(3),
    duration: e.target.parentElement.parentElement.querySelector("input").value,
  }
  
  let response = await fetch("/options/", {
    method: "POST",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  let jsonResponse = await response.json();
  refresh(jsonResponse.option, "UNDER_OPTION");
}

export default Furniture;