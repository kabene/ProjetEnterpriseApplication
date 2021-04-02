"use strict";

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

export {escapeHtml, removeTimeouts, generateLoadingAnimation, verifyAdmin, displayErrorMessage};