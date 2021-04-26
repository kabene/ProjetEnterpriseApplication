import {getUserSessionData} from "../utils/session";
import {displayErrorMessage, generateLoadingAnimation} from "../utils/utils";
import {RedirectUrl} from "./Router";

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

const Visits = async (id) => {

  currentUser = getUserSessionData();
  pageHTML = `
  <div class="col-5 mx-auto"><div id="errorDiv" class="d-none"></div></div>
  <div id="mainPage" class="col-12 px-0">${generateLoadingAnimation()}</div>`;
  page.innerHTML = pageHTML;
  mainPage = document.querySelector("#mainPage");
  await findVisitRequestList();
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
//displayers

const displayLargeTable = () => {
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
  let requestCardDiv = document.querySelector("#RequestCardDiv");
  requestCardDiv.innerHTML = generateLoadingAnimation();
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
  let id = element.attributes["requestId"].value;
  let request = requestMap[id];
  if (!request) {
    await reloadPage();
  }
  if (request) {
    currentRequestId = id;
    generateCard(request);
    document.querySelectorAll(".userLink").forEach(
        (link) => link.addEventListener("click", onUserLinkClicked));
    isDisplayingLargeTable = false;
  } else {
    displayErrorMessage("errorDiv", new Error("Request introuvable :'<"));
  }
}

const onUserLinkClicked = (e) => {
  e.preventDefault();
  let link = e.target;
  let userId = link.getAttribute("userid");
  console.log(`Linking to user card (id: ${userId})`);
  RedirectUrl("/users", userId);
}


const addTransitionBtnListeners = (request) => {
  document.querySelectorAll(".transitionBtn").forEach(element => {
    element.addEventListener("click",
        findTransitionMethod(element.id, request));
  })
}

const findTransitionMethod = (btnId, request) => {
  switch (btnId) {
    case "Accept":
     // return (e) => toAvailable(e, furniture); TODO
    case "Refuse":
     // return (e) => toRestoration(e, furniture); TODO
    default:
      return (e) => {
        e.preventDefault();
        console.log("unrecognized button id: " + btnId); //'do nothing' method
      };
  }
  ;
}


const generateCard = (request) => {

  let requestCardDiv = document.querySelector("#requestCardDiv");
  let cardHTML = generateCardHTML(request);
  requestCardDiv.innerHTML = cardHTML;
  addTransitionBtnListeners(request);
  document.querySelectorAll(".favRadio").forEach((element) => {
    element.addEventListener("click", onFavRadioSelected);
  });
  document.querySelectorAll(".visibleCheckbox").forEach((element) => {
    element.addEventListener("click", onVisibleCheckClicked);
  });
  document.querySelector("#saveBtnPhoto").addEventListener("click",
      onSaveModifPhotos);
  document.querySelector("#home-tab").addEventListener("click", () => {
    openTab = "infos";
  });
  document.querySelector("#profile-tab").addEventListener("click", () => {
    openTab = "photos";
  });
  addImage(furniture);

}

const generateCardHTML = (request) => {
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
  if (openTab === "photos") {
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
              <!-- <p>generateCardFavouritePhotoImgTag(request)</p>-->
              </div>
              <div class="col-md-6 text-left">
                <h5 id="descriptionCardEntry">${request.explanatoryNote}</h5>
                <p class="proile-rating">ÉTAT : <span id="statusCardEntry">${generateBadgeStatus(
      request)}</span></p>
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
              ${generateClientCardEntry(request)}
            </div>       
            <div class="tab-pane fade ${photoTab.tabClassname}" id="profile" role="tabpanel" aria-labelledby="profile-tab">
             
            </div>
          </div>
        </div>
      </div>
    </form>           
  </div>
  `;
  return res;
}

const generateClientCardEntry = (request) => {
  let res = "";
  if (request.user) {
    res = generateUserCardEntry("Client", "clientCardEntry", request.user);
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

/**
 * Reloads the page and re-fetch request information.
 * Displays loading animation while awaiting the fetch.
 */
const reloadPage = async () => {
  mainPage.innerHTML = generateLoadingAnimation();
  await findVisitRequestList();
  mainPage = generatePageHtml();
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
      <th class="${notNeededClassName}"><p>${generateSellerLink(request.user)}</p></th>
      <th class="${notNeededClassName}"><p>${request.address.street + ` `
  + request.address.buildingNumber + `,` + request.address.postcode + ` `
  + request.address.commune + ` ` + request.address.country}</p></th>
      <th class="${notNeededClassName}"><p>${request.requestDate}</p></th>
      <th class="tableStatus text-center align-middle" status="${request.requestStatus}">${statusHtml}</th>
    </tr>`;
  return res;
}
const generateSellerLink = (user) => {
  let res = "";
  if (user) {
    res = generateUserLink(user);
  }
  return res;
}

const generateColoredStatus = (request) => {
  let infos = generateStatusInfos(request.requestStatus);
  return `<p class="text-${infos.classname}">${infos.status}</p>`;
}

const generateBadgeStatus = (furniture) => {
  let infos = generateStatusInfos(furniture.status);
  let res = `<span class="badge badge-pill badge-${infos.classname} text-light">${infos.status}</span>`;
  return res;
}

const generateUserLink = (user) => {
  return `<a href="#" userId="${user.id}" class="userLink">${user.username}</a>`;
}

const generateDot = (colorClassName) => {
  return `<span class="badge badge-pill p-1 badge-${colorClassName}"> </span>`;
}

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
  document.querySelector('#largeTableContainer').id = "shortTableContainer";
}

const loadCard = (requestId) => {
  mainPage.innerHTML = generatePageHtml(false);
  generateCard(requestMap[requestId]);
  document.querySelectorAll(".toBeClicked").forEach(
      (element) => {
        let elementReqId = element.getAttribute("requestid");
        if (elementReqId == requestId) {
          element.className = "toBeClicked bg-secondary text-light";
        }
        element.addEventListener("click", displayShortElements)
      });
  document.querySelector("#buttonReturn").addEventListener("click",
      displayLargeTable);
}

//requests

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

