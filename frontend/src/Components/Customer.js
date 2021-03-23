import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";



let page = document.querySelector("#page");

const Customer =async () => {


    let pageHTML = `<h1>Page de gestion des clients du site (admins)</h1>`;
    page.innerHTML = pageHTML;
}




export default Customer;



