const STORE_NAME = "user";
const getUserSessionData = () => {
  const retrievedUser = localStorage.getItem(STORE_NAME);
  if (!retrievedUser) return;
  return JSON.parse(retrievedUser);
};

const setUserSessionData = (user) => {
  const storageValue = JSON.stringify(user);
  localStorage.setItem(STORE_NAME, storageValue);
};

const removeSessionData = () => {
  localStorage.removeItem(STORE_NAME);
};

export { getUserSessionData, setUserSessionData, removeSessionData };
