import Navbar from "./Navbar";
import {RedirectUrl} from "./Router";
import {verifyAdmin} from "../utils/utils.js";
import { getUserSessionData } from "../utils/session";
import {Loader} from "@googlemaps/js-api-loader";

let page = document.querySelector("#page");
let usersList;
let userDetail;
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
    //
    await AddressToGeo("roodebeek 52");
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
    <tr class="toBeClicked" data-toggle="collapse" data-target=".collapsedDiv">
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
    <tr class="toBeClicked">
        <th><p>` + user.lastName + `</p></th>
        <th><p>` + user.firstName + `</p></th>
   </tr>`;
}

const generateCustomerCard = (userDetail) => {
    return `
   <div class="container emp-profile">
            <form>
                <div class="row">
                    <div class="col-md-6">
                        <div class="profile-head">
                                    <h5 id="Name&Firstname">
                                      Kshiti Ghelani
                                    </h5>
                                    <p class="proile-rating" >ROLE : <span id="role"><p>     </p> </span></p>
                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">informations personneles</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">adresses et plan</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <input type="submit" class="profile-edit-btn" name="btnAddMore" value="prendre le controle"/>
                    </div>
                </div>
                <div class="row">
            
                    <div class="col-md-8">
                        <div class="tab-content profile-tab" id="myTabContent">
                            <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>username</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="username"> </p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Nom et prénom:</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="Name&Firstname" >Kshiti Ghelani</p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Email</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="email" >   </p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>nombre d'achat</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="purchased_furniture_nbr">   </p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>nombre de ventes</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="sold_furniture_nbr">   </p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>status</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p id="waiting">   </p>
                                            </div>
                                        </div>
                                         <div class="col-md-2" style="display: flex"> 
                        <input type="submit" class="profile-edit-btn" name="btnAddMore"  id="approuver" value="approuver" style="color: #0062cc; margin:5px" />
                        <input type="submit" class="profile-edit-btn" name="btnAddMore" id="refuser" value="refuser" style="color: red; margin:5px"/>
                    </div>
                            </div>
                    
                            <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label> Adresse </label>
                                            </div>
                                            <div class="col-md-6">
                                                <p> adresse concat</p>
                                            </div>
                                        </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <label>Map</label><br/>
                                        <div id="map" ></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>           
        </div>
    `;
}

const clientDetail=async (id)=>{
    await fetch(`/users/detail/${id}`, {
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
        userDetail= data;
    }).catch((err) => {
        console.error(err);
    });

}


const map=(latitude,lngitude) => {
    console.log(latitude,lngitude);
    let map;
    const additionalOptions = {};
    const place ={lat: latitude, lng: lngitude };
    const loader = new Loader({
        apiKey: "AIzaSyCOBWUhB79EsC0kEXXucgtPUgmLHqoJ1u4",
        version: "weekly",
        ...additionalOptions,
    });
    console.log(place);
    loader.load().then(() => {
        map = new google.maps.Map(document.getElementById("map"), {
            center: place,
            zoom: 13,
        });
        new google.maps.Marker({
            position: place,
            map:map,
        })
    });
}


const AddressToGeo=async (address)=>{

    var platform = new H.service.Platform({
        'apikey': 'QHZv6jItrBmW0n3fXSO5HbZzbBpxzunbSXquM_ap6o0'
    });

    var service = platform.getSearchService();

    await service.geocode({
        q: address
    }, (result) => {
        console.log(result.items[0].position)
        map(result.items[0].position.lat,result.items[0].position.lng);
    }, alert)
}

export default Customers;
