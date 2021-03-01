/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/index.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/Components/Footer.js":
/*!**********************************!*\
  !*** ./src/Components/Footer.js ***!
  \**********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\nvar Footer = () => {};\n\n/* harmony default export */ __webpack_exports__[\"default\"] = (Footer);//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiLi9zcmMvQ29tcG9uZW50cy9Gb290ZXIuanMuanMiLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vLi9zcmMvQ29tcG9uZW50cy9Gb290ZXIuanM/MzQ1MCJdLCJzb3VyY2VzQ29udGVudCI6WyJjb25zdCBGb290ZXIgPSAoKSA9PiB7XHJcblxyXG59XHJcblxyXG5leHBvcnQgZGVmYXVsdCBGb290ZXI7Il0sIm1hcHBpbmdzIjoiQUFBQTtBQUFBO0FBQ0E7QUFHQSIsInNvdXJjZVJvb3QiOiIifQ==\n//# sourceURL=webpack-internal:///./src/Components/Footer.js\n");

/***/ }),

/***/ "./src/Components/Navbar.js":
/*!**********************************!*\
  !*** ./src/Components/Navbar.js ***!
  \**********************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\nvar Navbar = () => {};\n\n/* harmony default export */ __webpack_exports__[\"default\"] = (Navbar);//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiLi9zcmMvQ29tcG9uZW50cy9OYXZiYXIuanMuanMiLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vLi9zcmMvQ29tcG9uZW50cy9OYXZiYXIuanM/NGJhOCJdLCJzb3VyY2VzQ29udGVudCI6WyJjb25zdCBOYXZiYXIgPSAoKSA9PiB7XHJcblxyXG59XHJcblxyXG5leHBvcnQgZGVmYXVsdCBOYXZiYXI7Il0sIm1hcHBpbmdzIjoiQUFBQTtBQUFBO0FBQ0E7QUFHQSIsInNvdXJjZVJvb3QiOiIifQ==\n//# sourceURL=webpack-internal:///./src/Components/Navbar.js\n");

/***/ }),

