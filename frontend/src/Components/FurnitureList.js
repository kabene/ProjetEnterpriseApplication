import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";

let page = document.querySelector("#page");

const FurnitureList = async () => {

    if ( await !verifyAdmin()) {
        Navbar();
        RedirectUrl("/");
        return;
    }


    let pageHTML = `<h1>Page de liste des meubles du site (admins)</h1>`;
    page.innerHTML = pageHTML;
}

export default FurnitureList;