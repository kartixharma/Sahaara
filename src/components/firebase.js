import { initializeApp } from "firebase/app";
import {getAuth} from "firebase/auth";
import { getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyDwq-fGJ5ht2m27xStxq0Q-l-sI7d6Xj9s",
  authDomain: "login1-da839.firebaseapp.com",
  projectId: "login1-da839",
  storageBucket: "login1-da839.appspot.com",
  messagingSenderId: "672076516695",
  appId: "1:672076516695:web:dcdae6c443e623fd2bc212"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

export const auth = getAuth();
export const db = getFirestore(app);