/***/ "./src/index.js":
/*!**********************!*\
  !*** ./src/index.js ***!
  \**********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _utils_render_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./utils/render.js */ \"./src/utils/render.js\");\n\nObject(_utils_render_js__WEBPACK_IMPORTED_MODULE_0__[\"setLayout\"])();\n/*import logo from \"./img/lampe_magique.png\";\r\n\r\nconst TIMER_START_LAMPE = 21;\r\n\r\nlet aladdinButton = document.querySelector(\"#aladdin_button\");\r\nlet genieButton = document.querySelector(\"#genie_button\");\r\nlet messageDiv = document.querySelector(\"#messageDiv\");\r\n\r\nlet intervals = [];\r\nlet timeouts = [];\r\n\r\nconst onAladdinClick = () => {\r\n    clearIntervalsTimeouts();\r\n    let timer = TIMER_START_LAMPE;\r\n    messageDiv.innerHTML = \"<p>Frotte la lampe magique et un voeu te sera accordé: </br>Pour frotter la lampe, bouge avec la souris sur l'image</p>\"\r\n                         + \"<img id='lampe' alt='lampe' src='\" + logo + \"'/><span id='timer_lampe'> \"+ timer +\"</span><br/>\"\r\n                         + \"<span id='voeu_display'><input type='submit' name='voeu_submit' id='voeu_submit' value='Faire apparaître mon voeu'/></span>\";\r\n    document.querySelector(\"#lampe\").addEventListener('mouseover', onMouseOnLampe);\r\n    document.querySelector(\"#voeu_display\").addEventListener('click', onVoeuDisplay);\r\n}\r\n\r\nconst onGenieClick = () => {\r\n    clearIntervalsTimeouts();\r\n    messageDiv.innerHTML = \"<input type='text' name='voeu' id='voeu'/> <input type='submit' name='voeu_submit' id='voeu_submit' value='Envoyer'/>\";\r\n    document.querySelector(\"#voeu_submit\").addEventListener(\"click\", onVoeuSubmit);\r\n}\r\n\r\nconst onMouseOnLampe = () => {\r\n    console.log(\"Timer On\");\r\n    let timerSpan = document.querySelector(\"#timer_lampe\"); \r\n    intervals.push(setInterval(function(){timerSpan.innerText=timerSpan.textContent-1;}, 1000));\r\n    timeouts.push(setTimeout(function() {onVoeuDisplay();}, timerSpan.textContent*1000+1000)); //arreter l'interval à -1\r\n    document.querySelector(\"#lampe\").addEventListener('mouseout', onMouseOutLampe);\r\n}\r\n\r\nconst onMouseOutLampe = () => {\r\n    clearIntervalsTimeouts();\r\n    console.log(\"Timer Off\");\r\n}\r\n\r\nconst onVoeuDisplay = async () => {\r\n    document.querySelector(\"#lampe\").removeEventListener('mouseover', onMouseOnLampe);\r\n    document.querySelector(\"#lampe\").removeEventListener('mouseout', onMouseOutLampe);\r\n    document.querySelector(\"#voeu_display\").removeEventListener('click', onVoeuDisplay);\r\n    clearIntervalsTimeouts();\r\n    let timer = document.querySelector(\"#timer_lampe\").textContent;\r\n    if (timer > TIMER_START_LAMPE-1) {\r\n        document.querySelector(\"#voeu_display\").innerText = \"Tu n'as pas frotté assez longtemps la lampe !\";\r\n    } else if (timer.textContent < 0) {\r\n        document.querySelector(\"#voeu_display\").innerText = \"Tu as frotté trop longtemps la lampe !\";\r\n    } else {\r\n        await fetch(\"/api/voeus/\"+timer).then(response => {\r\n            if(!response.ok){\r\n                throw new Error(response.status + \" \" + response.statusText);\r\n            }\r\n            console.log(response)\r\n            return response.json();\r\n        }).then( response => {\r\n            document.querySelector(\"#voeu_display\").innerText = response;\r\n        });\r\n    }\r\n}\r\n\r\nconst onVoeuSubmit = async () => {\r\n    let voeu = document.querySelector(\"#voeu\").value;\r\n    await fetch(\"/api/voeus/\", {\r\n        method: \"POST\",\r\n        body : JSON.stringify({voeu: voeu}),\r\n        headers: {\r\n            \"Content-Type\": \"application/json\",\r\n        },\r\n    })\r\n    .then((response) => {\r\n        if (!response.ok)\r\n            throw new Error(\"Error code : \" + response.status + \" : \" + response.statusText);\r\n        return response.json();\r\n    })\r\n    .then((data) => {\r\n        console.log(data);\r\n    })\r\n    .catch((err) => console.log(err.message));\r\n    messageDiv.innerHTML = \"\";\r\n}\r\n\r\nconst clearIntervalsTimeouts = () => {\r\n    timeouts.forEach(timeout => {\r\n        clearTimeout(timeout);\r\n    });\r\n\r\n    intervals.forEach(interval => {\r\n        clearInterval(interval); \r\n    });\r\n}\r\n\r\naladdinButton.addEventListener(\"click\", onAladdinClick);\r\ngenieButton.addEventListener(\"click\", onGenieClick);*///# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiLi9zcmMvaW5kZXguanMuanMiLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vLi9zcmMvaW5kZXguanM/YjYzNSJdLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQge3NldExheW91dH0gZnJvbSBcIi4vdXRpbHMvcmVuZGVyLmpzXCI7XHJcblxyXG5zZXRMYXlvdXQoKTtcclxuXHJcbi8qaW1wb3J0IGxvZ28gZnJvbSBcIi4vaW1nL2xhbXBlX21hZ2lxdWUucG5nXCI7XHJcblxyXG5jb25zdCBUSU1FUl9TVEFSVF9MQU1QRSA9IDIxO1xyXG5cclxubGV0IGFsYWRkaW5CdXR0b24gPSBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI2FsYWRkaW5fYnV0dG9uXCIpO1xyXG5sZXQgZ2VuaWVCdXR0b24gPSBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI2dlbmllX2J1dHRvblwiKTtcclxubGV0IG1lc3NhZ2VEaXYgPSBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI21lc3NhZ2VEaXZcIik7XHJcblxyXG5sZXQgaW50ZXJ2YWxzID0gW107XHJcbmxldCB0aW1lb3V0cyA9IFtdO1xyXG5cclxuY29uc3Qgb25BbGFkZGluQ2xpY2sgPSAoKSA9PiB7XHJcbiAgICBjbGVhckludGVydmFsc1RpbWVvdXRzKCk7XHJcbiAgICBsZXQgdGltZXIgPSBUSU1FUl9TVEFSVF9MQU1QRTtcclxuICAgIG1lc3NhZ2VEaXYuaW5uZXJIVE1MID0gXCI8cD5Gcm90dGUgbGEgbGFtcGUgbWFnaXF1ZSBldCB1biB2b2V1IHRlIHNlcmEgYWNjb3Jkw6k6IDwvYnI+UG91ciBmcm90dGVyIGxhIGxhbXBlLCBib3VnZSBhdmVjIGxhIHNvdXJpcyBzdXIgbCdpbWFnZTwvcD5cIlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgKyBcIjxpbWcgaWQ9J2xhbXBlJyBhbHQ9J2xhbXBlJyBzcmM9J1wiICsgbG9nbyArIFwiJy8+PHNwYW4gaWQ9J3RpbWVyX2xhbXBlJz4gXCIrIHRpbWVyICtcIjwvc3Bhbj48YnIvPlwiXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICArIFwiPHNwYW4gaWQ9J3ZvZXVfZGlzcGxheSc+PGlucHV0IHR5cGU9J3N1Ym1pdCcgbmFtZT0ndm9ldV9zdWJtaXQnIGlkPSd2b2V1X3N1Ym1pdCcgdmFsdWU9J0ZhaXJlIGFwcGFyYcOudHJlIG1vbiB2b2V1Jy8+PC9zcGFuPlwiO1xyXG4gICAgZG9jdW1lbnQucXVlcnlTZWxlY3RvcihcIiNsYW1wZVwiKS5hZGRFdmVudExpc3RlbmVyKCdtb3VzZW92ZXInLCBvbk1vdXNlT25MYW1wZSk7XHJcbiAgICBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI3ZvZXVfZGlzcGxheVwiKS5hZGRFdmVudExpc3RlbmVyKCdjbGljaycsIG9uVm9ldURpc3BsYXkpO1xyXG59XHJcblxyXG5jb25zdCBvbkdlbmllQ2xpY2sgPSAoKSA9PiB7XHJcbiAgICBjbGVhckludGVydmFsc1RpbWVvdXRzKCk7XHJcbiAgICBtZXNzYWdlRGl2LmlubmVySFRNTCA9IFwiPGlucHV0IHR5cGU9J3RleHQnIG5hbWU9J3ZvZXUnIGlkPSd2b2V1Jy8+IDxpbnB1dCB0eXBlPSdzdWJtaXQnIG5hbWU9J3ZvZXVfc3VibWl0JyBpZD0ndm9ldV9zdWJtaXQnIHZhbHVlPSdFbnZveWVyJy8+XCI7XHJcbiAgICBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI3ZvZXVfc3VibWl0XCIpLmFkZEV2ZW50TGlzdGVuZXIoXCJjbGlja1wiLCBvblZvZXVTdWJtaXQpO1xyXG59XHJcblxyXG5jb25zdCBvbk1vdXNlT25MYW1wZSA9ICgpID0+IHtcclxuICAgIGNvbnNvbGUubG9nKFwiVGltZXIgT25cIik7XHJcbiAgICBsZXQgdGltZXJTcGFuID0gZG9jdW1lbnQucXVlcnlTZWxlY3RvcihcIiN0aW1lcl9sYW1wZVwiKTsgXHJcbiAgICBpbnRlcnZhbHMucHVzaChzZXRJbnRlcnZhbChmdW5jdGlvbigpe3RpbWVyU3Bhbi5pbm5lclRleHQ9dGltZXJTcGFuLnRleHRDb250ZW50LTE7fSwgMTAwMCkpO1xyXG4gICAgdGltZW91dHMucHVzaChzZXRUaW1lb3V0KGZ1bmN0aW9uKCkge29uVm9ldURpc3BsYXkoKTt9LCB0aW1lclNwYW4udGV4dENvbnRlbnQqMTAwMCsxMDAwKSk7IC8vYXJyZXRlciBsJ2ludGVydmFsIMOgIC0xXHJcbiAgICBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI2xhbXBlXCIpLmFkZEV2ZW50TGlzdGVuZXIoJ21vdXNlb3V0Jywgb25Nb3VzZU91dExhbXBlKTtcclxufVxyXG5cclxuY29uc3Qgb25Nb3VzZU91dExhbXBlID0gKCkgPT4ge1xyXG4gICAgY2xlYXJJbnRlcnZhbHNUaW1lb3V0cygpO1xyXG4gICAgY29uc29sZS5sb2coXCJUaW1lciBPZmZcIik7XHJcbn1cclxuXHJcbmNvbnN0IG9uVm9ldURpc3BsYXkgPSBhc3luYyAoKSA9PiB7XHJcbiAgICBkb2N1bWVudC5xdWVyeVNlbGVjdG9yKFwiI2xhbXBlXCIpLnJlbW92ZUV2ZW50TGlzdGVuZXIoJ21vdXNlb3ZlcicsIG9uTW91c2VPbkxhbXBlKTtcclxuICAgIGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIjbGFtcGVcIikucmVtb3ZlRXZlbnRMaXN0ZW5lcignbW91c2VvdXQnLCBvbk1vdXNlT3V0TGFtcGUpO1xyXG4gICAgZG9jdW1lbnQucXVlcnlTZWxlY3RvcihcIiN2b2V1X2Rpc3BsYXlcIikucmVtb3ZlRXZlbnRMaXN0ZW5lcignY2xpY2snLCBvblZvZXVEaXNwbGF5KTtcclxuICAgIGNsZWFySW50ZXJ2YWxzVGltZW91dHMoKTtcclxuICAgIGxldCB0aW1lciA9IGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIjdGltZXJfbGFtcGVcIikudGV4dENvbnRlbnQ7XHJcbiAgICBpZiAodGltZXIgPiBUSU1FUl9TVEFSVF9MQU1QRS0xKSB7XHJcbiAgICAgICAgZG9jdW1lbnQucXVlcnlTZWxlY3RvcihcIiN2b2V1X2Rpc3BsYXlcIikuaW5uZXJUZXh0ID0gXCJUdSBuJ2FzIHBhcyBmcm90dMOpIGFzc2V6IGxvbmd0ZW1wcyBsYSBsYW1wZSAhXCI7XHJcbiAgICB9IGVsc2UgaWYgKHRpbWVyLnRleHRDb250ZW50IDwgMCkge1xyXG4gICAgICAgIGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIjdm9ldV9kaXNwbGF5XCIpLmlubmVyVGV4dCA9IFwiVHUgYXMgZnJvdHTDqSB0cm9wIGxvbmd0ZW1wcyBsYSBsYW1wZSAhXCI7XHJcbiAgICB9IGVsc2Uge1xyXG4gICAgICAgIGF3YWl0IGZldGNoKFwiL2FwaS92b2V1cy9cIit0aW1lcikudGhlbihyZXNwb25zZSA9PiB7XHJcbiAgICAgICAgICAgIGlmKCFyZXNwb25zZS5vayl7XHJcbiAgICAgICAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IocmVzcG9uc2Uuc3RhdHVzICsgXCIgXCIgKyByZXNwb25zZS5zdGF0dXNUZXh0KTtcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICBjb25zb2xlLmxvZyhyZXNwb25zZSlcclxuICAgICAgICAgICAgcmV0dXJuIHJlc3BvbnNlLmpzb24oKTtcclxuICAgICAgICB9KS50aGVuKCByZXNwb25zZSA9PiB7XHJcbiAgICAgICAgICAgIGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIjdm9ldV9kaXNwbGF5XCIpLmlubmVyVGV4dCA9IHJlc3BvbnNlO1xyXG4gICAgICAgIH0pO1xyXG4gICAgfVxyXG59XHJcblxyXG5jb25zdCBvblZvZXVTdWJtaXQgPSBhc3luYyAoKSA9PiB7XHJcbiAgICBsZXQgdm9ldSA9IGRvY3VtZW50LnF1ZXJ5U2VsZWN0b3IoXCIjdm9ldVwiKS52YWx1ZTtcclxuICAgIGF3YWl0IGZldGNoKFwiL2FwaS92b2V1cy9cIiwge1xyXG4gICAgICAgIG1ldGhvZDogXCJQT1NUXCIsXHJcbiAgICAgICAgYm9keSA6IEpTT04uc3RyaW5naWZ5KHt2b2V1OiB2b2V1fSksXHJcbiAgICAgICAgaGVhZGVyczoge1xyXG4gICAgICAgICAgICBcIkNvbnRlbnQtVHlwZVwiOiBcImFwcGxpY2F0aW9uL2pzb25cIixcclxuICAgICAgICB9LFxyXG4gICAgfSlcclxuICAgIC50aGVuKChyZXNwb25zZSkgPT4ge1xyXG4gICAgICAgIGlmICghcmVzcG9uc2Uub2spXHJcbiAgICAgICAgICAgIHRocm93IG5ldyBFcnJvcihcIkVycm9yIGNvZGUgOiBcIiArIHJlc3BvbnNlLnN0YXR1cyArIFwiIDogXCIgKyByZXNwb25zZS5zdGF0dXNUZXh0KTtcclxuICAgICAgICByZXR1cm4gcmVzcG9uc2UuanNvbigpO1xyXG4gICAgfSlcclxuICAgIC50aGVuKChkYXRhKSA9PiB7XHJcbiAgICAgICAgY29uc29sZS5sb2coZGF0YSk7XHJcbiAgICB9KVxyXG4gICAgLmNhdGNoKChlcnIpID0+IGNvbnNvbGUubG9nKGVyci5tZXNzYWdlKSk7XHJcbiAgICBtZXNzYWdlRGl2LmlubmVySFRNTCA9IFwiXCI7XHJcbn1cclxuXHJcbmNvbnN0IGNsZWFySW50ZXJ2YWxzVGltZW91dHMgPSAoKSA9PiB7XHJcbiAgICB0aW1lb3V0cy5mb3JFYWNoKHRpbWVvdXQgPT4ge1xyXG4gICAgICAgIGNsZWFyVGltZW91dCh0aW1lb3V0KTtcclxuICAgIH0pO1xyXG5cclxuICAgIGludGVydmFscy5mb3JFYWNoKGludGVydmFsID0+IHtcclxuICAgICAgICBjbGVhckludGVydmFsKGludGVydmFsKTsgXHJcbiAgICB9KTtcclxufVxyXG5cclxuYWxhZGRpbkJ1dHRvbi5hZGRFdmVudExpc3RlbmVyKFwiY2xpY2tcIiwgb25BbGFkZGluQ2xpY2spO1xyXG5nZW5pZUJ1dHRvbi5hZGRFdmVudExpc3RlbmVyKFwiY2xpY2tcIiwgb25HZW5pZUNsaWNrKTsqLyJdLCJtYXBwaW5ncyI6IkFBQUE7QUFBQTtBQUFBO0FBRUE7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EiLCJzb3VyY2VSb290IjoiIn0=\n//# sourceURL=webpack-internal:///./src/index.js\n");

