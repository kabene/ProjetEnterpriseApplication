import {getUserSessionData} from "../utils/session";
import {displayErrorMessage, generateLoadingAnimation} from "../utils/utils";
import {RedirectUrl} from "./Router";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals";

let page = document.querySelector("#page");
let mainPage;
let requestList;
let requestMap = [];
let timeouts = [];
let currentUser;
let pageHTML;
let currentRequestId;
let isDisplayingLargeTable;
let openTab = "infos";

/**
 * Runs visit request list page (for admins)
 * @param {*} id
 */
const Visits = async (id) => {

  currentUser = getUserSessionData();
  pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12 px-0">${generateLoadingAnimation()}</div>`;
  page.innerHTML = pageHTML;
  mainPage = document.querySelector("#mainPage");
  await findVisitRequestList();
  if (!id) {
    isDisplayingLargeTable = true;
    pageHTML = generatePageHtml();
    mainPage.innerHTML = pageHTML;
    document.querySelectorAll(".toBeClicked").forEach(
        element => element.addEventListener("click", displayShortElements));
    document.querySelector("#buttonReturn").addEventListener("click",
        displayLargeTable);
  } else {
    loadCard(id);
  }
  document.querySelectorAll(".userLink").forEach(
      (link) => link.addEventListener("click", onUserLinkClicked));
}

/**
 * displays the card for a given request id.
 * @param {int} requestId
 */
const loadCard = (requestId) => {
  isDisplayingLargeTable = false;
  currentRequestId = requestId;
  mainPage.innerHTML = generatePageHtml(false);
  generateCard(requestMap[requestId]);
  document.querySelectorAll(".toBeClicked").forEach(
      (element) => {
        let elementReqId = element.getAttribute("requestid");
        if (elementReqId == requestId) {
          element.className = "toBeClicked selected-row";
        }
        element.addEventListener("click", displayShortElements)
      });
  document.querySelector("#buttonReturn").addEventListener("click",
      displayLargeTable);
}

/**
 * Reloads the page and re-fetch request information.
 * Displays loading animation while awaiting the fetch.
 */
const reloadPage = async () => {
  mainPage.innerHTML = generateLoadingAnimation();
  await findVisitRequestList();
  mainPage = generatePageHtml();
}

//displayers

/**
 * sets CSS classes for transition to large table (callback for "back to list" btn)
 */
