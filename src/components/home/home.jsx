import React, { useEffect, useState } from "react";
import axios from "axios";
import HomeImage from "../home/background3.jpeg";
import { auth } from "../firebase"; // Ensure you import the necessary Firebase methods

const Home = () => {
  const [userDetails, setUserDetails] = useState(null);
  const [description, setDescription] = useState("");

  // Function to fetch user data and set user details
  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user) => {
      if (user) {
        console.log("User is logged in:", user);
        setUserDetails(user); // Set user details
      } else {
        console.log("User is not logged in");
        setUserDetails(null); // Set user details to null if not logged in
      }
    });

    return () => unsubscribe(); // Clean up the subscription on unmount
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

  const handleBlindAssistClick = async () => {
    try {
      const response = await axios.get("http://127.0.0.1:8000/description_provider/");
      setDescription(response.data.description);
    } catch (error) {
      console.error("There was an error fetching the description!", error);
    }
  };

  const handleMapAssistClick = async () => {
    try {
      const response = await axios.get("http://127.0.0.1:8000/map_description_provider/");
      setDescription(response.data.description);
    } catch (error) {
      console.error("There was an error fetching the description!", error);
    }
  };

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
        <h1 className="text-left mb-20 text-4xl italic text-shadow-lg">Sahara</h1>
      </div>
      <div className="lg:w-4/5 space-y-5 mt-10">
        <h1 className="text-5xl font-bold leading-tight">
          Empowering Best Choices for a Vibrant Life Your Trusted..
        </h1>
        <p>
          Lorem ipsum dolor sit amet consectetur adipisicing elit. Quam magnam
          omnis natus accusantium quos. Reprehenderit incidunt expedita
          molestiae impedit at sequi dolorem iste sit culpa, optio voluptates
          fugiat vero consequatur?
        </p>
        <div className="flex justify-between mt-10">
          <a
            href="/connect"
            className="bg-gradient-to-r from-teal-500 via-green-400 to-teal-500 hover:bg-right text-white text-xl font-semibold py-4 px-8 rounded-lg shadow-lg transition-all duration-500 ease-in-out"
          >
            <button>Emergency Skin Injury Help</button>
          </a>
          <a
            href="/form"
            className="bg-gradient-to-r from-teal-500 via-green-400 to-teal-500 hover:bg-right text-white text-xl font-semibold py-4 px-8 rounded-lg shadow-lg transition-all duration-500 ease-in-out"
            onClick={handleBlindAssistClick}
          >
            <button>Environment Detection Assist</button>
          </a>
          <a
            href="/form"
            className="bg-gradient-to-r from-teal-500 via-green-400 to-teal-500 hover:bg-right text-white text-xl font-semibold py-4 px-8 rounded-lg shadow-lg transition-all duration-500 ease-in-out"
            onClick={handleMapAssistClick}
          >
            <button>Get Food Details</button>
          </a>
        </div>
      </div>
    </div>
  );
};

export default Home;
