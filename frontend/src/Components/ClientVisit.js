import notFoundPhoto from "../img/notFoundPhoto.png";
import {getUserSessionData} from "../utils/session";
import {removeTimeouts, generateLoadingAnimation, displayErrorMessage} from "../utils/utils";

let page = document.querySelector("#page");
let currentUser;

let mapRequests;

let timeouts = [];

const errorDiv = `<div class="col-5 mx-auto">  <div id="errorDiv" class="d-none"></div>  </div>`;


const VisitRequest = async () => {
    currentUser = getUserSessionData();
    page.innerHTML = errorDiv + generateLoadingAnimation();

    let listRequests = await getUserRequestsForVisit();
    mapRequests = new Map(listRequests.map(request => [request.requestId, request]));
    
    page.innerHTML = errorDiv + generateVisitPage();

    document.querySelectorAll(".requestTableRow").forEach(requestTableRow => requestTableRow.addEventListener("click", onRequestTableRowClick));
    document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
    document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
}


/********************  Business methods  **********************/


/**
 * display the short elements if the table is large, else just refresh the user card.
 */
const onRequestTableRowClick = (e) => {
    if (!document.querySelector('#shortTableContainer'))
        displayShortElements();
    onUserClickHandler(e);
  }


/**
  * hide all the short elements, display the large ones and magnify the large.
  */
 const displayLargeTable = () => {
    removeTimeouts(timeouts);
    //hide
    document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
    //remove the bg-secondary
    document.querySelectorAll(".requestTableRow").forEach(element => element.className = "requestTableRow");
    //magnify
    document.querySelector('#shortTable').id = "largeTable";
    if (document.querySelector('#shortTableContainer') !== null) //can be undefined because of the setTimeout in displayShortElements
        document.querySelector('#shortTableContainer').id = "largeTableContainer";
	
    document.querySelectorAll(".requestState").forEach(element => {
        let status = element.getAttribute("status");
        element.innerHTML = generateColoredStatus(status);
    });
  }

  
  /**
   * Shrink the large table, hide the not needed element in the table and display the user card needed.
   */
  const displayShortElements = () => {
    removeTimeouts(timeouts);
    //shrink
    document.querySelector('#largeTable').id = "shortTable";
    timeouts.push(setTimeout( () => document.querySelector('#largeTableContainer').id = "shortTableContainer"
                      , 750));
    //display
    document.querySelectorAll(".shortElement").forEach(element => element.style.display = "block");
    document.querySelectorAll(".requestState").forEach(element => {
        let status = element.getAttribute("status");
        let classname = generateStatusInfos(status).classname;
        element.innerHTML = generateDot(classname);
    });
  }

  
  /**
   * Called when clicking on one of the rows of the table's body (large or short).
   * Display the card of the request that has been clicked on the table.
   */
  const onUserClickHandler = async (e) => {
    //remove the bg-secondary
    document.querySelectorAll(".requestTableRow").forEach(element => element.className = "requestTableRow");
    //get the tr element
    let element = e.target;
    while (!element.className.includes("requestTableRow"))
        element = element.parentElement;
    element.className += " bg-secondary text-light";
	
    await displayRequestCardById(element.attributes["requestId"].value);
  }


  /**
 * Displays the card of the corresponding request
 * @param {*} requestId the request's id 
 */
const displayRequestCardById = async (requestId) => {
    let requestCardDiv = document.querySelector("#requestCardDiv");
    requestCardDiv.innerHTML = generateLoadingAnimation();
    //generate the user card
    requestCardDiv.innerHTML = generateRequestCard(mapRequests.get(parseInt(requestId)));
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
								<a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Information</a>
							</li>
							<li class="nav-item">
								<a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Meubles acceptés/refusés</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
	
			<div class="row">
				<div class="col-md-8">
					<div class="tab-content profile-tab" id="myTabContent">
						<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">` + generateRequestInfoCard(request) + `</div>       
						<div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">` + generateRequestFurnitureCard(request) + `</div>
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
  request.furnitureList.forEach(furniture => photoList += generateSinglePhotoCaintainer(furniture));
  return photoList;
}


/**
 * generate a container having the favourite photo of the furniture and his status.
 * @param {*} furniture the furniture containing the favourite photo.
 * @returns a container having the favourite photo of the furniture and his status.
 */
const generateSinglePhotoCaintainer = (furniture) => {
  return `
  <div class="p-1 w-50 container photo-list-container"">
    <div class="row px-0">
      <div class="col-6">
        ` + generateFavouritePhotoImgTag(furniture) + `
        <p>` + generateStatusInfos(furniture.status).status + `</p>
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
    return `<img class="img-fluid" src="` + notFoundPhoto + `" alt="photoNotFound"/>`;
  else
    return `<img class="img-fluid" src="` + furniture.favouritePhoto.source + `" alt="photo"/>`;
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
    let response = await fetch("/requestForVisit/me", {
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

export default VisitRequest;