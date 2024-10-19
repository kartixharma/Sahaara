import React from 'react'
import SignIn from "../components/google.png"
import { GoogleAuthProvider, signInWithPopup } from 'firebase/auth'
import { auth } from './firebase';
import { toast } from "react-toastify"

const SignInWithGoogle = () => {
  function GoogleLogin(){
    const provider = new GoogleAuthProvider();
    signInWithPopup(auth, provider).then(async (result)=>{
      console.log(result)
      if(result.user){
        toast.success("User login successful",{
          position: "top-center",
        });
        window.location.href="/"
      }
    });
  }
  return (
    <div>
        <div>
            <img src={SignIn} style={{cursor: "pointer"}} onClick={GoogleLogin}></img>
        </div>
    </div>
  )
}

export default SignInWithGoogle;