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

async function verifyAdmin (){
    let token = getUserSessionData().token;
    let res = false;
    let result;
    await fetch("/users/me", {
        method: "GET",
        headers: {
            "Authorization": token,
            "Content-Type": "application/json",
        },
    }).then((response) => {
        if (!response.ok) {
            res = false;
        }
        return response.json();
    })
    .then((data) => {
        result = data.role;
        if (result === "admin") {
            res = true;
        }
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
    div.className = "alert alert-danger mx-1";
    div.innerHTML = `<p>${message}</p>`;
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
    return findFurnitureImgSrcFromFilename(furniture.favouritePhoto.source, images);
}

/**
 * @returns html code for a loading animation
 */
const generateLoadingAnimation = () => {
    return `
        <div class="text-center">
            <h2>Loading <div class="spinner-border"></div></h2>
        </div>`
}

export {escapeHtml, removeTimeouts, generateLoadingAnimation, verifyAdmin,
     displayErrorMessage, importAllFurnitureImg, findFurnitureImgSrcFromFilename, 
     findFavImgSrc, generateLoadingAnimation};