import cv2
from ultralytics import YOLO
from .gemini import get_video_desciption

# Load the YOLOv8 model from the .pt file
model_path = './DlModel/yolov8m.pt'
model = YOLO(model_path)

# Function to analyze the video
def analyze_video(video_file_path):
    # Open the video file
    cap = cv2.VideoCapture(video_file_path)

    if not cap.isOpened():
        print(f"Error: Unable to open video {video_file_path}")
        return
    
    detected_objects = set()  # Use a set to store unique objects
    
    # Loop through the video frames
    while cap.isOpened():
        ret, frame = cap.read()  # Read frame
        if not ret:
            break
        
        # Perform inference on the frame
        results = model(frame)  # This returns a list of results, so we need to iterate through it

        # Process each result in the list
        for result in results:
            # Extract the boxes and class indices
            for obj in result.boxes:  # `boxes` attribute contains all bounding box info
                detected_class = model.names[int(obj.cls)]  # Class index
                detected_objects.add(detected_class)  # Add detected object to set (unique objects)
        
        # Optionally visualize the frame with detections
        annotated_frame = result.plot()  # Use `.plot()` to get an annotated frame
        cv2.imshow('YOLOv8 Video Detection', annotated_frame)
        
        # Break the loop if 'q' is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
    
    # Release video capture and close windows
    cap.release()
    cv2.destroyAllWindows()
    
    # Convert the set to a list of detected objects
    detected_objects = list(detected_objects)
    res = get_video_desciption(detected_objects)
    
    # You can process the detected objects further or return them
    return detected_objects, res


