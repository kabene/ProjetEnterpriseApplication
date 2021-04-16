import notFoundPhoto from "../img/notFoundPhoto.png";
import {displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, generateLoadingAnimation} from "../utils/utils.js";

let page = document.querySelector("#page");
let visiblePhotos;
let images = importAllFurnitureImg();

const HomePage = async () => {
	page.innerHTML = generateLoadingAnimation();
	visiblePhotos;
	await getVisiblePhotos();
	console.log(visiblePhotos);

	page.innerHTML = getPageHTML();
}

const getPageHTML = () => {
	return `
	<div class="col-5 mx-auto">
        <div id="errorDiv" class="d-none"></div>
    </div>
    <div class="row mx-0 pt-5">
        <div class="col-2 col-lg-4"></div>
        <div class="col-8 col-lg-4">` + getCarousel() + `</div>
        <div class="col-2 col-lg-4"></div>
    </div>
    `;
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
			<img class="d-block img-fluid mx-auto mb-5" src="` + findFurnitureImgSrcFromFilename(firstPhoto.source, images) + `" alt="Meuble 1" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	visiblePhotoTmp.forEach(photo => {
		ret += `
		<div class="carousel-item">
			<img class="d-block img-fluid mx-auto mb-5" src="` + findFurnitureImgSrcFromFilename(photo.source, images) + `" alt="Photo meuble" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	});
	return ret;
}

const getVisiblePhotos = async () => {
	await fetch("/photos/homePage", {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	}).then((response) => {
		if (!response.ok) {
			throw new Error("Error code : " + response.status + " : " + response.statusText);
		}
		return response.json();
	}).then((data) => {
		visiblePhotos = data;
	}).catch((err) => {
        console.log("Erreur de fetch !! :´<\n" + err);
        displayErrorMessage("errorDiv", err);
      });
}

export default HomePage;