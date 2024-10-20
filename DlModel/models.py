from django.db import models
import os
from django.conf import settings

# Create your models here.

class UploadFile(models.Model):
    file = models.FileField(upload_to='uploads/')

    def __str__(self):
        return self.file.name
    
    def save(self, *args, **kwargs):
        # Get the base directory of the Django project
        base_directory = settings.BASE_DIR
        # Define the full path where the file will be saved
        file_path = os.path.join(base_directory, 'uploads', self.file.name)
        super().save(*args, **kwargs)