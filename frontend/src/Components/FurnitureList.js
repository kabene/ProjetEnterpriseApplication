import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";


let page = document.querySelector("#page");
let furnitureList;
let currentUser;
let pageHTML;


const FurnitureList = async () => {
    currentUser = getUserSessionData();

    pageHTML = `<div class="text-center"><h2>Loading <div class="spinner-border"></div></h2></div>`;
    page.innerHTML = pageHTML;

    await fetch("/furniture/detail", {
        method: "GET",
        headers: {
            "Authorization": currentUser.token,
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok) 
            throw error();
        return response.json();
    }).then((data) => {
        furnitureList = data;
    }).catch((err) => {
        console.log("Erreur de fetch !! :´\n" + err);
    });


    pageHTML = `<div class="mx-5"><h1>Liste des meubles:</h1>`;
    pageHTML += generateTable();
    page.innerHTML = pageHTML;
}

const generateTable = () => {
    let res = `
    <table class="table table-bordered table-hover">
        <thead class="table-secondary"><tr>
            <th></th>
            <th>Description</th>
            <th>Type</th>
            <th>État</th>
            <th>Vendeur</th>
            <th>Acheteur</th>
            <th>Prix de vente</th>
            <th>Prix spécial</th>
        </tr></thead>
        <tbody>`;
    furnitureList.forEach(furniture => {
        res += generateRow(furniture);
    });
    res = res + `</tbody>
    </table></div>`;
    return res;
} 

const generateRow = (furniture) => {
    let res = `
    <th>`;
    if(furniture.favouritePhoto) {
        res += `<img src="` + furniture.favouritePhoto.source +`" alt="thumbnail photoId=` + furniture.favouritePhoto.photoId + `"/>`;
    }
    res += `</th>
    <th><p>` + furniture.description + `</p></th>
    <th><p>` + furniture.type + `</p></th>
    <th>` + generateColoredState(furniture) +`</th>
    <th>`;
    if(furniture.seller) {
        res += `<a href="#">` + furniture.seller.username +`</a>`;
    }
    res += `</th><th>`
    if(furniture.buyer) {
        res += `<a href="#">` + furniture.buyer.username +`</a>`;
    };
    res += `</th><th>`
    if(furniture.sellingPrice) {
        res += `<p>` + furniture.sellingPrice +`€</p>`;
    };
    res += `</th><th>`
    if(furniture.specialSalePrice) {
        res += `<p>` + furniture.specialSalePrice +`</p>`;
    };
    res += `</th></tr>`
    return res;
}

const generateColoredState = (furniture) => {
    let res, classname, condition;
    switch(furniture.condition) {
        case "available_for_sale":
            classname="text-success";
            condition = "Disponible à la vente";
            break;
        case "accepted":
            classname="text-info";
            condition = "Accepté";
            break;
        case "in_restoration":
            classname="text-warning";
            condition="En restoration";
            break;
        case "under_option":
            classname="text-danger";
            condition="Sous option";
            break;
        case "sold":
            classname="text-danger";
            condition="Vendu";
            break;
        case "withdrawn":
            classname="text-dark";
            condition="Retiré de la vente";
            break;
        case "requested_for_visit":
        case "refused":
        case "reserved":
        case "delivered":
        case "collected":
        default:
            classname="";
            condition = furniture.condition;
    }
    res = `<p class="${classname}">${condition}</p>`; 
    return res;
}

//input: "primary", "secondary", "info", etc...
const generateDotNotif = (colorClassName) => {
    return `<span class="badge badge-pill badge-${colorClassName}">‏‏‎ ‎</span>`
}

export default FurnitureList;