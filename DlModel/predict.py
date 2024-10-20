import tensorflow as tf
from tensorflow.keras.preprocessing import image
import numpy as np
import os

# Load the trained model
model = tf.keras.models.load_model(os.path.join(os.path.dirname(__file__), 'wound_segmentation_model_epochs_150.h5'))

# Class labels (based on your folder names)
class_labels = ['Abrasions', 'Bruises', 'Burns', 'Cut', 'Ingrown_nails', 'Laceration', 'leg_wound', 'Stab_wound']

# Function to preprocess the image
def preprocess_image(img_path):
    img = image.load_img(img_path, target_size=(224, 224))  # Resize image to 224x224
    img_array = image.img_to_array(img)  # Convert the image to a numpy array
    img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension (1, 224, 224, 3)
    img_array /= 255.0  # Normalize the image (same as during training)
    return img_array

# Function to predict the class of a given image
def predict(img_path):
    # Preprocess the image
    img_array = preprocess_image(img_path)

    # Make the prediction
    predictions = model.predict(img_array)

    
    # Get the predicted class label
    predicted_class = np.argmax(predictions[0])
    predicted_label = class_labels[predicted_class]

    return predicted_label

