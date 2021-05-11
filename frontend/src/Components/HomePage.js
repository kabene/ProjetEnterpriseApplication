import notFoundPhoto from "../img/notFoundPhoto.png";
import noFurniturePhoto from "../img/noFurniture.png";
import {displayErrorMessage, generateLoadingAnimation, gdpr, baseUrl, getSignal} from "../utils/utils.js";


let page = document.querySelector("#page");

let visiblePhotos;
let furnitureTypeList;

const errorDiv = `
<div class="col-5 mx-auto">
  <div id="errorDiv" class="d-none"></div>
</div>`;

let filterType = "";
let nbrPhotosInCarousel;

const HomePage = async () => {
	page.innerHTML = errorDiv + generateLoadingAnimation();
	furnitureTypeList = await getFurnitureTypeList();
	getVisiblePhotos();

	page.innerHTML = errorDiv + getPageHTML();
	gdpr(page);
	addAllEventListeners();
}

/********************  Business methods  **********************/


/**
 * Set the filter then reload the page and display the correct current filter.
 */
const onChangeSelectFilters = () => {
  filterType = document.querySelector("#furnitureTypeFilter").value;
  page.innerHTML = errorDiv + getPageHTML();
  addAllEventListeners();
  document.querySelector("[value='" + filterType + "']").setAttribute('selected', 'true');
}


/**
 * Add all the event listeners required in the document.
 */
const addAllEventListeners = () => {
  document.querySelector("#furnitureTypeFilter").addEventListener('change', onChangeSelectFilters);
}

/********************  HTML generation  **********************/


const getPageHTML = () => {
  return `
	<div class="col-5 mx-auto">
    <div id="errorDiv" class="d-none"></div>
  </div>
	<div id="filterSelectHome">
    ` + generateFilterHTML() + `
  </div>
  <div class="row mx-0 pt-5">
  	<div class="col-1 col-lg-2"></div>
    <div class="col-10 col-lg-8">` + getCarousel() + `</div>
    <div class="col-1 col-lg-2"></div>
  </div>`;
}


const generateFilterHTML = () => {
  return `
  <form>
    <div class="form-group m-3">` + generateSelectTypeTag() + `</div>
  </form>`;
}


const generateSelectTypeTag = () => {
  let ret = `<select class="form-control" id="furnitureTypeFilter"> <option value="">Rechercher un type de meuble</option>`;
  furnitureTypeList.forEach(type => ret += generateOptionTypeTag(type));
  ret += `</select>`;
  return ret;
}


const generateOptionTypeTag = (type) => {
  return `<option value="` + type.typeName + `">` + type.typeName + `</option>`;
}


const getCarousel = () => {
	if(visiblePhotos){
		let photos = getHTMLVisiblePhotos();
		return `
		<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
			<ol class="carousel-indicators bg-secondary"> ` + getHTMLCarouselIndicators() + ` </ol>
			<div class="carousel-inner">
				` + photos + `
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
		`;
	} else {
		return generateLoadingAnimation();
	}

}


const getHTMLCarouselIndicators = () => {
  let ret = `<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>`;
  for (let i = 1; i < nbrPhotosInCarousel; i++)
    ret += `<li data-target="#carouselExampleIndicators" data-slide-to="` + i+ `"></li>`;
  return ret;
}


/**
 * create an html element containing all the modal item required with the current filter and update the number in photo in the carousel.
 * @returns an html element containing all the modal item required with the current filter.
 */
const getHTMLVisiblePhotos = () => {
  let ret = "";
  nbrPhotosInCarousel = 0;

  visiblePhotos.forEach(photo => {
    if (filterType === '' || filterType === photo.furniture.type) {
      if (nbrPhotosInCarousel === 0) {
        ret += `
          <div class="carousel-item active">
            <img class="d-block img-fluid mx-auto mb-5 carouselPhoto" src="` + photo.source + `" alt="Photo meuble" onError="this.src='` + notFoundPhoto + `'">
          </div>`;
      } else {
        ret += `
          <div class="carousel-item">
            <img class="d-block img-fluid mx-auto mb-5 carouselPhoto" src="` + photo.source + `" alt="Photo meuble" onError="this.src='` + notFoundPhoto + `'">
          </div>`;
      }
      nbrPhotosInCarousel++;
    }
  });
  if (nbrPhotosInCarousel === 0) {
    return `
		<div class="carousel-item active">
			<img class="d-block img-fluid mx-auto mb-5 carouselPhoto" src="` + noFurniturePhoto + `" alt="Aucun meuble trouvé">
		</div>`;
  }
  return ret;
}

const getVisiblePhotos = async () => {
	let data = await fetchVisiblePhotos();
	visiblePhotos = data;
	page.innerHTML = getPageHTML();
	gdpr(page);
	addAllEventListeners();
}

/********************  Backend fetch  **********************/

const fetchVisiblePhotos = async () => {
  let signal = getSignal();

	let response = await fetch(baseUrl+"/photos/homePage", {
    signal,
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	});
	if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  return response.json();
}


const getFurnitureTypeList = async () => {
  let signal = getSignal();

  let response = await fetch(baseUrl+"/furnitureTypes/", {
    signal,
    method: "GET",
  });
  if (!response.ok) {
    const err = "Erreur de fetch\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
    displayErrorMessage("errorDiv", err);
  }
  return response.json();
}

export default HomePage;