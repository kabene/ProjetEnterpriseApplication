import HomePage from "./HomePage.js";
import Authentication from "./Authentication.js";
import Furniture from "./Furniture.js";
import Users from "./Users.js";
import Visits from "./Visits.js";
import ErrorPage from "./ErrorPage.js";
import FurnitureList from "./FurnitureList.js";
import ClientVisit from "./ClientVisit.js";
import LogoutComponent from "./LogoutComponent.js";
import ReleaseTakeoverComponent from "./ReleaseTakeoverComponent";
import {fetchMe} from "../utils/utils.js";
import {getUserLocalData, getUserSessionData, setUserLocalData, setUserSessionData} from "../utils/session.js";
import {setLayout} from "../utils/render.js";

const routes = {
  "/": HomePage,
  "/authentication": Authentication,
  "/furniture": Furniture,
  "/users": Users,
  "/visits": Visits,
  "/myVisits": ClientVisit,
  "/furnitureList": FurnitureList,
  "/logout": LogoutComponent,
  "/releaseTakeover": ReleaseTakeoverComponent,
};

let componentToRender;
let navbar = document.querySelector("#navbar");


const Router = () => {
  window.addEventListener("load", onLoadHandler);
  navbar.addEventListener("click", onNavigateHandler);
  window.addEventListener("popstate", onHistoryHandler);
}


const onLoadHandler = async () => {
  let url = window.location.pathname;-
  await getRememberMe();
  componentToRender = routes[url];
  if (!componentToRender) {
    ErrorPage(url)
    return;
  }
  componentToRender();
}


const onNavigateHandler = (e) => {
	e.preventDefault();
  let uri;
  uri = e.target.dataset.uri;
  removeModals();
  if (uri) {
    window.history.pushState({}, uri, window.location.origin + uri);
    componentToRender = routes[uri];
    if (!componentToRender) {
      ErrorPage(uri);
      return;
    }
    componentToRender();
  }
};

const onHistoryHandler = () => {
  removeModals();
  componentToRender = routes[window.location.pathname];
  if (!componentToRender) {
    ErrorPage(window.location.pathname);
    return;
  }
  componentToRender();
};

const RedirectUrl = (uri, data) => {
  window.history.pushState({}, uri, window.location.origin + uri);
  removeModals();
  componentToRender = routes[uri];  
  if(!componentToRender){
    ErrorPage(uri);
    return;
  }
  if(!data)
    componentToRender();
  else 
    componentToRender(data);
};

const getRememberMe = async () => {
  let sessionData = getUserSessionData();
  if(!sessionData) {
    let token = getUserLocalData();
    if (token) {
      let response = await fetch("/users/login", {
        method: "GET",
        headers: {
          Authorization: token
        }
      });
			if (!response.ok) {
				const err = "Error code : " + response.status + " : " + response.statusText;
				console.error(err);
			}
			let data = await response.json();
			try {
				onUserLogin(data)
			} catch (err) {
				console.log("remember me token expired : ", err);
			}
    }
	}
}

const onUserLogin = async (data) => {
  let user = await fetchMe(data.token);
  const bundle = {...data, isAutenticated: true, isAdmin: user.role === "admin"};
  setUserSessionData(bundle);
  setUserLocalData(data.token);
  setLayout();
}

const removeModals = () => {
  let modalArray = document.querySelectorAll(".modal-backdrop");
  for (let i = 0; i < modalArray.length; i++) {
    let m = modalArray[i];
    m.parentNode.removeChild(m);
  }
}

export {Router, RedirectUrl};