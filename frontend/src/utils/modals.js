
const generateModal = (id, htmlHeader, htmlBody, htmlFooter, closeBtnLabel, classname) => {
    let closeBtn = "";
    if(closeBtnLabel) {
        closeBtn = generateCloseBtn(closeBtnLabel, id, classname);
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

const generateTriggerBtn = (modalId, label, classname) => {
    let res = `
    <button type="button" class="mx-1 ${classname}" data-toggle="modal" data-target="#${modalId}">
        ${label}
    </button>
    `;
    return res;
}

const generateCloseBtn = (label, id, classname) => {
    return `<button id="${id}" type="button" class="${classname}" data-dismiss="modal">${label}</button>`;
}

const generateModalPlusTriggerBtn = (modalId, triggerBtnLabel, triggerBtnClassname, modalHtmlHeader, modalHtmlBody, modalHtmlFooter, closeBtnLabel, closeBtnClassname) => {
    let res = `
    ${generateTriggerBtn(modalId, triggerBtnLabel, triggerBtnClassname)}
    ${generateModal(modalId, modalHtmlHeader, modalHtmlBody, modalHtmlFooter, closeBtnLabel, closeBtnClassname)}`;
    return res;
}

export { generateModal, generateTriggerBtn, generateCloseBtn, generateModalPlusTriggerBtn }