import { getUserSessionData } from "../utils/session";
import imageStub from "../img/furnitures/Bureau_1.png";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js";


let page = document.querySelector("#page");
let furnitureList;
let currentUser;
let optionList;



const Furniture = async () => {
    currentUser = getUserSessionData();

    page.innerHTML = generateLoadingAnimation();

    furnitureList = await getFurnitureList();
    optionList = await getOptionList();

    page.innerHTML = generateTable();

    document.querySelectorAll(".btnCreateOption").forEach(element =>{
        element.addEventListener("click", addOption )
    });
    document.querySelectorAll(".cancelOptButton").forEach(element=>{
      element.addEventListener("click")
    })
}
const addOption = async (e) => {
  let furnitureId = e.target.id.substring(3);
  let duration = e.target.parentElement.parentElement.querySelector("input").value;

  let bundle = {
    furnitureId: furnitureId,
    duration: duration,
  }
  console.log(bundle);
  await fetch("/option/", {
    method: "POST",
    body: JSON.stringify(bundle),
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
    }).then((response) => {
      if(!response.ok) {
        throw new Error(response.status + " : " + response.statusText);
      }
    }).then((data) => {
      refresh(data); //TODO
    }).catch((err) => {
      //TODO
      console.log(err);
    });

}

const refresh = (data) => {
  optionList.push(data);
  page.innerHTML = generateTable();

  document.querySelectorAll(".btnCreateOption").forEach(element =>{
    element.addEventListener("click", addOption )
  });
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

const getOptionList= async () => {
  let ret = [];
  await fetch("/option/list", {
    method: "GET",
    headers: {
      "Authorization": currentUser.token,
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



const generateTable = () => {
    return `
    <div class="wrapperFurniturePage">
        <div></div>
        <div>
            <!-- @author Milan Raring
                https://freefrontend.com/css-search-boxes/ -->
            <form action="" class="search-bar">
                <input type="search" name="search" pattern=".*\S.*" required>
                <button class="search-btn" type="submit">
                    <span>Search</span>
                </button>
            </form>
        </div>
        <div></div>
        <div></div>
        <div class="contentFurniturePage">` + generateAllItemsAndModals() + `</div>
        <div></div>
    </div>`;
}

const generateAllItemsAndModals = () => {
    let res = "";
    furnitureList.forEach(furniture => res += generateItemAndModal(furniture));
    return res;
}

const generateItemAndModal = (furniture) => {
    let item = `
        <div>
            <img class="imageFurniturePage" src="` + imageStub /*furniture.favouritePhoto.source*/ +`" alt="thumbnail" data-toggle="modal" data-target="#modal_` + furniture.furnitureId +`"/>
            <p>` + furniture.description + `</p>`
            + getOptionButton(furniture) +
        `</div>`;

    let tabPhotoToRender = getTabPhotoToRender(furniture);

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
                                    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>`;
                                    for (let i = 1; i < tabPhotoToRender.length; i++)
                                        modal += `<li data-target="#carouselExampleIndicators" data-slide-to="1"></li>`;
    modal+=
                                `</ol>
                                <div class="carousel-inner">`;
                                    for (let i = 0; i < tabPhotoToRender.length; i++) {
                                        modal += `
                                        <div class="carousel-item active text-center">
                                            <img class="w-75" src="` + imageStub + `" alt="Photo meuble">
                                        </div>`;
                                    }
    modal+=
                                `</div>
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
  let allreadyUnderOption;
  optionList.forEach(option=>{
       allreadyUnderOption = option.furnitureId !== furniture.furnitureId && !option.canceled;
  });

  if (furniture.condition === "available_for_sale" &&  !allreadyUnderOption && currentUser !== null ) {
    let sendBtn = generateCloseBtn("Confirmer", "btn"+furniture.furnitureId , " btnCreateOption btn btn-primary mx-5");
    return  generateModalPlusTriggerBtn("modal_"+furniture.furnitureId, "Mettre une option", "btn btn-primary", "<h4>Mettre une option</h4>", generateOptionForm(), sendBtn, "Annuler", "btn btn-danger");
  } else if( furniture.condition === "under_option" && allreadyUnderOption ) {
    return `<button type="button" id="cancelBtn${furniture.furnitureId}" class="btn cancelOptButton" style="background-color: red">annuler l'option</button>`;
  }else{
    return "";
  }
}

const generateTransitionModal = (id, label, triggerColorClass="primary", closeColorClass="danger") => {
  let body = generateOptionForm();
  let sendBtn = generateCloseBtn(label, "btn"+id, `btn btn-${triggerColorClass} mx-5 transitionBtn`);
  return generateModalPlusTriggerBtn("modal_"+id, label, `btn btn-${triggerColorClass}`, `<h4>${label}</h4>`, body, `${sendBtn}`, "Fermer", `btn btn-${closeColorClass}`);
}

const getTabPhotoToRender = (furniture) => {
    let photos = furniture.photos;
    let photosToRender = [furniture.favouritePhoto];
    let favId = furniture.favouritePhotoId;
    photos.forEach(p => {
        if (p.visible && p.photoId != favId)
            photosToRender.push(p);
    })
    return photosToRender;
}

const generateOptionForm = () => {
    let res = `
  <form>
    <div class="form-group">
      <label for="durationInput" class="mr-3">Duree: </label>
      <input type="number" id="durationInput" class="w-25" name="durationInput" min="1" step="1" max="5"> jours
    </div>
  </form>
  `;
    return res;
}


export default Furniture;