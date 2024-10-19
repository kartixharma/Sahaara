import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaUser, FaLock } from "react-icons/fa";
import axios from 'axios';
import SignInWithGoogle from '../SignInWithGoogle';

export let currentValue = 0;

export const LoginPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleOnSubmit = async (e) => {
        e.preventDefault();
        const { username, password } = formData;

        try {
            const response = await axios.post('http://localhost:8000/login/', {
                username,
                password
            });

            if (response.status === 200) {
                currentValue = response.data.val;
                alert("Login successful!");
                navigate('/'); 
            }
        } catch (error) {
            console.error("There was an error logging in!", error);
            alert("Login failed. Please try again.");
        }
    };

    return (
        <div className="flex items-center justify-center w-screen h-screen bg-cover bg-center bg-blue-200">
            <div className="bg-white bg-opacity-20 backdrop-blur-sm border border-gray-300 rounded-lg shadow-lg p-8 w-96">
                <h1 className="text-3xl font-bold text-center text-blue-500">Login</h1>
                <form onSubmit={handleOnSubmit}>
                    <SignInWithGoogle/>
                </form>
            </div>
        </div>
    );
};
