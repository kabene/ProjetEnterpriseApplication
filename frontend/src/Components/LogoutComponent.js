import {removeSessionData, removeLocalData} from "../utils/session.js";
import RedirectUrl from "./Router.js";
import setLayout from "../utils/render.js"

const LogoutComponent = () => {
    removeSessionData();
    removeLocalData();
    setLayout();
    RedirectUrl("/");
}

export default LogoutComponent;