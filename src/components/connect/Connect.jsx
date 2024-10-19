import React, { useState } from 'react';
import axios from 'axios';
import ConnectImage from '../connect/connect.jpeg';

export const Connect = () => {
    const [message, setMessage] = useState({});  // Start with an empty object
    const [loading, setLoading] = useState(false);  // Loader state

    const handleSubmitSkinDetection = async (event) => {
        event.preventDefault();
        setLoading(true);  // Start loader

        const fileInput = event.target.elements.skinFile;  // Use target from the form event
        const file = fileInput.files[0];

        const formData = new FormData();
        formData.append('file', file);

        try {
            // First Post request to get the initial message
            const postResponse = await axios.post('http://192.168.131.121:8000/wound', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            
            const initialMessage = postResponse.data.message;
            console.log("Post response message:", initialMessage);
            
            // Now we perform the Patch request
            const patchResponse = await axios.patch('http://192.168.131.121:8000/wound', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            const details = patchResponse.data;
            console.log("Patch response details:", details);
            
            // Combine the post and patch responses into one message
            setMessage({ message: initialMessage, ...details });
        } catch (error) {
            console.error('Error:', error);
            setMessage('Error encountered');
        } finally {
            setLoading(false);  // Stop loader after both requests are completed
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
            {/* Cards */}
            <div className="flex space-x-6">
                {/* Card 1 */}
                <div className="w-64 h-96 bg-transparent border-2 border-gray-300 backdrop-blur-lg shadow-lg text-white rounded-lg p-8">
                    <h1 className="text-3xl font-bold text-center mb-4">Emergency Help 1</h1>
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

                {/* Card 2 */}
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

                {/* Card 3 */}
                <div className="w-64 h-96 bg-transparent border-2 border-gray-300 backdrop-blur-lg shadow-lg text-white rounded-lg p-8">
                    <h1 className="text-3xl font-bold text-center mb-4">Emergency Help 3</h1>
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
            </div>

            {/* Response Box */}
            <div className="w-2/3 bg-gray-800 border-2 border-gray-500 rounded-lg shadow-lg p-8 mt-12">
                {loading ? (
                    <h1 className="text-2xl text-center text-white">Loading...</h1>
                ) : (
                    message && message.message ? ( // Render only if message is available
                        <>
                            {/* <h1 className="text-1xl font-bold text-white">Type: {message.message}</h1>
                            <br /> */}
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
                        </>
                    ) : null  // Show nothing if message is empty
                )}
            </div>
        </div>
    );
};
