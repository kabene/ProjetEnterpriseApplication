import notFoundPhoto from "../img/notFoundPhoto.png";
let page = document.querySelector("#page");
let visiblePhotos;

const HomePage = async () => {

	visiblePhotos = getVisiblePhotos();
	console.log(visiblePhotos);

	page.innerHTML = getPageHTML();
}

const getPageHTML = () => {
	return `
    <div class="row mx-0 pt-5">
        <div class="col-2"></div>
        <div class="col-8">` + getCarousel() + `</div>
        <div class="col-2"></div>
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
			<img class="d-block w-50 m-auto" src="` + notFoundPhoto + `" alt="Meuble 1">
		</div>`;
	}
	let ret = `
		<div class="carousel-item active">
			<img class="d-block w-50 m-auto" src="` + firstPhoto.src + `" alt="Meuble 1" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	visiblePhotoTmp.forEach(photo => {
		ret += `
		<div class="carousel-item">
			<img class="d-block w-50 m-auto" src="` + photo.src + `" alt="Photo meuble" onError="this.src='` + notFoundPhoto + `'">
		</div>`;
	});
	return ret;
}

const getVisiblePhotos = () => {
	let ret = [];
	fetch("/photos/homePage", {
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
		ret = data;
	}).catch((err) => {
		console.error(err);
	});

	return ret;
}



export default HomePage;