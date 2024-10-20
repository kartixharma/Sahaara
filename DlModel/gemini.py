import typing
import cv2, time, os, json
import google.generativeai as genai

# Define the expected structure for the response
class NutritionalInfo(typing.TypedDict):
    calories: float
    macronutrients: dict[str, float]
    micronutrients: dict[str, float]
    serving_size: str

class HealthRatings(typing.TypedDict):
    overall: float
    sustainability_score: float

class FoodItemResponse(typing.TypedDict):
    name: str
    brand: str
    ingredients: list[str]
    vegan_friendly: bool
    keto_friendly: bool
    diabetic_friendly: bool
    nutritional_info: NutritionalInfo
    allergens: list[str]
    health_ratings: HealthRatings
    additives: list[str]
    certifications: list[str]
    origin_country: str
    dietary_restrictions: list[str]


class IAnalysis:
    def __init__(self):
        GOOGLE_API_KEY = os.environ.get('GOOGLE_API_KEY')
        genai.configure(api_key=GOOGLE_API_KEY)
    
    def skin_description_generator(self,file_path_to_wound, predictedValue):
        # Upload the temporary file
        sample_file = genai.upload_file(path=file_path_to_wound, display_name="image")
        print(f"Uploaded file '{sample_file.display_name}' as: {sample_file.uri}")
        # Analyze the image using the Gemini model
        file = genai.get_file(name=sample_file.name)
        print(f"Retrieved file '{file.display_name}' as: {sample_file.uri}")
        model = genai.GenerativeModel('gemini-1.5-flash')

        prompt = f"""
                Our platform has a Deep Learning model that provides support for identifying skin injuries. The allowed categories of injuries in our model are:
                ['Abrasions', 'Bruises', 'Burns', 'Cut', 'Ingrown_nails', 'Laceration', 'leg_wound', 'Stab_wound'].
                The model has predicted the category | {predictedValue} | for the image provided.
                don't include any "/", "/n" or any special characters other than alphabets in your response
                Check if this predicted category is correct. Your response should include a JSON structure with the following keys:

                
                {{
                "category": "{predictedValue}",  /* This should match the model's predicted category*/.
                "treatment": /*Provide a list of treatment steps specific to this injury category.*/,
                "severity": /*Indicate the severity of the injury (e.g., mild, moderate, severe).*/,
                "symptoms": /*List common symptoms associated with this injury.*/,
                "complications": /*List possible complications if untreated.*/,
                "medications": /*List recommended medications for this type of injury.*/,
                "prevention": /*Provide prevention steps to avoid future injuries of this type.*/,
                "recovery_time": {{
                    "min_days": /*Minimum number of recovery days.*/,
                    "max_days": /*Maximum number of recovery days.*/
                }},
                "recommended_action": /*List recommended actions for the user, e.g., seek medical attention, home care tips.*/,
                "alternative_remedies": /*Optional: Provide alternative or natural remedies, if applicable.*/
                }}

                DONT INCLUDE BACKTIK ` OR json AT THE START OR END IT SHOULD BE PURE JSON FORMAT
                """
        
        res = model.generate_content([sample_file, prompt]).text

        return res
    
    def food_description_generator(self, file_path_to_food):
        # Upload the temporary file
        sample_file = genai.upload_file(path=file_path_to_food, display_name="image")
        print(f"Uploaded file '{sample_file.display_name}' as: {sample_file.uri}")
        # Analyze the image using the Gemini model
        file = genai.get_file(name=sample_file.name)
        print(f"Retrieved file '{file.display_name}' as: {sample_file.uri}")

        model = genai.GenerativeModel(model_name='gemini-1.5-flash',generation_config=genai.GenerationConfig(response_mime_type="application/json"))


        prompt = f"""
            Scan the following food product and return its detailed information in JSON format. 
            Include whether it is vegan, keto-friendly, or diabetic-friendly, along with a list of nutrients, ingredients, and other health-related details. The structure should include fields for:

            DO NOT RETURN NULL VALUES, EVEN IF ITS INACCURATE JUST SEND

            {{
                "name": /*str*/,
                "brand":/*str*/,
                "description":/*str*/,
                "ingredients": /*list of str*/,
                "vegan_friendly":/*str*/,
                "keto_friendly":/*str*/,
                "diabetic_friendly": /*str*/,
                "nutritional_info": {{
                    "calories":/*str",
                    "macronutrients": {{
                        "fat":/*str",
                        "carbohydrates":/*str*/,
                        "protein":/*str*/
                    }},
                    "micronutrients": {{
                        "calcium":/*str*/,
                        "vitamin_d":/*str*/,
                        "vitamin_e":/*str*/
                    }},
                    "serving_size":/*str*/
                }},
                "allergens": /*list of str*/,
                "health_ratings": {{
                    "overall":/*str*/,
                    "sustainability_score":/*str*/
                }},
                "additives": /*list of str*/,
                "certifications": /* of str*/,
                "origin_country":/*"str*/,
                "dietary_restrictions": /*list of str*/
            }}
            DONT INCLUDE BACKTIK ` OR json AT THE START OR END IT SHOULD BE PURE JSON FORMAT
            DONT INCLUDE / OR \ AT THE START OR END OF KEY VALUE PAIR OR IN BETWEEN THEM
        """



        res = model.generate_content([sample_file,prompt]).text

        return res
    
    def video_content_generator(self, detected_objects):

        model = genai.GenerativeModel(model_name='gemini-1.5-flash',generation_config=genai.GenerationConfig(response_mime_type="application/json"))

        prompt = f"""
        I have detected the following objects from a video using the YOLOv8 model | {detected_objects} |. Based on these objects, please provide a detailed analysis of how they can be utilized to create something useful or fun.
        DONT INCLUDE BACKTIK ` OR json AT THE START OR END IT SHOULD BE PURE JSON FORMAT
        DONT INCLUDE / OR \ AT THE START OR END OF KEY VALUE PAIR OR IN BETWEEN THEM
        OUTPUT SHOULD BE PURELY IN JSON FORMAT
        {{
            "type": "indoor",
            "project_name": /*str*/,
            "objects_that_can_be_used": /*str*/
            "time_taken": /*str*/,
            "detailed_process": {{
                "materials_needed": /*str/*
                "instructions": /*str*/
                "tips": /*str*/
            }}
            "type": "outdoor or indoor",
            "project_name": /*str*/,
            "objects_that_can_be_used": /*str*/
            "time_require": /*str*/,
            "detailed_process": {{
                "materials_needed":/*str*/,
                "instructions": "/*str*/",
                "tips": "/*str*/"
            }}
            }}
        """

        res = model.generate_content(prompt).text
        
        return res

    
def get_skin_description(full_file_path, predictedValue):
    analysis = IAnalysis()

    res = analysis.skin_description_generator(full_file_path, predictedValue)

    return res

def get_food_description(full_file_path):
    analysis = IAnalysis()

    res = analysis.food_description_generator(full_file_path)

    return res

def get_video_desciption(detected_objects):
    analysis = IAnalysis()

    res = analysis.video_content_generator(detected_objects)
    return res

