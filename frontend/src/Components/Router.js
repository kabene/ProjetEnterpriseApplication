import HomePage from "./HomePage.js";
import Authentication from "./Authentication.js";
import Furniture from "./Furniture.js";
import Customer from "./Customer.js";
import Visits from "./Visits.js";
import ErrorPage from "./ErrorPage.js";
import FurnitureList from "./FurnitureList.js";
import VisitRequest from "./VisitRequest.js";
import LogoutComponent from "./LogoutComponent.js";
import { getUserLocalData, getUserSessionData, setUserLocalData, setUserSessionData } from "../utils/session.js";
import { setLayout } from "../utils/render.js";

const routes = {
    "/": HomePage,
    "/authentication": Authentication,
    "/furniture": Furniture,
    "/customers": Customer,
    "/visits": Visits,
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
const onLoadHandler = async (e) => {
    let url = window.location.pathname;
    console.log("onLoad : ", url);
    getRememberMe(); // logs in if remember me
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
const onHistoryHandler = (e) => {
    console.log("onHistory : ", window.location.pathname);
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

const getRememberMe = () => {
    let sessionData = getUserSessionData();
    if(!sessionData) {
        let token = getUserLocalData();
        if(token) {
            fetch("/users/login", {
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

const onUserLogin = (data) => {
    console.log("Logged in via remember me token : ", data)
    const user = {...data, isAutenticated: true};
    setUserSessionData(user);
    setUserLocalData(data.token);
    setLayout();
  }

export {Router, RedirectUrl};