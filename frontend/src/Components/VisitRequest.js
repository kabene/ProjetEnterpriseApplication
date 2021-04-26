import {getUserSessionData} from "../utils/session";
import {displayErrorMessage, generateLoadingAnimation} from "../utils/utils";
import {
  displayErrorMessage,
  findFavImgSrc,
  generateLoadingAnimation
} from "../utils/utils.js"

let page = document.querySelector("#page");
let currentUser;
let mainPage;
let requestList;
let requestMap = [];
let timeouts = [];
let currentUser;
let pageHTML;
let openTab = "infos";

const VisitRequest = async (id) => {
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

}

// generators

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
            <th class="${notNeededClassName}">Client</th>
            <th class="align-middle">État</th>
            <th class="${notNeededClassName}">Adresse</th>
            <th class="${notNeededClassName}">Date de la demande</th>
            <th class="${notNeededClassName}">Etat</th>
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
    let infos = generateStatusInfos(request.status);
    statusHtml = generateDot(infos.classname);
    thumbnailClass += " w-100"
  }

  let res = `
    <tr class="toBeClicked" requestId="${request.requestId}">
      <th class="${notNeededClassName}"><p>${generateSellerLink(request.userId)}</p></th>
      <th class="${notNeededClassName}"><p>${request.requestDate}</p></th>
      <th class="${notNeededClassName}"><p>${request.address.street + ` ` +  request.address.buildingNumber + `,` +  request.address.postcode + ` ` + request.address.commune + ` ` + request.address.country }</p></th>
      <th class="tableStatus text-center align-middle" status="${request.status}">${statusHtml}</th>
    </tr>`;
  return res;
}
const generateSellerLink = (request) => {
  let res = "";
  if (request.userId) {
    res = generateUserLink(request.userId);
  }
  return res;
}

const generateColoredStatus = (request) => {
  let infos = generateStatusInfos(request.status);
  return `<p class="text-${infos.classname}">${infos.status}</p>`;
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
    case "confimed":
      res.classname = "success";
      res.status = "Accepté";
      break;
    case "canceled":
      res.classname = "dark";
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
 // generateCard(requestMap[requestId]); TODO
  document.querySelectorAll(".toBeClicked").forEach(
      (element) => {
        let elementReqId = element.getAttribute("requestid");
        if(elementReqId == requestId) {
          element.className = "toBeClicked bg-secondary text-light";
        }
        element.addEventListener("click", displayShortElements)
      });
  document.querySelector("#buttonReturn").addEventListener("click", displayLargeTable);
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

const displayUsercard = async () => {

}

export default VisitRequest;