import meuble1 from "../img/furnitures/Bureau_1.png";
import meuble2 from "../img/furnitures/Coiffeuse_2.png";
import meuble3 from "../img/furnitures/Secretaire.png";

let page = document.querySelector("#page");
let visiblePhotos;

const HomePage = async () => {
    
    visiblePhotos = await getVisiblePhotos();
    console.log(visiblePhotos);

    let pageHTML = `
    <div class="row mx-0 pt-5">
        <div class="col-2"></div>
        <div class="col-8">
            <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                <ol class="carousel-indicators bg-secondary">
                    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                </ol>
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img class="d-block w-50 m-auto" src="` + meuble1 + `" alt="Meuble 1">
                        <div class="carousel-caption text-dark pb-5 position-static">
                            <h5>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean eleifend enim eget dolor viverra, et dignissim lacus placerat. Praesent a est massa. Fusce volutpat nisl eu ligula tempus, nec maximus libero tempor. Quisque in purus ut arcu efficitur malesuada non id urna. In euismod diam quis est gravida posuere. Praesent.</h5>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img class="d-block w-50 m-auto" src="` + meuble2 + `" alt="Meuble 2">
                        <div class="carousel-caption text-dark pb-5 position-static">
                            <h5>Meuble 2 description</h5>
                        </div>
                    </div>
                    <div class="carousel-item">
                    <h4><span class="badge badge-light badgeSold">Vendu!</span></h4>
                        <img class="d-block w-50 m-auto" src="` + meuble3 + `" alt="Meuble 3">
                        <div class="carousel-caption text-dark pb-5 position-static">
                            <h5>Meuble 3 description</h5>
                        </div>
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
        <div class="col-2"></div>
    </div>
    `;
    page.innerHTML = pageHTML;
}

const getVisiblePhotos = async () => {
    let ret = [];
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
        ret = data;
      }).catch((err) => {
        console.error(err);
      });

    return ret;
}



export default HomePage;