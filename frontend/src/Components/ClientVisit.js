import notFoundPhoto from "../img/notFoundPhoto.png";
import loadingPhoto from "../img/loadingImg.png";
import {findCurrentUser} from "../utils/session";
import {removeTimeouts, generateLoadingAnimation, displayErrorMessage, gdpr, baseUrl, getSignal} from "../utils/utils";

let page = document.querySelector("#page");
let currentUser;

let mapRequests;

let activeRequestID = "";
let displayInfoItemInRequestCard = true;

let timeouts = [];

const errorDiv = `<div class="col-5 mx-auto">  <div id="errorDiv" class="d-none"></div>  </div>`;


const VisitRequest = async () => {
  currentUser = findCurrentUser();

  page.innerHTML = errorDiv + generateLoadingAnimation();

  let listRequests = await getUserRequestsForVisit();
  mapRequests = new Map(listRequests.map(request => [request.requestId, request]));
    
  page.innerHTML = errorDiv + generateVisitPage();

  setDefaultLargeValues(true);
  setDefaultEventListener();
  gdpr(page);
}


/********************  Business methods  **********************/


/**
 * set all the default event listeners needed in the page.
 */
const setDefaultEventListener = () => {
  document.querySelectorAll(".requestTableRow").forEach(requestTableRow => requestTableRow.addEventListener("click", onRequestTableRowClick));
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
}


/**
 * display the short elements if the table is large, else just refresh the user card.
 */
const onRequestTableRowClick = (e) => {
  if (!document.querySelector('#shortTableContainer'))
    displayShortElements();
  onUserClickHandler(e);
}


/**
 * set the default values of elements when the table is large.
 */
const setDefaultLargeValues = () => {
  //hide the element only displayed when the user table is small
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
  //remove the background on the selected row (if exists)
  removeActiveRow();
  //change the class of divs from small to large (if are small)
  let tableContainer = document.querySelector('#shortTableContainer');
  if (tableContainer/* !== null*/)
    tableContainer.id = "largeTableContainer";
  let table = document.querySelector('#shortTable');
  if (table)
    table.id = "largeTable";
  //set the info item to be displayed by default in the user card
  displayInfoItemInRequestCard = true;
  //create a colored status for each request
  document.querySelectorAll(".requestState").forEach(element => {
    let status = element.getAttribute("status");
    element.innerHTML = generateColoredStatus(status);
  });
}


/**
 * set the default values of elements when the table is short.
 * @param {boolean} changeTableContainer a boolean that inform the method if we need to change the class of the table container from large to short.
 */
const setDefaultShortValues = (changeTableContainer) => {
  //display the short element only displayed when the user table is small
  document.querySelectorAll(".shortElement").forEach(element => element.style.display = "block");
  //change the class of table from large to small (if is large)
  let table = document.querySelector("#largeTable");
  if (table)
    table.id = "shortTable";
  //change the class of table container from large to short (if is large and is asked to) 
  if (changeTableContainer) {
    let tableContainer = document.querySelector('#largeTableContainer');
    if (tableContainer)
      tableContainer.id = "shortTableContainer";
  }
  //create a color dot for the request status.
  document.querySelectorAll(".requestState").forEach(element => {
    let status = element.getAttribute("status");
    let classname = generateStatusInfos(status).classname;
    element.innerHTML = generateDot(classname);
  });
  //change the class of table container from large to short (if is large and is asked to) 
  if (changeTableContainer) {
    let tableContainer = document.querySelector('#largeTableContainer');
    if (tableContainer)
      document.querySelector('#largeTableContainer').id = "shortTableContainer";
  }
}


/**
* hide all the short elements, display the large ones and magnify the large.
*/
const displayLargeTable = () => {
  removeTimeouts(timeouts);
  setDefaultLargeValues();
}

  
/**
 * Shrink the large table, hide the not needed element in the table and display the user card needed.
 */
const displayShortElements = () => {
  removeTimeouts(timeouts);
  setDefaultShortValues(false);
  timeouts.push(setTimeout( () => document.querySelector('#largeTableContainer').id = "shortTableContainer"
                      , 750));
}

  
/**
 * change the active row and display the user card needed.
 */
