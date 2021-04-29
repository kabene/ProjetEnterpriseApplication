import {getUserSessionData} from "../utils/session";
import {removeTimeouts, generateLoadingAnimation, displayErrorMessage} from "../utils/utils";

let page = document.querySelector("#page");
let currentUser;
let mapRequests;
let timeouts = [];


const VisitRequest = async () => {
    currentUser = getUserSessionData();
    page.innerHTML = generateLoadingAnimation();

    let listRequests = await getUserRequestsForVisit();
    mapRequests = new Map(listRequests.map(request => [request.requestId, request]));
    console.log(mapRequests);
    page.innerHTML = generateVisitPage();

    document.querySelectorAll(".requestTableRow").forEach(requestTableRow => requestTableRow.addEventListener("click", onRequestTableRowClick));
    document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
    document.querySelectorAll(".shortElement").forEach(element => element.style.display = "none");
}


/********************  Business methods  **********************/

const onRequestTableRowClick = (e) => {
    if (document.querySelector('#shortTableContainer') == null)
      displayShortElements();
    onUserClickHandler(e);
  }

  /**
 * Called when clicking on the buttonReturn.
 * Hide all the short elements, display the large ones and magnify the large.
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
    while (!element.className.includes("requestTableRow")) {
      element = element.parentElement;
    }
    element.className += " bg-secondary text-light";
    let requestId = element.attributes["requestId"].value;
    await displayRequestCardById(requestId);
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
        <th class="requestState">` + request.requestStatus + `</th>
     </tr>`;
  }

  const generateRequestCard = (request) => {
    let info = "";
    if (request.requestStatus === "CANCELED")
        info = `<div><h5>Raison d'annulation : </h5> <span>` + request.explanatoryNote + `</span></div>`;
    else (request.requestStatus === "CONFIRMED")
        info = `<div><h5>Date de visite : </h5> <span>` + request.visitDateTime + `</span></div>`;
    let page= `
    <div class="container emp-profile">
        <form>
            <div class="row">
                <div class="col-md-6">
                    <div class="profile-head">
                        <h4>Demande de visite du ` + request.requestDate + `</h4>
                        <h5>Plage horaire demandé : ` + request.timeSlot + `</h5>
                    </div>
                </div>
            </div>
    
            <div class="row">
                <div class="col-md-8">
                    <div class="tab-content profile-tab" id="myTabContent">
                        <div>
                            <h5>Status : ` + request.requestStatus + `</h5>
                            `+ info + `
                        </div>
                </div>
            </div>

        </form>           
    </div>`;
    return page;
  }


/********************  Backend fetch  **********************/

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
        displayErrorMessage(err);
    }
    return response.json();
}

export default VisitRequest;