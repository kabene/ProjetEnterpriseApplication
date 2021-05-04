import logo from "../img/logoAE_v2.png";

let footer = document.querySelector("#footer");

const Footer = () => {
  footer.innerHTML = `   
    <div class="footer text-center justify-content-center mt-2 mb-2">
      <img id="logoFooter" src="` + logo + `" alt="logo" />
      <span>1bis sentier des artistes – 4800, Verviers, Belgique</span>
    </div>  
  `;
}

export default Footer;