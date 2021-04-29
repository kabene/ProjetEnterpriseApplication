let page = document.querySelector("#page");
const VisitRequest = async () => {
  let pageHTML = `<h1>Page des visites du site (admins/clients)</h1>`;
  page.innerHTML=pageHTML;
}

export default VisitRequest;