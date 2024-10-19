import React, { useState } from 'react';
import axios from 'axios';
import ConnectImage from '../connect/connect.jpeg';

export const Connect = () => {
    const [message, setMessage] = useState(null); 
    const [loading, setLoading] = useState(false); 
    const [isFoodInfo, setIsFoodInfo] = useState(false); 

    const handleSubmitSkinDetection = async (event) => {
        event.preventDefault();
        setLoading(true); 
        setIsFoodInfo(false); 
        setMessage(null); 

        const fileInput = event.target.elements.skinFile;  
        const file = fileInput.files[0];

        const formData = new FormData();
        formData.append('file', file);

        try {
            const postResponse = await axios.post('http://192.168.131.121:8000/wound', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            
            const initialMessage = postResponse.data.message;
            console.log("Post response message:", initialMessage);
            
            const patchResponse = await axios.patch('http://192.168.131.121:8000/wound', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            const details = patchResponse.data;
            console.log("Patch response details:", details);
            
            setMessage({ message: initialMessage, ...details });
        } catch (error) {
            console.error('Error:', error);
            setMessage('Error encountered');
        } finally {
            setLoading(false); 
        }
    };

    const handleSubmitFoodInfo = async (event) => {
        event.preventDefault();
        setLoading(true); 
        setIsFoodInfo(true); 
        setMessage(null); 
    
        const fileInput = event.target.elements.skinFile;
        const file = fileInput.files[0];
    
        const formData = new FormData();
        formData.append('file', file);
    
        try {
            const postResponse = await axios.post('http://192.168.131.121:8000/food', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
    
            const foodInfo = postResponse.data;
            console.log("Food Info:", foodInfo);
    
            setMessage(foodInfo);
        } catch (error) {
            console.error('Error:', error);
            setMessage('Error encountered');
        } finally {
            setLoading(false);
        }
    };    
    
    return (
        <div
            className="w-screen h-screen flex flex-col justify-center items-center"
            style={{
                backgroundImage: `url(${ConnectImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
            }}
        >
            <div className="flex space-x-6">

                <div className="w-64 h-96 bg-transparent border-2 border-gray-300 backdrop-blur-lg shadow-lg text-white rounded-lg p-8">
                    <h1 className="text-3xl font-bold text-center mb-4">Emergency Skin Injury Help</h1>
                    <form onSubmit={handleSubmitSkinDetection}>
                        <div className="relative w-full h-16 mb-8">
                            <input
                                id="skinFile"
                                type="file"
                                required
                                className="w-full h-full bg-transparent border-2 border-gray-300 outline-none rounded-full text-white text-base px-5 py-4"
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full h-11 bg-white rounded-full shadow-md text-gray-800 font-bold"
                        >
                            Submit
                        </button>
                    </form>
                </div>

                <div className="w-64 h-96 bg-transparent border-2 border-gray-300 backdrop-blur-lg shadow-lg text-white rounded-lg p-8">
                    <h1 className="text-3xl font-bold text-center mb-4">Emergency Help 2</h1>
                    <form onSubmit={handleSubmitSkinDetection}>
                        <div className="relative w-full h-16 mb-8">
                            <input
                                id="skinFile"
                                type="file"
                                required
                                className="w-full h-full bg-transparent border-2 border-gray-300 outline-none rounded-full text-white text-base px-5 py-4"
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full h-11 bg-white rounded-full shadow-md text-gray-800 font-bold"
                        >
                            Submit
                        </button>
                    </form>
                </div>

                <div className="w-64 h-96 bg-transparent border-2 border-gray-300 backdrop-blur-lg shadow-lg text-white rounded-lg p-8">
                    <h1 className="text-3xl font-bold text-center mb-4">Food Info</h1>
                    <form onSubmit={handleSubmitFoodInfo}>
                        <div className="relative w-full h-16 mb-8">
                            <input
                                id="skinFile"
                                type="file"
                                required
                                className="w-full h-full bg-transparent border-2 border-gray-300 outline-none rounded-full text-white text-base px-5 py-4"
                            />
                        </div>
                        <button
                            type="submit"
                            className="w-full h-11 bg-white rounded-full shadow-md text-gray-800 font-bold"
                        >
                            Submit
                        </button>
                    </form>
                </div>
            </div>

            <div className="w-2/3 bg-gray-800 border-2 border-gray-500 rounded-lg shadow-lg p-8 mt-12">
                {loading ? (
                    <h1 className="text-2xl text-center text-white">Loading...</h1>
                ) : (
                    message && (  
                        <>
                            {!isFoodInfo ? (  
                                <p className="text-white">
                                    <b>Category:</b> {message.category}
                                    <br /><br />
                                    <b>Treatment:</b> {message.treatment}
                                    <br /><br />
                                    <b>Severity:</b> {message.severity}
                                    <br /><br />
                                    <b>Symptoms:</b> {message.symptoms}
                                    <br /><br />
                                    <b>Complications:</b> {message.complications}
                                    <br /><br />
                                    <b>Medications:</b> {message.medications}
                                    <br /><br />
                                    <b>Prevention:</b> {message.prevention}
                                    <br /><br />
                                    <b>Recommended Action:</b> {message.recommended_action}
                                    <br /><br />
                                    <b>Alternative Remedies:</b> {message.alternative_remedies}
                                </p>
                            ) : (  
                                <p className="text-white">
                                    <b>Name:</b> {message.name}
                                    <br /><br />
                                    <b>Brand:</b> {message.brand}
                                    <br /><br />
                                    <b>Description:</b> {message.description}
                                    <br /><br />
                                    <b>Ingredients:</b> {message.ingredients.join(', ')}
                                    <br /><br />
                                    <b>Vegan Friendly:</b> {message.vegan_friendly}
                                    <br /><br />
                                    <b>Keto Friendly:</b> {message.keto_friendly}
                                    <br /><br />
                                    <b>Diabetic Friendly:</b> {message.diabetic_friendly}
                                    <br /><br />
                                    <b>Nutritional Info:</b> 
                                    <ul>
                                        <li><b>Calories:</b> {message.nutritional_info.calories}</li>
                                        <li><b>Fat:</b> {message.nutritional_info.macronutrients.fat}</li>
                                        <li><b>Carbohydrates:</b> {message.nutritional_info.macronutrients.carbohydrates}</li>
                                        <li><b>Protein:</b> {message.nutritional_info.macronutrients.protein}</li>
                                    </ul>
                                    <br /><br />
                                    <b>Allergens:</b> {message.allergens.join(', ')}
                                    <br /><br />
                                    <b>Health Ratings:</b> 
                                    <ul>
                                        <li><b>Overall:</b> {message.health_ratings.overall}</li>
                                        <li><b>Sustainability Score:</b> {message.health_ratings.sustainability_score}</li>
                                        <li><b>Ethical Score:</b> {message.health_ratings.ethical_score}</li>
                                    </ul>
                                </p>
                            )}
                        </>
                    )
                )}
            </div>
        </div>
    );
};
