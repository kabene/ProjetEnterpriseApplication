const USER_STORE_NAME = "user";
const TAKEOVER_STORE_NAME = "takeover";

const findCurrentUser = () => {
  let res = getTakeoverSessionData();
  if(!res)
    res = getUserSessionData();
  return res;
}

//session storage

const getUserSessionData = () => {
  return getSessionStorageData(USER_STORE_NAME);
};


const setUserSessionData = (user) => {
  setSessionStorageData(user, USER_STORE_NAME);
};


const removeSessionData = () => {
  removeSessionStorageData(USER_STORE_NAME);
};


//takeover
const getTakeoverSessionData = () => {
  return getSessionStorageData(TAKEOVER_STORE_NAME);
};


const setTakeoverSessionData = (bundle) => {
  setSessionStorageData(bundle, TAKEOVER_STORE_NAME);
}


const removeTakeoverSessionData = () => {
  removeSessionStorageData(TAKEOVER_STORE_NAME);
}


//general (not exported)
const getSessionStorageData = (storeName) => {
  const retrievedBundle = sessionStorage.getItem(storeName);
  if (!retrievedBundle) return;
  return JSON.parse(retrievedBundle);
}


const setSessionStorageData = (bundle, storeName) => {
  const storageValue = JSON.stringify(bundle);
  sessionStorage.setItem(storeName, storageValue);
}


const removeSessionStorageData = (storeName) => {
  sessionStorage.removeItem(storeName);
}


//GDPR
const getGDPRLocalData=()=>{
  const flag=localStorage.getItem("gdpr");
  if (!flag) return ;
  return flag;
}
const setGDPRLocalData=(res)=>{
  const storageValue = JSON.stringify(res);
  localStorage.setItem("gdpr", storageValue);
}
const removeGDPRLocalData=()=>{
  localStorage.removeItem("gdpr");
}



const getUserLocalData = () => {
  const retrievedUser = localStorage.getItem(USER_STORE_NAME);
  if (!retrievedUser) return;
  return JSON.parse(retrievedUser);
};


const setUserLocalData = (token) => {
  const storageValue = JSON.stringify(token);
  localStorage.setItem(USER_STORE_NAME, storageValue);
};


const removeLocalData = () => {
  localStorage.removeItem(USER_STORE_NAME);
};



export { 
  findCurrentUser,
  getUserSessionData, setUserSessionData, removeSessionData,
  getUserLocalData,setUserLocalData,removeLocalData, 
  getTakeoverSessionData, setTakeoverSessionData, removeTakeoverSessionData,
  getGDPRLocalData,setGDPRLocalData,removeGDPRLocalData
};
