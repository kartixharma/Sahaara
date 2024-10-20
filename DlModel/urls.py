from django.urls import path
from .views import Say, Wound, Food, Environment

urlpatterns = [
    path('', Say.as_view(), name='say'),
    path('wound', Wound.as_view(), name='Wound'),
    path('food', Food.as_view(), name='Food'),
    path('environment', Environment.as_view(), name='Environment')

    
]
     