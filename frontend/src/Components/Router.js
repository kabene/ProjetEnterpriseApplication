import HomePage from "./HomePage.js";
import Authentication from "./Authentication.js";
import Furniture from "./Furniture.js";
import Users from "./Users.js";
import Visits from "./Visits.js";
import ErrorPage from "./ErrorPage.js";
import FurnitureList from "./FurnitureList.js";
import VisitRequest from "./VisitRequest.js";
import ClientVisit from "./ClientVisit.js";
import LogoutComponent from "./LogoutComponent.js";
import {fetchMe} from "../utils/utils.js";
import { getUserLocalData, getUserSessionData, setUserLocalData, setUserSessionData } from "../utils/session.js";
import { setLayout } from "../utils/render.js";

const routes = {
    "/": HomePage,
    "/authentication": Authentication,
    "/furniture": Furniture,
    "/users": Users,
    "/visits": Visits,
    "/myVisits": ClientVisit,
    "/furnitureList": FurnitureList,
    "/visitRequest": VisitRequest,
    "/logout": LogoutComponent
};

let componentToRender;
let navbar = document.querySelector("#navbar");

const Router = () => {
    window.addEventListener("load", onLoadHandler);
    navbar.addEventListener("click", onNavigateHandler);
    window.addEventListener("popstate", onHistoryHandler);
}

//onLoadHandler
const onLoadHandler = async () => {
    let url = window.location.pathname;
    console.log("onLoad : ", url);
    await getRememberMe(); // logs in if remember me
    componentToRender = routes[url];
    if(!componentToRender){
        ErrorPage(url)
        return;
    }
    componentToRender();
}

//onNavigateHandler
const onNavigateHandler = (e) => {
    let uri;
    e.preventDefault();
    uri = e.target.dataset.uri;
    removeModals();
    if(uri) {
        console.log("onNavigate : ", uri);
        window.history.pushState({}, uri, window.location.origin + uri);
        componentToRender = routes[uri];
        if(!componentToRender) {
            ErrorPage(uri);
            return;
        }
        componentToRender();
    }
};

//onHistoryHandler (arrows <- -> )
const onHistoryHandler = () => {
    console.log("onHistory : ", window.location.pathname);
    removeModals();
    componentToRender = routes[window.location.pathname];
    if(!componentToRender){
        ErrorPage(window.location.pathname);
        return;
    }
    componentToRender();
};

const RedirectUrl = (uri, data) => {
    window.history.pushState({}, uri, window.location.origin + uri); 
    console.log(window.location.pathname);
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
        if(token) {
            console.log("GET /users/login");
            await fetch("/users/login", {
                method: "GET",
                headers: {
                    Authorization: token
                }
            }).then((response) => {
                if (!response.ok) throw new Error("Error code : " + response.status + " : " + response.statusText);
                return response.json();
            })
            .then((data) => onUserLogin(data))
            .catch((err) => {
                console.log("remember me token expired : ", err);
            });
        }else {
            console.log("Not logged in -> no remember me token stored");
        }
    }else {
        console.log("Already logged in -> existing session data");
    }
}

const onUserLogin = async (data) => {
    console.log("Logged in via remember me token : ", data)
    
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