from django.shortcuts import render, redirect
from .models import UploadFile
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import AllowAny
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from .predict import predict
from .gemini import get_skin_description, get_food_description
from .environments import analyze_video
import json
# Create your views here.
@method_decorator(csrf_exempt, name='dispatch')
class Say(APIView):
    permission_classes = [AllowAny]
    def get(self, request):
        return Response({'message': 'Hello, world!'}, status=status.HTTP_200_OK)
    

@method_decorator(csrf_exempt, name='dispatch')
class Wound(APIView):
    permission_classes = [AllowAny]
    def get(self, request):
        return Response({'isWorking': True,}, status=status.HTTP_200_OK)
    
    def post(self, request):
        uploaded_file = request.FILES.get('file')
        if uploaded_file:
            new_file = UploadFile(file=uploaded_file)
            new_file.save()
            # Full path of the uploaded file
            file_path_to_wound = new_file.file.path
            predictedValue = predict(file_path_to_wound)
            return Response({'message':predictedValue},status.HTTP_200_OK)  # Redirect to a success page
        return Response({'message':'Error encountered'},status.HTTP_412_PRECONDITION_FAILED)
    
    def patch(sef, request):
        uploaded_file = request.FILES.get('file')
        if uploaded_file:
            new_file = UploadFile(file=uploaded_file)
            new_file.save()
            file_path_to_food = new_file.file.path
            predictedValue = predict(file_path_to_food) 

            generate_description = get_skin_description(file_path_to_food, predictedValue)
            print(generate_description)
            generate_description = json.loads(generate_description)

            return Response( generate_description, status.HTTP_201_CREATED)
        return Response({'message':'Error encountered'},status.HTTP_412_PRECONDITION_FAILED)


@method_decorator(csrf_exempt, name='dispatch')
class Food(APIView):
    permission_classes =[AllowAny]

    def get(self, request):
        return Response({'isWorking': True,}, status=status.HTTP_200_OK)
    
    def post(self, request):
        uploaded_file = request.FILES.get('file')
        if uploaded_file:
            new_file = UploadFile(file=uploaded_file)
            new_file.save()
            # Full path of the uploaded file
            full_file_path = new_file.file.path
            res = get_food_description(full_file_path)
            res = json.loads(res)
            return Response(res,status.HTTP_200_OK)  # Redirect to a success page
        return Response({'message':'Error encountered'},status.HTTP_412_PRECONDITION_FAILED)          


@method_decorator(csrf_exempt, name='dispatch')
class Environment(APIView):
    permission_classes =[AllowAny]
    def get(self, request):
        return Response({'isWorking': True,}, status=status.HTTP_200_OK)
    
    def post(self, request):
        uploaded_file = request.FILES.get('video_file')
        if uploaded_file:
            new_file = UploadFile(file=uploaded_file)
            new_file.save()
            # Full path of the uploaded file
            video_file_path = new_file.file.path
            detected_objects, res = analyze_video(video_file_path)
            res = json.loads(res)
            return Response({'detected_objects':detected_objects, 'response':res},status.HTTP_200_OK)  # Redirect to a success page
        return Response({'message':'Error encountered'},status.HTTP_412_PRECONDITION_FAILED)  