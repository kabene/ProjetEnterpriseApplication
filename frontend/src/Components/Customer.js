import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session";

let user = getUserSessionData();
let page = document.querySelector("#page");

const Customer = () => {

    if (!verifyAdmin()) {
        Navbar();
        RedirectUrl("/");
        return;
    }

    let pageHTML = `<h1>Page de gestion des clients du site (admins)</h1>`;
    page.innerHTML = pageHTML;
}

const verifyAdmin = () => {
    let res;
    fetch("/users/me", {
        method: "GET",
        body: JSON.stringify(user),
        headers: {
            "Content-Type": "application/json",
        }
    }).then((response) => {
        if (!response.ok) res=false;
        return response.json();
    })
    .then((data) => {
        res = data.role === "admin";
    })
    .catch((err) => console.log("Erreur de fetch !! :´<\n" + err));
    return res;
}


export default Customer;



