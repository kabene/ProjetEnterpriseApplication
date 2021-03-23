import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {getUserSessionData} from "../utils/session";


let page = document.querySelector("#page");

const Customer =async () => {

    if (!verifyAdmin()) {
        Navbar();
        RedirectUrl("/");
        return;
    }

    let pageHTML = `<h1>Page de gestion des clients du site (admins)</h1>`;
    page.innerHTML = pageHTML;
}


const verifyAdmin = async () => {
    let token = getUserSessionData().token;
    let res = false;
    let result;
    await fetch("/users/me", {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok) {
            res = false;
        }
        return response.json();
    })
    .then((data) => {
        result = data.role;
        if (result === "admin") {
            res = true;
        }
    })
    .catch((err) => {
        console.log("Erreur de fetch !! :´\n" + err);
    });
    return res;
}



export default Customer;



