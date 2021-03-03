let page = document.querySelector("#page");

const LogoutComponent = () => {
    let pageHTML = `<h1>Lien pour se deconnecter (clients/admins)</h1>`;
    page.innerHTML = pageHTML;
}

export default LogoutComponent;