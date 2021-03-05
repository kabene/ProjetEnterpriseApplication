import HomePage from "./HomePage.js";
import Authentication from "./Authentication.js";
import Furnitures from "./Furnitures.js";
import Customer from "./Customer.js";
import Visits from "./Visits.js";
import ErrorPage from "./ErrorPage.js";
import FurnitureList from "./FurnitureList.js";
import VisitRequest from "./VisitRequest.js";
import LogoutComponent from "./LogoutComponent.js";

const routes = {
    "/": HomePage,
    "/authentication": Authentication,
    "/furnitures": Furnitures,
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

export {Router, RedirectUrl};