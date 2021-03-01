import logo from "../img/logoAE_v2.png";

let footer = document.querySelector("#footer");

const Footer = () => {
    let footerHtml = `   
        <div class="footer text-center justify-content-center mt-2 mb-2">
            <img id="logo-footer" src="` + logo + `" alt="logo" />
            <span>1bis sente des artistes – Verviers, num, codeCommune</span>
        </div>  
    `;
    footer.innerHTML = footerHtml;
}

export default Footer;