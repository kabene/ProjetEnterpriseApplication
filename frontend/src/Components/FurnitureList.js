import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";

let page = document.querySelector("#page");

const FurnitureList = async () => {

    if (!verifyAdmin()) {
        Navbar();
        RedirectUrl("/");
        return;
    }


    let pageHTML = `<h1>Page de liste des meubles du site (admins)</h1>`;
    page.innerHTML = pageHTML;
}


const verifyAdmin = async (pers) => {
    let token = pers.token;
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



export default FurnitureList;