import { getUserSessionData } from "../utils/session";
import imageStub from "../img/furnitures/Bureau_1.png"


let page = document.querySelector("#page");
let furnitureList;
let currentUser;


const Furniture = async () => {
    currentUser = getUserSessionData();

    page.innerHTML = generateLoadingAnimation();

    furnitureList = await getFurnitureList();

    page.innerHTML = generateTable();
}


const generateTable = () => {
    return `<div class="row mx-5 p-5 border">` + generateAllItemsAndModals() + `</div>`;
}

const generateAllItemsAndModals = () => {
    let res = "";
    furnitureList.forEach(furniture => res += generateItemAndModal(furniture));
    return res;
}

const generateItemAndModal = (furniture) => {
    let item = `
        <div class="col-4 px-0" data-toggle="modal" data-target="#modal_` + furniture.furnitureId +`">
            <img class="w-50" src="` + imageStub /*furniture.favouritePhoto.source*/ +`" alt="thumbnail"/>
            <p>` + furniture.description + `</p>`
            + getOptionButton(furniture) +
        `</div>`;


    let modal = `
        <div class="modal fade" id="modal_` + furniture.furnitureId + `">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <!-- Modal Header -->
                    <div class="modal-header"></div>
                    <!-- Modal Body -->
                    <div class="modal-body">
                    
                    </div>
                </div>
            </div>                         
        </div>`;
    return item + modal;
}

const getOptionButton = (furniture) => {
    if (furniture.condition === "available for sale" && currentUser !== null /*TODO check if the user is a simple customer*/) {
        //TODO add events when clicking on button
        return `<button type="button" class="btn btn-sm btn-primary">Introduire une option</button>`;
    } else {
        //TODO add 'annuler option' button + event when clicking on it if the user has booked the furniture
    }
    return "";
}


const generateLoadingAnimation = () => {
    return `
        <div class="text-center">
            <h2>Loading <div class="spinner-border"></div></h2>
        </div>`
}

const getFurnitureList = async () => {
    let ret = [];
    await fetch("/furniture/", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok)
            throw new Error("Error code : " + response.status + " : " + response.statusText);
        return response.json();
    }).then((data) => {
         ret = data;
    }).catch((err) => {
        console.error(err);
    });
    return ret;
}

export default Furniture;