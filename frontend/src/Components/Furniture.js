import { findCurrentUser } from "../utils/session";
import {generateCloseBtn, generateModalPlusTriggerBtn} from "../utils/modals.js";
import {displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, findFavImgSrc, generateLoadingAnimation} from "../utils/utils.js"


let page = document.querySelector("#page");
let furnitureList;
let currentUser;
let optionList;
let images = importAllFurnitureImg();

const Furniture = async () => {
    currentUser = findCurrentUser();

    page.innerHTML = `
    <div class="col-5 mx-auto">
        <div id="errorDiv" class="d-none"></div>
    </div>
    ${generateLoadingAnimation()}`;

    furnitureList = await getFurnitureList();
    optionList = await getOptionList();

    page.innerHTML = `
    <div class="col-5 mx-auto">
        <div id="errorDiv" class="d-none"></div>
    </div>
    ${generateTable()}`;

    document.querySelectorAll(".btnCreateOption").forEach(element =>{
        element.addEventListener("click", addOption );
    });
    document.querySelectorAll(".cancelOptButton").forEach(element=>{
      element.addEventListener("click",cancelOption);
    })
}

const cancelOption= (e)=>{
  e.preventDefault();
  let furnitureId = e.target.id.substring(4);
  let optionId;
  optionList.forEach(option=>{
    if(option.furnitureId == furnitureId ) {
      if (!option.isCanceled){
        optionId = option.optionId;
      }
    }
  });

   fetch("/options/cancel/"+optionId,{
    method: "PATCH",
    headers: {
      "Authorization": currentUser.token,
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if(!response.ok) {
      throw new Error(response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => {
    refresh(data, "AVAILABLE_FOR_SALE");
  }).catch((err) => {
     console.log("Erreur de fetch !! :´<\n" + err);
     displayErrorMessage("errorDiv", err);
   });
}




const addOption =  (e) => {
  e.preventDefault();
  let furnitureId = e.target.id.substring(3);
  let duration = e.target.parentElement.parentElement.querySelector("input").value;

  let bundle = {
    furnitureId: furnitureId,
    duration: duration,
  }
  console.log(bundle);
   fetch("/options/", {
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
      return response.json();
    }).then((data) => {
      refresh(data.option, "UNDER_OPTION");
    }).catch((err) => {
     console.log("Erreur de fetch !! :´<\n" + err);
     displayErrorMessage("errorDiv", err);
   });

}

const refresh = (data, status) => {
  optionList.push(data);
  updateFurnitureList(data.furnitureId, status)
  page.innerHTML = generateTable();

  document.querySelectorAll(".btnCreateOption").forEach(element =>{
    element.addEventListener("click", addOption )
  });
  document.querySelectorAll(".cancelOptButton").forEach(element=>{
    element.addEventListener("click",cancelOption);
  })
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
        console.log("Erreur de fetch !! :´<\n" + err);
        displayErrorMessage("errorDiv", err);
      });
    return ret;
}

const getOptionList= async () => {
  if(currentUser) {
    let ret = [];
    await fetch("/options/", {
      method: "GET",
      headers: {
        "Authorization": currentUser.token,
        "Content-Type": "application/json",
      },
    }).then((response) => {
      if (!response.ok)
        throw new Error(
            "Error code : " + response.status + " : " + response.statusText);
      return response.json();
    }).then((data) => {
      ret = data;
    }).catch((err) => {
      console.log("Erreur de fetch !! :´<\n" + err);
      displayErrorMessage("errorDiv", err);
    });
    return ret;
  }
}


const updateFurnitureList = (furnitureId, status) => {
    furnitureList.forEach(furniture => {
        if (furniture.furnitureId === furnitureId) {
            furniture.status = status;
        }
    })
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
        <div>`
            + getTag(furniture) +
            `<img class="imageFurniturePage" src="${findFavImgSrc(furniture, images)}" alt="thumbnail" data-toggle="modal" data-target="#modal_` + furniture.furnitureId +`"/>
            <p class="text-center">` + furniture.description + `</p>`
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
                                            <img class="w-75" src="${findFurnitureImgSrcFromFilename(tabPhotoToRender[i].source, images)}" alt="Photo meuble">
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

const getTag = (furniture) => {
  let ret;
  if (furniture.status=="SOLD")
    ret = `<span className="badge badgeSold">VENDU !</span>`;
  else if (furniture.status=="UNDER_OPTION") {
    let optionId;
    optionList.forEach(option=>{
      if(option.furnitureId == furnitureId ) {
        if (!option.isCanceled){
          optionId = option.optionId;
        }
      }
    });
    ret = `<span className="badge badgeUnderOption">Sous option durant ` + optionId.duration + ` jours</span>`;
  }
  else
    ret = `<span className="badge badgeSold">Error</span>`;
}


const getOptionButton = (furniture) => {
  let alreadyUnderOption=false;
  if(currentUser) {
  optionList.forEach(option=>{
      if( option.furnitureId == furniture.furnitureId){
          if( !option.canceled){
            if(option.userId == currentUser.user.id) {
              alreadyUnderOption =true;
            }
       }
  }

  })};
  if (furniture.status === "AVAILABLE_FOR_SALE" && typeof currentUser!=="undefined") { //place option

    let sendBtn = generateCloseBtn("Confirmer", "btn"+furniture.furnitureId , "btnCreateOption btn btn-primary mx-5");
    return  generateModalPlusTriggerBtn("modal_"+furniture.furnitureId, "Mettre une option", "btn btn-primary", "<h4>Mettre une option</h4>", generateOptionForm(), sendBtn, "Annuler", "btn btn-danger");
  }
  else if( furniture.status === "UNDER_OPTION" && alreadyUnderOption && typeof currentUser!=="undefined" ) { //cancel option
    return `<button type="button" id="cbtn${furniture.furnitureId}" class="btn btn-danger cancelOptButton">annuler l'option</button>`;
  }else{ // nothing
    return "";
  }
}

const getTabPhotoToRender = (furniture) => {
    return furniture.photos;
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