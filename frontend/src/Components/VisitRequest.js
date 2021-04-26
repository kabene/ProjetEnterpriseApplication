import {getUserSessionData} from "../utils/session";
import {displayErrorMessage, generateLoadingAnimation} from "../utils/utils";


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

const VisitRequest = async (id) => {
  let pageHTML = `<h1>Page des visites du site (admins/clients)</h1>`;
  page.innerHTML=pageHTML;
}




export default VisitRequest;