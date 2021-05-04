import notFoundPhoto from "../img/notFoundPhoto.png";


import {findCurrentUser} from "../utils/session";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js";
import {displayErrorMessage, importAllFurnitureImg, generateLoadingAnimation} from "../utils/utils.js"

let page = document.querySelector("#page");

let currentUser;

let images;
let furnitureList;
let furnitureTypeList;
let myOptionList;

const errorDiv = `<div class="col-5 mx-auto">  <div id="errorDiv" class="d-none"></div>  </div>`;

let filter = {
  description: "",
  type: ""
};

const Furniture = async () => {
  page.innerHTML =  errorDiv + generateLoadingAnimation();
  currentUser = findCurrentUser();

  images = importAllFurnitureImg()
  furnitureList = await getFurnitureList();
  furnitureTypeList = await getFurnitureTypeList();
  myOptionList = await getMyOptionList();

  page.innerHTML = errorDiv + generateTable();
  addAllEventListeners();
}

/********************  Business methods  **********************/


const refresh = (data, status) => {
  myOptionList.push(data);
  let furnitureId = data.furnitureId;
  furnitureList.forEach(furniture => {
    if (furniture.furnitureId == furnitureId) {
      furniture.option = data;
    }
  });
  updateFurnitureList(data.furnitureId, status)
  page.innerHTML = errorDiv + generateTable();
  addAllEventListeners();
}

const updateFurnitureList = (furnitureId, status) => {
  furnitureList.forEach(furniture => {
    if (furniture.furnitureId === furnitureId) {
      furniture.status = status;
    }
  })
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
  document.querySelectorAll(".btnCreateOption").forEach(element =>{
    element.addEventListener("click", addOption );
  });
  document.querySelectorAll(".cancelOptButton").forEach(element=>{
    element.addEventListener("click",cancelOption);
  });
  document.querySelector("#apply-filters-btn").addEventListener("click", onClickApplyFilter);
  document.querySelector("#clear-filters-btn").addEventListener("click", onClickClearFilter);
}

/********************  HTML generation  **********************/


