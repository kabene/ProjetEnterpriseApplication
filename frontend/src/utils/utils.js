"use strict";
import imageNotFound from "../img/notFoundPhoto.png";
import {getUserSessionData, getGDPRLocalData, setGDPRLocalData} from "./session";
import {abortControllers} from "../Components/Router.js"


const baseUrl = "/api";

/**
 * Escape the dangerous characters than can lead to an XSS attack or an SQL injection
 * @author Kip from https://stackoverflow.com/questions/1787322/htmlspecialchars-equivalent-in-javascript
 *  - replace(/\//g, "&#047;") added by Agrò Lucas
 * @param {*} text  the text to escape
 */
function escapeHtml(text) {
  return text
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;")
    .replace(/\//g, "&#047;");
}

/**
 * Finds current user's personal information via GET users/me
 * @param {string} token : jwt
 *
 */
async function fetchMe (token) {
  if(!token) 
		token = getUserSessionData().token;
  let response = await fetch(baseUrl+"/users/me", {
    method: "GET",
    headers: {
      "Authorization": token,
      "Content-Type": "application/json",
    },
  });
  if (!response.ok) {
    const err = "Erreur de fetch !! :´<\nError code : " + response.status + " : " + response.statusText;
    console.error(err);
  }
	return response.json();
}

const removeTimeouts = (timeouts) => {
  timeouts.forEach(timeout => clearTimeout(timeout))
}

const generateLoadingAnimation = () => {
  return `
    <div class="text-center">
      <h2>Loading <div class="spinner-border"></div></h2>
    </div>`
}

function displayErrorMessage(alertDivId, error) {
  if (typeof error === DOMException || typeof error === DOMError) return;
  let message = error.message;
  let div = document.querySelector(`#${alertDivId}`);
  div.className = "mx-1";
  div.innerHTML = `
    <div class="alert alert-danger alert-dismissible fade show">
      <button type="button" class="close" data-dismiss="alert">&times;</button>
      <p>${message}</p>
    </div>`;
}

/**
 * Loads all images (.png / .jpg / jpeg / .svg) from the src/img/furniture folder
 *
 * @author Webpack documentation: https://webpack.js.org/guides/dependency-management/#require-context
 *
 * key = filename
 * use the entries' default attribute in <img/> tags
 *
 * @returns an array containing all images
 */
const importAllFurnitureImg = () => {
  let r = require.context('../img/furniture', true, /\.(png|jpe?g|svg)$/);
  let images = {};
  r.keys().map((item, index) => {
    images[item.replace('./', '')] = r(item);
  });
  return images;
}

/**
 * finds furniture image src from loaded images array and filename ( -> return value from importAllFurnitureImg() )
 *
 * @param {string} filename
 * @param {Array} images
 * @returns <img/> src
 */
const findFurnitureImgSrcFromFilename = (filename, images) => {
  if (!images[filename])
    return imageNotFound;
  return images[filename].default;
}

/**
 * show the GDPR span if the user haven't filled it before.
 * In the other case save the data into local storage
 * @returns {string} html
 */
const getGDPR = () => {
  if (!getGDPRLocalData()) {
    return `
    <div id="gdpr-cookie-message" style="display: block;">
      <h4>Cookies &amp; Vie privée</h4>
      <p> Ce site web utilise des cookies et des technologies similaires. Pour consulter la politique européenne en matière de cookies, cliquez sur plus d'informations.</p>
      <div>
        <a href="https://ec.europa.eu/info/cookies_fr" target="_blank">Plus d'informations</a>
        <button id="gdpr-cookie-accept" type="button">Accepter</button>
      </div>
    </div>`;
  }
}

/**
 * listen the gdpr
 */
const acceptCookies = () => {
 if(getGDPRLocalData() === "accepted")
    document.querySelector("#gdpr-cookie-message").style.display="none";
  else {
    let button = document.querySelector("#gdpr-cookie-accept");
    if (button) {
      button.addEventListener('click',
          () => {
            setGDPRLocalData("accepted");
            document.querySelector("#gdpr-cookie-message").style.display = "none";
          }
        )
    }
  }
}

const gdpr = (page) => {
  if(!getGDPRLocalData())
    page.innerHTML += getGDPR();
  acceptCookies();
}


/**
 * Finds all <img/> tags with the matching photo-id attribute and updates their src
 *
 * @param {Array<photo>} photosLst
 */
export const displayImgs = (photosLst) => {
    photosLst.forEach((photo) => {
        document.querySelectorAll(`img[photo-id='${photo.photoId}']`)
        .forEach((img) => img.src = photo.source);
    });
}

/**
 * create a controller and push it in the stack of controllers and return a signal.
 * @returns a signal.
 */
const getSignal = () => {
  let controller = new AbortController();
  abortControllers.push(controller);
  return controller.signal;
}


export {escapeHtml, removeTimeouts, generateLoadingAnimation, fetchMe,
        displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, 
        acceptCookies, getGDPR, gdpr, baseUrl, getSignal};


