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
                        <div class="row mx-0 pt-5">
                            <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                                <ol class="carousel-indicators bg-secondary">
                                    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                                    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                                    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                                </ol>
                                <div class="carousel-inner">
                                    <div class="carousel-item active">
                                        <img class="w-75 m-auto" src="` + imageStub + `" alt="Meuble 1">
                                    </div>
                                    <div class="carousel-item">
                                            <img class="w-75 m-auto" src="` + imageStub + `" alt="Meuble 2">
                                    </div>
                                    <div class="carousel-item justify-content-center">
                                        <img class="w-75 m-auto" src="` + imageStub + `" alt="Meuble 3">
                                    </div>
                                </div>
                                <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                                    <span class="carousel-control-prev-icon bg-dark" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                                    <span class="carousel-control-next-icon bg-dark" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>
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