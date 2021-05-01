import notFoundPhoto from "../img/notFoundPhoto.png";
import {displayErrorMessage, generateLoadingAnimation} from "../utils/utils.js";

let page = document.querySelector("#page");

let visiblePhotos;
let furnitureTypeList;

let filterType = "";

const HomePage = async () => {
	page.innerHTML = generateLoadingAnimation();
	visiblePhotos = await getVisiblePhotos();
	furnitureTypeList = await getFurnitureTypeList();

	page.innerHTML = getPageHTML();

	addAllEventListeners();
}


/********************  Business methods  **********************/


/**
 * Called when clicking on the apply filter button.
 * Apply all filters chosen on the furniture and render them.
 */
 const onClickApplyFilter = (e) => {
  e.preventDefault();
  filterType = document.querySelector("#furnitureTypeFilter").value;
	page.innerHTML = getPageHTML();
  addAllEventListeners();
  document.querySelector("[value='" + filter.type + "']").setAttribute('selected', 'true');
}

/**
 * Called when clicking on the cancel filter button.
 * Clear all filters chosen on the furniture and render all the furniture if there already was filters.
 */
const onClickClearFilter = (e) => {
  e.preventDefault();
  if (filterType === "")
    return;
  filterType = "";
  page.innerHTML = getPageHTML();
  addAllEventListeners();
}

/**
 * Add all the event listeners required in the document.
 */
 const addAllEventListeners = () => {
  document.querySelector("#apply-filters-btn").addEventListener("click", onClickApplyFilter);
  document.querySelector("#clear-filters-btn").addEventListener("click", onClickClearFilter);
}

/********************  HTML generation  **********************/

const getPageHTML = () => {
	return `
	<div class="col-5 mx-auto">
    <div id="errorDiv" class="d-none"></div>
  </div>
	<div>
    <h3>Filtrer les meubles:</h3>
    ` + generateFilterHTML() + `
  </div>
  <div class="row mx-0 pt-5">
    <div class="col-2 col-lg-4"></div>
    <div class="col-8 col-lg-4">` + getCarousel() + `</div>
    <div class="col-2 col-lg-4"></div>
  </div>`;
}

const generateFilterHTML = () => {
  return `
  <form class="form-inline">
    <div class="form-group m-3">` + generateSelectTypeTag() + `</div>
     <button type="submit" id="apply-filters-btn" class="btn btn-primary m-3">Appliquer</button>
     <button type="submit" id="clear-filters-btn" class="btn btn-secondary m-3">Retirer les filtres</button>
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
	return `<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
						<ol class="carousel-indicators bg-secondary"> ` + getHTMLCarouselIndicators() + ` </ol>
						<div class="carousel-inner">
							` + getHTMLVisiblePhotos() + `
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
}

const getHTMLCarouselIndicators = () => {
	let ret = `<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>`;
	for (let i = 1; i < visiblePhotos.length; i++) 
		ret += `<li data-target="#carouselExampleIndicators" data-slide-to="` + i + `"></li>`;
	return ret;
}

const getHTMLVisiblePhotos = () => {
	let visiblePhotoTmp = visiblePhotos;
	let firstPhoto = visiblePhotos.pop();
	if (typeof firstPhoto === "undefined") {
		return `
		<div class="carousel-item active">
			<img class="d-block img-fluid mx-auto mb-5" src="` + notFoundPhoto + `" alt="Meuble 1">
		</div>`;
	}
	let ret = `
		<div class="carousel-item active">
			<img class="d-block img-fluid mx-auto mb-5" src="` + firstPhoto.source + `" alt="Meuble 1" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	visiblePhotoTmp.forEach(photo => {
		ret += `
		<div class="carousel-item">
			<img class="d-block img-fluid mx-auto mb-5" src="` + photo.source + `" alt="Photo meuble" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	});
	return ret;
}


/********************  Backend fetch  **********************/

const getVisiblePhotos = async () => {
	let response = await fetch("/photos/homePage", {
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
	let response = await fetch("/furnitureTypes/", {
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