const onUserClickHandler = async (e) => {
  removeActiveRow();
  
  //get the tr element
  let element = e.target;
  while (!element.className.includes("requestTableRow"))
      element = element.parentElement;
  setActiveRow(element.getAttribute("requestId"));

  let requestId = element.attributes["requestId"].value;
  await displayRequestCardById(requestId);
  getAllRequestPhotos(parseInt(requestId));
}


/**
 * displays the card of the corresponding request
 * @param {*} requestId the request's id 
 */
const displayRequestCardById = async (requestId) => {
  let requestCardDiv = document.querySelector("#requestCardDiv");
  requestCardDiv.innerHTML = generateLoadingAnimation();
  //generate the user card
  requestCardDiv.innerHTML = generateRequestCard(mapRequests.get(parseInt(requestId)));
  //add event listeners for nav item
  document.querySelector("#home-tab").addEventListener("click", () => displayInfoItemInRequestCard = true);
  document.querySelector("#profile-tab").addEventListener("click", () => displayInfoItemInRequestCard = false);
}

const updateThumbnail = (furniture, thumbnail) => {
  let furnitureId = furniture.furnitureId;
  let query = `img[furniture-id='${furnitureId}']`;
  document.querySelectorAll(query).forEach((img) => {
    img.src=thumbnail;
  });
}

/**
 * Set a row to active by changing his background to grey and his text to white.
 * @param {*} userID the id of the user needed for finding the new active row.
 */
 const setActiveRow = (requestID) => {
  let activeRow = document.querySelector(".requestTableRow[requestId='" + requestID + "']")
  if (activeRow)
    activeRow.className += " bg-secondary text-light";
    activeRequestID = requestID;
}


/**
 * set the active row to inactive and remove his background and text color.
 */
const removeActiveRow = () => {
  let activeRow = document.querySelector(".requestTableRow[requestId='" + activeRequestID + "']")
  if (activeRow) {
    activeRow.classList.remove("bg-secondary");
    activeRow.classList.remove("text-light");
  }
  activeRequestID = "";
}


/********************  HTML generation  **********************/

const generateVisitPage = () => {
    return `
          <div class="col-5 mx-auto">
            <div id="errorDiv" class="d-none"></div>
          </div>
          <div id="largeTableContainer">
            <div>
              <button type="button" id="buttonReturn" class="shortElement btn btn-dark m-3">Retour à la liste</button>`
              + generateTable() +
            `</div>
            <div class="shortElement" id="requestCardDiv"></div>
          </div>
          <div id="snackbar"></div>`;
}

  
const generateTable = () => {
  let res = `
      <table id="largeTable" class="table table-bordered table-hover">
          <thead class="table-secondary">
              <tr>
                <th>Date de la demande</th>
                <th>Adresse</th>
                <th>Etat</th>
              </tr>
          </thead>
          <tbody>`
            + getAllRequestsRows() + `
          </tbody>
      </table>`;
    return res;
}


const getAllRequestsRows = () => {
  let res = "";  
  mapRequests.forEach(request => res += generateRow(request));
  return res;
}


const generateRow = (request) => {
  return ` 
      <tr class="requestTableRow" requestId="` + request.requestId + `">
        <th>` + request.requestDate + `</th>
        <th>` + request.address.street + ` ` + request.address.buildingNumber + ` ` + request.address.postcode + `, ` + request.address.commune + `</th>
        <th class="requestState" status=` + request.requestStatus + `>` + generateColoredStatus(request.requestStatus) + `</th>
     </tr>`;
}

/**
 * generate the whole card for a given request.
 * @param {*} request the request that needs a card.
 * @returns the whole card for a given request.
 */
