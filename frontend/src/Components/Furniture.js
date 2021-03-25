import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";
import imageStub from "../img/furnitures/Bureau_1.png"


let page = document.querySelector("#page");
let furnitureList;
let currentUser;
let pageHTML;


const Furniture = async () => {
    currentUser = getUserSessionData();

    pageHTML = `<div class="text-center"><h2>Loading <div class="spinner-border"></div></h2></div>`;
    page.innerHTML = pageHTML;

    await fetch("/furniture/", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok)
            throw new Error(
                "Error code : " + response.status + " : " + response.statusText
            );
        return response.json();
    }).then((data) => {
        furnitureList = data;
    }).catch((err) => {
        console.error(err);
    });
    pageHTML = generateTable();
    page.innerHTML = pageHTML;
}


const generateTable = () => {
    let res = `<div class="row mx-5 p-5 border">`;
    furnitureList.forEach(furniture => {
        res += generateItem(furniture);
    })
    res += `</div>`;
    return res;
}

const generateItem = (furniture) => {
    let res = `
        <div class="col-4 px-0">
            <img class="w-50" src="` + imageStub /*furniture.favouritePhoto.source*/ +`" alt="thumbnail"/>
            <p> ` + furniture.description + ` </p>`;
    if (furniture.condition === "available for sale")
        res += `<button type="button" class="btn btn-sm btn-primary">Introduire une option</button>`;
    //TODO add 'annuler option button + events when clicking on 'Introduire une option' button
    res += `</div>`;
    return res;
}
export default Furniture;