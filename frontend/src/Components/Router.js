import HomePage from "./HomePage.js";
import Authentication from "./Authentication.js";
import Meubles from "./Meubles.js";
import Clients from "./Clients.js";
import Visites from "./Visites.js";
import ErrorPage from "./ErrorPage.js";
import ListeMeubles from "./ListeMeubles.js";
import DemanderVisite from "./DemanderVisite.js";
import LogoutComponent from "./LogoutComponent.js";

const routes = {
    "/": HomePage,
    "/auth": Authentication,
    "/meubles": Meubles,
    "/clients": Clients,
    "/visites": Visites,
    "/listeMeubles": ListeMeubles,
    "/demanderVisite": DemanderVisite,
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