const displayLargeTable = () => {
  openTab = "infos";
  removeTimeouts();
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

/**
 * shows columns in the table that were hidden in short table
 */
const displayLargeElements = () => {
  isDisplayingLargeTable = true;
  document.querySelectorAll('.notNeeded').forEach(
      element => element.className = "notNeeded align-middle");
}

/**
 * sets CSS class for transition to short table and hides not needed columns. Then, loads card.
 * @param {*} e : event object from event listener
 */
const displayShortElements = (e) => {
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
  let requestCardDiv = document.querySelector("#RequestCardDiv");
  requestCardDiv.innerHTML = generateLoadingAnimation();
  document.querySelectorAll(".toBeClicked").forEach(element => {
    element.className = "toBeClicked";
  });

  document.querySelectorAll(".tableStatus").forEach(element => {
    let status = element.getAttribute("status");
    let classname = generateStatusInfos(status).classname;
    element.innerHTML = generateDot(classname);
  });
  let element = e.srcElement;
  while (element.tagName != "TR") {
    element = element.parentNode;
  }
  let id = element.getAttribute("requestid");
  loadCard(id);
  document.querySelectorAll(".userLink").forEach(
      (link) => link.addEventListener("click", onUserLinkClicked));
}

/**
 * callback for clicks on user links.
 * redirects to user's card in user list page.
 * @param {*} e : event object from event listener
 */
const onUserLinkClicked = (e) => {
  e.preventDefault();
  let link = e.target;
  let userId = link.getAttribute("userid");
  console.log(`Linking to user card (id: ${userId})`);
  RedirectUrl("/users", userId);
}

/**
 * Adds event listeners for every transition btn. (on the request card)
 * @param {*} request
 */
const addTransitionBtnListeners = (request) => {
  document.querySelectorAll(".transitionBtn").forEach(element => {
    element.addEventListener("click",
        findTransitionMethod(element.id, request));
  })
}

/**
 * finds event listener callback for transition btn given it's id.
 * @param {*} btnId : html id of the transition btn
 * @param {*} request : request object
 * @returns {Function} callback
 */
const findTransitionMethod = (btnId, request) => {
  switch (btnId) {
    case "btnToConfirmed":
       return (e) => toAccept(e, request);
    case "btnToCanceled":
       return (e) => toRefuse(e, request);
    default:
      return (e) => {
        e.preventDefault();
        console.log("unrecognized button id: " + btnId); //'do nothing' method
      };
  }
  ;
}

/**
 * Generate request card html and updates current display.
 * Then, adds all necessary event listeners.
 * @param {*} request
 */
const generateCard = (request) => {
  let requestCardDiv = document.querySelector("#RequestCardDiv");
  let cardHTML = generateCardHTML(request);
  requestCardDiv.innerHTML = cardHTML;
  //event listeners
  addTransitionBtnListeners(request);
  document.querySelectorAll(".favRadio").forEach((element) => {
    element.addEventListener("click", onFavRadioSelected);
  });
  document.querySelectorAll(".visibleCheckbox").forEach((element) => {
    element.addEventListener("click", onVisibleCheckClicked);
  });
  document.querySelector("#home-tab").addEventListener("click", () => {
    openTab = "infos";
  });
  document.querySelector("#profile-tab").addEventListener("click", () => {
    openTab = "furniture";
  });
}

/**
 *
 * @param furniture
 * @returns {string}
 */
const generateButtonRow = (request) => {
  let res = `
  <div class="row d-flex mt-5">
    ${generateTransitionBtns(request)}
  </div>
  `;
  return res;
}

/**
 *  Generate change state buttons
 * @param request
 */
const generateTransitionBtns = (request) => {
  let res = "";
  if (request.requestStatus === "WAITING") {
    res += generateTransitionModal("ToConfirmed",
        "Accepter la demande");
    res += generateTransitionModal("ToCanceled",
        "Refuser la demande");
  }
  return res;
}
/**
 *
 * @param id
 * @param label
 * @param triggerColorClass
 * @param closeColorClass
 * @returns {string}
 */
const generateTransitionModal = (id, label, triggerColorClass = "primary",
    closeColorClass = "danger") => {
  let body = generateModalBodyFromTransitionId(id);
  let sendBtn = generateCloseBtn(label, "btn" + id,
      `btn btn-${triggerColorClass} mx-5 transitionBtn`);
  return generateModalPlusTriggerBtn("modal_" + id, label,
      `btn btn-${triggerColorClass}`, `<h4>${label}</h4>`, body, `${sendBtn}`,
      "Fermer", `btn btn-${closeColorClass}`);
}

/**
 *
 * @param transitionId
 * @returns {string|*}
 */
const generateModalBodyFromTransitionId = (transitionId) => {
  switch (transitionId) {
    case "ToConfirmed":
      return generateToAcceptForm();
    case "ToCanceled":
      return generateToCanceledForm();
    default:
      return "not implemented yet";
  }
}
/**
 *
 * @returns {string}
 */
const generateToAcceptForm = () => {
  let res = `
    <form class="form-group">
      <label for="acceptInput" class="mr-3">Entrez la date et heure de la visite: </label>
      <div class="mx-auto my-2">
        <input class=" mx-3 form-control" name="acceptInputDate" id="acceptInputDate" type="date" max="9999-12-12T00:00:00.00"/>
        <input type="time" class=" mx-3 form-control"  name="acceptInputTime" id="acceptInputTime" max="9999-12-12T00:00:00.00"/>
      </div>
    </form>
  `;
  return `<div class="form-inline">${res}</div>`;
}
/**
 *
 * @returns {string}
 */
const generateToCanceledForm = () => {
  let res = `
    <div>
      <div class="form-group">
        <label for="cancelInput" class="mr-3">Entrez la raison de l'annulation: </label>
        <br/>
        <textarea id="cancelInput" class="form-control" name="cancelInput" ></textarea> 
      </div>
    </div>
  `;
  return res;
}

/**
 * Generate request card html given a specific request.
 * @param {*} request : request object
 * @returns {String} request card html
 */
const generateCardHTML = (request) => {
  console.log(request);
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
  let furnitureTab = closedTabObject;
  if (openTab === "furniture") {
    infoTab = closedTabObject;
    furnitureTab = openTabObject;
  }
  let res = `
  <div class="container emp-profile">
    <form>
      <div class="row">
        <div class="col-12">
          <div class="profile-head">
            <div class="row">
              <div class="col-md-6">
              ${generateSummaryCardHeader(request)}
              </div>
              <div class="col-md-6 text-left">
                <p class="profile-rating">ÉTAT : <span id="statusCardEntry">${generateBadgeStatus(
      request)}</span></p>
              </div>
            </div>
            <ul class="nav nav-tabs" id="myTab" role="tablist">
              <li class="nav-item">
                <a class="nav-link ${infoTab.aClassname}" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="${infoTab.ariaSelected}">Information</a>
              </li>
              <li class="nav-item">
                <a class="nav-link ${furnitureTab.aClassname}" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="${furnitureTab.ariaSelected}">Meubles</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
  
      <div class="row">
        <div class="col-md-8">
          <div class="tab-content profile-tab" id="myTabContent">
            <div class="tab-pane fade ${infoTab.tabClassname}" id="home" role="tabpanel" aria-labelledby="home-tab">
              ${generateUserCardEntry("Utilisateur", "userCardEntry",
      request.user)}
              ${generateAddressCardEntry(request)}
              ${generateRequestDateCardEntry(request)}
              ${generateTimeSlotCardEntry(request)}
              ${generateVisitDateTimeCardEntry(request)}
              ${generateExplanatoryNoteCardEntry(request)}
            
            </div>       
            <div class="tab-pane fade ${furnitureTab.tabClassname}" id="profile" role="tabpanel" aria-labelledby="profile-tab">
            
            </div>
            ${generateButtonRow(request)}
          </div>
        </div>
      </div>
    </form>           
  </div>
  `;
  return res;
}

const generateSummaryCardHeader = (request) => {
  let res = "";
  if (request && request.user && request.requestDate) {
    res = `<h5>${request.user.username} (${request.requestDate})</h5>`;

  }
  return res;
}

/**
 * Generate html for standard user entry in cards. (with user link)
 * @param {String} label : text displayed in the label part of the entry
 * @param {String} userLinkId : html id of the user link
 * @param {*} user : user object
 * @returns {String} user entry html
 */
const generateUserCardEntry = (label, userLinkId, user) => {
  let res = "";
  if (user) {
    res = `
    <div class="row text-left">
      <div class="col-md-6">
        <label class="mr-3">${label}</label>
      </div>
      <div class="col-md-6">
        <p id="${userLinkId}">${generateUserLink(
        user)} (${user.firstName} ${user.lastName})</p>
      </div>
    </div>`;
  }
  return res;
}

const generateExplanatoryNoteCardEntry = (request) => {
  let res = "";
  if (request.explanatoryNote) {
    res = generateCardLabelKeyEntry("Justificatif de refus",
        "explanatory-note-entry", request.explanatoryNote);
  }
  return res;
}

const generateAddressCardEntry = (request) => {
  let res = "";
  let adr = generateAddressText(request);
  if (adr) {
    res = generateCardLabelKeyEntry("Adresse de visite", "address-entry", adr);
  }
  return res;
}

const generateRequestDateCardEntry = (request) => {
  let res = "";
  if (request.requestDate) {
    res = generateCardLabelKeyEntry("Date de la demande", "request-date-entry",
        request.requestDate);
  }
  return res;
}

const generateTimeSlotCardEntry = (request) => {
  let res = "";
  if (request.timeSlot) {
    res = generateCardLabelKeyEntry("Disponibilités", "time-slot-entry",
        request.timeSlot);
  }
  return res;
}

const generateVisitDateTimeCardEntry = (request) => {
  let res = "";
  if (request.visitDateTime) {
    res = generateCardLabelKeyEntry("Date/heure de visite",
        "visit-date-time-entry", request.visitDateTime);
  }
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

/**
 * Generates page html containing request list & card div.
 * @param {Boolean} largeTable : if CSS classes should be set for large tables (false = short table)
 * @returns page html
 */
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
            <th class="align-middle">Client</th>
            <th class="${notNeededClassName}">Adresse</th>
            <th class="align-middle">Date de la demande</th>
             <th class="align-middle">États</th>
          </tr>
        </thead>
        <tbody>
          ${generateAllRows(notNeededClassName)}
        </tbody>
      </table>
    </div>
    <div class="shortElement ${shortElementClassName}" id="RequestCardDiv">Hello</div>
  </div>
    
  </div>`;
  return res;
}

/**
 * generates all rows in the request list
 * @param {String} notNeededClassName : CSS classname for not needed columns (short list -> notNeeded d-none / large list -> notNeeded align-middle)
 * @returns {String} html for all list rows
 */
const generateAllRows = (notNeededClassName) => {
  let res = "";
  requestList.forEach(request => {
    if (!requestMap[request.requestId]) {
      requestMap[request.requestId] = request;
    } else if (request !== requestMap[request.requestId]) {
      request = requestMap[request.requestId];
    }
    res += generateRow(request, notNeededClassName);
    requestMap[request.requestId] = request;
  });
  return res;
}

/**
 * Generates one request list row (given one specific request)
 * @param {*} request : request object
 * @param {String} notNeededClassName : CSS classname for not needed columns (short list -> notNeeded d-none / large list -> notNeeded align-middle)
 * @returns {String} html for one row
 */
const generateRow = (request, notNeededClassName) => {
  let statusHtml;
  let thumbnailClass = "mx-auto";

  if (!notNeededClassName.includes("d-none")) { //large table
    statusHtml = generateColoredStatus(request);
    thumbnailClass += " w-50"
  } else { //short table
    let infos = generateStatusInfos(request.requestStatus);
    statusHtml = generateDot(infos.classname);
    thumbnailClass += " w-100"
  }
  let res = `
    <tr class="toBeClicked" requestId="${request.requestId}">
      <th class="align-middle"><p>${generateUserLink(request.user)}</p></th>
      <th class="${notNeededClassName}"><p>${generateAddressText(request)}</p></th>
      <th class="align-middle"><p>${request.requestDate}</p></th>
      <th class="tableStatus text-center align-middle" status="${request.requestStatus}">${statusHtml}</th>
    </tr>`;
  return res;
}

/**
 * generates an address String from a request
 * @param {*} request
 * @returns {String} address text
 */
const generateAddressText = (request) => {
  let adr = request.address;
  return `${adr.street} ${adr.buildingNumber}, ${adr.postcode} ${adr.commune} ${adr.country}`;
}

/**
 * Generate status entry for request list as colored <p> html tag (used in large tables)
 * @param {*} request : request  object
 * @returns {String} html <p> tag
 */
const generateColoredStatus = (request) => {
  let infos = generateStatusInfos(request.requestStatus);
  return `<p class="text-${infos.classname}">${infos.status}</p>`;
}

/**
 * Generate status entry for request list as colored bootstrap badge (used in cards)
 * @param {*} furniture
 * @returns
 */
const generateBadgeStatus = (request) => {
  let infos = generateStatusInfos(request.requestStatus);
  let res = `<span class="badge badge-pill badge-${infos.classname} text-light">${infos.status}</span>`;
  return res;
}

/**
 * generate <a> tag that links to user card
 * @param {*} user : user object
 * @returns {String} <a> html tag (class = userLink)
 */
const generateUserLink = (user) => {
  return `<a href="#" userId="${user.id}" class="userLink">${user.username}</a>`;
}

/**
 * generate colored dot for status in short table
 * @param {String} colorClassName : bootstrap color classname (primary / secondary / danger / etc...)
 * @returns {String} empty bootstrap pill badge in the needed color
 */
const generateDot = (colorClassName) => {
  return `<span class="badge badge-pill p-1 badge-${colorClassName}"> </span>`;
}

/**
 * find status label & color classname (primary / danger / etc...) for a given status
 * @param {String} status
 * @returns {
 *  classname: bootstrap color suffix,
 *  status: status label,
 * } object
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
      res.status = "Refusé";
      break;
    default:
      res.classname = "";
      res.status = status;
  }
  return res;
}

const removeTimeouts = () => {
  timeouts.forEach(timeout => {
    clearTimeout(timeout);
  })
}

const changeContainerId = () => {
  let tContainer = document.querySelector('#largeTableContainer');
  if (tContainer) {
    tContainer.id = "shortTableContainer";
  }
}

//request
/**
 * accept the request
 * @param e
 * @param request
 */
const toAccept=(e,request)=>{
  e.preventDefault();
  let acceptDate=e.target.parentElement.parentElement.querySelector("#acceptInputDate").value;
  let acceptTime=e.target.parentElement.parentElement.querySelector("#acceptInputTime").value;
  if(acceptDate && acceptTime){
    let date=acceptDate+" "+acceptTime;
    let bundle={
      visitDateTime:date,
    }
    fetch("/requestForVisit/accept/" + request.requestId, {
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
    }).then((data)=>{
      requestMap[data.requestId]=data;
      loadCard(data.requestId);
    }).catch((err)=>{
      displayErrorMessage("errorDiv", err);
    });
  }else {
    let error =new Error();
    throw displayErrorMessage('tous les champs doivent être sélectionner',error);
  }
}
/**
 * refuse
 * @param e
 * @param furniture
 */
const toRefuse= (e, request) => {
  e.preventDefault();
  let explain = e.target.parentElement.parentElement.querySelector(
      "#cancelInput").value;
  let bundle = {
    explanatoryNote: explain,
  };
  fetch("/requestForVisit/cancel/" + request.requestId ,{
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
    requestMap[data.requestId] = data;
    loadCard(data.requestId);
  }).catch((err) => {
    displayErrorMessage("errorDiv", err);
  });
}

/**
 * fetch all requests, then fill requestMap
 * @returns {Promise} fetch promise
 */
async function findVisitRequestList() {
  return fetch("/requestForVisit/", {
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
    requestList = data;
    requestList.forEach(request => {
      requestMap[request.requestId] = request;
    });
  }).catch((err) => {
    console.log("Erreur de fetch !! :´\n" + err);
    displayErrorMessage("errorDiv", err);
  });
}



export default Visits;