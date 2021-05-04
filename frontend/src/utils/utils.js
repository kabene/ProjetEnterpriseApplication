"use strict";
import imageNotFound from "../img/notFoundPhoto.png";
import {getUserSessionData} from "./session";

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
async function fetchMe (token){
    if(!token) token = getUserSessionData().token;
    let res;
    await fetch("/users/me", {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (response.ok) {
            return response.json();   
        }
    })
    .then((data) => {
        res = data;
    })
    .catch((err) => {
        console.log("Erreur de fetch !! :´\n" + err);
    });
    return res;
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
    r.keys().map((item, index) => { images[item.replace('./', '')] = r(item); });
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
    if(!images[filename]) {
      return imageNotFound;
    }
    return images[filename].default;
}

/**
 * finds favourite image src from loaded images array and Furniture object
 * @param {*} furniture
 * @param {Array} images
 * @returns <img/> src
 */
const findFavImgSrc = (furniture, images) => {
    if(!furniture.favouritePhoto) {
      return imageNotFound;
    }
    return furniture.favouritePhoto.source;
}

/**
 * Finds all <img/> tags with the matching photo-id attribute and updates their src
 * 
 * @param {Array<photo>} photosLst 
 */
export const displayImgs = (photosLst) => {
    photosLst.forEach((photo) => {
        document.querySelectorAll(`img[photo-id='${photo.photoId}']`)
        .forEach((img) => {
            img.src = photo.source;
        });
    });
}

export {escapeHtml, removeTimeouts, generateLoadingAnimation, fetchMe,
     displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, 
     findFavImgSrc};


