/**
 * Generates a modal (trigger button NOT included)
 * composed of specified html codes for the header, body and footer as well as a close 
 * button located in the modal's footer (after the specified footer html).
 * 
 * @param {string} id : html id of the modal element
 * @param {string} htmlHeader : Html code representing the modal's header
 * @param {string} htmlBody : Html code representing the modal's body
 * @param {string} htmlFooter : Html code representing the modal's footer
 * @param {string} closeBtnLabel : label of the close button located inside the modal
 * @param {string} closeBtnClassname : class name of the close button
 * @returns {string} The generated html code
 */
const generateModal = (id, htmlHeader, htmlBody, htmlFooter, closeBtnLabel, closeBtnClassname) => {
    let closeBtn = "";
    if(closeBtnLabel) {
        closeBtn = generateCloseBtn(closeBtnLabel, id, closeBtnClassname);
    }
    let res = `
        <div class="modal fade" id="${id}">
        <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                ${htmlHeader}
            </div>
            
            <div class="modal-body">
                ${htmlBody}
            </div>
            
            <div class="modal-footer">
                ${htmlFooter}
                ${closeBtn}
            </div>
            </div>
        </div>
    </div>
    `;
    return res;
}

/**
 * Generates a trigger button which opens a specific modal.
 * 
 * @param {string} modalId : The html id of the modal that should open on click 
 * @param {string} label : The label displayed on the generated button
 * @param {string} classname : The class name of the generated button
 * @returns {string} : The generated html code
 */
const generateTriggerBtn = (modalId, label, classname) => {
    let res = `
    <button type="button" class="mx-1 py-2 ${classname}" data-toggle="modal" data-target="#${modalId}">
        ${label}
    </button>
    `;
    return res;
}

/**
 * Generates a button that closes the modal it is imbedded into
 * (Place the result inside a modal <div>).
 * 
 * @param {string} label The label displayed on the generated button
 * @param {string} buttonId : The html id of the generated button (useful for addEventListener to add custom handler)
 * @param {string} classname : the class name of the generated button
 * @returns {string} : the generated html code
 */
const generateCloseBtn = (label, buttonId, classname) => {
    return `<button id="${buttonId}" type="button" class="${classname}" data-dismiss="modal">${label}</button>`;
}

/**
 * Generates a trigger button AND its modal at the same time
 * (the generated modal contains also a close button).
 * 
 * @param {string} modalId : The html id of the generated modal <div>
 * @param {string} triggerBtnLabel : The label displayed on the modal's trigger button
 * @param {string} triggerBtnClassname : The class name of the generated trigger button
 * @param {string} modalHtmlHeader : Html code representing the modal's header
 * @param {string} modalHtmlBody : Html code representing the modal's body
 * @param {string} modalHtmlFooter : Html code representing the modal's footer
 * @param {string} closeBtnLabel  : The label displayed on the modal's close button
 * @param {string} closeBtnClassname : The class name of the modal's close button
 * @returns {string} : The generated html in the following order: triggerButton -> modal 
 */
const generateModalPlusTriggerBtn = (modalId, triggerBtnLabel, triggerBtnClassname, modalHtmlHeader, modalHtmlBody, modalHtmlFooter, closeBtnLabel, closeBtnClassname) => {
    let res = `
    ${generateTriggerBtn(modalId, triggerBtnLabel, triggerBtnClassname)}
    ${generateModal(modalId, modalHtmlHeader, modalHtmlBody, modalHtmlFooter, closeBtnLabel, closeBtnClassname)}`;
    return res;
}

export { generateModal, generateTriggerBtn, generateCloseBtn, generateModalPlusTriggerBtn }