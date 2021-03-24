import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";

let page = document.querySelector("#page");
let usersList;
let currentUser;
let pageHTML;


const Customer = async () => {
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
            throw error();
        return response.json();
    }).then((data) => {
        usersList = data;
    }).catch((err) => {
        console.log("Erreur de fetch !! :´\n" + err);
    });


    pageHTML = `<div class="mx-5 bmd-layout-container"><h1>Liste des clients:</h1>`;
    pageHTML += generateShortTable()
    pageHTML += generateLargeTable();
    page.innerHTML = pageHTML;
}

const generateLargeTable = () => {
    let res = `
    <table id="largeTable" class="table table-bordered table-hover bmd-layout-content">
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
            res = res + `</tbody>
    </table></div>`;
    return res;
}

const generateShortTable = () => {
    let res = `
    <table id="shortTable" class="table table-bordered table-hover bmd-layout-drawer">
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
    res = res + `</tbody>
    </table></div>`;
    return res;
}

const generateLargeRow = (user) => {
    return ` 
    <tr data-toggle="drawer" data-target="#shortTabme">
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

export default Customer;