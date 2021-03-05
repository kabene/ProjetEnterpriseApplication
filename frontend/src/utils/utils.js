"use strict";

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

export {escapeHtml};