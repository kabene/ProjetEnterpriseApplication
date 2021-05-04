import removeTakeoverSessionData from "../utils/session.js";
import RedirectUrl from "./Router.js";
import setLayout from "../utils/render.js"

const ReleaseTakeoverComponent = () => {
    removeTakeoverSessionData();
    setLayout();
    RedirectUrl("/");
}

export default ReleaseTakeoverComponent;