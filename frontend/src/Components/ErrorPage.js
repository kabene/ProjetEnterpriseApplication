let page = document.querySelector("#page");

const ErrorPage = (url) => {
  page.innerHTML = `
		<div class="row mx-0 mt-3">
			<div class="col-3"></div>
			<div class="col-6 alert alert-danger">La page `+ url +` n'existe pas :'( </div>
		</div>
	`;
};

export default ErrorPage;