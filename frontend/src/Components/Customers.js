import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";

let page = document.querySelector("#page");
let usersList;
let currentUser;
let pageHTML;


const Customers = async () => {
    currentUser = getUserSessionData();

    pageHTML = `<div class="text-center"><h2>Loading <div class="spinner-border"></div></h2></div>`;
    page.innerHTML = pageHTML;

    await fetch("/users/customers", {
        method: "GET",
        headers: {
            "Authorization": currentUser.token,
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok) 
            throw new Error(
                "Error code : " + response.status + " : " + response.statusText
            );
        return response.json();
    }).then((data) => {
        usersList = data;
    }).catch((err) => {
        console.error(err);
    });


    pageHTML = `
    <h1>Liste des clients:</h1>
    <div class="mx-5 row">
        <div class="col-12">
        <input type="text" placeholder="Rechercher par nom, prénom, code postal ou ville" class="w-50 mb-2">`
            + generateLargeTable() + 
        `</div>
        <div id="shortTable" class="col-4 collapse collapsedDiv">
            <input type="text" placeholder="Rechercher" class="mb-2">
            <button type="button" class="btn btn-dark mb-2" data-toggle="collapse" data-target=".collapsedDiv">Retour à la liste</button>`
            + generateShortTable() +
        `</div>
        <div class="col-8 collapse collapsedDiv">`
            + generateCustomerCard() +
        `</div>
    </div>`;
    page.innerHTML = pageHTML;
}

const generateLargeTable = () => {
    let res = `
    <table id="largeTable" class="table table-bordered table-hover">
        <thead class="table-secondary">
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Pseudo</th>
                <th>Email</th>
                <th>Nombre de meubles achetés</th>
                <th>Nombre de meubles vendus</th>
                <th>Rôle</th>
            </tr>
        </thead>
        <tbody>`;
            usersList.users.forEach(user => {
                res += generateLargeRow(user);
            });
            res = res + `
        </tbody>
    </table>`;
    return res;
}

const generateShortTable = () => {
    let res = `
    <table class="table table-bordered table-hover">
        <thead class="table-secondary">
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
            </tr>
        </thead>
        <tbody>`;
            usersList.users.forEach(user => {
                res += generateShortRow(user);
            });
            res = res + `
        </tbody>
    </table>`;
    return res;
}

const generateLargeRow = (user) => {
    return ` 
    <tr data-toggle="collapse" data-target=".collapsedDiv">
        <th><p>` + user.lastName + `</p></th>
        <th><p>` + user.firstName + `</p></th>
        <th><p>` + user.username + `</p></th>
        <th><p>` + user.email + `</p></th>
        <th><p>0 (STUB)</p></th>
        <th><p>0 (STUB)</p></th>
        <th><p>` + user.role + `</p></th>
   </tr>`;
}


const generateShortRow = (user) => {
    return ` 
    <tr>
        <th><p>` + user.lastName + `</p></th>
        <th><p>` + user.firstName + `</p></th>
   </tr>`;
}

const generateCustomerCard = () => {
    return `
    <div  id="customerCard" class="w-50 h-50 border">
      <a>Fiche Client</a>
    </div>
    `;
}

export default Customers;