const generateRequestCard = (request) => {
  let navLinkInfo = {
    status: "",
    ariaSelected: "",
    tabFade: ""
  }
  let navLinkFurniture = {
    status: "",
    ariaSelected: "",
    tabFade: ""
  }
  if (displayInfoItemInRequestCard) {
    navLinkInfo.status = "active";
    navLinkInfo.ariaSelected = "true";
    navLinkInfo.tabFade = "active show"
  } else {
    navLinkFurniture.status = "active";
    navLinkFurniture.ariaSelected = "true";
    navLinkFurniture.tabFade = "active show"
  }

  return`
  <div class="container emp-profile">
		<form>
			<div class="row">
				<div class="col-12">
					<div class="profile-head">
						<div class="row">
							<div class="col-md-6">
								<h4>Demande de visite du ` + request.requestDate + `</h4>
								<h5>Plage horaire demandé : ` + request.timeSlot + `</h5>
							</div>
							<div class="col-md-6 text-left">
								<p class="profile-rating">ÉTAT : <span id="statusCardEntry">` + generateBadgeStatus(request) + `</span></p>
							</div>
						</div>
						<ul class="nav nav-tabs" id="myTab" role="tablist">
							<li class="nav-item">
								<a class="nav-link ` + navLinkInfo.status + `" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="` + navLinkInfo.ariaSelected + `">Information</a>
							</li>
							<li class="nav-item">
								<a class="nav-link ` + navLinkFurniture.status + `" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected=" ` + navLinkFurniture.ariaSelected + `">Meubles acceptés/refusés</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="col-md-8">
					<div class="tab-content profile-tab" id="myTabContent">
						<div class="tab-pane fade ` + navLinkInfo.tabFade + `" id="home" role="tabpanel" aria-labelledby="home-tab">` + generateRequestInfoCard(request) + `</div>       
						<div class="tab-pane fade ` + navLinkFurniture.tabFade + `" id="profile" role="tabpanel" aria-labelledby="profile-tab">` + generateRequestFurnitureCard(request) + `</div>
					</div>	
				</div>
			</div>
		</form>           
  </div>`;
}


/**
 * generate the whole info card for the request.
 * @param {*} request the request that need an info card.
 * @returns the whole info card for the request.
 */
const generateRequestInfoCard = (request) => {    
	let info = "";
	if (request.requestStatus === "CANCELED")
		info = generateCardLabelKeyEntry("Justificatif de refus", "explanatory-note-entry", request.explanatoryNote);
	else if (request.requestStatus === "CONFIRMED")
		info = generateCardLabelKeyEntry("Date/heure de visite", "visit-date-time-entry", request.visitDateTime);

	return generateCardLabelKeyEntry("Adresse de visite", "address-entry", request.address.street + ` ` + request.address.buildingNumber + ` ` + request.address.postcode + `, ` + request.address.commune) + ` 
	  ` + generateCardLabelKeyEntry("Date de la demande", "request-date-entry", request.requestDate) + `
	  ` + generateCardLabelKeyEntry("Disponibilités", "time-slot-entry", request.timeSlot)
	  + info;
}


/**
 * generate the whole furniture card for a given request.
 * @param {*} request the request that needs a furniture card.
 * @returns the whole furniture card for a given request.
 */
const generateRequestFurnitureCard = (request) => {
  return `
  <form>
    <input id="originalFav" type="hidden"/>
    <div class="form-check d-flex flex-lg-fill flex-row">` + generatePhotoList(request) + `</div>
  </form>`;
}


/**
 * generate a list of container having each a photo and a status.
 * @param {*} request the request containing all the furniture.
 * @returns a list of container having each a photo and a status.
 */
const generatePhotoList = (request) => {
  let photoList = "";
  request.furnitureList.forEach(furniture => photoList += generateSinglePhotoContainer(furniture));
  return photoList;
}


/**
 * generate a container having the favourite photo of the furniture and his status.
 * @param {*} furniture the furniture containing the favourite photo.
 * @returns a container having the favourite photo of the furniture and his status.
 */
const generateSinglePhotoContainer = (furniture) => {
  return `
  <div class="p-1 w-50 container photo-list-container"">
    <div class="row px-0">
      <div class="col-6">
         <p>` + furniture.description +`</p> 
        ` + generateFavouritePhotoImgTag(furniture) + `
        <p>` + generateColoredFurnitureStatus(furniture)+ `</p>
      </div>
    </div>
  </div>`;
}


/**
 * generate a <img/> tag containing the favourite photo of the given furniture.
 * @param {*} furniture the furniture containing the photo to generate
 * @returns an <img/> tag containing the favourite photo of the furniture.
 */
const generateFavouritePhotoImgTag = (furniture) => {
  if (!furniture.favouritePhoto)
    return `<img class="img-fluid" furniture-id="${furniture.furnitureId}" src="` + loadingPhoto + `" alt="photoNotFound"/>`;
  else
    return `<img class="img-fluid" furniture-id="${furniture.furnitureId}" src="` + furniture.favouritePhoto.source + `" alt="photo"/>`;
}


