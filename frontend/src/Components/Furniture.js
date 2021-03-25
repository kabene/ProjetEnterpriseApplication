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
    return `<div class="row mx-5 p-5 border">` + getAllItems() + `</div>`;
}

const getAllItems = () => {
    let res = "";
    furnitureList.forEach(furniture => res += generateItem(furniture));
    return res;
}

const generateItem = (furniture) => {
    return`
        <div class="col-4 px-0">
            <img class="w-50" src="` + imageStub /*furniture.favouritePhoto.source*/ +`" alt="thumbnail"/>
            <p>` + furniture.description + `</p>`
            + getOptionButton(furniture) +
        `</div>`;
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