/***/ }),

/***/ "./src/utils/render.js":
/*!*****************************!*\
  !*** ./src/utils/render.js ***!
  \*****************************/
/*! exports provided: setLayout */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"setLayout\", function() { return setLayout; });\n/* harmony import */ var _Components_Footer_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../Components/Footer.js */ \"./src/Components/Footer.js\");\n/* harmony import */ var _Components_Navbar_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../Components/Navbar.js */ \"./src/Components/Navbar.js\");\n\"strict mode\";\n\n\n\n\nfunction setLayout() {\n  Object(_Components_Navbar_js__WEBPACK_IMPORTED_MODULE_1__[\"default\"])();\n  Object(_Components_Footer_js__WEBPACK_IMPORTED_MODULE_0__[\"default\"])();\n}\n\n//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiLi9zcmMvdXRpbHMvcmVuZGVyLmpzLmpzIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vLy4vc3JjL3V0aWxzL3JlbmRlci5qcz8yYWVmIl0sInNvdXJjZXNDb250ZW50IjpbIlwic3RyaWN0IG1vZGVcIjtcclxuXHJcbmltcG9ydCBGb290ZXIgZnJvbSBcIi4uL0NvbXBvbmVudHMvRm9vdGVyLmpzXCI7XHJcbmltcG9ydCBOYXZCYXIgZnJvbSBcIi4uL0NvbXBvbmVudHMvTmF2YmFyLmpzXCI7XHJcblxyXG5mdW5jdGlvbiBzZXRMYXlvdXQoKSB7XHJcbiAgICBOYXZCYXIoKTtcclxuICAgIEZvb3RlcigpO1xyXG59XHJcblxyXG5leHBvcnQge3NldExheW91dH07Il0sIm1hcHBpbmdzIjoiQUFBQTtBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOyIsInNvdXJjZVJvb3QiOiIifQ==\n//# sourceURL=webpack-internal:///./src/utils/render.js\n");

/***/ })

/******/ });