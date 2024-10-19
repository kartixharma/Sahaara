import React, { useEffect, useState } from "react";
import axios from "axios";
import HomeImage from "../home/background3.jpeg";
import { auth } from "../firebase"; 

const Home = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [description, setDescription] = useState("");

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user) => {
      if (user) {
        console.log("User is logged in:", user);
        setUserDetails(user);
      } else {
        console.log("User is not logged in");
        setUserDetails(null);
      }
    });

    return () => unsubscribe();
  }, []);

  async function handleLogout() {
    try {
      await auth.signOut();
      window.location.href = "/login";
      console.log("User logged out successfully!");
    } catch (error) {
      console.error("Error logging out:", error.message);
    }
  }

  return (
    <div
      className="min-h-screen flex flex-col justify-center lg:px-32 px-5 text-white opacity-90 bg-cover bg-no-repeat"
      style={{
        backgroundImage: `url(${HomeImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <div className="absolute top-5 right-5 flex space-x-4">
        {!userDetails ? (
          <a
            href="/login"
            className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-lg"
          >
            Login
          </a>
        ) : (
          <button
            onClick={handleLogout}
            className="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg"
          >
            Logout
          </button>
        )}
      </div>

      <div className="AppName">
        <span className="text-left mb-20 text-8xl italic font-bold text-shadow-lg text-[#ff6600]">Sa</span>
        <span className="text-left mb-20 text-8xl italic font-bold text-shadow-lg text-white">h</span>
        <span className="text-left mb-20 text-8xl italic font-bold text-shadow-lg text-[#1c1ca5]">a</span>
        <span className="text-left mb-20 text-8xl italic font-bold text-shadow-lg text-white">a</span>
        <span className="text-left mb-20 text-8xl italic font-bold text-shadow-lg text-[#046434]">ra</span>
      </div>
      <div className="lg:w-4/5 space-y-5 mt-10">
        <h1 className="text-5xl font-bold leading-tight">
          Empowering Best Choices for a Vibrant Life Your Trusted..
        </h1>
        <p>
          <b>Skin Injury Detection and Solution Model</b>: This model is designed to assist users in identifying and managing skin injuries. It can provide immediate solutions and recommendations for treatment, making it especially useful in emergency situations.
          <br /><br />
          <b>Environmental Detection Model</b>: This model focuses on assessing the quality of the user's environment, including air, water, and soil quality. It is aimed at promoting awareness of environmental conditions that could affect health and well-being.
          <br /><br />
          <b>Food Information Detection Model</b>: This model is designed to provide users with nutritional information and safety assessments regarding their food choices. It aims to promote healthier eating habits and ensure food safety.
        </p>
        <div className="flex justify-between mt-10">
          <a
            href="/connect"
            className="bg-gradient-to-r from-teal-500 via-green-400 to-teal-500 hover:bg-right text-white text-xl font-semibold py-4 px-8 rounded-lg shadow-lg transition-all duration-500 ease-in-out"
          >
            <button>Our Services</button>
          </a>
        </div>
      </div>
    </div>
  );
};

export default Home;
