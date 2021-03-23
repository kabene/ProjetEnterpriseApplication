import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";


let page = document.querySelector("#page");
let furnitureList;
let currentUser;
let pageHTML;


const FurnitureList = async () => {
    if ( await !verifyAdmin()) {
        Navbar();
        RedirectUrl("/");
        return;
    }
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
        console.log(response);
        if (!response.ok) 
            throw error();
        return response.json();
    }).then((data) => {
        console.log(data);
        furnitureList = data;
    }).catch((err) => {
        console.log("Erreur de fetch !! :´\n" + err);
    });

    console.log(furnitureList);

    pageHTML = `<h1>Furniture list:</h1>`;
    pageHTML += generateTable();
    page.innerHTML = pageHTML;
}

const generateTable = () => {
    let res = `
    <table class="table table-bordered table-hover">
        <thead><tr>
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
    </table>`;
    return res;
} 

const generateRow = (furniture) => {
    let res = `
    <th>img TODO</th>
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
    let res;
    switch(furniture.condition) {
        case "available for sale":
            res = `<p class="text-success">Disponible à la vente</p>`; 
            break;
        default:
            res = furniture.condition;   
    }
    return res;
}
export default FurnitureList;