const generateTable = () => {
  return `
    <div class="wrapperFurniturePage">
        <div></div>
        <div>
          <h3>Filtrer les meubles:</h3>
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
  furnitureTypeList.forEach(type => ret += generateOptionTypeTag(type));
  ret += `</select>`;
  return ret;
}

const generateOptionTypeTag = (type) => {
  return `<option value="` + type.typeName + `">` + type.typeName + `</option>`;
}

const generateAllItemsAndModals = () => {
  let res = "";
  furnitureList.forEach(furniture => res += generateItemAndModal(furniture));
  return res;
}

const generateItemAndModal = (furniture) => {
  //filter
  if (filter.description !== "") {
    if (!furniture.description.toLowerCase().includes(filter.description.toLowerCase()))
      return "";
  }
  if (filter.type !== "") {
    if (filter.type !== furniture.type)
      return "";
  }

  let item = `
        <div>
          ` + getTag(furniture) + `
          <img class="imageFurniturePage img-furniture" src="` + furniture.favouritePhoto.source + `" alt="thumbnail" data-toggle="modal" data-target="#modal__`+ furniture.furnitureId + `" onError="this.src='` + notFoundPhoto + `'"/>
          <p class="text-center">` + furniture.description + `</p>`
          + getOptionButton(furniture) +
        `</div>`; //TODO: fixer la taille des images

  let tabPhotoToRender = getTabPhotoToRender(furniture);

  let modal = `
        <div class="modal fade" id="modal__` + furniture.furnitureId + `">
          <div class="modal-dialog modal-xl">
            <div class="modal-content">
              <div class="row mx-0 pt-5">
                <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                  <ol class="carousel-indicators bg-secondary">` + getHTMLCarouselIndicators(tabPhotoToRender.length) + `</ol>
                  <div class="carousel-inner">` + getHTMLCarouselPhotos(tabPhotoToRender) + `</div>
                  <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                    <span class="carousel-control-prev-icon bg-dark" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                  </a>
                  <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                    <span class="carousel-control-next-icon bg-dark" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                  </a>
                </div>
              </div>
            </div>
          </div>                     
        </div>`;

  return item + modal;
}

/**
 * create an html element containing all the carouselIndicators (small bars below the carousel).
 * @param {*} nbrPhoto the number of photo in the carousel.
 * @returns a html element containing all the carouselIndicators needed for the carousel.
 */
const getHTMLCarouselIndicators = (nbrPhoto) => {
	let ret = `<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>`;
	for (let i = 1; i < nbrPhoto; i++) 
		ret += `<li data-target="#carouselExampleIndicators" data-slide-to="` + i + `"></li>`;
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
      <img class="w-75 my-3" src="` + notFoundPhoto + `" alt="Photo meuble">
    </div>
    `;
  }
  tabPhotoToRender.forEach(photo => {
    ret += `
    <div class="carousel-item active text-center">
      <img class="w-75 my-3" src="` + photo.source + `" alt="Photo meuble onError="this.src='` + notFoundPhoto + `'">
    </div>`;
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
  let alreadyUnderOption = false;
  if (currentUser) {
    myOptionList.forEach(option => {
      alreadyUnderOption = true;
    })
  }
  ;
  if (furniture.status === "AVAILABLE_FOR_SALE" && typeof currentUser
      !== "undefined") { //place option

    let sendBtn = generateCloseBtn("Confirmer", "btn" + furniture.furnitureId,
        "btnCreateOption btn btn-primary mx-5");
    return generateModalPlusTriggerBtn("modal_" + furniture.furnitureId,
        "Mettre une option", "btn btn-primary", "<h4>Mettre une option</h4>",
        generateOptionForm(), sendBtn, "Annuler", "btn btn-danger");
  } else if (furniture.status === "UNDER_OPTION" && alreadyUnderOption
      && typeof currentUser !== "undefined") { //cancel option
    return `<button type="button" id="cbtn${furniture.furnitureId}" class="btn btn-danger cancelOptButton">annuler l'option</button>`;
  } else { // nothing
    return "";
  }
}

const getTabPhotoToRender = (furniture) => {
  return furniture.photos;
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
  let ret = [];
  await fetch("/furniture/", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    ret = data;
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
  });
  return ret;
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
  if (currentUser) {
    let ret = [];

    await fetch("/options/me", {
      method: "GET",
      headers: {
        "Authorization": currentUser.token,
        //"Content-Type": "application/json",
      },
    }).then((response) => {
      if (!response.ok) {
        throw new Error(
            "Error code : " + response.status + " : " + response.statusText);
      }
      return response.json();
    }).then((data) => {
      ret = data;
    }).catch((err) => {
      console.log("Erreur de fetch !! :´<\n" + err);
      displayErrorMessage("errorDiv", err);
    });
    return ret;
  }
}

const cancelOption = (e) => {
  e.preventDefault();
  let furnitureId = e.target.id.substring(4);
  let optionId;
  myOptionList.forEach(option => {
    if (option.furnitureId == furnitureId) {
      if (!option.isCanceled) {
        optionId = option.optionId;
      }
    }
  });

  fetch("/options/cancel/" + optionId, {
    method: "PATCH",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    refresh(data, "AVAILABLE_FOR_SALE");
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}

const addOption = (e) => {
  e.preventDefault();
  let furnitureId = e.target.id.substring(3);
  let duration = e.target.parentElement.parentElement.querySelector(
      "input").value;

  let bundle = {
    furnitureId: furnitureId,
    duration: duration,
  }
  console.log(bundle);
  fetch("/options/", {
    method: "POST",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    refresh(data.option, "UNDER_OPTION");
  }).catch((err) => {
    console.log("Erreur de fetch !! :´<\n" + err);
    displayErrorMessage("errorDiv", err);
  });

}



export default Furniture;