/**
 * create a div containing a label used in the requestCard.
 * @param {*} label the 'title' of the value.
 * @param {*} id the HTML id of the value.
 * @param {*} value the value to display in a <p> tag.
 * @returns a div containing a label used in the requestCard.
 */
const generateCardLabelKeyEntry = (label, id, value) => {
  let res = `
    <div class="row text-left">
      <div class="col-md-6">
        <label> ` + label + `</label>
      </div>
      <div class="col-md-6">
        <p id="` + id + `"> ` + value + `</p>
      </div>
    </div>
    `;
  return res;
}


/**
 * Generate status entry for request list as colored bootstrap badge (used in cards).
 * @param {*} request the request that will have the badge.
 * @returns a bootstrap badge.
 */
 const generateBadgeStatus = (request) => {
  let infos = generateStatusInfos(request.requestStatus);
  let res = `<span class="badge badge-pill badge-` + infos.classname + ` text-light"> ` + infos.status + `</span>`;
  return res;
}


/**
 * Generate status entry for request list as colored <p> html tag (used in large tables)
 * @param {*} request : request  object
 * @returns an html <p> tag
 */
const generateColoredStatus = (requestStatus) => {
  let infos = generateStatusInfos(requestStatus);
  return `<p class="text-` + infos.classname + `"> ` + infos.status + `</p>`;
}

/**
 * Generate status entry for request list as colored <p> html tag (used in large tables)
 * @param {*} request : furniture  object
 * @returns an html <p> tag
 */
const generateColoredFurnitureStatus = (furniture) => {
  let infos = generateStatusInfos(furniture.status);
  return `<p class="text-${infos.classname}">${infos.status}</p>`;
}


/**
 * create a dot of a certain color in function of the given class name given.
 * @param {*} colorClassName the name of the bootstrap class corresponding to a color.
 * @returns a dot of a certain color.
 */
const generateDot = (colorClassName) => {
  return `<span class="badge badge-pill p-1 badge-` + colorClassName + `"> </span>`;
}
  

/**
 * find status label & color classname (primary / danger / etc...) for a given status
 * @param {String} status
 * @returns {
 * classname: bootstrap color suffix,
 * status: status label,
 * }
 */
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
      res.classname = "warning";
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
      res.classname = "secondary";
      res.status = "Vendu";
      break;
    case "WITHDRAWN":
      res.classname = "secondary";
      res.status = "Retiré de la vente";
      break;
    case "REFUSED":
      res.classname = "secondary";
      res.status = "Refusé";
      break;
    case "REQUESTED_FOR_VISIT":
      res.classname = "info";
      res.status = "En attente de visite";
      break;
    case "CONFIRMED":
      res.classname = "success";
      res.status = "Accepté";
      break;
    case "WAITING":
      res.classname = "warning";
      res.status = "en attente";
      break;
    case "CANCELED":
      res.classname = "danger";
      res.status = "Annulé";
      break;
    default:
      res.classname = "";
      res.status = status;
  }
  return res;
}



/********************  Backend fetch  **********************/


/**
 * ask the backend for all the request for visits of a client and return a promise of the result.
 * @returns a promise of an array containing the result of the fetch.
 */
const getUserRequestsForVisit = async () => {
  let signal = getSignal();

  let response = await fetch(baseUrl+"/requestForVisit/me", {
    signal,
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  })
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage(err, errorDiv);
  }
  return response.json();
}

const getAllRequestPhotos = async (requestId) => {
  let request = mapRequests.get(requestId);
  let furniture;
  for(furniture of request.furnitureList)
    await getFurnitureRequestPhotos(furniture);
}

const getFurnitureRequestPhotos = async (furniture) => {
  if (!furniture.hasFetchedPhotos) {
    furniture.hasFetchedPhotos = true;
    let signal = getSignal();
    
    let response = await fetch(baseUrl+"/photos/byFurniture/request/" + furniture.furnitureId, {
      signal,
      method: "GET",
      headers: {
        Authorization: currentUser.token,
      }
    });
    if (response.ok) {
      let photoArray = await response.json()
      let thumbnail = notFoundPhoto;
      if (photoArray.length >= 1)
        thumbnail = photoArray[0].source;
      furniture.thumbnail = thumbnail;
      updateThumbnail(furniture, thumbnail);
    }
  }
}

export default VisitRequest;