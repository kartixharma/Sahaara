import React, { useState } from 'react';
import axios from 'axios';
import ConnectImage from '../connect/connect.jpeg';

export const Connect = () => {
    const [message, setMessage] = useState(null); 
    const [loading, setLoading] = useState(false); 
    const [isFoodInfo, setIsFoodInfo] = useState(false); 
    const [isEnvironmentInfo, setIsEnvironmentInfo] = useState(false); 

    const handleSubmitSkinDetection = async (event) => {
        event.preventDefault();
        setLoading(true); 
        setIsFoodInfo(false); 
        setIsEnvironmentInfo(false);
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
        setIsEnvironmentInfo(false); 
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

    const handleSubmitEnvironmentInfo = async (event) => {
        event.preventDefault();
        setLoading(true); 
        setIsEnvironmentInfo(true);
        setIsFoodInfo(false);
        setMessage(null);

        const fileInput = event.target.elements.video_file;
        const file = fileInput.files[0];

        const formData = new FormData();
        formData.append('video_file', file);

        try {
            const postResponse = await axios.post('http://192.168.131.121:8000/environment', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            const environmentInfo = postResponse.data;
            console.log("Environment Info:", environmentInfo);

            setMessage(environmentInfo);
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
                    <h1 className="text-3xl font-bold text-center mb-12">Environment Info</h1>
                    <form onSubmit={handleSubmitEnvironmentInfo}>
                        <div className="relative w-full h-16 mb-8">
                            <input
                                id="video_file"
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
                    <h1 className="text-3xl font-bold text-center mb-12">Food Info</h1>
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
                            {!isFoodInfo && !isEnvironmentInfo ? (  
                                <div className="text-white">
                                    <h2>Treatment Information for Cuts</h2>
                                    <p><b>Category:</b> {message.category}</p>
                                    
                                    <p><b>Severity:</b> {message.severity}</p>
                            
                                    <p><b>Symptoms:</b> {message.symptoms.join(', ')}</p>
                            
                                    <p><b>Complications:</b> {message.complications.join(', ')}</p>
                            
                                    <p><b>Medications:</b> {message.medications.join(', ')}</p>
                            
                                    <p><b>Prevention:</b> {message.prevention.join(', ')}</p>
                            
                                    <p><b>Recovery Time:</b> {message.recovery_time.min_days} - {message.recovery_time.max_days} days</p>
                            
                                    <h3>Treatment Steps:</h3>
                                    <ol>
                                        {message.treatment.map((step, index) => (
                                            <li key={index}>{step}</li>
                                        ))}
                                    </ol>
                            
                                    <h3>Recommended Actions:</h3>
                                    <ul>
                                        {message.recommended_action.map((action, index) => (
                                            <li key={index}>{action}</li>
                                        ))}
                                    </ul>
                            
                                    <h3>Alternative Remedies:</h3>
                                    <ul>
                                        {message.alternative_remedies.map((remedy, index) => (
                                            <li key={index}>{remedy}</li>
                                        ))}
                                    </ul>
                                </div>
                            ) : isFoodInfo ? (  
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
                                </p>
                            ) : isEnvironmentInfo ? (  
                                <p className="text-white">
                                    <b>Detected Objects:</b> {message.detected_objects.join(', ')}
                                    <br /><br />
                                    <b>Type:</b> {message.response.type}
                                    <br /><br />
                                    <b>Project Name:</b> {message.response.project_name}
                                    <br /><br />
                                    <b>Objects That Can Be Used:</b> {message.response.objects_that_can_be_used}
                                    <br /><br />
                                    <b>Time Taken:</b> {message.response.time_taken}
                                    <br /><br />
                                    <b>Materials Needed:</b> {message.response.detailed_process.materials_needed}
                                    <br /><br />
                                    <b>Instructions:</b> {message.response.detailed_process.instructions}
                                    <br /><br />
                                    <b>Tips:</b> {message.response.detailed_process.tips}
                                    <br /><br />
                                    <b>Time Required:</b> {message.response.time_require}
                                </p>
                            ) : null}
                        </>
                    )
                )}
            </div>
        </div>
    );
};
