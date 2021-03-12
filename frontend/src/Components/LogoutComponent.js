import {removeSessionData, removeLocalData} from "../utils/session.js";
import {RedirectUrl} from "./Router.js";
import {setLayout} from "../utils/render.js"

let page = document.querySelector("#page");

const LogoutComponent = () => {
    removeSessionData();
    removeLocalData();
    setLayout();
    RedirectUrl("/");
}

export default LogoutComponent;